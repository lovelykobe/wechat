package com.tjaide.wechat.core.bean.event;

/**
 * 自定义菜单事件
 * 
 * @author wangjun
 * @date 2015-07-12
 */
public class MenuEvent extends BaseEvent {
	// 事件KEY值，与自定义菜单接口中KEY值对应
	private String EventKey;

	public String getEventKey() {
		return EventKey;
	}

	public void setEventKey(String eventKey) {
		EventKey = eventKey;
	}
}
