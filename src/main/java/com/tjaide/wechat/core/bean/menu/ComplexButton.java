package com.tjaide.wechat.core.bean.menu;

/**
 * @author wangjun
 * @param      
 * @throws
 * @date 2015年7月14日下午2:26:35
 * @Description: 复合类型的按钮(包含二级菜单的一级菜单)
 */
public class ComplexButton extends Button {
	private Button[] sub_button;

	public Button[] getSub_button() {
		return sub_button;
	}

	public void setSub_button(Button[] sub_button) {
		this.sub_button = sub_button;
	}
}
