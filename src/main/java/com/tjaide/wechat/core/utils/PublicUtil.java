package com.tjaide.wechat.core.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.config.ConfigurationException;
import org.apache.shiro.subject.Subject;

import com.tjaide.wechat.core.bean.weixin.Token;
import com.tjaide.wechat.core.shiro.ShiroUser;

/**
 * @author wangjun
 * @param
 * @throws
 * @date 2015年7月13日下午1:56:43
 * @Description: TODO
 */
public class PublicUtil {
	
	public static final String PROJECT_ROOT = "http://wonderlucky.imwork.net";

	private static final Logger LOGGER = Logger.getLogger(PublicUtil.class);

	private static long randomSeed = System.currentTimeMillis();

	private static String oldSerialId = "";

	private static java.util.Random rand = null;

	private static String web_inf_Path = PublicUtil.class.getResource("/").getPath();
	/**
	 * 获取access_token 请求地址 GET方式
	 */
	public static String GET_ACCESSTOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

	/**
	 * 生成一个18位长度的字符串序号
	 */
	public static synchronized String generalStandardId() {
		String id = "";
		do {
			id = "";
			String str = "";
			int number = 0;
			java.util.Calendar datatime = java.util.Calendar.getInstance();
			number = datatime.get(Calendar.YEAR);
			str = new Integer(number).toString();
			id = id + str.substring(0, 4);

			number = datatime.get(Calendar.MONTH) + 101;
			str = new Integer(number).toString();
			id = id + str.substring(1, 3);

			number = datatime.get(Calendar.DAY_OF_MONTH) + 100;
			str = new Integer(number).toString();
			id = id + str.substring(1, 3);

			number = datatime.get(Calendar.HOUR_OF_DAY) + 100;
			str = new Integer(number).toString();
			id = id + str.substring(1, 3);

			number = datatime.get(Calendar.MINUTE) + 100;
			str = new Integer(number).toString();
			id = id + str.substring(1, 3);

			number = datatime.get(Calendar.SECOND) + 100;
			str = new Integer(number).toString();
			id = id + str.substring(1, 3);
			rand = new java.util.Random(randomSeed);
			number = rand.nextInt(9999) + 10000;
			str = new Integer(number).toString();
			id = id + str.substring(1, 5);
			randomSeed = rand.nextLong();
		} while (id.equals(oldSerialId));
		oldSerialId = id;
		return id;
	}

