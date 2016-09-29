package com.gykj.controller;


import java.util.Map;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.gykj.interceptor.AuthPassport;
import com.gykj.pojo.Log;
import com.gykj.pojo.Param;
import com.gykj.service.LogService;
import com.gykj.utils.BeanUtils;




/**
 * 日志 控制器
 * @author xianyl
 *
 */
@RestController
@RequestMapping("/log")
public class LogController extends BaseControllerByCRUD<LogService, Log>{	
	
	

	@RequestMapping(value="/list", method={RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> list(Log log, Integer pageNo, Integer pageSize) {			
		return super.list(BeanUtils.beanToMap(log), pageNo, pageSize);
	}
	

	@RequestMapping(value="/page", method={RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> page(Log log, Integer pageSize){
		return super.page(BeanUtils.beanToMap(log), pageSize);
	} 



	@RequestMapping(value="/info", method={RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> info(String id){
		return super.info(id);
	} 
	
	
	@AuthPassport(roles={"admin"})
	@RequestMapping(value="/delete", method={RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> delete(Log log) {
		return super.delete(log);
	}
	
	
	@AuthPassport(roles={"admin"})
	@RequestMapping(value="/delBatch", method={RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> delBatch(@RequestParam("ids[]")Object[] ids) {
		//get的方式 delBatch?ids[]=16&ids[]=17
		return super.delBatch(ids);
	} 
	
	@AuthPassport(roles={"admin"})
	@RequestMapping(value="/editBatch", method={RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> editBatch(@RequestParam("ids[]")Object[] ids, Param param) {
		return super.editBatch(BeanUtils.beanToMap(param), ids);
	}
	
}
