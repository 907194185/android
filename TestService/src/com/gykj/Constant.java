package com.gykj;



/***
 * 常量池
 * @author xianyl
 *
 */
public class Constant {
	/**存放文件的目录**/
	public static final String FILE_DIR = "/file/"; 
	

	/**返回码，数据格式错误**/
	public static final int RETURN_CODE_DATA_ERROR = -3;
	/**返回码，指数据已经存在**/
	public static final int RETURN_CODE_DATA_EXIST = -2;
	/**平台返回码,失败 **/
	public static final int RETURN_CODE_FAIL = -1;
	/**平台返回码,成功 **/
	public static final int RETURN_CODE_SUCCESS = 0;
	

	
	
	/**返回码,验证信息 **/
	public static final int RETURN_CODE_VALIDATION = 10;
	

	/**json的返回字段，状态码**/
	public static final String JSON_RESPONSE_FIELD_CODE = "code";
	/**json的返回字段，消息描述**/
	public static final String JSON_RESPONSE_FIELD_MESSAGE = "message";
	/**json的返回字段，数据**/
	public static final String JSON_RESPONSE_FIELD_DATA = "data";
	/**json的返回字段，总页数**/
	public static final String JSON_RESPONSE_FIELD_TOTALPAGE = "totalpage";
	/**json的返回字段，总记录**/
	public static final String JSON_RESPONSE_FIELD_TOTALRECORD = "totalrecord";
	
	
	/**微信id**/
	public static final String FIELD_WX_OPENID = "openid";
	/**用户令牌的字段名**/
	public static final String FIELD_TOKEN = "token";
	/**用户的集合名**/
	public static final String FIELD_USER = "user";
	/**用户登录的动作名**/
	public static final String FIELD_USER_LOGIN = "login";
	/**用户退出登录的动作名**/
	public static final String FIELD_USER_LOGOUT = "logout";
	
	
	/**指定获取jsonp 格式得字段名**/
	public static final String FIELD_JSONP = "jsonp";
	public static final String FIELD_JSONP_CALLBACK = "callback";
	
	/**存放本机的tcp.name**/
	public static  String VALUE_LOCALHOST_TCP_NAME = "";
	
	//-----------实际数据返回码------------
	/**微信openid无绑定用户返回码,失败 **/
	public static final String RETURN_CODE_STRING_WEIXIN_BING_ERROR = "0010";
	/**返回码,失败 **/
	public static final String RETURN_CODE_STRING_FAIL = "0001";
	/**返回码,成功 **/
	public static final String RETURN_CODE_STRING_SUCCESS = "0000";
	
	
}
