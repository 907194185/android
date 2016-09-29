package com.gykj.controller;


import java.util.List;
import java.util.Map;

import com.gykj.Constant;
import com.gykj.service.BaseService;





/**
 * 主要做增删查改的基本控制类
 * 
 * @author xianyl
 * 
 */
public class BaseControllerByCRUD<S extends BaseService<?, T>,T> extends BaseController<S , T>{
	
	
	/**
	 * 按条件分页查询列表
	 * @param param 参数
	 * @param page 第几页数据
	 * @param pageSize 每页多少行数据
	 * @return
	 */
	protected Map<String, Object> list(Map<String, Object> params, Integer pageNo, Integer pageSize) {
		//分页获取数据	
		List<T> data = baseService.list(params, pageNo, pageSize);
		//组合数据
		return getBaseData(null, null, data);
    } 
	
	/**
	 * 按条件查找总页数
	 * @param param 参数
	 * @param pageSize 每页多少行数据
	 * @return
	 */
	protected Map<String, Object> page(Map<String, Object> params, Integer pageSize) {
		//获取数据
		int[] count = baseService.countPage(params, pageSize);	
		//组合数据
		Map<String, Object> pojo = getBaseData(null, null, null);
		pojo.put(Constant.JSON_RESPONSE_FIELD_TOTALRECORD, count[0]);
		pojo.put(Constant.JSON_RESPONSE_FIELD_TOTALPAGE, count[1]);
		//转成jsonp
		return pojo;
    } 
	
	
	/**
	 * 根据id查找数据
	 * @param id
	 * @return
	 */
	protected Map<String, Object> info(Object id) {
		// 获取数据
		Map<String, Object> pojo;
		try {
			T model = baseService.get(id);
			// 组合数据
			pojo = getBaseData(null, null, model);
		} catch (Exception e) {
			pojo = getBaseData(Constant.RETURN_CODE_FAIL, null, null);
		}
		return pojo;
	}

	/**
	 * 保存一条新数据
	 * @param modelJson 数据的json字符串
	 * @return
	 */
	protected Map<String, Object> save(T model) {
		// TODO 验证对象，如果没问题，则加入		
		if(model != null){
			model = baseService.insert(model);
		}
		// 根据id返回值判断是否加入成功
		Map<String, Object> pojo;
		if (model != null) {
			pojo = getBaseData(null, null, model);
		} else {
			pojo = getBaseData(Constant.RETURN_CODE_FAIL, null, null);
		}
		//System.out.println("--model--"+model);
		return pojo;
	}

	/**
	 * 更新一条数据
	 * @param modelJson 数据的json字符串
	 * @return
	 */
	protected Map<String, Object> edit(T model) {
		Object object = null;
		int state = Constant.RETURN_CODE_FAIL;
		try {
			// TODO 验证对象，如果没问题，则更新			
			object = baseService.update(model);
			if(object != null)
				state = Constant.RETURN_CODE_SUCCESS;
		} catch (Exception e) {
		}
		return getBaseData(state, null, object);
	}

	/**
	 * 根据id删除一条数据
	 * @param id
	 * @return
	 */
	protected Map<String, Object> delete(T model) {
		Object object = null;
		int state = Constant.RETURN_CODE_FAIL;
		try {
			object = baseService.delete(model);
			if(object != null)
				state = Constant.RETURN_CODE_SUCCESS;
		} catch (Exception e) {
		}
		return getBaseData(state, null, object);
	}

	
	/**
	 * 批量删除
	 * @param ids id的数组
	 * @return
	 */
	protected Map<String, Object> delBatch(Object[] ids) {
		Integer rows = null;
		int state = Constant.RETURN_CODE_FAIL;
		try {
			rows = baseService.delBatch(ids);			
			if(rows != Constant.RETURN_CODE_FAIL){
				//如果object不是返回-1, 则为操作成功，没有异常
				state = Constant.RETURN_CODE_SUCCESS;
			}else{
				rows = 0;//如果操作失败，不返回-1,返回空？还是返回0?
			}				
		} catch (Exception e) {
		}
		return getBaseData(state, null, rows);
	}
	
	
	/**
	 * 批量修改
	 * @param ids id的数组
	 * @param params 修改的参数
	 * @return
	 */
	protected Map<String, Object> editBatch(Map<String, Object> params, Object[] ids) {
		Integer rows = null;
		int state = Constant.RETURN_CODE_FAIL;
		try {
			rows = baseService.editBatch(params, ids);			
			if(rows != Constant.RETURN_CODE_FAIL){
				//如果object不是返回-1, 则为操作成功，没有异常
				state = Constant.RETURN_CODE_SUCCESS;
			}else{
				rows = 0;//如果操作失败，不返回-1,返回空？还是返回0?
			}				
		} catch (Exception e) {
		}
		return getBaseData(state, null, rows);
	}

	
	

	
	
	
}
