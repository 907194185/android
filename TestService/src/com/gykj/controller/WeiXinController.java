package com.gykj.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import com.gykj.utils.DateTimeUtil;
import com.gykj.utils.HttpUtils;
import com.gykj.utils.JsonUtils;
import com.gykj.utils.WxUtils;





/**
 * 微信 请求入口 控制器
 * @author xianyl
 *http://mp.weixin.qq.com/wiki/4/9ac2e7b1f1d22e9e57260f6553822520.html
 */
@RestController
@RequestMapping("/weixin")
public class WeiXinController{
	private final String APPID = "";//微信APPID  wx1db4c9335d280036
	private final String SECRET = "";//微信APPSECRET 95dffc454b7177ae2d759af33aa6b636
	private final String SCOPE = "snsapi_base";//静默授权
	private Logger logger = Logger.getLogger(WeiXinController.class);
	@Resource
	private UserCacheDao userCacheDao;

	@Resource
	private InformationService informationService;

	//http://192.168.1.140:8080/gykj/weixin/information/4945f9a9-5f59-4c99-b720-3d5f8c4e370f/user/edit?openid=sdfs&obj=json[{}]
	@RequestMapping(value="/{controller}/{token}/{collection}/{action}", method={RequestMethod.POST, RequestMethod.GET})
	public ModelAndView doMethod(HttpServletRequest request) {
		// 先获取openid
		Object openid = request.getParameter(Constant.FIELD_WX_OPENID);
		StringBuilder url = null;
		//如果openid还是为null,则说明从微信获取openid失败
		if(openid == null) {
			//返回数据	  
			url = HttpUtils.getErrorUrl(Constant.RETURN_CODE_STRING_FAIL, "请传递微信openid");        
			return new ModelAndView("forward:"+url.toString());       
		}

		//用openid查找userCache,如果没有,则返回用户,让他登陆
		HashMap<String,Object> map = new HashMap<String, Object>();
		map.put(Constant.FIELD_WX_OPENID, openid);
		UserCache userCache = userCacheDao.find(map);
		if(userCache == null) {
			url = HttpUtils.getErrorUrl(Constant.RETURN_CODE_STRING_WEIXIN_BING_ERROR, "用户没有绑定openid,请重新登陆"); 
			return new ModelAndView("forward:"+url.toString());
		}

		//最后则跳转去information或者file
		url = new StringBuilder(request.getRequestURI());
		url.replace(0, 12, "")//   /gykj/weixin   0~12
		.append("?").append(Constant.FIELD_WX_OPENID).append("=").append(openid);//openid记录到request    ?openid=xxx
		return new ModelAndView("forward:"+url.toString());
	}

	//http://192.168.1.7:8080/gykj/weixin/openid
	@RequestMapping(value="/openid", method={RequestMethod.POST, RequestMethod.GET})
	public void getOpenid(HttpServletRequest request, HttpServletResponse response,@RequestParam String callBackUrl) throws IOException {
		//获取code
		String code = request.getParameter("code");
		String state = request.getParameter("state");
		System.out.println("-WeiXinController-getOpenid-code-"+code);
		System.out.println("-WeiXinController-getOpenid-state-"+state);
		//避免重复授权这个死循环
		/*if(!StringUtils.isEmpty(state)){
			StringBuilder url = HttpUtils.getErrorUrl(Constant.RETURN_CODE_FAIL, "获取openid失败！");
			System.out.println(url);
 		   	try {
				request.getRequestDispatcher(url.toString()).forward(request, response);
				return null;
			}catch (Exception e) {
			} 
		}*/
		//如果没有code,则获取 code
		if(code==null||code.isEmpty()){
			// 获取本请求的请求你地址，
			String REDIRECT_URI = callBackUrl;
			logger.info("回调地址："+REDIRECT_URI);
			String URL_CODE = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=1#wechat_redirect";
			URL_CODE = URL_CODE.replaceFirst("APPID", WxUtils.getPropertyValue("appId")).replaceFirst("REDIRECT_URI", REDIRECT_URI).replaceFirst("SCOPE", SCOPE);
			//请求
			response.sendRedirect(URL_CODE);
			return;
		}
		System.err.println("------------------"+code);
		logger.info("------code=="+code);
		//如果有code，则获取openid
		String URL_OPENID = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
		URL_OPENID = URL_OPENID.replaceFirst("APPID", WxUtils.getPropertyValue("appId")).replaceFirst("SECRET", WxUtils.getPropertyValue("appsecret")).replaceFirst("CODE", code);
		logger.info("------URL_OPENID=="+URL_OPENID);
		String json=HttpUtils.execute(URL_OPENID);
		logger.info("token=="+json);
		String openid;		
		Map map = null;
		if(!StringUtils.isEmpty(json)){
			map = JsonUtils.toObject(json, Map.class);
		}
		logger.info("map="+map.toString());
		callBackUrl+="?openid="+map.get("openid");
		response.sendRedirect(callBackUrl);
	}

