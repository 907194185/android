package com.gykj.utils;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 拼接TaskJson 工具类
 * @author xianyl
 *
 */
public class TaskJsonUtils {

	
	
	/**
	 *  组装contentJson参数
	 * @param document
	 * @param action
	 * @param parameters
	 * @return
	 */
    public static Map<String, Object> joinContent(String document
									    		,String action
									    		,Map<String, Object> parameters) {  
    	Map<String, Object> content_param = new HashMap<String, Object>();
    	content_param.put("document", document);
    	content_param.put("action", action);
    	content_param.put("parameter", parameters);
		return content_param;
    }  
	
	
	/**
	 *  组装Task参数
	 * @param taskId 任务id
	 * @param clientId 
	 * @param datetime
	 * @param taskType 请求的类型码  0.表示发送后就断开；1.表示需要等待响应，交互完成后才断开
	 * @param contentJson
	 * @return
	 */
    public static Map<String, Object> joinTask(String taskId
									    		,String clientId
									    		,Date datetime
									    		,Integer taskType
									    		,Map<String, Object> contentJson) {  
    	Map<String, Object> tack_param = new HashMap<String, Object>();
		tack_param.put("taskId", taskId);
		tack_param.put("clientId", clientId);
		tack_param.put("datetime", DateTimeUtil.formatDate(datetime, DateTimeUtil.formatter_no_separator));
		tack_param.put("taskType", taskType);		
		tack_param.put("contentJson", contentJson);		
		return tack_param;
    }  
    
    
    /**
	 *  从返回数据获取ContentJson
	 * @param result 返回数据
	 * @return
	 */
    @SuppressWarnings("unchecked")
	public static Map<String, Object> getContentJson(Map<String, Object> result) {
    	return (Map<String, Object>) result.get("contentJson");		
    } 
  
    /**
	 *  从ContentJson获取数据
	 * @param ContentJson 数据,获取第1个模型
	 * @return
	 */
    @SuppressWarnings("unchecked")
	public static Map<String, Object> getModel(Map<String, Object> result) {
    	Object object = result.get("result");
    	if(object != null && object instanceof List){ 
    		List<Map<String, Object>> models = (List<Map<String, Object>>)object;
    		if(models != null && models.size() > 0)
    			return models.get(0);
    	}
    	return null;		
    } 
}  