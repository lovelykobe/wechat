package com.tjaide.wechat.dao;

public interface SignDao extends BaseDao{
	//保存微信用户
	void saveWeixinUser(String openId);
	//保存用户签到
	void saveWeixinSign(String openId,int signPoints);
	//更新用户总积分
	void updateUserPoints(String openId,int signPoints);
	//判断用户今天是否签到
	boolean isTodaySigned(String openId);
	//判断用户本周是否第7次签到
	/**
	 * @author wangjun
	 * @param @param openId
	 * @param @param monday 本周一（yyyy-mm-dd HH:mm:ss）
	 * @param @return   
	 * @return boolean  
	 * @throws
	 * @date 2015年7月12日下午9:17:34
	 * @Description: TODO
	 */
	boolean isSevenSign(String openId,String monday);
	
}
