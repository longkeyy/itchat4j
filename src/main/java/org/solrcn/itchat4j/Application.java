package org.solrcn.itchat4j;

import cn.zhouyafeng.itchat4j.Wechat;
import org.solrcn.itchat4j.robot.MoLiRobot;
import org.solrcn.itchat4j.robot.SimsimiRobot;
import org.solrcn.itchat4j.robot.TulingRobot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class Application implements ApplicationRunner {

    @Autowired
    MoLiRobot moLiRobot;

    @Autowired
    TulingRobot tulingRobot;

    @Autowired
    SimsimiRobot simsimiRobot;

    @Value("${wx.data:data}")
    String appdata;

    @Override
    public void run(ApplicationArguments applicationArguments) {
        Wechat wechat = new Wechat(tulingRobot, appdata +"/login");
        wechat.start();
    }

    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "false");
        new SpringApplicationBuilder(Application.class).headless(false).web(false).run(args);
//        SpringApplication.run(Application.class, args);
    }


}