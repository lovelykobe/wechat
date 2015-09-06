package com.tjaide.wechat.core.utils;


import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.tjaide.wechat.core.bean.menu.Menu;

/**
 * @author wangjun
 * @param      
 * @throws
 * @date 2015年7月14日下午2:36:40
 * @Description: 自定义菜单工具类
 */
public class MenuUtil {
	private static final Logger LOGGER = Logger.getLogger(MenuUtil.class);

	// 菜单创建（POST）
	public final static String MENU_CREATE_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	// 菜单查询（GET）
	public final static String MENU_GET_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";
	// 菜单删除（GET）
	public final static String MENU_DELETE_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";

	/**
	 * 创建菜单
	 * 
	 * @param menu 菜单实例
	 * @param accessToken 凭证
	 * @return true成功 false失败
	 */
	public static boolean createMenu(String jsonMenu, String accessToken) {
		boolean result = false;
		String url = MENU_CREATE_URL.replace("ACCESS_TOKEN", accessToken);
		// 将菜单对象转换成json字符串
		// 发起POST请求创建菜单
		JSONObject jsonObject = JSONObject.fromObject(PublicUtil.httpsRequest(url, "POST", jsonMenu));

		if (null != jsonObject) {
			int errorCode = jsonObject.getInt("errcode");
			String errorMsg = jsonObject.getString("errmsg");
			if (0 == errorCode) {
				result = true;
			} else {
				result = false;
				LOGGER.error("创建菜单失败 errcode:{" + errorCode +"} errmsg:{ " + errorMsg +"}");
			}
		}

		return result;
	}

	/**
	 * 查询菜单
	 * 
	 * @param accessToken 凭证
	 * @return
	 */
	public static String getMenu(String accessToken) {
		String result = null;
		String requestUrl = MENU_GET_URL.replace("ACCESS_TOKEN", accessToken);
		// 发起GET请求查询菜单
		JSONObject jsonObject = JSONObject.fromObject(PublicUtil.httpsRequest(requestUrl, "GET", null));

		if (null != jsonObject) {
			result = jsonObject.toString();
		}
		return result;
	}

	/**
	 * 删除菜单
	 * 
	 * @param accessToken 凭证
	 * @return true成功 false失败
	 */
	public static boolean deleteMenu(String accessToken) {
		boolean result = false;
		String requestUrl = MENU_DELETE_URL.replace("ACCESS_TOKEN", accessToken);
		// 发起GET请求删除菜单
		JSONObject jsonObject = JSONObject.fromObject(PublicUtil.httpsRequest(requestUrl, "GET", null));

		if (null != jsonObject) {
			int errorCode = jsonObject.getInt("errcode");
			String errorMsg = jsonObject.getString("errmsg");
			if (0 == errorCode) {
				result = true;
			} else {
				result = false;
				LOGGER.error("删除菜单失败 errcode:{" + errorCode +"} errmsg:{ " + errorMsg +"}");
			}
		}
		return result;
	}
}