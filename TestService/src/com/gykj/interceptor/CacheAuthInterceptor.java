package com.gykj.interceptor;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.gykj.Constant;
import com.gykj.dao.UserCacheDao;
import com.gykj.pojo.UserCache;
import com.gykj.utils.HttpUtils;









/**
 * 缓存用户权限管理的拦截器 
 * @author xianyl
 *
 */
public class CacheAuthInterceptor extends HandlerInterceptorAdapter {
	@Resource
	private UserCacheDao userCacheDao;
	
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {    	
    
    	if(handler.getClass().isAssignableFrom(HandlerMethod.class)){
    		CachePassport cachePassport = ((HandlerMethod) handler).getMethodAnnotation(CachePassport.class);    		
    		//没有注解CachePassport，直接通过
    		if(cachePassport == null) return true;
    		
    		Object action = request.getAttribute("cachePassport_action");
    		Object collection = request.getAttribute("cachePassport_collection");
    		//如果没有collection/action或者登录的时候直接通过
    		if(collection==null || action==null || cachePassport.pass_action().equals(action.toString())) return true;
    		
    		//获取用户信息
    		Object token = request.getParameter(Constant.FIELD_TOKEN);
    		StringBuilder url = null;
    		if(token == null){
     		   	url = HttpUtils.getErrorUrlRedirect(Constant.RETURN_CODE_STRING_FAIL, "请传递token值！");
     		    response.sendRedirect(url.toString());
    			return false;
    		}
    		UserCache userCache = userCacheDao.findUserByToken(token.toString());
    		if(userCache == null){
     		   	url = HttpUtils.getErrorUrlRedirect(Constant.RETURN_CODE_STRING_FAIL, "无user缓存信息！");
     		   	response.sendRedirect(url.toString());
    			return false;
    		}
    		
    		//保存用户
    		request.setAttribute("user", userCache.getCache());
    		
			//退出登录的时候,修改数据后直接拦截
			if(cachePassport.prevent_action().equals(action.toString())){
				userCache.setToken(null);
				userCache.setLogin_time(null);
				userCacheDao.update(userCache);
				//返回数据
				url = HttpUtils.getErrorUrlRedirect(Constant.RETURN_CODE_STRING_SUCCESS, "退出登陆成功！");
				response.sendRedirect(url.toString());       
				return false;
			}
			//正常验证
			boolean ispass = validationCache(userCache.getCache(), action.toString(), collection.toString());
			if(!ispass){
            	//返回数据
				url = HttpUtils.getErrorUrlRedirect(Constant.RETURN_CODE_STRING_FAIL, "用户没有权限访问该接口！");
				response.sendRedirect(url.toString());
			}
			return ispass; 		
        }else{
        	return true;
        }
     }
   

	@Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request,
    		HttpServletResponse response, Object handler) throws Exception {
    	String[] uri = request.getRequestURI().split("/");//长度7   /gykj//information/4945f9a9-5f59-4c99-b720-3d5f8c4e370f/user/edit
    	
    	request.setAttribute("cachePassport_action", uri[uri.length-1]);
    	request.setAttribute("cachePassport_collection", uri[uri.length-2]);
    	super.afterConcurrentHandlingStarted(request, response, handler);
    }
    
	
	/**
	 * 验证实际用户的权限
	 * @param request
	 * @param string
	 * @return
	 */
    @SuppressWarnings("unchecked")
	private boolean validationCache(Map<String, Object> cache, String action, String collection) {
		if(cache == null) return false;
		List<Map<String, Object>> roles = (List<Map<String, Object>>) cache.get("roles");
		if(roles == null) return false;
		List<Map<String, Object>> permissions;
		String p_action;
		String p_collection;
		for(Map<String, Object> role : roles){
			permissions = (List<Map<String, Object>>) role.get("permissions");
			if(permissions == null) continue;
			for(Map<String, Object> permission: permissions){
				p_action = (String) permission.get("action");
				p_collection = (String) permission.get("collection");
				//System.out.println(p_action+"="+action+";"+p_collection+"="+collection);
				if(action.equals(p_action) && collection.equals(p_collection))
					return true;
			}
			
		}
		return false;
	}
	
   
}
