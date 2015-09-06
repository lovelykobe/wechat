package com.tjaide.wechat.core.bean.menu;



/**
 * @author wangjun
 * @param      
 * @throws
 * @date 2015年7月14日下午2:26:48
 * @Description: click类型的按钮
 */
public class ClickButton extends Button {
	private String type;
	private String key;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}