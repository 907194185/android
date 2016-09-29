package com.gykj.controller;


import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gykj.Constant;
import com.gykj.interceptor.AuthPassport;
import com.gykj.pojo.Tcp;
import com.gykj.service.TcpService;
import com.gykj.utils.BeanUtils;
import com.gykj.utils.JsonUtils;




/**
 * Tcp 控制器
 * @author xianyl
 *
 */
@RestController
@RequestMapping("/tcp")
public class TcpController extends BaseControllerByCRUD<TcpService, Tcp>{	
	
	

	@RequestMapping(value="/list", method={RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> list(Tcp tcp, Integer pageNo, Integer pageSize) {			
		return super.list(BeanUtils.beanToMap(tcp), pageNo, pageSize);
	}
	

	@RequestMapping(value="/page", method={RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> page(Tcp tcp, Integer pageSize){
		return super.page(BeanUtils.beanToMap(tcp), pageSize);
	} 



	@RequestMapping(value="/info", method={RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> info(String id){
		return super.info(id);
	} 
	
	
	@AuthPassport(roles={"admin"})
	@RequestMapping(value="/save", method={RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> save(@RequestParam(required=false) String model) {
		Tcp tcp = JsonUtils.toObject(model, baseService.getDao().getPojoClass());
		//验证对象，如果有问题则返回信息
		
		
		//检查一下数据是否已经存在数据
		Tcp bean = null;
		try {
			//检查一下数据是否已经存在数据
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("name", tcp.getName());
			bean = baseService.find(params);
		} catch (Exception e) {
		}
		if(bean != null){
			return getBaseData(Constant.RETURN_CODE_DATA_EXIST, "该名字的数据已存在", bean);
		}
		//再保存		
		return super.save(tcp);
    } 
	
	@AuthPassport(roles={"admin"})
	@RequestMapping(value="/edit", method={RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> edit(@RequestParam(required=false) String model) {
		Tcp tcp = JsonUtils.toObject(model, baseService.getDao().getPojoClass());
		//验证对象，如果有问题则返回信息
		
		//检查一下数据是否已经存在数据
		Tcp bean = null;
		try {
			bean = baseService.get(tcp.getId());
		} catch (Exception e) {
		}
		if(bean == null){
			return getBaseData(Constant.RETURN_CODE_FAIL, "该id的数据不存在", tcp);
		}		
		
		return super.edit(tcp);
	}
	
	@AuthPassport(roles={"admin"})
	@RequestMapping(value="/delete", method={RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> delete(Tcp tcp) {
		return super.delete(tcp);
	}
	
	
	@AuthPassport(roles={"admin"})
	@RequestMapping(value="/delBatch", method={RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> delBatch(@RequestParam("ids[]")Object[] ids) {
		//get的方式 delBatch?ids[]=16&ids[]=17
		return super.delBatch(ids);
	} 
	
	@AuthPassport(roles={"admin"})
	@RequestMapping(value="/editBatch", method={RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> editBatch(@RequestParam("ids[]")Object[] ids, Tcp tcp) {
		return super.editBatch(BeanUtils.beanToMap(tcp), ids);
	}
	
	
	
}
