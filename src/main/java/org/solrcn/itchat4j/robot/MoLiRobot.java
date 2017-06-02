package org.solrcn.itchat4j.robot;

import org.solrcn.itchat4j.utils.HttpUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.logging.Logger;

@Component
public class MoLiRobot extends RobotHandler {

    private static Logger logger = Logger.getLogger("MoLiRobot");

    @Value("${robot.moli.api:http://i.itpk.cn/api.php}")
    private String ITPK_API;

    @Value("${robot.moli.key}")
    private String apiKey;

    @Value("${robot.moli.secret}")
    private String apiSecret;

    OkHttpClient okHttpClient = HttpUtil.newClient();

    @Override
    public String talk(String msg) {
        String url = ITPK_API + "?api_key=" + apiKey + "&api_secret=" + apiSecret + "&question=" + msg;
        logger.info(url);
        Request request = HttpUtil.newRequest(url);

        try {
            return HttpUtil.getBodyString(okHttpClient, request);
        } catch (IOException e) {
            logger.warning(e.getMessage());
        }
        return null;
    }
}
