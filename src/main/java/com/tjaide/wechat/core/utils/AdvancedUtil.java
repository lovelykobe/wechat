package com.tjaide.wechat.core.utils;

import java.util.List;

import org.apache.log4j.Logger;

import com.tjaide.wechat.core.bean.resp.Article;
import com.tjaide.wechat.core.bean.resp.Music;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 高级接口调用
 * 
 * @author jun
 *
 */
public class AdvancedUtil {
	private static final Logger LOGGER = Logger.getLogger(AdvancedUtil.class);

	/**
	 * 组装文本消息
	 * @param openId 发送消息对象
	 * @param content 发送内容
	 * @return
	 */
	public static String makeTextCustomMessage(String openId, String content) {
		content = content.replace("\"", "\\\"");
		String jsonMsg = "{\"touser\":\"%s\",\"msgtype\":\"text\",\"text\":{\"content\":\"%s\"}}";
		return String.format(jsonMsg, openId, content);
	}
	
	/**
	 * 组装图片消息
	 * @param openId 发送消息对象
	 * @param mediaId 媒体文件id
	 * @return
	 */
	public static String makeImageCustomMessage(String openId, String mediaId) {
		String jsonMsg = "{\"touser\":\"%s\",\"msgtype\":\"image\",\"image\":{\"media_id\":\"%s\"}}";
		return String.format(jsonMsg, openId, mediaId);
	}
	
	/**
	 * 组装语音消息
	 * @param openId 发送消息对象
	 * @param mediaId 媒体文件id
	 * @return
	 */
	public static String makeVoiceCustomMessage(String openId, String mediaId) {
		String jsonMsg = "{\"touser\":\"%s\",\"msgtype\":\"voice\",\"voice\":{\"media_id\":\"%s\"}}";
		return String.format(jsonMsg, openId, mediaId);
	}
	
	/**
	 * 组装视频消息
	 * @param openId 发送消息对象
	 * @param mediaId 媒体文件id
	 * @param thumbMediaId 
	 * @return
	 */
	public static String makeVideoCustomMessage(String openId, String mediaId,String thumbMediaId) {
		String jsonMsg = "{\"touser\":\"%s\",\"msgtype\":\"video\",\"video\":{\"media_id\":\"%s\",\"thumb_media_id\":\"%s\"}}";
		return String.format(jsonMsg, openId, mediaId,thumbMediaId);
	}

	/**
	 * 组装音乐消息
	 * @param openId 发送消息对象
	 * @param music 音乐消息对象
	 * @return
	 */
	public static String makeMusicCustomMessage(String openId, Music music) {
		String jsonMsg = "{\"touser\":\"%s\",\"msgtype\":\"music\",\"music\":%s}";
		jsonMsg= String.format(jsonMsg, openId, JSONObject.fromObject(music).toString());
		jsonMsg = jsonMsg.replace("musicUrl", "musicurl");
		jsonMsg = jsonMsg.replace("HQMusicUrl", "hqmusicurl");
		jsonMsg = jsonMsg.replace("thumbMediaId", "thumb_media_id");
		return jsonMsg;
	}
	
	/**
	 * 组装图文消息客服
	 * @param openId 消息发送对象
	 * @param articleList 图文消息列表
	 * @return
	 */
	public static String makeNewsCustomMessage(String openId, List<Article> articleList) {
		String jsonMsg = "{\"touser\":\"%s\",\"msgtype\":\"news\",\"news\":{\"articles\":%s}}";
		jsonMsg = String.format(jsonMsg, openId,JSONArray.fromObject(articleList).toString().replaceAll("\"", "\\\""));
		jsonMsg = jsonMsg.replace("picUrl", "picurl");
		return jsonMsg;
	}
	
	
	public static boolean sendCustomMessage(String accessToken, String jsonMsg) {
		boolean result = false;
		// 连接地址
		String requestUrl = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token={ACCESS_TOKEN}";
		requestUrl = requestUrl.replace("{ACCESS_TOKEN}", accessToken);
		String respJSON = PublicUtil.httpsRequest(requestUrl, "POST", jsonMsg);
		JSONObject jsonObject = JSONObject.fromObject(respJSON);
		if (null != jsonObject) {
			int errorCode = jsonObject.getInt("errcode");
			String errorMsg = jsonObject.getString("errmsg");
			if (0 == errorCode) {
				return true;
			} else {
				LOGGER.error("客服消息发送失败errcode：" + errorCode);
			}
		}
		return result;
	}
}