	/**
	 * 获得系统时间，格式为yyyy-MM-dd HH:mm:ss
	 */
	public static synchronized String getSystemTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		return df.format(new Date());
	}

	/**
	 * 读取配置文件的文件名，在默认在classes根目录下，如果失败再读取WEB-INF目录下
	 * 
	 * @param propertiesFileName
	 *            配置文件的文件全名
	 * @param cpath
	 *            要获得某个属性的名称
	 * @return 某个属性对应的值
	 */
	public static synchronized String getConfig(String propertiesFileName, String cpath) {
		try {
			PropertiesConfiguration config = new PropertiesConfiguration(propertiesFileName);
			long reloadPeriod = 0;
			FileChangedReloadingStrategy fs = new FileChangedReloadingStrategy();
			fs.setConfiguration(config);
			if (reloadPeriod > 0) {
				fs.setRefreshDelay(reloadPeriod);
			}
			config.setReloadingStrategy(fs);
			String str = config.getString(cpath);
			return str == null ? "" : str;
		} catch (ConfigurationException ce) {
			try {
				String path = web_inf_Path.substring(0, web_inf_Path.length() - 8) + propertiesFileName;
				FileInputStream is = null;
				File file = new File(path);
				try {
					file.createNewFile();
					is = new FileInputStream(file);
					Properties pro = new Properties();
					pro.load(is);
					is.close();
					return pro.getProperty(cpath);
				} catch (Exception e) {
					return "";
				}
			} catch (Exception e) {
				return "";
			}
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 去除html所有样式
	 */
	public static synchronized String Html2Text(String inputString) {
		String htmlStr = inputString; // 含html标签的字符串
		String textStr = "";
		java.util.regex.Pattern p_script;
		java.util.regex.Matcher m_script;
		java.util.regex.Pattern p_style;
		java.util.regex.Matcher m_style;
		java.util.regex.Pattern p_html;
		java.util.regex.Matcher m_html;

		try {
			String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
			// 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
			// }
			String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
			// 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
			// }
			String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式

			p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			m_script = p_script.matcher(htmlStr);
			htmlStr = m_script.replaceAll(""); // 过滤script标签

			p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			m_style = p_style.matcher(htmlStr);
			htmlStr = m_style.replaceAll(""); // 过滤style标签

			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			htmlStr = m_html.replaceAll(""); // 过滤html标签

			textStr = htmlStr.replaceAll("&nbsp;", "").replaceAll("\r\n", "").replaceAll("　", "");
		} catch (Exception e) {

		}
		return textStr;// 返回文本字符串
	}

	/**
	 * 将字符串转化为html代码
	 */
	public static synchronized String formatInputStr(String str) {
		try {
			str = str.replaceAll("&", "&amp;");
			str = str.replaceAll("'", "&apos;");
			str = str.replaceAll("\"", "&quot;");
			str = str.replaceAll("\\\\", "\\\\\\\\");
			str = str.replaceAll("<", "&lt;");
			str = str.replaceAll(">", "&gt;");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return str;
	}

	/**
	 * 将html代码转换为页面可显示代码
	 */
	public static synchronized String formatOutputStr(String str) {
		str = str.replaceAll("&amp;", "&");
		str = str.replaceAll("&amp;nbsp;", " ");
		str = str.replaceAll("&apos;", "'");
		str = str.replaceAll("\\\\\\\\", "\\\\");
		str = str.replaceAll("&lt;", "<");
		str = str.replaceAll("&gt;", ">");
		str = str.replaceAll("&quot;", "\"");
		return str;
	}

	public static String nullToString(String value) {
		if (value == null) {
			return "";
		}
		return value;
	}

	public static ShiroUser getLoginUmnb() {
		Subject subject = SecurityUtils.getSubject();
		ShiroUser shiroUser = (ShiroUser) subject.getPrincipal();
		return shiroUser;
	}

	public static String getpfHomePage(HttpServletRequest request) {
		String localPage = request.getRequestURL().toString().split(request.getContextPath().toString())[0];
		String pfhomePage = localPage.split("//")[1];
		return pfhomePage;
	}

	/**
	 * @author wangjun
	 * @param @param content
	 * @param @return
	 * @return int
	 * @throws
	 * @date 2015年7月13日下午1:56:48
	 * @Description: 得到内容的字节数（UTF-8）
	 */
	public static int getByteSize(String content) {
		int size = 0;
		if (null != content) {
			try {
				size = content.getBytes("UTF-8").length;
			} catch (UnsupportedEncodingException e) {
				LOGGER.info(e);
			}
		}
		return size;
	}

	/**
	 * emoji表情转换(hex -> utf-16)
	 * 
	 * @param hexEmoji
	 * @return
	 */
	public static String emoji(int hexEmoji) {
		return String.valueOf(Character.toChars(hexEmoji));
	}

	/**
	 * 发起https请求并获取结果
	 * 
	 * @param requestUrl
	 *            请求地址
	 * @param requestMethod
	 *            请求方式（GET、POST）
	 * @param outputStr
	 *            提交的数据
	 * @return String
	 */
	public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
		StringBuffer buffer = new StringBuffer();
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod))
				httpUrlConn.connect();

			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
		} catch (ConnectException ce) {
			LOGGER.error("Weixin server connection timed out.");
		} catch (Exception e) {
			LOGGER.error("https request error:{}", e);
		}
		return buffer.toString();
	}
	
	
	/**
	 * 发起http请求并获取结果
	 * 
	 * @param requestUrl
	 *            请求地址
	 * @param requestMethod
	 *            请求方式（GET、POST）
	 * @param outputStr
	 *            提交的数据
	 * @return String
	 */
	public static String httpRequest(String requestUrl, String requestMethod, String outputStr) {
		StringBuffer buffer = new StringBuffer();
		try {

			URL url = new URL(requestUrl);
			HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod))
				httpUrlConn.connect();

			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
		} catch (ConnectException ce) {
			LOGGER.error("Weixin server connection timed out.");
		} catch (Exception e) {
			LOGGER.error("https request error:{}", e);
		}
		return buffer.toString();
	}
	
	
	
	
	

	/**
	 * 获取公众号的唯一凭证
	 * 
	 * @author wangjun
	 * @param @param appid
	 * @param @param appsecret
	 * @param @return
	 * @return Token
	 * @throws
	 * @date 2015年7月14日下午1:56:59
	 * @Description: TODO
	 */
	public static Token getAccessToken(String appid, String appsecret) {
		Token token = new Token();
		String requestUrl = GET_ACCESSTOKEN_URL.replace("APPID", appid).replace("APPSECRET", appsecret);
		// 调用http请求
		try {
			JSONObject jsonObject = JSONObject.fromObject(httpsRequest(requestUrl, "GET", null));
			String accessToken = jsonObject.getString("access_token");
			int expiresIn = jsonObject.getInt("expires_in");
			token.setAccess_token(accessToken);
			token.setExpires_in(expiresIn);
		} catch (Exception e) {
			LOGGER.info(e);
		}
		return token;
	}

	public static void main(String[] args) {
		String reuestUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx807d87440bdfe9da&secret=e34b12b677aa140882df0f0e4de19a9b";
		System.out.println(httpsRequest(reuestUrl, "GET", null));
	}
	
	/**
	 * @author wangjun
	 * @param @param source
	 * @param @return   
	 * @return String  
	 * @throws
	 * @date 2015年7月15日上午10:02:11
	 * @Description: URL编码 UTF-8
	 */
	public static String urlEncodeUTF8(String source){
		String result = source;
		try {
			result = URLEncoder.encode(source,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			LOGGER.error(e);
		}
		return result;
	}
	//识别微信浏览器
	public static boolean isMicroMessenger(HttpServletRequest request){
		boolean result = false;
		String userAgent = request.getHeader("User-Agent");
		if(userAgent.contains("MicroMessenger"))
			result = true;
		return result;
	}
}
