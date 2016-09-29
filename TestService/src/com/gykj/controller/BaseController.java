package com.gykj.controller;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.gykj.Constant;
import com.gykj.PropertyConfigurer;
import com.gykj.service.BaseService;






/**
 * 基本控制类,控制层就不抽接口了，没地方需要调用它，这里提供控制层能复用的方法
 * 使用注解@RequestMapping映射，应该相当于继承了MultiActionController，不管了，先用着。^-^!!
 * 所以这里也就不需要继承spring提供的控制器。
 * 
 * @author xianyl
 * 
 */
public class BaseController<S extends BaseService<?, T>,T> {
	@Resource
	protected PropertyConfigurer propertyConfigurer;// 这是获取配置文件数据的类	
	@Autowired
	protected S baseService;
	
	//@Resource
	//protected HttpSession session;

	/**
	 * * 获取基本json数据 {
	 * 					"code": 0 // 接口访问成功或者失败的状态码
	 *					"message": "操作成功" // 接口访问返回的消息文字 
	 *				  }
	 * 
	 * @param code 参考ResultCode.properties
	 * @param message 可以传空，如果为空，自动到ResultCode.properties查消息
	 * @param data 需要返回的数据
	 * @return
	 */
	protected Map<String, Object> getBaseData(Integer code, String message, Object data) {
		if (StringUtils.isEmpty(code))
			code = Constant.RETURN_CODE_SUCCESS;// 默认为"0"请求成功，参考ResultCode.properties
		if (StringUtils.isEmpty(message))
			message = propertyConfigurer.getContextProperty(String.valueOf(code));
		// 组合数据
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(Constant.JSON_RESPONSE_FIELD_CODE, code);
		map.put(Constant.JSON_RESPONSE_FIELD_MESSAGE, message);
		// 加入数据
		if (data != null)
			map.put(Constant.JSON_RESPONSE_FIELD_DATA, data);
		return map;
	}


	
	

	/**
	 * 重定向
	 * @param address 重定向到的地址
	 *  controller内部重定向，redirect:加上同一个controller中的requestMapping的值，例如 "redirect:toform.do"
	 *  controller之间的重定向：必须要指定好controller的命名空间再指定requestMapping的值，redirect：后必须要加/,是从根目录开始 ，如"redirect:/test1/toForm.do"
	 * @return
	 */
	protected String redirect(String address){
		return new StringBuilder("redirect:").append(address).toString();
	}
	
	

}
