package com.gykj.interceptor;

import java.lang.reflect.Field;
import java.util.Iterator;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.gykj.utils.BeanUtils;

/**
 * 用于解析xx.yy的参数,只支持一层.
 * xx对应注解FormModel的值
 * @author xianyl
 *
 */
public class FromModelMethodArgumentsResolver implements HandlerMethodArgumentResolver {

	//在supportsParameter里判断参数是否满足我的Resolver
	@Override	
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(FormModel.class);
	}

	//在resolveArgument里我们可以处理这些参数。
	@Override
	public Object resolveArgument(MethodParameter parameter,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) throws Exception {
		String objName = parameter.getParameterName() + ".";
		//根据参数类型创建对象
		Object o = BeanUtils.instantiate(parameter.getParameterType());
		
		
		//获取对象的所有字段
		Field[] frr = parameter.getParameterType().getDeclaredFields();
		StringBuffer tmp;
		Object[] val;
		//获取所有参数,并挑选出来设置进o对象
		for (Iterator<String> itr = webRequest.getParameterNames(); itr
				.hasNext();) {
			tmp = new StringBuffer(itr.next());
			//如果参数前缀不一致,继续下一个
			if (tmp.indexOf(objName) < 0)
				continue;
			
			for (int i = 0; i < frr.length; i++) {
				//o对象成员变量为private,故必须进行此操作				
				frr[i].setAccessible(true);
				if (tmp.toString().equals(objName + frr[i].getName())) {
					//获取参数的值
					val = webRequest.getParameterValues(tmp.toString());
					//设置进对象
					BeanUtils.setProperty(o, frr[i].getName(), val);
				}
			}
		}
		return o;
	}

}
