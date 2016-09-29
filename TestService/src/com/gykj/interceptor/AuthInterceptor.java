package com.gykj.interceptor;


import java.util.Arrays;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.gykj.Constant;
import com.gykj.dao.UserDao;
import com.gykj.pojo.User;
import com.gykj.utils.HttpUtils;









/**
 * 权限管理的拦截器 
 * @author xianyl
 *
 */
public class AuthInterceptor extends HandlerInterceptorAdapter {
	@Resource
	private UserDao userDao;
	
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {    	
    
    	if(handler.getClass().isAssignableFrom(HandlerMethod.class)){
    		//正常验证本系统的用户
    		AuthPassport authPassport = ((HandlerMethod) handler).getMethodAnnotation(AuthPassport.class);
            //没有声明需要权限,或者声明不验证权限
            if(authPassport == null || authPassport.validate() == false){
            	return true;
            }else{            	
            	//这里直接写验证的处理，实际项目，把这里替换一下即可
            	boolean isPass = validationPermissions(request, authPassport);
                if(isPass){
                	//如果验证成功返回true
                	return true;
                }else{
         		   	StringBuilder url = HttpUtils.getErrorUrl(Constant.RETURN_CODE_FAIL, "token验证失败，你没有权限访问！");
         		   	request.getRequestDispatcher(url.toString()).forward(request, response);         		   
                    return false;
                }  
            }
        }else{
        	return true;
        }
     }
 
    
    /**
     * 验证权限
     * @param token
     * @param authPassport
     * @return
     */
    public boolean validationPermissions(HttpServletRequest request, AuthPassport authPassport) {
    	Object token = request.getParameter(Constant.FIELD_TOKEN);
    	if(token == null) return false;
    	User user = userDao.findUserByToken(token.toString());
    	if(user == null) return false;
    	String role = user.getRole();
    	if(StringUtils.isEmpty(role)) return false;
    	try {
    		return Arrays.asList(authPassport.roles()).contains(role);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return false;
	}

   
}
