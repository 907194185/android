package com.gykj.dao.impl;

import com.gykj.dao.BaseDao;
import com.gykj.utils.BeanUtils;

/**
 * 数据层基本实现类
 * 
 * @author xianyl
 * 
 * @param <T>
 */
public abstract class BaseDaoimpl<T> implements BaseDao<T> {
		//主键的字段名
		protected final String field_id = "id";
		// 每个dao对应操作的数据模型
		private Class<T> clazz;
		
		/**
		 * 构造方法，获取泛型的类型，即操作的pojo
		 */
		@SuppressWarnings("unchecked")
		public BaseDaoimpl() {
			clazz = BeanUtils.getSuperClassGenricType(getClass());
		}
	
		/**
		 * 获取泛型的类型
		 * @return
		 */
		@Override
		public Class<T> getPojoClass(){
			return this.clazz;
		}




}
