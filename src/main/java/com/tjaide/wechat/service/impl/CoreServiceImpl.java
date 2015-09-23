package com.tjaide.wechat.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tjaide.wechat.core.bean.CustomerServiceMessage;
import com.tjaide.wechat.core.bean.req.TextMessage;
import com.tjaide.wechat.core.bean.resp.Article;
import com.tjaide.wechat.core.bean.resp.Music;
import com.tjaide.wechat.core.bean.resp.MusicMessage;
import com.tjaide.wechat.core.bean.resp.NewsMessage;
import com.tjaide.wechat.core.bean.semantic.SemanticParam;
import com.tjaide.wechat.core.bean.weixin.Token;
import com.tjaide.wechat.core.utils.AdvancedUtil;
import com.tjaide.wechat.core.utils.FacePlusPlusUtil;
import com.tjaide.wechat.core.utils.MessageUtil;
import com.tjaide.wechat.core.utils.PublicUtil;
import com.tjaide.wechat.core.utils.WeatherUtil;
import com.tjaide.wechat.dao.SignDao;
import com.tjaide.wechat.service.CoreService;

import net.sf.json.JSONObject;

/**
 * @author wangjun
 * @param
 * @throws
 * @date 2015年7月12日下午1:58:53
 * @Description: TODO
 */
@Service
public class CoreServiceImpl implements CoreService {

	private static final Logger LOGGER = Logger.getLogger(CoreServiceImpl.class);
	@Autowired
	private SignDao signDao;

