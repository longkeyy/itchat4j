package cn.zhouyafeng.itchat4j.utils.enums;

public enum RetCodeEnum {

	NORMAL("0", "普通"),
	LOGIN_OUT("1102", "退出"),
	LOGIN_OTHERWHERE("1101", "其它地方登陆"),
	MOBILE_LOGIN_OUT("1102", "移动端退出"),
	UNKOWN("9999", "未知")
	
	;
	
	
	private String code;
	private String type;

	RetCodeEnum(String code, String type) {
		this.code = code;
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public String getType() {
		return type;
	}

	public static RetCodeEnum codeOf(String code){
		RetCodeEnum[] values = RetCodeEnum.values();
		for (RetCodeEnum v : values){
			if(v.getCode().equals(code))
				return v;
		}
		return UNKOWN;
	}

}
