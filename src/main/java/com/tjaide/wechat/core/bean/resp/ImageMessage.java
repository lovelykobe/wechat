package com.tjaide.wechat.core.bean.resp;

/**
 * 图片消息
 * 
 * @author wangjun
 * @date 2015-05-12
 */
public class ImageMessage extends BaseMessage {
	// 图片
	private Image Image;

	public Image getImage() {
		return Image;
	}

	public void setImage(Image image) {
		Image = image;
	}
}
