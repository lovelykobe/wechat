package com.tjaide.wechat.dao;

import java.util.List;
import java.util.Map;

public interface BaseDao {

	/**
	 * 添加数据
	 * 
	 * @param tableName
	 *           插入表表名
	 * @param map
	 *           所要插入表数据的集合,格式为("列名", "值")
	 * @param clazz
	 *           调用类
	 */
	int save(String tableName, Map<String, Object> map, Class<?> clazz);

	/**
	 * 添加数据
	 * 
	 * @param sql
	 *           sql语句
	 * @param params
	 *           参数
	 * @param clazz
	 *           调用类
	 */
	int save(String sql, Object[] params, Class<?> clazz);

	/**
	 * 修改数据
	 * 
	 * @param tableName
	 *           插入表表名
	 * @param updateMap
	 *           所要修改表数据的集合,格式为("列名", "值")
	 * @param whereMap
	 *           所要修改表数据条件的集合,格式为("列名", "值")
	 * @param clazz
	 *           调用类
	 */
	int update(String tableName, Map<String, Object> updateMap, Map<String, Object> whereMap, Class<?> clazz);

	/**
	 * 修改数据
	 * 
	 * @param sql
	 *           sql语句
	 * @param params
	 *           参数
	 * @param clazz
	 *           调用类
	 */
	int update(String sql, Object[] params, Class<?> clazz);

	/**
	 * 删除数据
	 * 
	 * @param tableName
	 *           删除表表名
	 * @param map
	 *           删除条件的集合,格式为("列名", "值")
	 * @param clazz
	 *           调用类
	 */
	int delete(String tableName, Map<String, Object> map, Class<?> clazz);

	/**
	 * 删除数据
	 * 
	 * @param sql
	 *           sql语句
	 * @param params
	 *           参数
	 * @param clazz
	 *           调用类
	 */
	int delete(String sql, Object[] params, Class<?> clazz);

	/**
	 * 查询结果，类型为Map
	 * 
	 * @param sql
	 *           要执行的sql语句
	 * @param params
	 *           sql所对应的参数列表
	 * @param clazz
	 *           调用类
	 */
	List<Map<String, Object>> queryForList(String sql, Object[] params, Class<?> clazz);

	/**
	 * 查询结果，类型为List
	 * 
	 * @param sql
	 *           要执行的sql语句
	 * @param params
	 *           sql所对应的参数列表
	 * @param clazz
	 *           调用类
	 */
	Map<String, Object> queryForMap(String sql, Object[] params, Class<?> clazz);

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
	 */
	List<Map<String, Object>> queryForList(String sql, Object[] params, int count, int pageNo, int pageSize, Class<?> clazz);
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
	 */
	 List<Map<String, Object>> queryForList(String sql, Object[] params, int count, int start,int end, int pageSize, Class<?> clazz);
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
	 */
	String queryForString(String sql, Object[] params, String tableColumn, Class<?> clazz);

	/**
	 * 查询结果，类型为int
	 * 
	 * @param sql
	 *           要执行的sql语句
	 * @param params
	 *           sql所对应的参数列表
	 * @param clazz
	 *           调用类
	 */
	int queryForInt(String sql, Object[] params, Class<?> clazz);
}
