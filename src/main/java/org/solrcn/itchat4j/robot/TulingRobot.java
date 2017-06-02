package org.solrcn.itchat4j.robot;

import cn.zhouyafeng.itchat4j.core.Core;
import cn.zhouyafeng.itchat4j.utils.MyHttpClient;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * 图灵机器人示例
 *
 * @author https://github.com/yaphone
 * @version 1.0
 * @date 创建时间：2017年4月24日 上午12:13:26
 */
@Component
public class TulingRobot extends RobotHandler {
    MyHttpClient myHttpClient = Core.getInstance().getMyHttpClient();

    @Value("${robot.tuling.api:http://www.tuling123.com/openapi/api}")
    String API_URI;

    @Value("${robot.tuling.key}")
    String apiKey;
    Logger logger = Logger.getLogger("TulingRobot");

    @Override
    public String talk(String msg) {
        String result = null;
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("key", apiKey);
        paramMap.put("info", msg);
        paramMap.put("userid", "123456");
        String paramStr = JSON.toJSONString(paramMap);
        try {
            HttpEntity entity = myHttpClient.doPost(API_URI, paramStr);
            result = EntityUtils.toString(entity, "UTF-8");
            JSONObject obj = JSON.parseObject(result);
            if (obj.getString("code").equals("100000")) {
                return obj.getString("text");
            }
        } catch (Exception e) {
            logger.warning(e.getMessage());
        }
        return "好好说话";
    }
}
