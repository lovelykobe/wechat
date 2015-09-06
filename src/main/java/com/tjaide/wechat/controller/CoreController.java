package com.tjaide.wechat.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tjaide.wechat.core.bean.TextItem;
import com.tjaide.wechat.core.bean.TextMessageKf;
import com.tjaide.wechat.core.bean.weixin.Token;
import com.tjaide.wechat.core.utils.MessageUtil;
import com.tjaide.wechat.core.utils.PublicUtil;
import com.tjaide.wechat.core.utils.SignUtil;
import com.tjaide.wechat.service.CoreService;

@RestController
@RequestMapping(value = "/core")
public class CoreController {

	private static final Logger LOGGER = Logger.getLogger(CoreController.class);

	public static final String CALL_CENTER_REPLAY_URL = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token={ACCESS_TOKEN}";
	private String result;
	@Autowired
	private CoreService coreService;

	@RequestMapping(value = "/validation/{wechatid}", method = { RequestMethod.POST, RequestMethod.GET })
	public Map<String, Object> validation(@PathVariable("wechatid") String wechatid,HttpServletRequest request, HttpServletResponse response) {
		String method = request.getMethod();
		System.out.println("====================="+wechatid);
		String signature = request.getParameter("signature");

		String echostr = request.getParameter("echostr");

		String timestamp = request.getParameter("timestamp");

		String nonce = request.getParameter("nonce");
		// 加解密类型
		String encryptType = request.getParameter("encrypt_type");

		LOGGER.info("加密类型" + encryptType);
		Map<String, String> requestMap = null;
		try {
			if (request.getParameter("debug") != null) {
				try {
					request.setCharacterEncoding("UTF-8");
					response.setCharacterEncoding("UTF-8");
					requestMap = MessageUtil.parseXml(request);
					result = coreService.process(requestMap);// 调用CoreService类的processRequest方法接收、处理消息，并得到处理结果；
				} catch (Exception e) {
					LOGGER.info(e);
				}
			} else {
				if (SignUtil.checkSignature(signature, timestamp, nonce)) {
					// 调用parseXml方法解析请求消息
					if (method.equalsIgnoreCase("get")) {
						result = echostr;
					} else if (method.equalsIgnoreCase("post")) {
						try {
							request.setCharacterEncoding("UTF-8");
							response.setCharacterEncoding("UTF-8");
							if ("aes".equals(encryptType)) {
								// 加解密模式
								requestMap = MessageUtil.parseXmlCrypt(request);
								result = MessageUtil.getWxCrypt().encryptMsg(coreService.process(requestMap),
										timestamp, nonce);
								LOGGER.info("加解" + result);
							} else {
								// 明文模式
								requestMap = MessageUtil.parseXml(request);
								// 调用CoreService类的processRequest方法接收、处理消息，并得到处理结果；
								result = coreService.process(requestMap);
								LOGGER.info("明文" + result);
							}

						} catch (Exception e) {
							LOGGER.info(e);
						}
					}
				} else {
					LOGGER.info("不是微信服务器发来的请求,请小心!");
				}
			}
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out;
			try {
				out = response.getWriter();
				out.println(result);// 返回的字符串数据
			} catch (IOException e) {
				LOGGER.info(e);
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/testDao", method = { RequestMethod.POST, RequestMethod.GET })
	public void testDao() {
		coreService.testDao();
	}

	@RequestMapping(value = "/replayMe", method = { RequestMethod.GET })
	public String replayMe(HttpServletRequest request) {
		String replayContent = request.getParameter("content");
		List<String> openIds = new ArrayList<String>();
		openIds.add("o8GTCuKSBhKZzJdSD8VPL94rHel0");
		openIds.add("o8GTCuIhV1Bb_9amaK4AeCdv-WIQ");
		Token token = PublicUtil.getAccessToken("wx56803fead87914d7", "8f8653c4d1580f7eca8efefaa2513e63");
		String url = CALL_CENTER_REPLAY_URL.replace("{ACCESS_TOKEN}", token.getAccess_token());
		for (String tempStr : openIds) {
			TextMessageKf customMessage = new TextMessageKf();
			customMessage.setMsgtype("text");
			TextItem textItem = new TextItem();
			textItem.setContent(replayContent);
			customMessage.setText(textItem);
			customMessage.setTouser(tempStr);

			JSONObject jsonObj = JSONObject.fromObject(customMessage);

			LOGGER.info("发送消息：" + jsonObj.toString());
			JSONObject result = JSONObject.fromObject(PublicUtil.httpsRequest(url, "POST", jsonObj.toString()));
			if (!result.getString("errcode").toString().equals("0")) {
				return "0";
			}
		}
		return "1";
	}
}
