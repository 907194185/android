package com.gykj.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.gykj.pojo.Pojo;
import com.mongodb.DBRef;


/**
 * Mongodb 工具类,拼接常用参数
 * @author xianyl
 *
 */
public class MongodbUtil {
	
	/**
	 * Map参数转为Query条件对象
	 * @param params
	 * @return 
	 */
	public static Query map2Query(Map<String, Object> params) {
		return addCriteria2Query(new Query(), params);
	}
	
	/**
	 * 设置查询条件排序的条件以及是否倒序
	 * @param isDesc 是否倒序 ，如果false，则为正序;如果true则为倒序
	 * @param properties 排序 的字段,如果这里吗，没值，则忽略isDesc的传值
	 * @return
	 */
	public static Query isDesc(Query query, Boolean isDesc, String... properties){
    	if(!ArrayUtils.isEmpty(properties)){
    		if(isDesc){
    			query.with(new Sort(Direction.DESC, properties));
    		}else{
    			query.with(new Sort(Direction.ASC, properties));
    		}
    	}
        return query;
    }
	
	/**
	 * 为Query加入Criteria条件对象
	 * @param query
	 * @param params 条件的参数，会加进Query, 支持模糊，%value%
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Query addCriteria2Query(Query query, Map<String, Object> params) {
		if(params == null || params.size()==0) return query;
		//拼接条件
		Criteria criteria;
		Object value;
		for(String key : params.keySet()){
			value = params.get(key);
			if(value == null) continue;
			//组合查询的字段
			criteria = Criteria.where(key);
			//判断是否需要使用like条件
			if(value instanceof String 
					&& value.toString().endsWith("%") 
					&& value.toString().startsWith("%")){
				//模糊加条件,这个算法不太给力?只支持%..%
				String regex = value.toString().replace("%", "");
				criteria.regex(regex);
			}else if(value instanceof Pojo){
				//如果存放的是引用对象
				criteria.is(pojo2Dbref((Pojo)value));
			}else if(value instanceof List){
				//如果存放的是引用对象 集合
				try {
					criteria.is(pojos2Dbref((List<Pojo>) value));
				} catch (Exception e) {
					criteria.is(value);
				}
			}else{
				//正常加条件
				criteria.is(value);
			}
			query.addCriteria(criteria);
		}
		return query;
	}
	
	
	/**
	 * Map参数转为Update对象
	 * @param params 需要更新的参数跟值
	 * @param keys 忽略的参数，如果有这个值，params会remove掉对应的key
	 * @return 有可能返回null
	 */
	@SuppressWarnings("unchecked")
	public static Update map2Update(Map<String, Object> params, String... keys) {
		Update update = new Update();
		if(params == null || params.size()==0)
			return update;
		//过滤需要忽略的参数
		if(keys != null && keys.length > 0){
			for(String key : keys){
				if(params.containsKey(key))
					params.remove(key);
			}
		}
		//设置参数
		Object object = null;
		for(String key : params.keySet()){
			object = params.get(key);
			
			//转换一下数据
			if(object instanceof Pojo){
				object = pojo2Dbref((Pojo)object);
			}else if(object instanceof List){
				try {
					object = pojos2Dbref((List<Pojo>) object);
				} catch (Exception e) {
				}
			}
			//设置数据 
			update.set(key, object);
		}
		return update;
	}

	
	/**
	 * 对象集合转DBRef
	 * @param model
	 * @return
	 */
	public static List<DBRef> pojos2Dbref(List<? extends Pojo> models){
		if(models == null) 
			return null;
		ArrayList<DBRef> list = new ArrayList<DBRef>();
		DBRef dbRef = null;
		for(Pojo pojo : models){
			dbRef = pojo2Dbref(pojo);
			if(dbRef == null) continue;
			list.add(dbRef);
		}
		
		return list;
	}
	
	/**
	 * 对象转DBRef
	 * @param model
	 * @return
	 */
	public static DBRef pojo2Dbref(Pojo model){
		if(model == null) 
			return null;
		//如果首字母是大写,则变小写
		char[] name = model.getClass().getSimpleName().toCharArray();
		if(name[0] > 64 && name[0] < 91){
			name[0] += 32;
		}
		return new DBRef(String.valueOf(name), new ObjectId(model.getId()));
	}
}
