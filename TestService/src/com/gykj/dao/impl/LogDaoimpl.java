package com.gykj.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.gykj.dao.LogDao;
import com.gykj.pojo.Log;
import com.gykj.utils.MongodbUtil;
import com.gykj.utils.ObjectUtils;



/**
 * 参数字段  持久层实现类
 * 
 * @author xianyl
 * 
 * @param <T>
 */
@Repository(value = "logDao")
public class LogDaoimpl extends MongoDaoimpl<Log> implements LogDao {

	@Override
	public List<Log> list(Map<String, Object> params, Integer pageNo,
			Integer pageSize) {		
		return queryList(disposeParams(params), pageNo, pageSize);
	}
	
	@Override
	public Integer countRow(Map<String, Object> params) {
		return getPageCount(disposeParams(params)).intValue();
	}
	
	//处理参数,主要是日期
	private Query disposeParams(Map<String, Object> params){
		Object create_time = params.remove("create_time");//挑create_time		
		Object ack_time = params.remove("ack_time");//挑ack_time
		Integer flag = ObjectUtils.toInteger(params.remove("flag"));//挑flag
		Query query = MongodbUtil.map2Query(params);
		if(create_time != null){
			query.addCriteria(Criteria.where("create_time").gte(create_time));
		}
		if(ack_time != null){
			query.addCriteria(Criteria.where("ack_time").gte(ack_time));
		}
		if(flag == 0){
			query.addCriteria(Criteria.where("flag").gte(flag));
		}else{
			query.addCriteria(Criteria.where("flag").is(flag));
		}
		
		return query;
	}
	
}
