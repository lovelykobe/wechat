package com.tjaide.http;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import com.tjaide.wechat.core.utils.MyX509TrustManager;


public class HttpsTest {
	public static void main(String[] args) throws Exception {
		//https://www.12306.cn/  https://mp.weixin.qq.com/
		TrustManager[] tm = { new MyX509TrustManager() };
		SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
		sslContext.init(null, tm, new java.security.SecureRandom());
		// 从上述SSLContext对象中得到SSLSocketFactory对象
		SSLSocketFactory ssf = sslContext.getSocketFactory();
		
		URL url = new URL("https://kyfw.12306.cn/");
		HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
		conn.setSSLSocketFactory(ssf);
		conn.setRequestMethod("GET");
		conn.connect();
		
		InputStream is = conn.getInputStream();
		InputStreamReader isr = new InputStreamReader(is,"utf-8");
		BufferedReader br = new BufferedReader(isr);
		StringBuffer buffer = new StringBuffer();
		String line = null;
		while((line=br.readLine())!=null){
			buffer.append(line);
		}
		System.out.println(buffer.toString());
		
	}
}
