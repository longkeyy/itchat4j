package org.solrcn.itchat4j.robot;

import org.solrcn.itchat4j.utils.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.util.logging.Logger;

/**
 * Created by longkeyy on 17/6/2.
 */
@Component
public class SimsimiRobot extends RobotHandler {

    private static Logger logger = Logger.getLogger("SimsimiRobot");

    @Value("${robot.simsimi.api:https://secureapp.simsimi.com/v1/simsimi/talkset?ak=no_auth&av=6.8.9.0&cc=CN&isFilter=0&lc=ch&message_sentence=%s&normalProb=10&os=i&tz=Asia/Shanghai&uid=199106325}")
    String API_URI;
    OkHttpClient client = HttpUtil.newClient();

    @Override
    public String talk(String msg) {

        try {
            Request request = HttpUtil.newRequest(String.format(API_URI, URLEncoder.encode(msg, "utf-8")));
            JSONObject bodyJsonObject = HttpUtil.getBodyJsonObject(client, request);
            JSONObject jsonObject = bodyJsonObject.getJSONObject("simsimi_talk_set").getJSONArray("answers").getJSONObject(0);
            if ("text".equals(jsonObject.getString("type"))) {
                return jsonObject.getString("sentence");
            } else {
                System.out.println(jsonObject.toJSONString());
            }
        } catch (Exception e) {
            logger.warning(e.getMessage());
        }
        return null;
    }
}
