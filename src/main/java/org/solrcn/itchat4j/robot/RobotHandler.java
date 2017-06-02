package org.solrcn.itchat4j.robot;

import cn.zhouyafeng.itchat4j.core.Core;
import cn.zhouyafeng.itchat4j.face.IMsgHandlerFace;
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
import java.util.List;

/**
 * Created by longkeyy on 17/5/18.
 */
public abstract class RobotHandler implements IMsgHandlerFace, Robot {
    private static final Logger LOG = LoggerFactory.getLogger(RobotHandler.class);

    @Value("${wx.data:data}")
    String dataDir;

    private String checkPath(String path){
        File f = new File(path);
        if(!f.isDirectory()){
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
        LOG.info(msg.toJSONString());
        if (needResponse(msg)) {
            return talk(text);
        }
        return null;
    }

    @Override
    public String picMsgHandle(JSONObject msg) {
        if (needResponse(msg)) {
            String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
            String picPath = checkPath(dataDir + "/pic") + File.separator + fileName + ".jpg";
            DownloadTools.getDownloadFn(msg, MsgTypeEnum.PIC.getType(), picPath);
            return "你发的啥图片打不开";
        } else {
            return "";
        }

    }

    @Override
    public String voiceMsgHandle(JSONObject msg) {
        if (needResponse(msg)) {
            String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
            String voicePath = checkPath(dataDir + "/voice") + File.separator + fileName + ".mp3";
            DownloadTools.getDownloadFn(msg, MsgTypeEnum.VOICE.getType(), voicePath);
            return "你说什么我听不懂";
        } else {
            return "";
        }

    }

    @Override
    public String viedoMsgHandle(JSONObject msg) {
        if (needResponse(msg)) {
            String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
            String viedoPath = checkPath(dataDir + "/viedo") + File.separator + fileName + ".mp4";
            DownloadTools.getDownloadFn(msg, MsgTypeEnum.VIEDO.getType(), viedoPath);
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
