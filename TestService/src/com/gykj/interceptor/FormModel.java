package com.gykj.interceptor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 添加自定义注解FormModel 如果同一个controller同一个方法接收2个实体,这两个实体有相同字段的时候
 * 只需要在接收的实体加此注解即可
 * @author xianyl
 * 
 */
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FormModel {
	String value();
}