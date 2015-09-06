package com.tjaide.wechat.core.bean.resp;

/**
 * 语音消息
 * 
 * @author wangjun
 * @date 2015-05-12
 */
public class VoiceMessage extends BaseMessage {
	// 语音
	private Voice Voice;

	public Voice getVoice() {
		return Voice;
	}

	public void setVoice(Voice voice) {
		Voice = voice;
	}
}
