package com.gykj.tcp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gykj.Constant;
import com.gykj.dao.LogDao;
import com.gykj.dao.UserCacheDao;
import com.gykj.pojo.InterfaceList;
import com.gykj.pojo.Log;
import com.gykj.pojo.Tcp;
import com.gykj.pojo.UserCache;
import com.gykj.tcp.TCPClient.TCPClienListener;
import com.gykj.utils.DateTimeUtil;
import com.gykj.utils.DigestUtils;
import com.gykj.utils.InstitutionFilterUtils;
import com.gykj.utils.JsonUtils;
import com.gykj.utils.ParamUtils;
import com.gykj.utils.TaskJsonUtils;





/**
 * tcp请求回调对象
 * @author xianyl
 *
 */
public class TcpCallable implements  Callable<Map<String, Object>>,TCPClienListener{
	private LogDao logDao;
	private UserCacheDao userCacheDao;
	private String json = null;//tcp返回的数据
	private String tackJson;
	private Log log;//任务日志
	private Map<String, Object> result = new HashMap<String, Object>();//结果
	private InterfaceList interfaceList;//清单
	private Map<String, Object> param;//参数
	private TCPClient tcpClient = null;
	private Logger logger = Logger.getLogger(TcpCallable.class);


	public TcpCallable(InterfaceList interfaceList, Map<String, Object> param, Log log, LogDao logDao, UserCacheDao userCacheDao) {
		this.interfaceList = interfaceList;
		this.param = param;
		this.log = log;
		this.logDao = logDao;
		this.userCacheDao = userCacheDao;
		this.result.put(Constant.JSON_RESPONSE_FIELD_CODE, Constant.RETURN_CODE_STRING_FAIL);			
	}

