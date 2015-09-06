package com.tjaide.wechat.core.database;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

public class SpringDB {

	private static JdbcTemplate jdbc;

	public static void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		SpringDB.jdbc = jdbcTemplate;
	}

	/**
	 * 增、删、改，类型为int
	 * 
	 * @param sql
	 *           要执行的sql语句
	 * @param params
	 *           sql所对应的参数列表
	 * @param clazz
	 *           调用类
	 * @return 成功返回1,失败返回-1
	 */
	public int executeUpdate(String sql, Object[] params) {
		return jdbc.update(sql, params);
	}

	/**
	 * 查询结果，类型为Map
	 * 
	 * @param sql
	 *           要执行的sql语句
	 * @param params
	 *           sql所对应的参数列表
	 * @param clazz
	 *           调用类
	 * @return 成功返回map结果，失败返回null
	 */
	protected Map<String, Object> queryForMap(String sql, Object[] params, Class<?> clazz) {
		// Logger log = Logger.getLogger(clazz);
		try {
			return jdbc.queryForMap(sql, params);
		} catch (DataAccessException e) {
			// log.error(clazz + e.getMessage(), e);
			return Collections.emptyMap();
		}
	}

	/**
	 * 查询结果，类型为List
	 * 
	 * @param sql
	 *           要执行的sql语句
	 * @param params
	 *           sql所对应的参数列表
	 * @param clazz
	 *           调用类
	 * @return 成功返回list结果，失败返回null
	 */
	protected List<Map<String, Object>> queryForList(String sql, Object[] params, Class<?> clazz) {
		Logger log = Logger.getLogger(clazz);
		try {
			return jdbc.queryForList(sql, params);
		} catch (DataAccessException e) {
			log.error(clazz + e.getMessage(), e);
			return Collections.emptyList();
		}
	}

	/**
	 * 查询结果，类型为List
	 * 
	 * @param sql
	 *           要执行的sql语句
	 * @param params
	 *           sql所对应的参数列表
	 * @param pageNo
	 *           当前页码
	 * @param pageSize
	 *           行数
	 * @param clazz
	 *           调用类
	 * @return 成功返回list结果，失败返回null
	 */
	protected List<Map<String, Object>> queryForList(String sql, Object[] params, int count, int pageNo, int pageSize, Class<?> clazz) {
		Logger log = Logger.getLogger(clazz);
		try {
			return fetchPage(sql, params, count, pageNo, pageSize).getPageItems();
		} catch (DataAccessException e) {
			log.error(clazz + e.getMessage(), e);
			return Collections.emptyList();
		}
	}

	/**
	 * 查询结果，类型为String
	 * 
	 * @param sql
	 *           要执行的sql语句
	 * @param params
	 *           sql所对应的参数列表
	 * @param tableColumn
	 *           所要查询表的列名
	 * @param clazz
	 *           调用类
	 * @return 成功返回string结果，失败返回空字符串
	 */
	protected String queryForString(String sql, Object[] params, String tableColumn, Class<?> clazz) {
		Logger log = Logger.getLogger(clazz);
		try {
			return jdbc.queryForMap(sql, params).get(tableColumn).toString();
		} catch (DataAccessException e) {
			log.error(clazz + e.getMessage(), e);
			return "";
		}
	}

	/**
	 * 查询结果，类型为int
	 * 
	 * @param sql
	 *           要执行的sql语句
	 * @param params
	 *           sql所对应的参数列表
	 * @param clazz
	 *           调用类
	 * @return 成功返回int结果，失败返回0
	 */
	@SuppressWarnings("deprecation")
	protected int queryForInt(String sql, Object[] params, Class<?> clazz) {
		// Logger log = Logger.getLogger(clazz);
		try {
			return jdbc.queryForInt(sql, params);
		} catch (DataAccessException e) {
			// log.error(clazz + e.getMessage(), e);
			return 0;
		}
	}

	/**
	 * 分页查询
	 * 
	 * @param sql
	 *           要执行的sql语句
	 * @param params
	 *           sql所对应的参数列表
	 * @param count
	 *           总数
	 * @param pageNo
	 *           当前页
	 * @param pageSize
	 *           每页显示数据条数
	 * @return 当前页面对象CurrentPage
	 */
	protected CurrentPage<Map<String, Object>> fetchPage(final String sql, final Object[] params, final int count, final int pageNo, final int pageSize) {
		final CurrentPage<Map<String, Object>> page = new CurrentPage<Map<String, Object>>(pageNo, pageSize, count);
		try {
			jdbc.query(sql, params, new ResultSetExtractor<Object>() {
				public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
					final List<Map<String, Object>> pageItems = page.getPageItems();
					ResultSetMetaData srmd = rs.getMetaData();
					int currentRow = 0;
					while (rs.next() && currentRow < page.getBeginRecordNum() + pageSize) {
						if (currentRow >= page.getBeginRecordNum()) {
							int i = srmd.getColumnCount();
							Map<String, Object> map = new HashMap<String, Object>();
							while (i > 0) {
								try {
									map.put(srmd.getColumnLabel(i), rs.getObject(i));
								} catch (Exception e) {
									map.put(srmd.getColumnLabel(i), null);
								}
								i--;
							}
							pageItems.add(map);
						}
						currentRow++;
					}
					return page;
				}
			});
			return page;
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * 分页查询
	 * 
	 * @param sql
	 *           要执行的sql语句
	 * @param params
	 *           sql所对应的参数列表
	 * @param count
	 *           总数
	 * @param pageNo
	 *           当前页
	 * @param pageSize
	 *           每页显示数据条数
	 * @return 当前页面对象CurrentPage
	 */
	protected CurrentPage<Map<String, Object>> fetchPage(final String sql, final Object[] params, final int count, final int start,int end, final int pageSize) {
		final CurrentPage<Map<String, Object>> page = new CurrentPage<Map<String, Object>>(start,end, pageSize, count);
		try {
			jdbc.query(sql, params, new ResultSetExtractor<Object>() {
				public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
					final List<Map<String, Object>> pageItems = page.getPageItems();
					ResultSetMetaData srmd = rs.getMetaData();
					int currentRow = 0;
					while (rs.next() && currentRow < page.getBeginRecordNum() + pageSize) {
						if (currentRow >= page.getBeginRecordNum()) {
							int i = srmd.getColumnCount();
							Map<String, Object> map = new HashMap<String, Object>();
							while (i > 0) {
								try {
									map.put(srmd.getColumnLabel(i), rs.getObject(i));
								} catch (Exception e) {
									map.put(srmd.getColumnLabel(i), null);
								}
								i--;
							}
							pageItems.add(map);
						}
						currentRow++;
					}
					return page;
				}
			});
			return page;
		} catch (Exception e) {
			return null;
		}
	}
}
