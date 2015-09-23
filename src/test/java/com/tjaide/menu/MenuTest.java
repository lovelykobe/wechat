package com.tjaide.menu;

import org.junit.BeforeClass;
import org.junit.Test;

import net.sf.json.JSONObject;

import com.tjaide.wechat.core.bean.menu.Button;
import com.tjaide.wechat.core.bean.menu.ClickButton;
import com.tjaide.wechat.core.bean.menu.ComplexButton;
import com.tjaide.wechat.core.bean.menu.Menu;
import com.tjaide.wechat.core.bean.menu.ViewButton;
import com.tjaide.wechat.core.bean.weixin.Token;
import com.tjaide.wechat.core.utils.MenuUtil;
import com.tjaide.wechat.core.utils.PublicUtil;

public class MenuTest {
	
	private static Menu menu = null;
	@BeforeClass
	public static void init(){

		
//		ClickButton btn11 = new ClickButton();
//		btn11.setName("天气预报");
//		btn11.setKey("KEY_WEATHER");
//		btn11.setType("click");
//		
//		ClickButton btn21 = new ClickButton();
//		btn21.setName("每日歌曲");
//		btn21.setKey("KEY_MUSIC");
//		btn21.setType("click");
//		
//		ViewButton btn22 = new ViewButton();
//		btn22.setName("百度");
//		btn22.setType("view");
//		btn22.setUrl("http://www.baidu.com/");
//		
//		ViewButton btn23 = new ViewButton();
//		btn23.setName("微社区");
//		btn23.setType("view");
//		btn23.setUrl("http://www.baidu.com/");
//		
//		ComplexButton btn2 = new ComplexButton();
//		btn2.setName("菜单");
//		btn2.setSub_button(new Button[]{btn21,btn22,btn23});
//		menu = new Menu();
//		menu.setButton(new Button[]{btn11,btn2});
		
		ClickButton btn11 = new ClickButton();
		btn11.setName("扫码带提示");
		btn11.setKey("KEY_11");
		btn11.setType("scancode_waitmsg");
		ClickButton btn12 = new ClickButton();
		btn12.setName("扫码推事件");
		btn12.setKey("KEY_12");
		btn12.setType("scancode_push");
		
		ClickButton btn21 = new ClickButton();
		btn21.setName("系统拍照发图");
		btn21.setKey("KEY_21");
		btn21.setType("pic_sysphoto");
		ClickButton btn22 = new ClickButton();
		btn22.setName("拍照或相册选择");
		btn22.setKey("KEY_22");
		btn22.setType("pic_photo_or_album");
		ClickButton btn23 = new ClickButton();
		btn23.setName("相册选择");
		btn23.setKey("KEY_23");
		btn23.setType("pic_weixin");
		

		ClickButton btn31 = new ClickButton();
		btn31.setName("发送位置");
		btn31.setKey("KEY_31");
		btn31.setType("location_select");
		
		
		
		ComplexButton btn1 = new ComplexButton();
		btn1.setName("扫码");
		btn1.setSub_button(new Button[]{btn11,btn12});
		
		ComplexButton btn2 = new ComplexButton();
		btn2.setName("发图");
		btn2.setSub_button(new Button[]{btn21,btn22,btn23});
		
		menu = new Menu();
		menu.setButton(new Button[]{btn1,btn2,btn31});
	}
	
	@Test
	public void getMenu() {
		
		System.out.println(JSONObject.fromObject(menu).toString());
		// 输出ssl 日志
//		System.setProperty("javax.net.debug", "ssl,handshake");
//		System.setProperty("https.protocols", "TLSv1");
		Token token1 = PublicUtil.getAccessToken("wx56803fead87914d7", "8f8653c4d1580f7eca8efefaa2513e63");
		
		Token token = PublicUtil.getAccessToken("wx51d620cbfde8ba2c", "9af9d133c6771ecb7aab2e698be696fa");
		
		boolean result = MenuUtil.createMenu(JSONObject.fromObject(menu).toString(), token.getAccess_token());
		//String result = MenuUtil.getMenu(token.getAccess_token());
		
		System.out.println(result);
	}
}
