package com.gykj.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gykj.utils.JsonUtils;
import com.gykj.utils.WxUtils;
/**
 * 微信webapi接口服务管理
 * @author cwz
 * 2016年1月18日上午10:56:34
 */
@Controller
@RequestMapping("/")
public class WxWebSdkController {
	/**
	 * 删除接口
	 * @param id
	 * @param page
	 * @return
	 */
	@RequestMapping(value={"wx/jsSDK"})
	@ResponseBody
	public Object sign(HttpServletRequest request){
		String json= WxUtils.getParam(request);
		Map jo=JsonUtils.toObject(json, Map.class);
		Map<String,Object> wxJs=new HashMap<String,Object>();
		wxJs.put("timestamp", jo.get("timestamp"));
		wxJs.put("appId", jo.get("appid"));
		wxJs.put("nonceStr", jo.get("nonceStr"));
		wxJs.put("signature", jo.get("signature"));
		return wxJs;
	}
}