	//http://192.168.1.7:8080/gykj/weixin/validation
	@RequestMapping(value="/validation", method={RequestMethod.POST, RequestMethod.GET})
	public String validation(@RequestParam Map<String, Object> param) {
		System.out.println("-param-"+param);
		return "validation OK";
	}

	@RequestMapping(value={"/upload/{token}/{collection}/{action}"}, method={org.springframework.web.bind.annotation.RequestMethod.POST, org.springframework.web.bind.annotation.RequestMethod.GET})
	public Callable<Map<String, Object>> wxData(@PathVariable String collection, @PathVariable String action, @PathVariable String token, @RequestParam Map<String, Object> param, HttpServletRequest request)
	{
		Log log = new Log();
		InterfaceList interfaceList = this.informationService.getInterfaceList(collection, action, token, log);
		String obj = (String)param.get("obj");
		String usertoken = (String)param.get("token");
		List objObject = (List)JsonUtils.toObject(obj, List.class);
		if ((objObject == null) || (objObject.isEmpty())) {
			return getError("上传记录为空");
		}
		Map map = (Map)objObject.get(0);
		List<String> imgUrlList = (List)map.get("idcardpic");
		String basePath = request.getServletContext().getRealPath("/") + WxUtils.getPropertyValue("public_image_url");
		if ((basePath == null) || (basePath.trim().length() == 0)) {
			return getError("图片保存路径不能为空");
		}
		String userName=(log.getU_name()==null?"":log.getU_name());
		basePath=basePath+DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd")+"/"+map.get("username");
		StringBuffer imageNameList = new StringBuffer();
		if (imgUrlList != null) {
			for (String imgUrl : imgUrlList)
			{
				String locaPath = WxUtils.urlDownload(imgUrl, basePath + "/" + imgUrl + ".jpg");
				if (locaPath == null) {
					return getError("保存图片失败");
				}
				imageNameList.append(locaPath.substring(locaPath.lastIndexOf("/file")) + ",");
			}
		}
		map.remove("idcardpic");
		map.put("idcardpic", imageNameList.length() == 0 ? "" : imageNameList.substring(0, imageNameList.length() - 1));
		//map.put("filepath", basePath);
		objObject.clear();
		objObject.add(map);
		param.put("obj", JsonUtils.toJson(objObject));
		param.put("token", usertoken);
		return informationService.requestTCP(interfaceList, param, log);
	}

	private Callable<Map<String, Object>> getError(final String errorMessage)
	{
		new Callable()
	    {
	      public Map<String, Object> call()
	        throws Exception
	      {
	        Map<String, Object> param = new HashMap();
	        param.put("code", "0001");
	        param.put("message", errorMessage);
	        return param;
	      }
	    };
	    return null;
	}
	
	
	
	@RequestMapping(value={"/uploadCompany/{token}/{collection}/{action}"}, method={org.springframework.web.bind.annotation.RequestMethod.POST, org.springframework.web.bind.annotation.RequestMethod.GET})
	public Callable<Map<String, Object>> wxCompanyData(@PathVariable String collection, @PathVariable String action, @PathVariable String token, @RequestParam Map<String, Object> param, HttpServletRequest request)
	{
		Log log = new Log();
		InterfaceList interfaceList = this.informationService.getInterfaceList(collection, action, token, log);
		String obj = (String)param.get("obj");
		String usertoken = (String)param.get("token");
		List objObject = (List)JsonUtils.toObject(obj, List.class);
		if ((objObject == null) || (objObject.isEmpty())) {
			return getError("上传记录为空");
		}
		Map map = (Map)objObject.get(0);
		List<String> imgUrlList = (List)map.get("img_url");
		String basePath = request.getServletContext().getRealPath("/") + WxUtils.getPropertyValue("company_image_url");
		if ((basePath == null) || (basePath.trim().length() == 0)) {
			return getError("图片保存路径不能为空");
		}
		String userName=(log.getU_name()==null?"":log.getU_name());
		basePath=basePath+DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd")+"_"+userName;
		//StringBuffer imageNameList = new StringBuffer();
		String[] keys = new String[]{"business_certificate_pic","business_license_pic","registration_number_pic","pic"};
		if (imgUrlList != null) {
			String imgUrl;
			for (int i = 0; i < imgUrlList.size(); i++)
			{
				imgUrl = imgUrlList.get(i);
				String locaPath = WxUtils.urlDownload(imgUrl, basePath + "/" + imgUrl + ".jpg");
				if (imgUrl.trim().length()>0 && locaPath == null) {
					return getError("保存图片失败");
				}else if (imgUrl.trim().length() < 1) {
					locaPath = "";
				}
				map.put(keys[i], locaPath == "" ? "" :locaPath.substring(locaPath.lastIndexOf("/file")));
				//imageNameList.append(locaPath.substring(locaPath.lastIndexOf("/") + 1) + ",");
			}
		}
		map.remove("img_url");
		//map.put("filename", imageNameList.length() == 0 ? "" : imageNameList.substring(0, imageNameList.length() - 1));
		//map.put("filepath", basePath);
		objObject.clear();
		objObject.add(map);
		param.put("obj", JsonUtils.toJson(objObject));
		param.put("token", usertoken);
		return informationService.requestTCP(interfaceList, param, log);
	}
	
	
	
