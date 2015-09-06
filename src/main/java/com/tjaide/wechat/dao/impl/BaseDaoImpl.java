package com.tjaide.wechat.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.tjaide.wechat.core.database.SpringDB;
import com.tjaide.wechat.dao.BaseDao;


@Repository
public class BaseDaoImpl extends SpringDB implements BaseDao {
	
	/**
	 * 添加数据
	 * 
	 */
	@Override
	public int save(String tableName, Map<String, Object> map, Class<?> clazz) {
		Logger log = Logger.getLogger(clazz);
		try {
			if (tableName != null && tableName.length() > 0) {
				StringBuffer sql = new StringBuffer();
				StringBuffer sql2 = new StringBuffer();
				List<Object> params = new ArrayList<Object>();
				sql.append("insert into " + tableName + " (");
				for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
					String colName = iterator.next();
					Object colVal = map.get(colName);
					if (iterator.hasNext()) {
						sql.append(colName + ",");
						sql2.append("?, ");
					} else {
						sql.append(colName + ") values(");
						sql2.append("?)");
					}
					params.add(colVal);
				}
				return this.executeUpdate(sql.toString() + sql2.toString(), params.toArray());
			} else {
				return -1;
			}
		} catch (Exception ex) {
			log.error(clazz + ex.getMessage(), ex);
			throw new RuntimeException();
		}
	}

	/**
	 * 修改数据
	 */
	@Override
	public int update(String tableName, Map<String, Object> updateMap, Map<String, Object> whereMap, Class<?> clazz) {
		Logger log = Logger.getLogger(clazz);
		try {
			if (tableName != null && tableName.length() > 0) {
				StringBuffer sql = new StringBuffer();
				sql.append("update " + tableName + " set ");
				List<Object> params = new ArrayList<Object>();
				for (Iterator<String> iterator = updateMap.keySet().iterator(); iterator.hasNext();) {
					String colName = iterator.next();
					Object colVal = updateMap.get(colName);
					if (iterator.hasNext()) {
						sql.append(colName + " = ?, ");
					} else {
						sql.append(colName + " = ? where ");
					}
					params.add(colVal);
				}
				for (Iterator<String> iterator = whereMap.keySet().iterator(); iterator.hasNext();) {
					String colName = iterator.next();
					String colVal = whereMap.get(colName).toString();
					if (iterator.hasNext()) {
						sql.append(colName + " = ? and ");
					} else {
						sql.append(colName + " = ?");
					}
					params.add(colVal);
				}
				return this.executeUpdate(sql.toString(), params.toArray());
			} else {
				return -1;
			}
		} catch (Exception ex) {
			log.error(clazz + ex.getMessage(), ex);
			throw new RuntimeException();
		}
	}

	/**
	 * 刪除数据
	 */
	@Override
	public int delete(String tableName, Map<String, Object> map, Class<?> clazz) {
		Logger log = Logger.getLogger(clazz);
		try {
			if (tableName != null && tableName.length() > 0) {
				StringBuffer sql = new StringBuffer();
				List<Object> params = new ArrayList<Object>();
				sql.append("delete from " + tableName);
				if (!map.isEmpty()) {
					sql.append(" where ");
					for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
						String colName = iterator.next();
						String colVal = map.get(colName).toString();
						if (iterator.hasNext()) {
							sql.append(colName + " = ? and ");
						} else {
							sql.append(colName + " = ?");
						}
						params.add(colVal);
					}
				}
				return this.executeUpdate(sql.toString(), params.toArray());
			} else {
				return -1;
			}
		} catch (Exception ex) {
			log.error(clazz + ex.getMessage(), ex);
			throw new RuntimeException();
		}
	}

	/**
	 * 特殊添加，使用此方法
	 */
	@Override
	public int save(String sql, Object[] params, Class<?> clazz) {
		Logger log = Logger.getLogger(clazz);
		try {
			return super.executeUpdate(sql, params);
		} catch (Exception ex) {
			log.error(clazz + ex.getMessage(), ex);
			throw new RuntimeException();
		}
	}

	/**
	 * 特殊修改，使用此方法
	 */
	@Override
	public int update(String sql, Object[] params, Class<?> clazz) {
		Logger log = Logger.getLogger(clazz);
		try {
			return super.executeUpdate(sql, params);
		} catch (Exception ex) {
			log.error(clazz + ex.getMessage(), ex);
			throw new RuntimeException();
		}
	}

	/**
	 * 特殊删除，使用此方法
	 */
	@Override
	public int delete(String sql, Object[] params, Class<?> clazz) {
		Logger log = Logger.getLogger(clazz);
		try {
			return super.executeUpdate(sql, params);
		} catch (Exception ex) {
			log.error(clazz + ex.getMessage(), ex);
			throw new RuntimeException();
		}
	}

	/**
	 * 查询结果，类型为Map
	 */
	@Override
	public Map<String, Object> queryForMap(String sql, Object[] params, Class<?> clazz) {
		Logger log = Logger.getLogger(clazz);
		try {
			return super.queryForMap(sql, params, clazz);
		} catch (Exception ex) {
			log.error(clazz + ex.getMessage(), ex);
			throw new RuntimeException();
		}
	}

	/**
	 * 查询结果，类型为List
	 */
	@Override
	public List<Map<String, Object>> queryForList(String sql, Object[] params, Class<?> clazz) {
		return super.queryForList(sql, params, clazz);
	}

	/**
	 * 查询结果，类型为List
	 */
	@Override
	public List<Map<String, Object>> queryForList(String sql, Object[] params, int count, int pageNo, int pageSize, Class<?> clazz) {
		return super.fetchPage(sql, params, count, pageNo, pageSize).getPageItems();
	}
	/**
	 * 查询结果，类型为List
	 */
	@Override
	public List<Map<String, Object>> queryForList(String sql, Object[] params, int count, int start,int end, int pageSize, Class<?> clazz) {
		List<Map<String,Object>> list = super.fetchPage(sql, params, count, start,end, pageSize).getPageItems();
		return list;
	}
	/**
	 * 查询结果，类型为String
	 */
	@Override
	public String queryForString(String sql, Object[] params, String tableColumn, Class<?> clazz) {
		return super.queryForMap(sql, params, clazz).get(tableColumn).toString();
	}

	/**
	 * 查询结果，类型为int
	 */
	@Override
	public int queryForInt(String sql, Object[] params, Class<?> clazz) {
		return super.queryForInt(sql, params, clazz);
	}
}
