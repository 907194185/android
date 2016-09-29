package com.gykj.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WxUtils {
	//private static WXApi wxApi;
	public static String APP_ID;//微信APPID  wx1db4c9335d280036
	public static String APP_SECRET;//微信APPSECRET 95dffc454b7177ae2d759af33aa6b636
	public static final String TOKEN="meteo";
	private static final String EncodingAESKey="";
	private static Properties properties = null;
	public static String token = null;
	public static String time = null;
	public static String jsapi_ticket = null;
	public enum EventKey
	{
		INFO_ALERT,
		INFO_PREDICT,
		INFO_ACTUAL,
		INFO_STOP_CLASS,
		HELP;
	}
	static{
		initProperties();
		APP_ID=(String) properties.get("appId");
		APP_SECRET=(String) properties.get("appsecret");
	}


	public static void initProperties(){
		properties = new Properties();
		try {

			InputStream in;
			in = WxUtils.class.getClassLoader().getResourceAsStream("wx_config.properties");
			properties.load(new InputStreamReader(in, "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static String getPropertyValue(String key){
		return properties.getProperty(key);
	}


	//private static Logger log = LoggerFactory.getLogger(WxUtils.class);
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(WxUtils.class);

	// 菜单创建（POST） 限100（次/天）

	public static String menu_create_url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

	/**
	 * 创建菜单
	 * 
	 * @param menu 菜单实例
	 * @param accessToken 有效的access_token
	 * @return 0表示成功，其他值表示失败
	 */
	public static int createMenu(Map menu) {
		int result = 0;		
		// 拼装创建菜单的url
		String url = menu_create_url.replace("ACCESS_TOKEN", getAccess_token(APP_ID, APP_SECRET));
		// 将菜单对象转换成json字符串
		String jsonMenu = JsonUtils.toJson(menu);
		System.out.println(jsonMenu);
		// 调用接口创建菜单
		Map jsonObject = HttpsUtil.httpRequest(url, "POST", jsonMenu);

		if (null != jsonObject) {
			if (0 != (Integer)jsonObject.get("errcode")) {
				result = (Integer)jsonObject.get("errcode");
				System.out.println("创建菜单失败 errcode:{} errmsg:{}"+jsonObject.get("errcode")+jsonObject.get("errmsg"));
				//log.error("创建菜单失败 errcode:{} errmsg:{}", jsonObject.get("errcode"), jsonObject.get("errmsg"));

			}
		}

		return result;
	}

	public static String getAccess_token(String appid, String appsecret) {
		//凭证获取(GET)
		String token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
		String requestUrl = token_url.replace("APPID", appid).replace("APPSECRET", appsecret);
		// 发起GET请求获取凭证
		String access_token = null;
		Map jsonObject = null;
		String json;
		try {
			json = HttpUtils.execute(requestUrl);
			jsonObject = JsonUtils.toObject(json, Map.class);
			if (null != jsonObject) {
				log.info("jsonObject="+jsonObject.toString());

				access_token = String.valueOf(jsonObject.get("access_token"));
			}
		} catch (Exception e) {
			// 获取token失败
			// log.error("获取token失败 errcode:{} errmsg:{}", jsonObject.get("errcode"), jsonObject.get("errmsg"));
		}
		return access_token;
	}

	public static String getJsApiTicket(String access_token) {
		String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
		String requestUrl = url.replace("ACCESS_TOKEN", access_token);
		// 发起GET请求获取凭证
		String ticket = null;
		Map jsonObject = null;
		try {
			jsonObject = HttpsUtil.httpRequest(requestUrl, "GET", null);

			if (null != jsonObject) 
				ticket = String.valueOf(jsonObject.get("ticket"));
		} catch (Exception e) {
			// 获取token失败
			// log.error("获取token失败 errcode:{} errmsg:{}", jsonObject.get("errcode"), jsonObject.get("errmsg"));
		}
		return ticket;
	}

	public static String getParam(HttpServletRequest request){
		time=(time==null?getTime():time);
		if(token == null){
			token = getAccess_token(APP_ID, APP_SECRET);
			log.error("---accesstoken1="+token);
			jsapi_ticket = getJsApiTicket(token);
			log.error("---ticket1="+jsapi_ticket);
			time = getTime();
		}else{
			if(!time.substring(0, 13).equals(getTime().substring(0, 13))){ //每小时刷新一次
				token = null;
				token = getAccess_token(APP_ID, APP_SECRET);
				log.error("---accesstoken2="+token);
				jsapi_ticket = getJsApiTicket(token);
				log.error("---ticket2="+jsapi_ticket);
				time = getTime();
			}
		}

		String url = getUrl(request);

		Map<String, String> params = sign(jsapi_ticket, url);
		params.put("appid", APP_ID);
		String jsonStr = JsonUtils.toJson(params);
		System.out.println(jsonStr);
		return jsonStr;
	}

	private static String getAbleAccessToken(){
		time=(time==null?getTime():time);
		if(token!=null){
			if(!time.substring(0, 13).equals(getTime().substring(0, 13))){ //每小时刷新一次
				token = null;
				token = getAccess_token(APP_ID, APP_SECRET);
				time = getTime();
			}
		}else
			token = getAccess_token(APP_ID, APP_SECRET);
		return token;
	}
	private static String getUrl(HttpServletRequest request){
		//   HttpServletRequest request = ServletActionContext.getRequest();

		String requestUrl = request.getParameter("url");
		System.err.println(requestUrl);
		/*String queryString = request.getQueryString();
        String url = queryString!=null?requestUrl +"?"+queryString:requestUrl.toString();*/
		return requestUrl;
	}

	public static Map<String, String> sign(String jsapi_ticket, String url) {
		Map<String, String> ret = new HashMap<String, String>();
		String nonce_str = create_nonce_str();
		String timestamp = create_timestamp();
		String str;
		String signature = "";

		//注意这里参数名必须全部小写，且必须有序
		str = "jsapi_ticket=" + jsapi_ticket +
				"&noncestr=" + nonce_str +
				"&timestamp=" + timestamp +
				"&url=" + url;

		try
		{
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(str.getBytes("UTF-8"));
			signature = byteToHex(crypt.digest());
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}

		ret.put("url", url);
		ret.put("jsapi_ticket", jsapi_ticket);
		ret.put("nonceStr", nonce_str);
		ret.put("timestamp", timestamp);
		ret.put("signature", signature);

		return ret;
	}

	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash)
		{
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

	private static String create_nonce_str() {
		return UUID.randomUUID().toString();
	}

	private static String create_timestamp() {
		return Long.toString(System.currentTimeMillis() / 1000);
	}

	//获取当前系统时间 用来判断access_token是否过期
	public static String getTime(){
		Date dt=new Date();
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(dt);
	}

	/*public static void main(String[] args) throws WXException {
		String s="<xml><ToUserName><![CDATA[gh_774ddb43aa09]]></ToUserName>" +
				"<FromUserName><![CDATA[oczQnw6T8LvmDUNJbtDdh4XIEDTo]]></FromUserName>" +
				"<CreateTime>1450691146</CreateTime>" +
				"<MsgType><![CDATA[text]]></MsgType>" +
				"<Content><![CDATA[阿里郎]]></Content>" +
				"<MsgId>6230671029068129122</MsgId></xml>";
		try {
			WXMessageBase msg = new WxUtils().getWxApi().consumeMessage(s);
			String token = getJsApiTicket(getAccess_token(APP_ID,APP_SECRET));
			System.out.println(msg);
			System.out.println(token);
		} catch (WXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//WXMenuGetResponse wxmenu = new WxUtils().getWxApi().getMenu();

		//System.out.println(JSONObject.fromObject(wxmenu.getMenu()));
	}*/

	public static String URLRequest(String url) throws IOException{
		URL readUrl=new URL(url);
		URLConnection con = readUrl.openConnection();
		con.setConnectTimeout(1000 * 60 * 2);			
		con.setReadTimeout(1000 * 60);
		java.io.BufferedReader readStr = new BufferedReader(
				new InputStreamReader(con.getInputStream(),"utf-8"));

		StringBuffer retStr=new StringBuffer();
		String line = readStr.readLine();
		while (line != null) {
			retStr.append(line);
			line = readStr.readLine();
		}
		return retStr.toString();
	}



	public static List<Map> clearMenu(List<Map> menuList,String collection){
		List<Map> clearList=new ArrayList<Map>();
		for(Map menu:menuList){
			Map menuTemp=new HashMap();
			Set<String> keys=menu.keySet();
			for(String key:keys){
				Object item=menu.get(key);
				if("name".equalsIgnoreCase(key))
					menuTemp.put("name", item);	
				if("type".equalsIgnoreCase(key))
					menuTemp.put("type", item);
				if("url".equalsIgnoreCase(key)){
					if("click".equalsIgnoreCase((String) menuTemp.get("type")))
						menuTemp.put("key", item);
					if("view".equalsIgnoreCase((String) menuTemp.get("type")))
						menuTemp.put("url", item);
				}
				/*	if("key".equalsIgnoreCase(key))

    			if("url".equalsIgnoreCase(key))
    				menuTemp.put("url", item);*/
				if(collection.equalsIgnoreCase(key)){ 				
					menuTemp.put("sub_button", clearMenu((List)item,collection));
				}
			}
			clearList.add(menuTemp);
		}
		return clearList;
	}


	public static String urlDownload(String serverId,String localPath){
		try {
			URL url=new URL(getWxSourceUrl(serverId));
			URLConnection uc = url.openConnection();
			File file=new File(localPath);
			if(!file.getParentFile().exists())
				file.getParentFile().mkdirs();
			File log = new File("/home/uploadfilelog.txt");
			if(!log.exists())
				log.createNewFile();
			FileOutputStream out = new FileOutputStream(log, true);
			out.write((serverId + ":" + localPath + "=" + uc.getContentLength() + "\r\n").getBytes());
			
			BufferedInputStream bis=new BufferedInputStream(uc.getInputStream());
			BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(file));
			byte[] buffer=new byte[1024];
			int data=0;
			while((data=bis.read(buffer))!=-1){
				bos.write(buffer,0,data);
			}
			bis.close();
			bos.close();
			out.close();
			return localPath;

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static String getWxSourceUrl(String serverid) throws IOException{
		String sourceurl="http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=%s&media_id=%s";
		sourceurl=String.format(sourceurl, getAbleAccessToken(),serverid);
		return sourceurl;
	}

	public static void main(String[] args) {

		try {
			System.err.println(getWxSourceUrl("ygmMys7T7zult2f7657C_wx1wnE4Qfhq826rL6chn3RhrwWfCMWT5_zkWkAn3fCD"));;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
