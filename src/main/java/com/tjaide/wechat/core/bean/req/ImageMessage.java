package com.tjaide.wechat.core.bean.req;

/**
 * 图片消息
 * 
 * @author wangjun
 * @date 2015-07-12
 */
public class ImageMessage extends BaseMessage {
	// 图片链接
	private String PicUrl;

	public String getPicUrl() {
		return PicUrl;
	}

	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}
}
