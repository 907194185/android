package com.gykj.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.gykj.Constant;
import com.gykj.dao.UserCacheDao;
import com.gykj.pojo.InterfaceList;
import com.gykj.pojo.Log;
import com.gykj.pojo.UserCache;
import com.gykj.service.impl.InformationService;
import com.gykj.utils.HttpUtils;





/**
 * 微信 请求入口 控制器
 * @author xianyl
 *http://mp.weixin.qq.com/wiki/4/9ac2e7b1f1d22e9e57260f6553822520.html
 */
@RestController
@RequestMapping("/home")
public class WeiXinQRController{
	
	@Resource
	private InformationService informationService;
	
	//http://192.168.1.140:8080/gykj/weixin/information/4945f9a9-5f59-4c99-b720-3d5f8c4e370f/user/edit?openid=sdfs&obj=json[{}]
	@RequestMapping(value="/Inspector/company/id/{id}", method={RequestMethod.POST, RequestMethod.GET})
    public Callable<Map<String, Object>> doMethod(@PathVariable(value="id") Long id,String systoken,String token,String username) {
		Log log = new Log();
		
		Map<String, Object> param = new HashMap<String, Object> ();
		param.put("obj", "[{\"qr\":"+id+"}]");
		param.put("token", token);
		param.put("weixin", true);
		param.put("page", 1);
		param.put("pageSize", 1);

		//获取接口清单
		InterfaceList interfaceList = informationService.getInterfaceList("organization", "list", systoken, log);		
		//请求tcp
		//跳转到认证页。
		Callable<Map<String, Object>> res= informationService.requestTCP(interfaceList, param, log);
		//String url="/home/weixinqr.jsp?id="+id;
		//System.out.print(url);
        //return  new ModelAndView("forward:"+url.toString());
		return res;
	}



	
	

}
