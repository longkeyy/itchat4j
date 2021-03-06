package org.solrcn.itchat4j.robot;

import cn.zhouyafeng.itchat4j.core.Core;
import cn.zhouyafeng.itchat4j.face.IMsgHandlerFace;
import cn.zhouyafeng.itchat4j.utils.SleepUtils;
import cn.zhouyafeng.itchat4j.utils.enums.MsgTypeEnum;
import cn.zhouyafeng.itchat4j.utils.tools.DownloadTools;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.solrcn.itchat4j.utils.RadmonUtil;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by longkeyy on 17/5/18.
 */
public abstract class RobotHandler implements IMsgHandlerFace, Robot {
    private static final Logger LOG = LoggerFactory.getLogger(RobotHandler.class);

    @Value("${wx.data:data}")
    String dataDir;

    private String checkPath(String path) {
        File f = new File(path);
        if (!f.isDirectory()) {
            try {
                FileUtils.forceMkdir(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return path;
    }

    protected boolean needResponse(JSONObject msg) {
        Core core = Core.getInstance();
        String chatUser = "小冰";
        String fromUserName = msg.getString("FromUserName");
        return chatUser.equals(core.getMemberNickName(fromUserName));
    }

    @Override
    public String textMsgHandle(JSONObject msg) {
        String text = msg.getString("Text");
        String userName = msg.getString("FromUserName");
        String memberNickName = Core.getInstance().getMemberNickName(userName);

        LOG.info("收到消息 " + memberNickName + " :" + text);
        if (needResponse(msg)) {
            String talk = talk(text);
            if (talk != null && !talk.isEmpty()) {
                SleepUtils.sleep(RadmonUtil.randomnumlong(1000, talk.length() + 1 * 1000));
            }
            return talk;
        }
        return null;
    }

    @Override
    public String picMsgHandle(JSONObject msg) {
        String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
        String picPath = checkPath(dataDir + "/pic") + File.separator + fileName + ".jpg";
        DownloadTools.getDownloadFn(msg, MsgTypeEnum.PIC.getType(), picPath);
        if (needResponse(msg)) {
            return "你发的啥图片打不开";
        } else {
            return "";
        }

    }

    @Override
    public String voiceMsgHandle(JSONObject msg) {
        String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
        String voicePath = checkPath(dataDir + "/voice") + File.separator + fileName + ".mp3";
        DownloadTools.getDownloadFn(msg, MsgTypeEnum.VOICE.getType(), voicePath);
        if (needResponse(msg)) {
            return "你说什么我听不懂";
        } else {
            return "";
        }

    }

    @Override
    public String viedoMsgHandle(JSONObject msg) {
        String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
        String viedoPath = checkPath(dataDir + "/viedo") + File.separator + fileName + ".mp4";
        DownloadTools.getDownloadFn(msg, MsgTypeEnum.VIEDO.getType(), viedoPath);
        if (needResponse(msg)) {
            return "你又发小视频了是不是";
        } else {
            return "";
        }
    }

    @Override
    public String nameCardMsgHandle(JSONObject msg) {
        if (needResponse(msg)) {
            return "收到名片";
        } else {
            return "";
        }
    }
}
