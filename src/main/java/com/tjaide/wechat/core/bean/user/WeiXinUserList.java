package com.tjaide.wechat.core.bean.user;

import java.util.List;

/**
 * 用户列表
 * @author jun
 *
 */
public class WeiXinUserList {

	private int total;
	
	private int count;
	
	private List<String> openidList;
	
	private String nextOpenid;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<String> getOpenidList() {
		return openidList;
	}

	public void setOpenidList(List<String> openidList) {
		this.openidList = openidList;
	}

	public String getNextOpenid() {
		return nextOpenid;
	}

	public void setNextOpenid(String nextOpenid) {
		this.nextOpenid = nextOpenid;
	}
	
	
}
