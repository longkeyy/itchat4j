package org.solrcn.itchat4j.robot;

import org.springframework.stereotype.Component;

@Component
public interface Robot {

	String talk(String msg);
	
}
