package com.tjaide.wechat.core.bean.menu;

/**
 * @author wangjun
 * @param      
 * @throws
 * @date 2015年7月14日下午2:26:19
 * @Description: view类型的按钮
 */
public class ViewButton extends Button {
	private String type;
	private String url;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
