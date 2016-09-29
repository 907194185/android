package com.gykj.interceptor;


import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

import com.gykj.Constant;


/**
 * 让返回视图支持jsonp
 * @author xianyl
 *
 */
@ControllerAdvice(basePackages = "com.gykj.controller")
public class JsonpAdvice extends AbstractJsonpResponseBodyAdvice {	
	
	
	/**
	 * 当请求参数中包含callback 或 jsonp参数时，就会返回jsonp协议的数据。其value就作为回调函数的名称
	 */
	public JsonpAdvice() {
        super(Constant.FIELD_JSONP_CALLBACK, Constant.FIELD_JSONP);
    }
	
}