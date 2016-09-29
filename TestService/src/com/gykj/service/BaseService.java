package com.gykj.service;


import java.util.List;
import java.util.Map;

import com.gykj.dao.BaseDao;





/**
 * 服务层基本接口
 * @author xianyl
 * @param <T>
 *
 * @param <T>
 */
public interface BaseService<D extends BaseDao<?>, T> {
	
	/**
	 * 获得对应的Dao
	 */
	public D getDao();

	/**
	 * 增加一条信息
	 * @param t
	 * @return 返回对象 , 如果插入失败返回null
	 */
	public T insert(T t);
	
	/**
	 * 删除一条信息
	 * @param t
	 * @return 返回对象,如果操作不成功，返回null
	 */
	public T delete(T t);
	
	/**
	 * 更新一条信息
	 * @param t
	 * @return 返回更新后的对象,如果操作不成功，返回null
	 */
	public T update(T t);
	
	/**
	 * 根据ID查一条信息
	 * @param id
	 * @return 返回对象， 如果异常，返回null
	 */
	public T get(Object id);
	
	/**
	 * 根据条件查一条信息
	 * @param params 参数 
	 * @return 返回对象， 如果异常，返回null
	 */
	public T find(Map<String,Object> params);

	/**
	 * 分页查询表中所有信息
	 * @param params 查询对象的参数
	 * @param pageNo 要显查询的第几页
	 * @param pageSize 每一页要查询多少条数据
	 * @return 如果异常，返回null,
	 */
	public List<T> list(Map<String,Object> params, Integer pageNo, Integer pageSize);
	
	
	/**
	 * 计算数据库数据有多少项
	 * @param params 参数 
	 * @return 如果异常，返回null
	 */
	public Integer countRow(Map<String,Object> params);
	
	
	
	/**
	 * 计算数据库数据能分多少页
	 * @param param 查询语句的条件，可以为null，为null时的条件为全部
	 * @param pageSize 每页显示数据的数量
	 * @return 返回1个2个值的数组，坐标0为记录数totalRecord，坐标1为页数据totalpage
	 */
	public int[] countPage(Map<String,Object> param, Integer pageSize);
	
	

	/**
	 * 批量删除
	 * @param ids id数组 
	 * @return  返回受影响的行数，如果有异常返回-1
	 */
	public Integer delBatch(Object... ids);

	/**
	 * 批量修改
	 * @param ids id数组 
	 * @param params 修改的参数 
	 * @return  返回受影响的行数，如果有异常返回-1
	 */
	public Integer editBatch(Map<String, Object> params, Object... ids);
	
	

}
