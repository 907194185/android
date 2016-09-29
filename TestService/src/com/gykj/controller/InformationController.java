package com.gykj.controller;

import java.util.Map;
import java.util.concurrent.Callable;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gykj.interceptor.CachePassport;
import com.gykj.pojo.InterfaceList;
import com.gykj.pojo.Log;
import com.gykj.service.impl.InformationService;





/**
 * 请求入口 控制器
 * @author xianyl
 *
 */
@RestController
@RequestMapping("/information")
public class InformationController{
	@Resource
	private InformationService informationService;
	
	
	@CachePassport
	@RequestMapping(value="/{token}/{collection}/{action}", method={RequestMethod.POST, RequestMethod.GET})
    public Callable<Map<String, Object>> doMethod(@PathVariable String collection
    										,@PathVariable String action
    										,@PathVariable String token
    										,@RequestParam Map<String, Object> param, HttpServletRequest request) {
		//贯通整个tcp任务的日志对象
		Log log = new Log();
		System.out.println(request.getParameterMap());
		//获取接口清单
		InterfaceList interfaceList = informationService.getInterfaceList(collection, action, token, log);		
		//请求tcp
        return informationService.requestTCP(interfaceList, param, log);
    }
	
	
}
