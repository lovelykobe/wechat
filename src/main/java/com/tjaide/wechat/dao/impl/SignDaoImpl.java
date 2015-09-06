package com.tjaide.wechat.dao.impl;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.tjaide.wechat.core.database.TableName;
import com.tjaide.wechat.dao.SignDao;
@Repository
public class SignDaoImpl extends BaseDaoImpl implements SignDao {

	@Override
	public void saveWeixinUser(String openId) {
		// TODO 保存微信用户
		String sql = "insert into "+TableName.WEIXINUSER+"(open_id,subscribe_time,subscribe_status) values(?,now(),1)";
		this.executeUpdate(sql, new Object[]{openId});
	}

	@Override
	public void saveWeixinSign(String openId,int signPoints) {
		// TODO 保存用户签到
		String sql = "insert into "+TableName.WEIXINSIGN+"(open_id,sign_time,sign_points) values(?,now(),?)";
		this.executeUpdate(sql, new Object[]{openId,signPoints});
	}

	@Override
	public void updateUserPoints(String openId, int signPoints) {
		// TODO 更新用户总积分
		String selectSql = "select * from "+TableName.WEIXINUSER+" where open_id = ?";
		Map<String,Object> userMap = this.queryForMap(selectSql, new Object[]{openId}, SignDaoImpl.class);
		signPoints = Integer.parseInt(userMap.get("points").toString())+signPoints;
		String sql = "update "+TableName.WEIXINUSER+" set points = ? where open_id = ?";
		this.executeUpdate(sql, new Object[]{signPoints,openId});
		
	}

	@Override
	public boolean isTodaySigned(String openId) {
		// TODO 判断用户今天是否签到
		String sql = "select count(*) from "+TableName.WEIXINSIGN+" where open_id = ? AND DATE_FORMAT(sign_time,'%Y-%m-%d') = DATE_FORMAT(now(),'%Y-%m-%d')";
		int result = this.queryForInt(sql, new Object[]{openId}, SignDaoImpl.class);
		return result > 0 ? true:false;
	}

	@Override
	public boolean isSevenSign(String openId, String monday) {
		// TODO 判断用户本周是否第7次签到
		String sql = "select count(*) from "+TableName.WEIXINSIGN+" where open_id = ? AND sign_time between str_to_date('?','%Y-%m-%d %H:%i:%s') and now()";
		int result = this.queryForInt(sql, new Object[]{openId}, SignDaoImpl.class);
		return result == 6 ? true : false;
	}

}
