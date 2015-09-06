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

		
		ClickButton btn11 = new ClickButton();
		btn11.setName("天气预报");
		btn11.setKey("KEY_WEATHER");
		btn11.setType("click");
		
		ClickButton btn21 = new ClickButton();
		btn21.setName("每日歌曲");
		btn21.setKey("KEY_MUSIC");
		btn21.setType("click");
		
		ViewButton btn22 = new ViewButton();
		btn22.setName("百度");
		btn22.setType("view");
		btn22.setUrl("http://www.baidu.com/");
		
		ViewButton btn23 = new ViewButton();
		btn23.setName("微社区");
		btn23.setType("view");
		btn23.setUrl("http://www.baidu.com/");
		
		ComplexButton btn2 = new ComplexButton();
		btn2.setName("菜单");
		btn2.setSub_button(new Button[]{btn21,btn22,btn23});
		menu = new Menu();
		menu.setButton(new Button[]{btn11,btn2});
	}
	
	@Test
	public void getMenu() {
		
		System.out.println(JSONObject.fromObject(menu).toString());
		
		Token token = PublicUtil.getAccessToken("wx56803fead87914d7", "8f8653c4d1580f7eca8efefaa2513e63");
		
		boolean result = MenuUtil.createMenu(JSONObject.fromObject(menu).toString(), token.getAccess_token());
		//String result = MenuUtil.getMenu(token.getAccess_token());
		
		System.out.println(result);
	}
}
