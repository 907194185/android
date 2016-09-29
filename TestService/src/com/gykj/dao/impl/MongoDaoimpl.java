package com.gykj.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;


import com.gykj.utils.BeanUtils;
import com.gykj.utils.LogUtils;
import com.gykj.utils.MongodbUtil;
import com.mongodb.WriteResult;


/**
 * Mongo数据操作基础类,切记，主键为_id，这是数据库决定的，对应的类也应该有这个字段
 * @author xianyl
 *
 * @param <T>
 */
public class MongoDaoimpl<T> extends BaseDaoimpl<T>{
	
	//mongodb 的模板
	@Resource
	protected MongoTemplate mongoTemplate;
	
	
	 /**
	  *  根据条件删除实体
      * @param query   
	  * @return  如果大于0，则为删除成功，即删除的行数
	  */
	protected Integer delete(Query query) { 
		try {			
			return this.mongoTemplate.remove(query, this.getPojoClass()).getN();			
		} catch (Exception e) {
			LogUtils.error("MongoDao criteria删除一条记录异常", e);
			return 0;
		}
    }
	
	/**
	 * 更新满足条件的第一个记录
	 * @param query
	 * @param update
	 * @return 如果大于0，则为更新成功
	 */
	protected Integer updateFirst(Query query,Update update){
        try {
			return this.mongoTemplate.updateFirst(query, update, this.getPojoClass()).getN();			
		} catch (Exception e) {
			LogUtils.error("MongoDao updateFirst更新一条记录异常", e);
			return 0;
		}
    }
    /**
     * 更新满足条件的所有记录
     * @param query
	 * @param update
	 * @return 如果大于0，则为更新成功
     */
    protected Integer updateMulti(Query query, Update update){
        try {
			return this.mongoTemplate.updateMulti(query, update, this.getPojoClass()).getN();			
		} catch (Exception e) {
			LogUtils.error("MongoDao updateFirst更新一条记录异常", e);
			return 0;
		}
    }

    /**
     * 查找更新,如果没有找到符合的记录,则将更新的记录插入库中
     * @param query
	 * @param update
	 * @return 如果大于0，则为更新成功
     */
    protected Integer updateInser(Query query, Update update){
    	 try {
 			return this.mongoTemplate.upsert(query, update, this.getPojoClass()).getN();			
 		} catch (Exception e) {
 			LogUtils.error("MongoDao updateFirst更新一条记录异常", e);
 			return 0;
 		}
    }


	/**
	 * 通过条件查询单个实体
	 * @param query
	 * @return
	 */
    protected T queryOne(Query query){    	
        return this.mongoTemplate.findOne(query, this.getPojoClass());
    }
    
    
	
    /**
     * 通过条件进行分页查询,如果pageNo跟pageSize都大于0才分页，否则全部数据
     * @param query 查询条件
     * @param pageNo 查询起始值
     * @param pageSize 查询大小   
     * @return 满足条件的集合
     */
	protected List<T> queryList(Query query, Integer pageNo, Integer pageSize){
		if(pageNo!=null && pageSize!=null && pageNo>0 && pageSize>0){
			//分页，设置起始行每页条数
			query.skip((pageNo - 1) * pageSize);
			query.limit(pageSize);
		}
        return this.mongoTemplate.find(query, this.getPojoClass());
    }
    
    /**
     * 根据条件查询库中符合记录的总数,为分页查询服务
     * @param query 查询条件
     * @return 满足条件的记录总数
     */
    protected Long getPageCount(Query query){
        return this.mongoTemplate.count(query, this.getPojoClass());
    }
	
    /**
	 * 查询某条数据是否存在
	 * @param query
	 * @param collectionName 集合名称
	 * @return
	 */
    protected Boolean exists(Query query){
        return this.mongoTemplate.exists(query, this.getPojoClass());
    }
    
	
	
	//--------------------------------对外公共方法，
	@Override
	public T insert(T t) {
		Query query = null;
		try {
			query = MongodbUtil.map2Query(BeanUtils.beanToMap(t));
			if(!exists(query))				
				this.mongoTemplate.insert(t);
		} catch (Exception e) {
			LogUtils.error("MongoDao 插入一条记录异常", e);
			return null;
		}
		return find(query);
	}

	@Override
	public T delete(T t) {
		try {
			WriteResult result = this.mongoTemplate.remove(t);
			//没有删除数据
			if(result.getN() <= 0) return null;
		} catch (Exception e) {
			LogUtils.error("MongoDao T删除一条记录异常", e);
			return null;
		}
		return t;
	}

	
	@Override
	public T get(Object id) {
		try {
			return this.mongoTemplate.findById(id, this.getPojoClass());
		} catch (Exception e) {
			LogUtils.error("MongoDao 根据id获取一条记录异常", e);
			return null;
		}
	}
	
	/**
	 * 建议使用{@link #update(Object, Object)}
	 */
	@Override
	@Deprecated
	public T update(T t) {
		try {
			Map<String, Object> map = BeanUtils.beanToMap(t);	
			//组装条件,_id为需要修改的对象主键，其它属性为新属性
			Object _id = map.remove(field_id);
			if(_id == null) return null;					
			//先查找原来的对象
			T old_t = get(_id);
			return update(old_t, map);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * mongo尽量少用更新的指令，它更多的情况是用于储存流水数据
	 */
	@Override
	public T update(T old_t, Map<String,Object> new_params) {
		if(old_t == null || new_params == null) return null;
		try {
			Map<String, Object> old_map = BeanUtils.beanToMap(old_t);
			//更新
			Query query = MongodbUtil.map2Query(old_map);
			
			return this.mongoTemplate.findAndModify(MongodbUtil.isDesc(query, true, field_id)//查找条件
									, MongodbUtil.map2Update(new_params, field_id)//需要更新的值
									, new FindAndModifyOptions().returnNew(true)//
									, getPojoClass());//
		} catch (Exception e) {
			LogUtils.error("MongoDao 更新一条记录异常", e);
			return null;
		}
	}

	@Override
	public T find(Map<String, Object> params) {
		return find(MongodbUtil.map2Query(params));
	}

	public T find(Query query) {
		//query 按主键倒序，获取第一个
		return queryOne(MongodbUtil.isDesc(query, true, field_id));
	}

	@Override
	public List<T> list(Map<String, Object> params, Integer pageNo,
			Integer pageSize) {
		return queryList(MongodbUtil.map2Query(params), pageNo, pageSize);
	}

	@Override
	public Integer countRow(Map<String, Object> params) {
		return getPageCount(MongodbUtil.map2Query(params)).intValue();
	}

	@Override
	public Integer delBatch(Object... ids) {
		Integer code = null;
		try {
			code = delete(new Query(Criteria.where(field_id).in(ids)));			
		} catch (Exception e) {
			code = 0;
		}
		return code;
	}

	@Override
	public Integer editBatch(Map<String, Object> params, Object... ids) {
		Integer code = null;
		try {
			code = updateMulti(new Query(Criteria.where(field_id).in(ids)),MongodbUtil.map2Update(params, field_id));			
		} catch (Exception e) {
			code = 0;
		}
		return code;
	}

	@Override
	public T findModelByName(String name) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", name);
		return find(params);
	}
	

	
    
}