	@Override
	public Map<String, Object> call() throws Exception {		
		//检查interfaceList是否为null
		if(interfaceList == null){
			this.result.put(Constant.JSON_RESPONSE_FIELD_MESSAGE, "无对应接口清单");
			return result;
		}
		//检查tcp 参数
		Tcp tcp = interfaceList.getTcp();
		if(tcp == null || StringUtils.isEmpty(tcp.getName()) || StringUtils.isEmpty(tcp.getIp()) || tcp.getPort() == null){
			this.result.put(Constant.JSON_RESPONSE_FIELD_MESSAGE, "无对应Tcp,或者tcp信息错误");
			return result;
		}
		List<Map<String,Object>> param_obj = null;
		//不是login动作进入
		if (!Constant.FIELD_USER_LOGIN.equals(interfaceList.getAction())) {
			int ret = InstitutionFilterUtils.filter(interfaceList, param, userCacheDao);
			if (ret==1) {
				result.put(Constant.JSON_RESPONSE_FIELD_MESSAGE, "您暂无权限查询！");
				return result;
			}
		}
		System.out.println("institution过滤后参数："+param);
		//整理参数
		List<Map<String,Object>> obj = ParamUtils.tidyParam(param);
		//过滤参数
		ParamUtils.filterParam(obj, interfaceList.getReq_param());
		//验证参数
		if(!ParamUtils.validateParam(obj, interfaceList.getAll_param(), interfaceList.getAction().equals("save"))){
			//验证不成功
			this.result.put(Constant.JSON_RESPONSE_FIELD_MESSAGE, "参数验证不成功");
			return result;
		}

		//先记录请求的日志
		if(log == null) log = new Log();
		log.setIl_action(interfaceList.getAction());
		log.setIl_collection(interfaceList.getCollection());
		log.setIl_name(interfaceList.getName());
		log.setIscycle(false);
		log.setTs_name(tcp.getName());
		log.setCreate_time(DateTimeUtil.getCurrenteDate());
		log.setFlag(0);


		//获取taskType
		Integer taskType = (Integer) param.remove("taskType");
		if(taskType == null) taskType = 1;
		String openid = null;
		//组装tackJson
		if(Constant.FIELD_USER_LOGIN.equals(interfaceList.getAction()) 
				|| "link".equalsIgnoreCase(interfaceList.getAction())){
			param_obj = (List<Map<String, Object>>) param.get("obj");
			if (param_obj==null) {
				this.result.put(Constant.JSON_RESPONSE_FIELD_MESSAGE, "参数不正确！");
				return result;
			}
			for (Map<String, Object> map : param_obj) {
				if (map.containsKey("password")) {
					map.put("password",DigestUtils.md5FromString(map.get("password").toString()));
				} 
				if (map.containsKey("openid") && map.get("openid")!=null) {
					openid = (String) map.get("openid");
					map.remove("openid");

				}
			}
			param = TaskJsonUtils.joinContent(interfaceList.getCollection(), "list", param);
		}else {
			param = TaskJsonUtils.joinContent(interfaceList.getCollection(), interfaceList.getAction(), param);		
		}
		log.setContent(JsonUtils.toJson(param));
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				logDao.insert(log);
			}
		}).start();
		//log = this.logDao.insert(log);

		param = TaskJsonUtils.joinTask("", Constant.VALUE_LOCALHOST_TCP_NAME, log.getCreate_time(), taskType, param);	//传过去的tcpname，是本服务器的标认识		
		tackJson = JsonUtils.toJson(param);
		System.out.println("发送数据："+tackJson);
		//tcp请求
		if(tcpClient == null){
			tcpClient = new TCPClient(this, false);
		}	
		tcpClient.open(tcp.getIp(), tcp.getPort());//连接
		tcpClient.sendResult(tackJson);//发送信息
		System.out.println("-send---"+tackJson);		
		//检查是否返回数据,因为需要防止服务端接收到消息而不返回的情况，这个时候等tcp超时，超时就会关掉而跳出这个循环
		while(tcpClient.isConnect()){
			Thread.sleep(100);				
		}

		//json转result
		if(StringUtils.isEmpty(this.json)){
			log.setFlag(2);//处理不成功,1分钟才返回这个，说明tcp服务无数据返回；几秒返回这个，说tcp服务没开
			this.result.put(Constant.JSON_RESPONSE_FIELD_MESSAGE, "Tcp无连接或者请求超时");
		}else{
			log.setFlag(1);//处理成功
			log.setAck_time(DateTimeUtil.getCurrenteDate());
			System.out.println("返回："+json);
			result = JsonUtils.toCollection(json, new TypeReference<Map<String,Object>>() {});
			result = TaskJsonUtils.getContentJson(result);//获取ContentJson
			//登录的操作，缓存用户信息
			if(Constant.FIELD_USER_LOGIN.equals(interfaceList.getAction()) && !"organization".equals(interfaceList.getCollection())){
				UserCache userCache = new UserCache();
				ArrayList username =  (ArrayList) result.get("result");
				Map<String, Object> map = null;
				if (username!=null && username.size()>0) {
					map = (Map<String, Object>)username.get(0);
				}else {
					this.result.put(Constant.JSON_RESPONSE_FIELD_MESSAGE, "用户名或密码不正确！");
					return result;
				}
				if(map != null){
					userCache.setName(map.get("username").toString());
					userCache.setIg_name(log.getIg_name());				
					userCache.setFlag(1);
					userCache.setToken(UUID.randomUUID().toString());
					userCache.setLogin_time(DateTimeUtil.getCurrenteDate());
					userCache.setCache(TaskJsonUtils.getModel(result));
					//先获取openid,使用微信的时候会有值的
					//Object openid = param.get(Constant.FIELD_WX_OPENID);				
					if(openid != null) userCache.setOpenid(openid.toString());
					//更新
					userCacheDao.update(userCache);
					//加上token
					result.put(Constant.FIELD_TOKEN, userCache.getToken());
					logger.info(result);
				}
			}
		}

		//更新日志		
		this.logDao.update(log);

		return result;
	}



	@Override
	public void getStask(String json) {		
		//这是服务端返回的信息，不一定触发，比如协议包规则不对或者服务端不主动返回信息。
		this.json = json;
		//关闭连接
		if(tcpClient != null) tcpClient.close();
	}
	@Override
	public void getError(byte[] data) {
		//try {
		//	//System.out.println("error-data-"+ ByteUtils.toHexAscii(data));

		//System.out.println("error-data-"+ new String(data,"UTF-8"));
		//} catch (UnsupportedEncodingException e) {
		//e.printStackTrace();
		//}
		//关闭连接
		if(tcpClient != null) tcpClient.close();
	}


	@Override
	public void getAck(byte type) {
		//这里是tcp服务端对客户端的应答信息
	}
	@Override
	public void close() {
		//tcp服务超时时间120秒，如果120秒还没有任何数据返回，则通知spring异步响应不用等待，spring异步响应超时时间90秒
	}


}