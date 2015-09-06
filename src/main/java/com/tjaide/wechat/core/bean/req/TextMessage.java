package com.tjaide.wechat.core.bean.req;


/**
 * 文本消息
 * 
 * @author wangjun
 * @date 2015-07-12
 */
public class TextMessage extends BaseMessage {
	// 消息内容
	private String Content;

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}
}
