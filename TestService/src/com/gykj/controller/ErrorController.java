package com.gykj.controller;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;






/**
 *  错误入口 控制器,主要用来处理特殊消息的返回
 * @author xianyl
 */
@RestController
@RequestMapping("/error")
public class ErrorController{
	
	@RequestMapping(value="/dispose", method={RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> dispose(@RequestParam Map<String, Object> param) {
		Map<String, Object> params=new HashMap<String,Object>();
		params.put("code", "0001");
		params.put("code", "非法请求！没有权限访问该接口！");
		return params;
	}
	
	@RequestMapping(value="/callable", method={RequestMethod.POST, RequestMethod.GET})
	public Callable<Map<String, Object>> callable(@RequestParam final Map<String, Object> param) {
		return new Callable<Map<String,Object>>() {
			@Override
			public Map<String, Object> call() throws Exception {			
				return param;
			}
		};
	}
}
