package com.gykj.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtilsBean;




/**
 * bean工具类,为补充org.apache.commons.beanutils的方法
 * @author xianyl
 *
 */
@SuppressWarnings("rawtypes")
public class BeanUtils extends org.apache.commons.beanutils.BeanUtils{

	
	/**
     * bean转map
     * @param obj
     * @return
     */
    public static Map<String, Object> beanToMap(Object obj) { 
        Map<String, Object> params = new HashMap<String, Object>(0); 
        try { 
            PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean(); 
            PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(obj); 
            for (int i = 0; i < descriptors.length; i++) { 
                String name = descriptors[i].getName();
                Object value = propertyUtilsBean.getNestedProperty(obj, name);
                if (!"class".equals(name) && value != null) { 
                    params.put(name, value);
                } 
            } 
        } catch (Exception e) {
        	LogUtils.error("beanToMap 对象转map获取成员变量操作失败", e);  
        }
        //System.out.println("-BeanUtils-对象转map的数据，测试几个后可以删掉这句打印代码：       "+params);
        return params; 
    }

    
    /**
     * 根据类型创建bean对象
     * @param parameterType
     * @return
     */
	public static Object instantiate(Class<?> clazz) {
		return org.springframework.beans.BeanUtils.instantiate(clazz);
	}

	/**
     * 通过反射，获得定义Class时声明的父类的第一个范型参数的类型。
     */
	public static Class getSuperClassGenricType(Class<?> clazz) {
        return getSuperClassGenricType(clazz, 0);
    }
    
    /**
     * 通过反射，获得定义Class时声明的父类的范型参数的类型。
     * 如没有找到符合要求的范型参数，则递归向上直到Object。
     *
     * @param Type 要进行查询的类
     * @param index 如有多个范型声明该索引从0开始
     * @return 在index位置的范型参数的类型，如果无法判断则返回Object.class
     */
	public static Class getSuperClassGenricType(Class<?> clazz, int index) {
        boolean flag = true;
        Type genType = clazz.getGenericSuperclass();
        Type[] params = null;
        
        if (!(genType instanceof ParameterizedType))
            flag = false;
        else {
            params = ((ParameterizedType) genType).getActualTypeArguments();
            if (index >= params.length || index < 0)
                flag = false;
            if (!(params[index] instanceof Class))
                flag = false;
        }
        if (!flag) {
            clazz = clazz.getSuperclass();
            if (clazz == Object.class)
                return Object.class;
            else
                return getSuperClassGenricType(clazz, index);
        }        
        return (Class) params[index];
    }
    
}
