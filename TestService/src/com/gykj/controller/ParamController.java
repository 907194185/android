package com.gykj.controller;


import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gykj.Constant;
import com.gykj.interceptor.AuthPassport;
import com.gykj.pojo.Param;
import com.gykj.service.ParamService;
import com.gykj.utils.BeanUtils;
import com.gykj.utils.JsonUtils;




/**
 * Param 控制器
 * @author xianyl
 *
 */
@RestController
@RequestMapping("/param")
public class ParamController extends BaseControllerByCRUD<ParamService, Param>{	
	
	

	@RequestMapping(value="/list", method={RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> list(Param param, Integer pageNo, Integer pageSize) {			
		return super.list(BeanUtils.beanToMap(param), pageNo, pageSize);
	}
	

	@RequestMapping(value="/page", method={RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> page(Param param, Integer pageSize){
		return super.page(BeanUtils.beanToMap(param), pageSize);
	} 



	@RequestMapping(value="/info", method={RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> info(String id){
		return super.info(id);
	} 
	
	
	@AuthPassport(roles={"admin"})
	@RequestMapping(value="/save", method={RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> save(@RequestParam(required=false) String model) {
		Param param = JsonUtils.toObject(model, baseService.getDao().getPojoClass());	
		//验证对象，如果有问题则返回信息
		
		
		//检查一下数据是否已经存在数据
		Param bean = null;
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("name", param.getName());
			params.put("type", param.getType());
			params.put("isnecessity", param.getIsnecessity());
			bean = baseService.find(params);
		} catch (Exception e) {
		}
		
		if(bean != null){
			return getBaseData(Constant.RETURN_CODE_DATA_EXIST, "该参数的名字、类型、是否必要的组合已存在", bean);
		}
		
		//再保存		
		return super.save(param);
    } 
	
	@AuthPassport(roles={"admin"})
	@RequestMapping(value="/edit", method={RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> edit(@RequestParam(required=false) String model) {
		Param param = JsonUtils.toObject(model, baseService.getDao().getPojoClass());
		//验证对象，如果有问题则返回信息
		
		
		//检查一下数据是否已经存在数据
		Param bean = null;
		try {
			bean = baseService.get(param.getId());
		} catch (Exception e) {
		}
		if(bean == null){
			return getBaseData(Constant.RETURN_CODE_FAIL, "该id的数据不存在", param);
		}
		
		return super.edit(param);
	}
	
	@AuthPassport(roles={"admin"})
	@RequestMapping(value="/delete", method={RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> delete(Param param) {
		return super.delete(param);
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
