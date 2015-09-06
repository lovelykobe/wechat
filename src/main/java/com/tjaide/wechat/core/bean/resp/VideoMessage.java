package com.tjaide.wechat.core.bean.resp;


/**
 * 视频消息
 * 
 * @author wangjun
 * @date 2015-05-12
 */
public class VideoMessage extends BaseMessage {
	// 视频
	private Video Video;

	public Video getVideo() {
		return Video;
	}

	public void setVideo(Video video) {
		Video = video;
	}
}
