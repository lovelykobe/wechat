package com.tjaide.wechat.core.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import com.tjaide.wechat.core.bean.CustomerServiceMessage;
import com.tjaide.wechat.core.bean.req.ImageMessage;
import com.tjaide.wechat.core.bean.req.TextMessage;
import com.tjaide.wechat.core.bean.req.VideoMessage;
import com.tjaide.wechat.core.bean.req.VoiceMessage;
import com.tjaide.wechat.core.bean.resp.Article;
import com.tjaide.wechat.core.bean.resp.MusicMessage;
import com.tjaide.wechat.core.bean.resp.NewsMessage;

/**
 * 消息处理工具类
 * 
 * @author wangjun
 * @date 2015-07-12
 */
public class MessageUtil {
	
	private static final Logger LOGGER = Logger.getLogger(MessageUtil.class);
	// 请求消息类型：文本
	public static final String REQ_MESSAGE_TYPE_TEXT = "text";
	// 请求消息类型：图片
	public static final String REQ_MESSAGE_TYPE_IMAGE = "image";
	// 请求消息类型：语音
	public static final String REQ_MESSAGE_TYPE_VOICE = "voice";
	// 请求消息类型：视频
	public static final String REQ_MESSAGE_TYPE_VIDEO = "video";
	// 请求消息类型：地理位置
	public static final String REQ_MESSAGE_TYPE_LOCATION = "location";
	// 请求消息类型：链接
	public static final String REQ_MESSAGE_TYPE_LINK = "link";

	// 请求消息类型：事件推送
	public static final String REQ_MESSAGE_TYPE_EVENT = "event";

	// 事件类型：subscribe(订阅)
	public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";
	// 事件类型：unsubscribe(取消订阅)
	public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";
	// 事件类型：scan(用户已关注时的扫描带参数二维码)
	public static final String EVENT_TYPE_SCAN = "scan";
	// 事件类型：LOCATION(上报地理位置)
	public static final String EVENT_TYPE_LOCATION = "LOCATION";
	// 事件类型：CLICK(自定义菜单)
	public static final String EVENT_TYPE_CLICK = "CLICK";
	// 事件类型：scancode_waitmsg(自定义菜单)
	public static final String EVENT_TYPE_SCANCODE_WAITMSG = "scancode_waitmsg";
	// 事件类型：scancode_push(自定义菜单)
	public static final String EVENT_TYPE_SCANCODE_PUSH = "scancode_push";
	// 事件类型：pic_sysphoto(自定义菜单)
	public static final String EVENT_TYPE_PIC_SYSPHOTO = "pic_sysphoto";
	// 事件类型：pic_photo_or_album(自定义菜单)
	public static final String EVENT_TYPE_PIC_PHOTO_OR_ALBUM = "pic_photo_or_album";
	// 事件类型：pic_weixin(自定义菜单)
	public static final String EVENT_TYPE_PIC_WEIXIN = "pic_weixin";
	// 事件类型：location_select(自定义菜单)
	public static final String EVENT_TYPE_LOCATION_SELECT = "location_select";

	// 响应消息类型：文本
	public static final String RESP_MESSAGE_TYPE_TEXT = "text";
	// 响应消息类型：图片
	public static final String RESP_MESSAGE_TYPE_IMAGE = "image";
	// 响应消息类型：语音
	public static final String RESP_MESSAGE_TYPE_VOICE = "voice";
	// 响应消息类型：视频
	public static final String RESP_MESSAGE_TYPE_VIDEO = "video";
	// 响应消息类型：音乐
	public static final String RESP_MESSAGE_TYPE_MUSIC = "music";
	// 响应消息类型：图文
	public static final String RESP_MESSAGE_TYPE_NEWS = "news";
	
	// 多客服
	public static final String RESP_MESSAGE_TYPE_CUSTOMER_SERVICE = "transfer_customer_service";
	
	
	//获取微信加解密实例
	public static WXBizMsgCrypt getWxCrypt(){
		WXBizMsgCrypt pc = null;
		try {
			pc = new WXBizMsgCrypt(SignUtil.token, "PFgn9maSBcmVFupL00sMljZxi5RVQgiPzpH1kvlGpXV", "wx807d87440bdfe9da");
		} catch (AesException e) {
			// TODO Auto-generated catch block
			LOGGER.info(e);
		}
		return pc;
	}
	