	/**
	 * 处理微信发来的请求
	 * 
	 * @param request
	 * @return xml
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public String process(Map<String, String> requestMap) {
		// xml格式的消息数据
		String respXml = null;
		// 默认返回的文本消息内容
		TextMessage textMessage = new TextMessage();
		try {
			// 调用parseXml方法解析请求消息
			// 发送方帐号
			String fromUserName = requestMap.get("FromUserName");
			// 开发者微信号
			String toUserName = requestMap.get("ToUserName");
			// 消息类型
			String msgType = requestMap.get("MsgType");

			// 回复文本消息
			textMessage.setToUserName(fromUserName);
			textMessage.setFromUserName(toUserName);
			textMessage.setCreateTime(new Date().getTime());
			textMessage.setContent("服务器未响应");
			textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
			respXml = MessageUtil.messageToXml(textMessage);
			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
				// 文本消息
				// textMessage.setContent("文本消息");
				// 文本内容
				String content = requestMap.get("Content");

				LOGGER.info(fromUserName + "发送了消息" + content);
				if ("签到".equals(content)) {
					// 今天是否签到
					if (!signDao.isTodaySigned(fromUserName)) {
						// 是否连续7天签到（12积分）
						if (signDao.isSevenSign(fromUserName, getMondayOfThisWeek())) {
							signDao.saveWeixinSign(fromUserName, 12);
							signDao.updateUserPoints(fromUserName, 12);
							textMessage.setContent("签到成功，获得2个积分！\n本周连续7次签到，额外赠送10个积分！");
							respXml = MessageUtil.messageToXml(textMessage);
						} else {
							signDao.saveWeixinSign(fromUserName, 2);
							signDao.updateUserPoints(fromUserName, 2);
							textMessage.setContent("签到成功，获得2个积分！");
							respXml = MessageUtil.messageToXml(textMessage);
						}
					} else {
						textMessage.setContent("您今天已经签到过！");
						respXml = MessageUtil.messageToXml(textMessage);
					}
				} else if ("1".equals(content)) {
					StringBuffer sb = new StringBuffer();
					sb.append("我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说");
					sb.append("我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说");
					sb.append("我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说");
					sb.append("我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说");
					sb.append("我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说");
					sb.append("我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说");
					sb.append("我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说");
					sb.append("我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说");
					sb.append("我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说");
					sb.append("我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说");
					sb.append("我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说");
					sb.append("我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说");
					sb.append("我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说");
					sb.append("我你说我你说我你说说我你说我你说我你我你说我你我你说我你说我2048....");
					textMessage.setContent(sb.toString());
					respXml = MessageUtil.messageToXml(textMessage);
				} else if ("2".equals(content)) {
					StringBuffer sb = new StringBuffer();
					sb.append("我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说");
					sb.append("我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说");
					sb.append("我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说");
					sb.append("我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说");
					sb.append("我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说");
					sb.append("我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说");
					sb.append("我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说");
					sb.append("我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说");
					sb.append("我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说");
					sb.append("我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说");
					sb.append("我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说");
					sb.append("我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说");
					sb.append("我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说我你说我你说我你说说");
					sb.append("我你说我你说我你说说我你说我你说我你我你说我你我你说我你说我2049.....");
					textMessage.setContent(sb.toString());
					respXml = MessageUtil.messageToXml(textMessage);
				} else if ("3".equals(content)) {
					textMessage.setContent("点击访问<a href=\"http://www.baidu.com\">百度</a>");
					respXml = MessageUtil.messageToXml(textMessage);
				} else if ("微社区".equals(content)) {
					textMessage.setContent("点击进入<a href=\"http://www.baidu.com\">微社区</a>");
					respXml = MessageUtil.messageToXml(textMessage);
				}else if ("4".equals(content)) {
					textMessage.setContent("祝你生日快乐！");
					respXml = MessageUtil.messageToXml(textMessage);
				} else if ("5".equals(content)) {
					textMessage.setContent("亲，想你了，怎么才来呢？");
					respXml = MessageUtil.messageToXml(textMessage);
				} else if ("6".equals(content)) {
					textMessage.setContent("XOXO");
					respXml = MessageUtil.messageToXml(textMessage);
				} else if ("/::D".equals(content)) {
					// 如果用户发送的是呲牙表情
					textMessage.setContent("什么事情这么高兴呀？[疑问]/疑问/:?");
					respXml = MessageUtil.messageToXml(textMessage);
				} else if ("/::<".equals(content)) {
					// 如果用户发送的是呲牙表情
					textMessage.setContent("什么事情这么伤心呀？说出来让我开心下！[疑问]/疑问/:?");
					respXml = MessageUtil.messageToXml(textMessage);
				} else if ("自行车".equals(content)) {
					// 如果用户发送的是呲牙表情
					// utf8mb4 超级字符集 5.5.3 mysql
					textMessage.setContent("这辆" + PublicUtil.emoji(0x1F6B2) + "怎么样，好看吧？");
					respXml = MessageUtil.messageToXml(textMessage);
				} else if ("歌曲".equals(content)) {
					Music music = new Music();
					music.setTitle("旅行");
					music.setDescription("许巍");
					music.setMusicUrl(PublicUtil.PROJECT_ROOT + "/music/libai.mp3");
					music.setHQMusicUrl(PublicUtil.PROJECT_ROOT + "/music/tfboys.mp3");

					MusicMessage musicMessage = new MusicMessage();
					musicMessage.setToUserName(fromUserName);
					musicMessage.setFromUserName(toUserName);
					musicMessage.setCreateTime(new Date().getTime());
					musicMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_MUSIC);
					musicMessage.setMusic(music);
					respXml = MessageUtil.messageToXml(musicMessage);
				} else if ("7".equals(content)) {
					// 回复单图文消息（不含图片）
					Article article = new Article();
					article.setTitle("王狗狗");
					article.setDescription("王狗狗的测试信息");
					article.setPicUrl("");
					article.setUrl("http://www.baidu.com");

					List<Article> articles = new ArrayList<Article>();
					articles.add(article);

					NewsMessage newsMessage = new NewsMessage();
					newsMessage.setToUserName(fromUserName);
					newsMessage.setFromUserName(toUserName);
					newsMessage.setCreateTime(new Date().getTime());
					newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
					newsMessage.setArticleCount(articles.size());
					newsMessage.setArticles(articles);

					respXml = MessageUtil.messageToXml(newsMessage);
				} else if ("8".equals(content)) {
					// 回复单图文消息
					Article article = new Article();
					article.setTitle("王狗狗");
					article.setDescription("王狗狗的测试信息");
					article.setPicUrl(PublicUtil.PROJECT_ROOT + "/image/test.jpg");
					article.setUrl("http://www.baidu.com");

					List<Article> articles = new ArrayList<Article>();
					articles.add(article);

					NewsMessage newsMessage = new NewsMessage();
					newsMessage.setToUserName(fromUserName);
					newsMessage.setFromUserName(toUserName);
					newsMessage.setCreateTime(new Date().getTime());
					newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
					newsMessage.setArticleCount(articles.size());
					newsMessage.setArticles(articles);

					respXml = MessageUtil.messageToXml(newsMessage);
				} else if ("9".equals(content)) {
					// 回复多图文消息
					// 10 条以内 1-3条
					Article article1 = new Article();
					article1.setTitle("王狗狗1");
					article1.setDescription("王狗狗的测试信息1");
					article1.setPicUrl(PublicUtil.PROJECT_ROOT + "/image/test.jpg");
					article1.setUrl("http://www.baidu.com");

					Article article2 = new Article();
					article2.setTitle("王狗狗2");
					article2.setDescription("王狗狗的测试信息2");
					article2.setPicUrl(PublicUtil.PROJECT_ROOT + "/image/test.jpg");
					article2.setUrl("http://www.baidu.com");

					Article article3 = new Article();
					article3.setTitle("王狗狗3");
					article3.setDescription("王狗狗的测试信息3");
					article3.setPicUrl(PublicUtil.PROJECT_ROOT + "/image/test.jpg");
					article3.setUrl("http://www.baidu.com");

					List<Article> articles = new ArrayList<Article>();
					articles.add(article1);
					articles.add(article2);
					articles.add(article3);

					NewsMessage newsMessage = new NewsMessage();
					newsMessage.setToUserName(fromUserName);
					newsMessage.setFromUserName(toUserName);
					newsMessage.setCreateTime(new Date().getTime());
					newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
					newsMessage.setArticleCount(articles.size());
					newsMessage.setArticles(articles);

					respXml = MessageUtil.messageToXml(newsMessage);
				} else if (content.startsWith("天气") || content.endsWith("天气")) {
					// 截取城市名称
					String cityName = content.trim().replace("天气", "");
					// 调用百度地图的天气查询的接口
					textMessage.setContent(WeatherUtil.queryWeather(cityName));
					respXml = MessageUtil.messageToXml(textMessage);
				} else if (content.endsWith("天气图")) {
					// 截取城市名称
					String cityName = content.trim().replace("天气图", "");
					// 调用百度地图的天气查询的接口
					List<Article> articles = WeatherUtil.queryWeatherIMG(cityName);
					if (articles != null) {
						NewsMessage nm = new NewsMessage();
						nm.setFromUserName(toUserName);
						nm.setToUserName(fromUserName);
						nm.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
						nm.setCreateTime(new Date().getTime());
						nm.setArticleCount(articles.size());
						nm.setArticles(articles);
						respXml = MessageUtil.messageToXml(nm);
					} else {
						textMessage.setContent("请输入正确的城市名称");
						respXml = MessageUtil.messageToXml(textMessage);

					}
				} else if ("2048".equals(content)) {
					textMessage.setContent("点击开始玩<a href=\""+PublicUtil.PROJECT_ROOT + "/games/2048/index.html\">2048</a>游戏");
					respXml = MessageUtil.messageToXml(textMessage);
				}else if ("2048图".equals(content)) {
					Article article = new Article();
					article.setTitle("挑战2048");
					article.setDescription("这么好玩的游戏，快快来挑战你的智商吧！");
					article.setPicUrl(PublicUtil.PROJECT_ROOT + "/image/2048.png");
					article.setUrl(PublicUtil.PROJECT_ROOT + "/games/2048/index.html");

					List<Article> articles = new ArrayList<Article>();
					articles.add(article);
					
					NewsMessage nm = new NewsMessage();
					nm.setFromUserName(toUserName);
					nm.setToUserName(fromUserName);
					nm.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
					nm.setCreateTime(new Date().getTime());
					nm.setArticleCount(articles.size());
					nm.setArticles(articles);
					respXml = MessageUtil.messageToXml(nm);
				}else if ("bmi".equalsIgnoreCase(content)) {
					textMessage.setContent("<a href=\""+PublicUtil.PROJECT_ROOT + "/games/BMI/index.jsp\">BMI计算器</a>");
					respXml = MessageUtil.messageToXml(textMessage);
				} else if ("地理位置".equals(content)) {
					textMessage.setContent("<a href=\""+PublicUtil.PROJECT_ROOT + "/location.html\">地理位置</a>");
					respXml = MessageUtil.messageToXml(textMessage);
				} else if ("人工服务".equals(content)) {
					CustomerServiceMessage csm = new CustomerServiceMessage();
					csm.setFromUserName(toUserName);
					csm.setToUserName(fromUserName);
					csm.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_CUSTOMER_SERVICE);
					csm.setCreateTime(new Date().getTime());
					respXml = MessageUtil.messageToXml(csm);
				}else {
					if(toUserName.equals("gh_c8888d381b2c")){
						textMessage.setContent("王狗狗、文本消息！");
					}
					if(toUserName.equals("gh_e91e3bcc90c2")){
						textMessage.setContent("丁笑笑、文本消息！");
					}
					respXml = MessageUtil.messageToXml(textMessage);
				}
			} else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
				// 图片消息
				String imageUrl = requestMap.get("PicUrl");
				System.out.println(imageUrl);
				textMessage.setContent(FacePlusPlusUtil.detectFace(imageUrl));
				respXml = MessageUtil.messageToXml(textMessage);
			} else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
				// 链接消息
				textMessage.setContent("链接消息");
				respXml = MessageUtil.messageToXml(textMessage);
			} else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
				// 地理位置消息
				textMessage.setContent("地理位置消息");
				respXml = MessageUtil.messageToXml(textMessage);
			} else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VIDEO)) {
				// 视频消息
				textMessage.setContent("视频消息");
				respXml = MessageUtil.messageToXml(textMessage);
			} else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
				// 音频消息
				String recognition = requestMap.get("Recognition");
				
				
				SemanticParam sp = new SemanticParam();
				sp.setAppid("wx56803fead87914d7");
				sp.setCategory("weather");
				sp.setCity("天津");
				sp.setQuery(recognition);
				sp.setUid("o8GTCuNm5UZYsCwgDrZ2-rfoa5YA");
				
				String reqJSON = JSONObject.fromObject(sp).toString();
				// TODO 需要去掉
				Token token = PublicUtil.getAccessToken("wx56803fead87914d7", "8f8653c4d1580f7eca8efefaa2513e63");

				String respJSON = AdvancedUtil.semproxy(token.getAccess_token(), reqJSON);
				JSONObject jsonObject = JSONObject.fromObject(respJSON);
				int errorCode = jsonObject.getInt("errcode");
				String city = "天津";
				if(0 == errorCode){
					JSONObject semanticObject = (JSONObject) jsonObject.get("semantic");
					JSONObject detailsObject = (JSONObject) semanticObject.get("details");
					JSONObject locationObject = (JSONObject) detailsObject.get("location");
					city = locationObject.getString("city");
					List<Article> articles = WeatherUtil.queryWeatherIMG(city);
					if (articles != null) {
						NewsMessage nm = new NewsMessage();
						nm.setFromUserName(toUserName);
						nm.setToUserName(fromUserName);
						nm.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
						nm.setCreateTime(new Date().getTime());
						nm.setArticleCount(articles.size());
						nm.setArticles(articles);
						respXml = MessageUtil.messageToXml(nm);
					} else {
						textMessage.setContent("请输入正确的城市名称");
						respXml = MessageUtil.messageToXml(textMessage);

					}
				}else{
					System.out.println("语义理解错误"+errorCode);
					textMessage.setContent("语义理解错误"+errorCode);
					respXml = MessageUtil.messageToXml(textMessage);
				}
			} else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
				// 事件
				// 时间类型
				String eventType = requestMap.get("Event");
				if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
					// 关注事件
					if(toUserName.equals("gh_c8888d381b2c")){
						textMessage.setContent("王狗狗，欢迎您关注！");
					}
					if(toUserName.equals("gh_e91e3bcc90c2")){
						textMessage.setContent("丁笑笑，欢迎您关注！");
					}
					signDao.saveWeixinUser(fromUserName);
					respXml = MessageUtil.messageToXml(textMessage);
				} else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
					// 取消关注事件
				} else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
					// 菜单点击事件
					// 事件类型
					String eventKey = requestMap.get("EventKey");
					// 天气预报
					if (eventKey.equals("KEY_WEATHER")) {
						textMessage.setContent("您点击了天气预报！");
						respXml = MessageUtil.messageToXml(textMessage);
					} else if (eventKey.equals("KEY_MUSIC")) {
						textMessage.setContent("您点击了每日歌曲");
						respXml = MessageUtil.messageToXml(textMessage);
					} 
				}else if(eventType.equals(MessageUtil.EVENT_TYPE_SCANCODE_WAITMSG)) {
					// 扫码带提示   可以 回信息
					String eventKey = requestMap.get("EventKey");
					System.out.println(requestMap);
					String scanResult = requestMap.get("ScanResult");
					if (eventKey.equals("KEY_11")) {
						textMessage.setContent("您点击了【扫码带提示】菜单！\n\n扫码结果："+scanResult);
						respXml = MessageUtil.messageToXml(textMessage);
					} 
				} else if(eventType.equals(MessageUtil.EVENT_TYPE_SCANCODE_PUSH)) {
					// 扫码推事件   只会显示结果
					String eventKey = requestMap.get("EventKey");
					String scanResult = requestMap.get("ScanResult");
					if (eventKey.equals("KEY_12")) {
						textMessage.setContent("您点击了【扫码推事件】菜单！\n\n扫码结果："+scanResult);
						respXml = MessageUtil.messageToXml(textMessage);
					} 
				}  else if(eventType.equals(MessageUtil.EVENT_TYPE_PIC_SYSPHOTO)) {
					// 拍照
					String eventKey = requestMap.get("EventKey");
					if (eventKey.equals("KEY_21")) {
						System.out.println("您点击了【拍照】菜单！");
						textMessage.setContent("您点击了【拍照】菜单！");
						respXml = MessageUtil.messageToXml(textMessage);
					} 
				}  else if(eventType.equals(MessageUtil.EVENT_TYPE_PIC_PHOTO_OR_ALBUM)) {
					// 拍照或相册选择
					String eventKey = requestMap.get("EventKey");
					if (eventKey.equals("KEY_22")) {
						textMessage.setContent("您点击了【拍照或相册选择】菜单！");
						respXml = MessageUtil.messageToXml(textMessage);
					} 
				}  else if(eventType.equals(MessageUtil.EVENT_TYPE_PIC_WEIXIN)) {
					// 相册选择
					String eventKey = requestMap.get("EventKey");
					if (eventKey.equals("KEY_23")) {
						textMessage.setContent("您点击了【相册选择】菜单！");
						respXml = MessageUtil.messageToXml(textMessage);
					} 
				}  else if(eventType.equals(MessageUtil.EVENT_TYPE_LOCATION_SELECT)) {
					// 位置
					String eventKey = requestMap.get("EventKey");
					String locationX = requestMap.get("Location_X");
					String locationY = requestMap.get("Location_Y");
					if (eventKey.equals("KEY_31")) {
						textMessage.setContent("您点击了【位置】菜单！\n\nx:"+locationX+"\n\ny:"+locationY);
						respXml = MessageUtil.messageToXml(textMessage);
					} 
				}  else if(eventType.equals(MessageUtil.EVENT_TYPE_LOCATION)) {
					String Longitude = requestMap.get("Longitude");
					String Latitude = requestMap.get("Latitude");
					System.out.println(String.format("【自动上报地理位置】经度：%s 维度：%s", Longitude,Latitude));
					
				}
			}

		} catch (Exception e) {
			LOGGER.info(e);
		}
		// 将文本消息对象转换成xml
		return respXml;
	}

	private String getMondayOfThisWeek() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar c = Calendar.getInstance();
		// 得到今天是星期几
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		// 对星期天的特殊处理
		if (dayOfWeek == 0) {
			dayOfWeek = 7;
		}
		c.add(Calendar.DATE, -(dayOfWeek - 1));
		return df.format(c.getTime());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void testDao() {
		// TODO Auto-generated method stub
		LOGGER.info(signDao.isTodaySigned("oj_xDw-bDeq7q6P19urYJ1fEwjT4"));
		signDao.saveWeixinSign("oj_xDw-bDeq7q6P19urYJ1fEwjT4", 12);
		signDao.updateUserPoints("oj_xDw-bDeq7q6P19urYJ1fEwjT4", 12);
	}

}
