package com.tjaide.wechat.core.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.tjaide.wechat.core.bean.group.WeixinGroup;
import com.tjaide.wechat.core.bean.resp.Article;
import com.tjaide.wechat.core.bean.resp.Music;
import com.tjaide.wechat.core.bean.semantic.SemanticParam;
import com.tjaide.wechat.core.bean.template.Template;
import com.tjaide.wechat.core.bean.user.WeiXinUser;
import com.tjaide.wechat.core.bean.user.WeiXinUserList;
import com.tjaide.wechat.core.bean.weixin.Token;

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
	
	
	public static boolean sendTemplateMessage(String accessToken, Template template) {
		boolean result = false;
		// 连接地址
		String requestUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token={ACCESS_TOKEN}";
		requestUrl = requestUrl.replace("{ACCESS_TOKEN}", accessToken);
		String respJSON = PublicUtil.httpsRequest(requestUrl, "POST", template.toJSON());
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
	
	/**
	 * 创建用户组
	 * @param accessToken 接口访问凭证
	 * @param groupName 组名
	 * @return
	 */
	public static WeixinGroup createGroup(String accessToken, String groupName) {
		WeixinGroup weixinGroup = null;
		boolean result = false;
		// 连接地址
		String requestUrl = "https://api.weixin.qq.com/cgi-bin/groups/create?access_token={ACCESS_TOKEN}";
		requestUrl = requestUrl.replace("{ACCESS_TOKEN}", accessToken);
		String reqJSON = String.format("{\"group\":{\"name\":\"%s\"}}",groupName);
		// 创建用户组
		String respJSON = PublicUtil.httpsRequest(requestUrl, "POST", reqJSON);

		System.out.println(respJSON);
		JSONObject jsonObject = JSONObject.fromObject(respJSON);
		if (null != jsonObject) {
			try {
				JSONObject groupObject = jsonObject.getJSONObject("group");
				int id = groupObject.getInt("id");
				String name = groupObject.getString("name");
				
				weixinGroup = new WeixinGroup();
				weixinGroup.setId(id);
				weixinGroup.setName(name);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				LOGGER.error("errcode:"+jsonObject.getInt("errcode")+",errmsg"+jsonObject.getString("errmsg"));
			}
		}
		return weixinGroup;
	}
	
	
	/**
	 * 查询所有分组
	 * @param accessToken
	 * @return
	 */
	public static List<WeixinGroup> getGroups(String accessToken) {
		List<WeixinGroup> weixinGroupList = new ArrayList<WeixinGroup>();
		WeixinGroup weixinGroup = null;
		boolean result = false;
		// 连接地址
		String requestUrl = "https://api.weixin.qq.com/cgi-bin/groups/get?access_token={ACCESS_TOKEN}";
		requestUrl = requestUrl.replace("{ACCESS_TOKEN}", accessToken);
		// 创建用户组
		String respJSON = PublicUtil.httpsRequest(requestUrl, "GET", null);

		System.out.println(respJSON);
		JSONObject jsonObject = JSONObject.fromObject(respJSON);
		if (null != jsonObject) {
			try {
				weixinGroupList = (List<WeixinGroup>) JSONArray.toCollection(jsonObject.getJSONArray("groups"),WeixinGroup.class);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				LOGGER.error("errcode:"+jsonObject.getInt("errcode")+",errmsg"+jsonObject.getString("errmsg"));
			}
		}
		return weixinGroupList;
	}
	
	
	/**
	 * 查询用户所在组
	 * @param accessToken
	 * @return
	 */
	public static int getMemberGroups(String accessToken,String openId) {
		int groupId = -1;
		// 连接地址
		String requestUrl = "https://api.weixin.qq.com/cgi-bin/groups/getid?access_token={ACCESS_TOKEN}";
		requestUrl = requestUrl.replace("{ACCESS_TOKEN}", accessToken);
		// 创建用户组
		String reqJSON = String.format("{\"openid\":\"%s\"}",openId);
		String respJSON = PublicUtil.httpsRequest(requestUrl, "POST", reqJSON);

		System.out.println(respJSON);
		JSONObject jsonObject = JSONObject.fromObject(respJSON);
		if (null != jsonObject) {
			try {
				groupId = jsonObject.getInt("groupid");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				LOGGER.error("errcode:"+jsonObject.getInt("errcode")+",errmsg"+jsonObject.getString("errmsg"));
			}
		}
		return groupId;
	}
	
	
	/**
	 * 修改分组名称
	 * @param accessToken
	 * @param groupId 
	 * @param newGroupName
	 * @return
	 */
	public static boolean updateGroupName(String accessToken, int groupId,String newGroupName) {
		boolean result = false;
		// 连接地址
		String requestUrl = "https://api.weixin.qq.com/cgi-bin/groups/update?access_token={ACCESS_TOKEN}";
		requestUrl = requestUrl.replace("{ACCESS_TOKEN}", accessToken);
		String reqJSON = String.format("{\"group\":{\"id\":%s,\"name\":\"%s\"}}",groupId,newGroupName);
		// 创建用户组
		String respJSON = PublicUtil.httpsRequest(requestUrl, "POST", reqJSON);

		System.out.println(respJSON);
		JSONObject jsonObject = JSONObject.fromObject(respJSON);
		if (null != jsonObject) {
			int errorCode = jsonObject.getInt("errcode");
			String errorMsg = jsonObject.getString("errmsg");
			if(errorCode == 0){
				result = true;
			}else{
				LOGGER.error("errcode:"+jsonObject.getInt("errcode")+",errmsg"+jsonObject.getString("errmsg"));
			}
		}
		return result;
	}
	
	
	
	/**
	 * 更新用户分组
	 * @param accessToken
	 * @param openId
	 * @param groupId
	 * @return
	 */
	public static boolean updateMemberGroup(String accessToken, String openId,int groupId) {
		boolean result = false;
		// 连接地址
		String requestUrl = "https://api.weixin.qq.com/cgi-bin/groups/members/update?access_token={ACCESS_TOKEN}";
		requestUrl = requestUrl.replace("{ACCESS_TOKEN}", accessToken);
		String reqJSON = String.format("{\"openid\":\"%s\",\"to_groupid\":%d}",openId,groupId);
		// 创建用户组
		String respJSON = PublicUtil.httpsRequest(requestUrl, "POST", reqJSON);

		System.out.println(respJSON);
		JSONObject jsonObject = JSONObject.fromObject(respJSON);
		if (null != jsonObject) {
			int errorCode = jsonObject.getInt("errcode");
			String errorMsg = jsonObject.getString("errmsg");
			if(errorCode == 0){
				result = true;
			}else{
				LOGGER.error("errcode:"+jsonObject.getInt("errcode")+",errmsg"+jsonObject.getString("errmsg"));
			}
		}
		return result;
	}
	
	
	/**
	 * 批量移动人
	 * @param accessToken
	 * @param openIds
	 * @param groupId
	 * @return
	 */
	public static boolean updateBatchMemberGroup(String accessToken, List<String> openIds,int groupId) {
		boolean result = false;
		// 连接地址
		String requestUrl = "https://api.weixin.qq.com/cgi-bin/groups/members/batchupdate?access_token={ACCESS_TOKEN}";
		requestUrl = requestUrl.replace("{ACCESS_TOKEN}", accessToken);
		String reqJSON = String.format("{\"openid\":\"%s\",\"to_groupid\":%d}",JSONArray.fromObject(openIds).toString(),groupId);
		// 创建用户组
		String respJSON = PublicUtil.httpsRequest(requestUrl, "POST", reqJSON);

		System.out.println(respJSON);
		JSONObject jsonObject = JSONObject.fromObject(respJSON);
		if (null != jsonObject) {
			int errorCode = jsonObject.getInt("errcode");
			String errorMsg = jsonObject.getString("errmsg");
			if(errorCode == 0){
				result = true;
			}else{
				LOGGER.error("errcode:"+jsonObject.getInt("errcode")+",errmsg"+jsonObject.getString("errmsg"));
			}
		}
		return result;
	}
	
	/**
	 * 删除分组
	 * @param accessToken
	 * @param groupId
	 * @return
	 */
	public static boolean deleteGroup(String accessToken, int groupId) {
		boolean result = false;
		// 连接地址
		String requestUrl = "https://api.weixin.qq.com/cgi-bin/groups/delete?access_token={ACCESS_TOKEN}";
		requestUrl = requestUrl.replace("{ACCESS_TOKEN}", accessToken);
		String reqJSON = String.format("{\"group\":{\"id\":%s}}",groupId);
		// 创建用户组
		String respJSON = PublicUtil.httpsRequest(requestUrl, "POST", reqJSON);

		System.out.println(respJSON);
		JSONObject jsonObject = JSONObject.fromObject(respJSON);
		if (null != jsonObject) {
			int errorCode = jsonObject.getInt("errcode");
			String errorMsg = jsonObject.getString("errmsg");
			if(errorCode == 0){
				result = true;
			}else{
				LOGGER.error("errcode:"+jsonObject.getInt("errcode")+",errmsg"+jsonObject.getString("errmsg"));
			}
		}
		return result;
	}
	
	
	
	
	public static WeiXinUser getUserInfo(String accessToken, String openId) {
		WeiXinUser weiXinUser = null;
		// 连接地址
		String requestUrl = "https://api.weixin.qq.com/cgi-bin/user/info?access_token={ACCESS_TOKEN}&openid={OPENID}&lang=zh_CN";
		requestUrl = requestUrl.replace("{ACCESS_TOKEN}", accessToken);
		requestUrl = requestUrl.replace("{OPENID}", openId);
		// 创建用户组
		String respJSON = PublicUtil.httpsRequest(requestUrl, "GET", null);

		System.out.println(respJSON);
		JSONObject jsonObject = JSONObject.fromObject(respJSON);
		if (null != jsonObject) {
			try {
				weiXinUser = new WeiXinUser();
				// 关注状态
				weiXinUser.setSubscribe(jsonObject.getInt("subscribe"));
				weiXinUser.setOpenid(jsonObject.getString("openid"));
				weiXinUser.setNickname(jsonObject.getString("nickname"));
				weiXinUser.setSex(jsonObject.getInt("sex"));
				weiXinUser.setLanguage(jsonObject.getString("language"));
				weiXinUser.setCity(jsonObject.getString("city"));
				weiXinUser.setProvince(jsonObject.getString("province"));
				weiXinUser.setCountry(jsonObject.getString("country"));
				weiXinUser.setHeadimgurl(jsonObject.getString("headimgurl"));
				weiXinUser.setSubscribeTime(jsonObject.getInt("subscribe_time"));
			//	weiXinUser.setUnionid(jsonObject.getString("unionid"));
				weiXinUser.setRemark(jsonObject.getString("remark"));
				weiXinUser.setGroupid(jsonObject.getInt("groupid"));
			} catch (Exception e) {
				if(0 == weiXinUser.getSubscribe()){
					LOGGER.info("用户"+openId+"已经取消关注微信公众号");
				}else{
					int errorCode = jsonObject.getInt("errcode");
					String errorMsg = jsonObject.getString("errmsg");
					LOGGER.error("errcode:"+errorCode+",errmsg"+errorMsg);
				}
			}
		}
		return weiXinUser;
	}
	
	
	public static WeiXinUserList getUserList(String accessToken, String nextOpenid) {
		WeiXinUserList weiXinUserList = null;
		// 连接地址
		String requestUrl = "https://api.weixin.qq.com/cgi-bin/user/get?access_token={ACCESS_TOKEN}&next_openid={NEXT_OPENID}";
		requestUrl = requestUrl.replace("{ACCESS_TOKEN}", accessToken);
		requestUrl = requestUrl.replace("{NEXT_OPENID}", nextOpenid);
		// 创建用户组
		String respJSON = PublicUtil.httpsRequest(requestUrl, "GET", null);

		System.out.println(respJSON);
		JSONObject jsonObject = JSONObject.fromObject(respJSON);
		if (null != jsonObject) {
			try {
				weiXinUserList = new WeiXinUserList();
				// 关注状态
				weiXinUserList.setTotal(jsonObject.getInt("total"));
				weiXinUserList.setCount(jsonObject.getInt("count"));
				weiXinUserList.setNextOpenid(jsonObject.getString("next_openid"));
				JSONObject dataObject = (JSONObject) jsonObject.get("data");
				weiXinUserList.setOpenidList((List<String>) JSONArray.toCollection(dataObject.getJSONArray("openid")));
			} catch (Exception e) {
				weiXinUserList = null;
				int errorCode = jsonObject.getInt("errcode");
				String errorMsg = jsonObject.getString("errmsg");
				LOGGER.error("errcode:"+errorCode+",errmsg"+errorMsg);
			}
		}
		return weiXinUserList;
	}
	
	
	
	
	public static String getShortUrl(String accessToken, String longUrl) {
		String shortUrl = longUrl;
		// 连接地址
		String requestUrl = "https://api.weixin.qq.com/cgi-bin/shorturl?access_token={ACCESS_TOKEN}";
		requestUrl = requestUrl.replace("{ACCESS_TOKEN}", accessToken);
		// 创建用户组
		String reqJSON = String.format("{\"action\":\"long2short\",\"long_url\":\"%s\"}",longUrl);
		String respJSON = PublicUtil.httpsRequest(requestUrl, "POST", reqJSON);

		System.out.println(respJSON);
		JSONObject jsonObject = JSONObject.fromObject(respJSON);
		if (null != jsonObject) {
				int errorCode = jsonObject.getInt("errcode");
				String errorMsg = jsonObject.getString("errmsg");
				if(0 == errorCode){
					shortUrl = jsonObject.getString("short_url");
				}else{
					System.out.println("长链接转换短链接失败");
					LOGGER.error("errcode:"+errorCode+",errmsg"+errorMsg);
				}
		}
		return shortUrl;
	}
	
	
	
	public static List<String> getWeiXinServerIPList(String accessToken) {
		List<String> ipList = null;
		// 连接地址
		String requestUrl = "https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token={ACCESS_TOKEN}";
		requestUrl = requestUrl.replace("{ACCESS_TOKEN}", accessToken);
		// 创建用户组
		String respJSON = PublicUtil.httpsRequest(requestUrl, "GET", null);

		System.out.println(respJSON);
		JSONObject jsonObject = JSONObject.fromObject(respJSON);
		if (null != jsonObject) {
			try {
				ipList = (List<String>) JSONArray.toCollection(jsonObject.getJSONArray("ip_list"));
			} catch (Exception e) {
				int errorCode = jsonObject.getInt("errcode");
				String errorMsg = jsonObject.getString("errmsg");
				LOGGER.error("errcode:"+errorCode+",errmsg"+errorMsg);
			}
		}
		return ipList;
	}
	
	
	public static String semproxy(String accessToken, String reqJSON) {
		String requestUrl = "https://api.weixin.qq.com/semantic/semproxy/search?access_token={ACCESS_TOKEN}";
		requestUrl = requestUrl.replace("{ACCESS_TOKEN}", accessToken);
		String respJSON = PublicUtil.httpsRequest(requestUrl, "POST", reqJSON);
		return respJSON;
	}
	
	
	public static void main(String[] args) {
//		List<TemplateParam> templateParamList = new ArrayList<TemplateParam>();
//		templateParamList.add(new TemplateParam("first", "亲爱的顾客您好！为您安排的K房如下", "#173177"));
//		templateParamList.add(new TemplateParam("keyword1", "2015年09月08日 13点50分", "#173177"));
//		templateParamList.add(new TemplateParam("keyword2", "迎水道店", "#173177"));
//		templateParamList.add(new TemplateParam("keyword3", "018房", "#173177"));
//		templateParamList.add(new TemplateParam("keyword4", "二逼人", "#173177"));
//		templateParamList.add(new TemplateParam("keyword5", "42元/位", "#173177"));
//		templateParamList.add(new TemplateParam("remark", "祝您欢唱愉快！！", "#173177"));
//		Template template = new Template();
//		template.setTemplateId("Qa6LK1znSOTaXjUYhY7WjR07rN36ds49YRitWJJTbNY");
//		template.setToUser("o8GTCuNm5UZYsCwgDrZ2-rfoa5YA");
//		template.setTopColor("#173177");
//		template.setUrl("");
//		template.setTemplateParamList(templateParamList);
//		
//
		Token token = PublicUtil.getAccessToken("wx56803fead87914d7", "8f8653c4d1580f7eca8efefaa2513e63");
//		
//		sendTemplateMessage(token.getAccess_token(), template);
		//createGroup(token.getAccess_token(),"测试组");
//		List<WeixinGroup> weixinGroupList = getGroups(token.getAccess_token());
//		System.out.println(weixinGroupList);
//		deleteGroup(token.getAccess_token(),102);
		//updateGroupName(token.getAccess_token(),101,"测试组2");
		
		SemanticParam sp = new SemanticParam();
		sp.setAppid("wx56803fead87914d7");
		sp.setCategory("music");
		sp.setCity("天津");
		sp.setQuery("我想听零点乐队唱的相信自己这首歌");
		sp.setUid("o8GTCuNm5UZYsCwgDrZ2-rfoa5YA");
		
		String reqJSON = JSONObject.fromObject(sp).toString();
		String respJSON = semproxy(token.getAccess_token(), reqJSON);
		
		JSONObject jsonObject = JSONObject.fromObject(respJSON);
		int errorCode = jsonObject.getInt("errcode");
		
		if(0 == errorCode){
			JSONObject semanticObject = (JSONObject) jsonObject.get("semantic");
			JSONObject detailsObject = (JSONObject) semanticObject.get("details");
			String song = detailsObject.getString("song");
			String singer = detailsObject.getString("singer");
			System.out.println("语义识别结果"+song+singer);
			
		}else{
			System.out.println("语义理解错误"+errorCode);
		}
		
	}
	
}
