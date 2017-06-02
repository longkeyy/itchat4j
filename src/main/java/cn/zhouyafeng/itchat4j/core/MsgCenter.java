package cn.zhouyafeng.itchat4j.core;

import cn.zhouyafeng.itchat4j.api.MessageTools;
import cn.zhouyafeng.itchat4j.face.IMsgHandlerFace;
import cn.zhouyafeng.itchat4j.utils.MsgCodeEnum;
import cn.zhouyafeng.itchat4j.utils.enums.MsgTypeEnum;
import cn.zhouyafeng.itchat4j.utils.tools.CommonTools;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;

/**
 * 消息处理中心
 *
 * @author https://github.com/yaphone
 * @version 1.0
 * @date 创建时间：2017年5月14日 下午12:47:50
 */
public class MsgCenter {
    private static Logger LOG = LoggerFactory.getLogger(MsgCenter.class);

    private static Core core = Core.getInstance();

    /**
     * 接收消息，放入队列
     *
     * @param msgList
     * @return
     * @author https://github.com/yaphone
     * @date 2017年4月23日 下午2:30:48
     */
    public static JSONArray produceMsg(JSONArray msgList) {
        JSONArray result = new JSONArray();
        for (int i = 0; i < msgList.size(); i++) {
            JSONObject msg = new JSONObject();
            JSONObject m = msgList.getJSONObject(i);
            m.put("groupMsg", false);// 是否是群消息
            if (m.getString("FromUserName").contains("@@") || m.getString("ToUserName").contains("@@")) { // 群聊消息
                if (m.getString("FromUserName").contains("@@")
                        && !core.getGroupIdList().contains(m.getString("FromUserName"))) {
                    core.getGroupIdList().add((m.getString("FromUserName")));
                } else if (m.getString("ToUserName").contains("@@")
                        && !core.getGroupIdList().contains(m.getString("ToUserName"))) {
                    core.getGroupIdList().add((m.getString("ToUserName")));
                }
                // 群消息与普通消息不同的是在其消息体（Content）中会包含发送者id及":<br/>"消息，这里需要处理一下，去掉多余信息，只保留消息内容
                if (m.getString("Content").contains("<br/>")) {
                    String content = m.getString("Content").substring(m.getString("Content").indexOf("<br/>") + 5);
                    m.put("Content", content);
                    m.put("groupMsg", true);
                }
            } else {
                CommonTools.msgFormatter(m, "Content");
            }
            if (m.getInteger("MsgType") == MsgCodeEnum.MSGTYPE_TEXT.getCode()) { // words
                // 文本消息
                if (m.getString("Url").length() != 0) {
                    String regEx = "(.+?\\(.+?\\))";
                    Matcher matcher = CommonTools.getMatcher(regEx, m.getString("Content"));
                    String data = "Map";
                    if (matcher.find()) {
                        data = matcher.group(1);
                    }
                    msg.put("Type", "Map");
                    msg.put("Text", data);
                } else {
                    msg.put("Type", MsgTypeEnum.TEXT.getType());
                    msg.put("Text", m.getString("Content"));
                }
                m.put("Type", msg.getString("Type"));
                m.put("Text", msg.getString("Text"));
            } else if (m.getInteger("MsgType") == MsgCodeEnum.MSGTYPE_IMAGE.getCode()
                    || m.getInteger("MsgType") == MsgCodeEnum.MSGTYPE_EMOTICON.getCode()) { // 图片消息
                m.put("Type", MsgTypeEnum.PIC.getType());
            } else if (m.getInteger("MsgType") == MsgCodeEnum.MSGTYPE_VOICE.getCode()) { // 语音消息
                m.put("Type", MsgTypeEnum.VOICE.getType());
            } else if (m.getInteger("MsgType") == MsgCodeEnum.MSGTYPE_VERIFYMSG.getCode()) {// friends
                // 好友确认消息

            } else if (m.getInteger("MsgType") == MsgCodeEnum.MSGTYPE_SHARECARD.getCode()) { // 共享名片
                m.put("Type", MsgTypeEnum.NAMECARD.getType());

            } else if (m.getInteger("MsgType") == MsgCodeEnum.MSGTYPE_VIDEO.getCode()
                    || m.getInteger("MsgType") == MsgCodeEnum.MSGTYPE_MICROVIDEO.getCode()) {// viedo
                m.put("Type", MsgTypeEnum.VIEDO.getType());
            } else if (m.getInteger("MsgType") == MsgCodeEnum.MSGTYPE_APP.getCode()) { // sharing
                // 分享链接

            } else if (m.getInteger("MsgType") == MsgCodeEnum.MSGTYPE_STATUSNOTIFY.getCode()) {// phone
                // init
                // 微信初始化消息

            } else if (m.getInteger("MsgType") == MsgCodeEnum.MSGTYPE_SYS.getCode()) {// 系统消息

            } else if (m.getInteger("MsgType") == MsgCodeEnum.MSGTYPE_RECALLED.getCode()) { // 撤回消息

            } else {
                LOG.info("Useless msg");
            }
            LOG.info("收到消息一条，来自: " + core.getMemberNickName(m.getString("FromUserName")));
            result.add(m);
        }
        return result;
    }

    /**
     * 消息处理
     *
     * @param msgHandler
     * @author https://github.com/yaphone
     * @date 2017年5月14日 上午10:52:34
     */
    public static void handleMsg(IMsgHandlerFace msgHandler) {
        while (true) {
            if (core.getMsgList().size() > 0 && core.getMsgList().get(0).getString("Content") != null) {
                if (core.getMsgList().get(0).getString("Content").length() > 0) {
                    JSONObject msg = core.getMsgList().get(0);
                    String fromUserName = msg.getString("FromUserName");
                    String type = msg.getString("Type");
                    if (type == null || type.isEmpty()) {
                        core.getMsgList().remove(0);
                        continue;
                    }
                    MsgTypeEnum msgTypeEnum = MsgTypeEnum.valueOf(type.toUpperCase());
                    ;
                    String result = "";
                    switch (msgTypeEnum) {
                        case TEXT:
                            result = msgHandler.textMsgHandle(msg);
                            break;
                        case PIC:
                            result = msgHandler.picMsgHandle(msg);
                            break;
                        case VOICE:
                            result = msgHandler.voiceMsgHandle(msg);
                            break;
                        case VIEDO:
                            result = msgHandler.viedoMsgHandle(msg);
                            break;
                        case NAMECARD:
                            result = msgHandler.nameCardMsgHandle(msg);
                            break;
                        default:
                    }
                    if (result!=null && !result.isEmpty()) {
                        MessageTools.sendMsgById(result, fromUserName);
                    }
                }

                core.getMsgList().remove(0);
            }
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