	@RequestMapping(value={"/uploadReceiveCheck/{token}/{collection}/{action}"}, method={org.springframework.web.bind.annotation.RequestMethod.POST, org.springframework.web.bind.annotation.RequestMethod.GET})
	public Callable<Map<String, Object>> wxReceiveCheckData(@PathVariable String collection, @PathVariable String action, @PathVariable String token, @RequestParam Map<String, Object> param, HttpServletRequest request)
	{
		Log log = new Log();
		InterfaceList interfaceList = this.informationService.getInterfaceList(collection, action, token, log);
		String obj = (String)param.get("obj");
		String usertoken = (String)param.get("token");
		List objObject = (List)JsonUtils.toObject(obj, List.class);
		if ((objObject == null) || (objObject.isEmpty())) {
			return getError("上传记录为空");
		}
		Map map = (Map)objObject.get(0);
		List<String> imgUrlList = (List)map.get("express_pic");
		List<String> imgUrlList2 = (List)map.get("box_pic");
		String basePath = request.getServletContext().getRealPath("/") + WxUtils.getPropertyValue("check_image_url");
		if ((basePath == null) || (basePath.trim().length() == 0)) {
			return getError("图片保存路径不能为空");
		}
		String userName=(log.getU_name()==null?"":log.getU_name());
		basePath=basePath+DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd")+"/"+(String)map.get("courier_username");
		StringBuffer imageNameList = new StringBuffer();
		//String[] keys = new String[]{"business_certificate_pic","business_license_pic","registration_number_pic","pic"};
		if (imgUrlList != null) {
			String imgUrl;
			for (int i = 0; i < imgUrlList.size(); i++)
			{
				imgUrl = imgUrlList.get(i);
				String locaPath = WxUtils.urlDownload(imgUrl, basePath + "/" + imgUrl + ".jpg");
				if (imgUrl.trim().length()>0 && locaPath == null) {
					return getError("保存图片失败");
				}else if (imgUrl.trim().length() < 1) {
					locaPath = "";
				}
				imageNameList.append(locaPath.substring(locaPath.lastIndexOf("/file")) + ",");
			}
		}
		map.remove("express_pic");
		map.put("express_pic", imageNameList.length() == 0 ? "" : imageNameList.substring(0, imageNameList.length() - 1));
		imageNameList = new StringBuffer();
		if (imgUrlList2 != null) {
			String imgUrl;
			for (int i = 0; i < imgUrlList2.size(); i++)
			{
				imgUrl = imgUrlList2.get(i);
				String locaPath = request.getServletContext().getRealPath("/") + WxUtils.urlDownload(imgUrl, basePath + "/" + imgUrl + ".jpg");
				if (imgUrl.trim().length()>0 && locaPath == null) {
					return getError("保存图片失败");
				}else if (imgUrl.trim().length() < 1) {
					locaPath = "";
				}
				imageNameList.append(locaPath.substring(locaPath.lastIndexOf("/file")) + ",");
			}
		}
		map.remove("box_pic");
		map.put("box_pic", imageNameList.length() == 0 ? "" : imageNameList.substring(0, imageNameList.length() - 1));

		//map.put("filename", imageNameList.length() == 0 ? "" : imageNameList.substring(0, imageNameList.length() - 1));
		//map.put("filepath", basePath);
		objObject.clear();
		objObject.add(map);
		param.put("obj", JsonUtils.toJson(objObject));
		param.put("token", usertoken);
		return informationService.requestTCP(interfaceList, param, log);
	}

}