	/**
	 * 解析微信发来的请求（XML）
	 * 明文模式解析请求参数
	 * @param request
	 * @return Map<String, String>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> parseXml(HttpServletRequest request) throws Exception {
		// 将解析结果存储在HashMap中
		Map<String, String> map = new HashMap<String, String>();

		// 从request中取得输入流
		InputStream inputStream = request.getInputStream();
		// 读取输入流
		SAXReader reader = new SAXReader();
		Document document = reader.read(inputStream);
		// 得到xml根元素
		Element root = document.getRootElement();
		// 得到根元素的所有子节点
		List<Element> elementList = root.elements();

		// 遍历所有子节点
		for (Element e : elementList){
			System.out.println(e.getName()+"+++++++++++++++"+e.getText());
			List<Element> eList = e.elements();
			if(eList!= null){
				for (Element a : eList){
					System.out.println(a.getName()+"+++++++++++++++"+a.getText());
					map.put(a.getName(), a.getText());
				}
			}
			map.put(e.getName(), e.getText());
		}

		// 释放资源
		inputStream.close();
		inputStream = null;

		return map;
	}
	
	/**
	 * 解析微信发来的请求（XML）
	 * 加解密模式解析请求参数
	 * @param request
	 * @return Map<String, String>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> parseXmlCrypt(HttpServletRequest request) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		// 从inputstream中读取xml文本
		InputStream inputStream = request.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		//每次读取的内容
		String line = null;
		//最终读取的内容
		StringBuffer buffer = new StringBuffer();
		
		while ((line = br.readLine())!=null) {
			buffer.append(line);
		}
		br.close();
		inputStream.close();
		//将xml解密变成明文
		String msgSignature = request.getParameter("msg_signature");
		String timeStamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		WXBizMsgCrypt wXBizMsgCrypt = MessageUtil.getWxCrypt();
		String fromXML = wXBizMsgCrypt.decryptMsg(msgSignature, timeStamp, nonce, buffer.toString());	
		//解析xml获取请求参数
		Document doc = DocumentHelper.parseText(fromXML);
		// 将解析结果存储在HashMap中

		// 从request中取得输入流
		// 读取输入流
		// 得到xml根元素
		Element root = doc.getRootElement();
		// 得到根元素的所有子节点
		List<Element> elementList = root.elements();

		// 遍历所有子节点
		for (Element e : elementList)
			map.put(e.getName(), e.getText());

		// 释放资源
		inputStream.close();
		inputStream = null;

		return map;
	}

	/**
	 * 扩展xstream使其支持CDATA
	 */
	private static XStream xstream = new XStream(new XppDriver() {
		public HierarchicalStreamWriter createWriter(Writer out) {
			return new PrettyPrintWriter(out) {
				// 对所有xml节点的转换都增加CDATA标记
				boolean cdata = true;

				public void startNode(String name, Class clazz) {
					super.startNode(name, clazz);
				}

				protected void writeText(QuickWriter writer, String text) {
					if (cdata) {
						writer.write("<![CDATA[");
						writer.write(text);
						writer.write("]]>");
					} else {
						writer.write(text);
					}
				}
			};
		}
	});

	/**
	 * 文本消息对象转换成xml
	 * 
	 * @param textMessage 文本消息对象
	 * @return xml
	 */
	public static String messageToXml(TextMessage textMessage) {
		xstream.alias("xml", textMessage.getClass());
		return xstream.toXML(textMessage);
	}

	/**
	 * 图片消息对象转换成xml
	 * 
	 * @param imageMessage 图片消息对象
	 * @return xml
	 */
	public static String messageToXml(ImageMessage imageMessage) {
		xstream.alias("xml", imageMessage.getClass());
		return xstream.toXML(imageMessage);
	}

	/**
	 * 语音消息对象转换成xml
	 * 
	 * @param voiceMessage 语音消息对象
	 * @return xml
	 */
	public static String messageToXml(VoiceMessage voiceMessage) {
		xstream.alias("xml", voiceMessage.getClass());
		return xstream.toXML(voiceMessage);
	}

	/**
	 * 视频消息对象转换成xml
	 * 
	 * @param videoMessage 视频消息对象
	 * @return xml
	 */
	public static String messageToXml(VideoMessage videoMessage) {
		xstream.alias("xml", videoMessage.getClass());
		return xstream.toXML(videoMessage);
	}

	/**
	 * 音乐消息对象转换成xml
	 * 
	 * @param musicMessage 音乐消息对象
	 * @return xml
	 */
	public static String messageToXml(MusicMessage musicMessage) {
		xstream.alias("xml", musicMessage.getClass());
		return xstream.toXML(musicMessage);
	}

	/**
	 * 图文消息对象转换成xml
	 * 
	 * @param newsMessage 图文消息对象
	 * @return xml
	 */
	public static String messageToXml(NewsMessage newsMessage) {
		xstream.alias("xml", newsMessage.getClass());
		xstream.alias("item", new Article().getClass());
		return xstream.toXML(newsMessage);
	}
	
	
	public static String messageToXml(CustomerServiceMessage customerServiceMessage) {
		xstream.alias("xml", customerServiceMessage.getClass());
		return xstream.toXML(customerServiceMessage);
	}
	
}
