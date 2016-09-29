package com.gykj.interceptor;


import java.util.HashMap;

import javax.servlet.ServletContextEvent;

import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.gykj.Constant;
import com.gykj.pojo.User;
import com.gykj.service.UserService;
import com.gykj.tcp.TcpControl;
import com.gykj.utils.LogUtils;




/**
 * web项目监听，启动或销毁
 * @author xianyl
 *
 */
public class WebContextListener extends ContextLoaderListener{
	private WebApplicationContext context;
	private TcpControl tcpControl;
	
    
    /**
     * 启动
     */
    public void contextInitialized(ServletContextEvent event) {
    	super.contextInitialized(event);
    	//先获取当前服务的tcp.name
    	HashMap<String, Object> params = new HashMap<String, Object>();
    	params.put("name", "admin");
    	UserService userService = (UserService) getBean(event, "userService");
    	User user = userService.find(params);
    	try {
    		Constant.VALUE_LOCALHOST_TCP_NAME = user.getTcps().get(0).getName();
		} catch (Exception e) {
			LogUtils.error("请检查是否有admin管理员，并且他有 tcp服务器 ", e);
		}
    	
    	
    	//获取tcpControl
    	tcpControl = null;
    	try {
    		//tcpControl = (TcpControl) getBean(event, "tcpControl");
		} catch (Exception e) {
		}
    	// 启动tcp服务
    	if(tcpControl != null) tcpControl.openTcp();
    	
    	
    }
	
	/**
	 * 销毁
	 */
    public void contextDestroyed(ServletContextEvent event) {
    	// 停止tcp服务
    	if(tcpControl != null) tcpControl.closeTcp();
    	
    }
	
    
    /**
     * 获取bean
     * @param event
     * @param beanName
     * @return
     */
	private Object getBean(ServletContextEvent event, String beanName){
    	Object object = null;
    	if(context == null){
    		context = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
    	}
    	if(context != null){ 
    		object = context.getBean(beanName); 
    	}
    	return object; 
    } 
    
}
