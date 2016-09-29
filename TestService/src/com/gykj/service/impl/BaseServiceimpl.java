package com.gykj.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.gykj.dao.BaseDao;
import com.gykj.service.BaseService;







/**
 * 服务层基本实现类 ，如果要用到其它的Dao，或者其它Dao的方法 ，在各自子类获取Dao对象，这里只提供最基本的操作方法
 * @author xianyl
 *
 * @param <D>
 * @param <T>
 */
public class BaseServiceimpl<D extends BaseDao<T>, T> implements BaseService<D, T> {
	@Autowired
	private D baseDao;

	@Override
	public D getDao() {
		return this.baseDao;
	}


	@Override
	public T insert(T t) {
		return getDao().insert(t);
	}

	@Override
	public T delete(T t) {
		return getDao().delete(t);
	}

	@Override
	public T update(T t) {
		return getDao().update(t);
	}

	@Override
	public T get(Object id) {
		return getDao().get(id);
	}

	@Override
	public T find(Map<String, Object> params) {
		return getDao().find(params);
	}

	@Override
	public List<T> list(Map<String, Object> params, Integer pageNo,
			Integer pageSize) {
		return getDao().list(params, pageNo, pageSize);
	}


	@Override
	public Integer countRow(Map<String, Object> params) {
		return getDao().countRow(params);
	}

	
	@Override
	public int[] countPage(Map<String,Object> params, Integer pageSize) {		
		int[] count = new int[]{0, 0};
		//获取总项数 
		count[0] = this.countRow(params);
		if(count[0] == 0) return count;
		//如果传入的pageSize小于1,则认为希望1页显示完，即row有多少条，pageSize则有多少条
		if(pageSize == null || pageSize < 1)  pageSize = count[0];
		//计算页码数量，逢1进1; 正常计算方法 row%pageSize==0?row/pageSize:row/pageSize+1;
		count[1] = (count[0] - 1) / pageSize + 1;
		return count;
	}


	@Override
	public Integer delBatch(Object... ids) {
		return getDao().delBatch(ids);
	}


	@Override
	public Integer editBatch(Map<String, Object> params, Object... ids) {
		return getDao().editBatch(params, ids);
	}
	

	
}
