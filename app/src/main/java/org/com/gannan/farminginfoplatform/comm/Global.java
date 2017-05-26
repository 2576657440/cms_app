package org.com.gannan.farminginfoplatform.comm;

public class Global {
	public static final String TAG = "甘南头条1";
	public static final String WEBSERVICE_TIMEOUTERROR = "连接超时，请检查网络状态";
	public static final String LOGIN_ERROR = "登录失败，请稍后再试";
	public static final String WEB_DISCONNECT = "网络连接失败";
	public static final String WEB_CONNECT_STATE = "请检查网络连接状态";
	//221.8.52.6 6090      d infoadmin dept
	//新地址 47.93.34.112:8080
	public static final String IP = "192.168.0.112";
	public static final String PORT = "8080";
	public static final String URL = "http://"+IP+":"+PORT;

	public static final String SERVER_URL = URL+"/CMS/";//json串获取
//	public static final String UPLOADPIC_URL =URL+"/medconint/servlet/UploadServlet?";//图片上传
//	public static final String UPLOADHEADPIC_URL =URL+"/medconint/servlet/UserServlet?";//头像上传
	public static final String MEDIA_URL = URL;//图片下载
	public static final String MEDIA_RELATIVE_URL = "/AICMS/userfiles/images/";//图片下载相对路径

	//SharedPreference fileName
	public static final String SHARED_USER = "user_info";
	//SharedPreference key
	public static final String SHARED_KEY_USERNAME = "userName";
	public static final String SHARED_KEY_PASSWORD = "password";
	public static final String SHARED_KEY_TOKEN = "token";
	public static final String SHARED_KEY_LOGINFLAG = "loginFlag";
	public static final String SHARED_KEY_REALNAME = "realName";
	public static final String SHARED_KEY_SAVEPWDFLAG = "savePwFlag";
	public static final String SHARED_KEY_AREANAME = "areaName";
	public static final String SHARED_KEY_AREAID = "areaId";
	public static final String SHARED_KEY_PHONENO = "phoneNo";
	public static final String SHARED_KEY_ROLEFLAG = "roleFlag";

	public static String APK_NAME = "gntt.apk";


}
