package com.tjaide.wechat.core.utils;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import com.google.gson.Gson;
import com.tjaide.wechat.core.bean.resp.Article;
import com.tjaide.wechat.core.bean.third.WeatherData;
import com.tjaide.wechat.core.bean.third.WeatherResp;

/**
 * @author wangjun
 * @param
 * @throws
 * @date 2015年7月15日上午11:40:58
 * @Description: 百度地图天气查询工具类
 */
public class WeatherUtil {
	/**
	 * @author wangjun
	 * @param @param cityName
	 * @param @return
	 * @return String
	 * @throws
	 * @date 2015年7月15日上午11:42:23
	 * @Description: 根据城市名称查询天气
	 */
	public static String queryWeather(String cityName) {
		String requestUrl = "http://api.map.baidu.com/telematics/v3/weather?location={LOCATION}&output=json&ak={AK}";
		requestUrl = requestUrl.replace("{LOCATION}", PublicUtil.urlEncodeUTF8(cityName));
		requestUrl = requestUrl.replace("{AK}", "XelDx7AfP1VthFwGAOvjnaY2");

		// 调用接口获取天气预报
		String respJSON = PublicUtil.httpRequest(requestUrl, "GET", null);

		if (JSONObject.fromObject(respJSON).getString("error").equals("0")) {

			Gson gson = new Gson();
			WeatherResp weatherResp = gson.fromJson(respJSON, WeatherResp.class);

			// 解析返回结果
			String currentCity = weatherResp.getResults().get(0).getCurrentCity();
			// 天气数据
			List<WeatherData> weatherDataList = weatherResp.getResults().get(0).getWeather_data();
			StringBuffer buffer = new StringBuffer();
			buffer.append(currentCity).append("天气预报").append("\n\n");
			for (WeatherData data : weatherDataList) {
				buffer.append(data.getDate()).append(" ").append(data.getTemperature()).append("，");
				buffer.append(data.getWeather()).append("，");
				buffer.append(data.getWind()).append("\n\n");
			}
			String result = buffer.toString();
			return result.substring(0,result.length()-4);
		} else {
			return "请输入正确的城市！";
		}
	}
	
	
	/**
	 * @author wangjun
	 * @param @param cityName
	 * @param @return   
	 * @return List<Article>  
	 * @throws
	 * @date 2015年7月15日下午2:53:04
	 * @Description: 返回图文
	 */
	public static List<Article> queryWeatherIMG(String cityName) {
		String requestUrl = "http://api.map.baidu.com/telematics/v3/weather?location={LOCATION}&output=json&ak={AK}";
		requestUrl = requestUrl.replace("{LOCATION}", PublicUtil.urlEncodeUTF8(cityName));
		requestUrl = requestUrl.replace("{AK}", "XelDx7AfP1VthFwGAOvjnaY2");

		// 调用接口获取天气预报
		String respJSON = PublicUtil.httpRequest(requestUrl, "GET", null);

		if (JSONObject.fromObject(respJSON).getString("error").equals("0")) {

			Gson gson = new Gson();
			WeatherResp weatherResp = gson.fromJson(respJSON, WeatherResp.class);

			// 解析返回结果
			String currentCity = weatherResp.getResults().get(0).getCurrentCity();
			// 天气数据
			List<WeatherData> weatherDataList = weatherResp.getResults().get(0).getWeather_data();
			List<Article> articleList = new ArrayList<Article>();
			//取得当天的天气
			WeatherData weatherDate = weatherDataList.get(0);
			
			Article article = new Article();
			article.setTitle(currentCity+" "+weatherDate.getDate()+" "+weatherDate.getWeather()+" "+weatherDate.getWind());
			article.setDescription("");
			article.setPicUrl(PublicUtil.PROJECT_ROOT+"/image/top.jpg");
			article.setUrl("");
			//移除当日的天气信息
			weatherDataList.remove(0);
			articleList.add(article);
			for(WeatherData data : weatherDataList){
				article = new Article();
				article.setTitle(data.getDate()+" "+data.getTemperature()+" "+data.getWeather()+" "+data.getWind());
				article.setDescription("");
				article.setPicUrl(PublicUtil.PROJECT_ROOT+"/image/"+getWeatherPic(data.getDayPictureUrl()));
				article.setUrl("");
				articleList.add(article);
			}
			return articleList;
		} else {
			return null;
		}
	}

	public static void main(String[] args) {
		System.out.println(queryWeather("天津"));
	}
	/**
	 * @author wangjun
	 * @param @param picUrl
	 * @param @return   
	 * @return String  
	 * @throws
	 * @date 2015年7月15日下午3:02:12
	 * @Description: 获取图片对应图片
	 */
	private static String getWeatherPic(String picUrl){
		String result = picUrl.substring(picUrl.lastIndexOf("/")+1);
		result = "weather_" + result;
		return result;
	}

}
