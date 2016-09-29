package com.gykj.interceptor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.gykj.Constant;



/**
 * 添加自定义注解CachePassport ,在返回Callable<Map<String, Object>>的方法加
 * 只需要在我们在需要权限验证的action上加上这个注解就可以实现权限控制功能了
 * @author xianyl
 *
 */
@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CachePassport {
	String pass_action() default Constant.FIELD_USER_LOGIN;
    String prevent_action() default Constant.FIELD_USER_LOGOUT;
}