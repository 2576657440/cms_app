package org.com.gannan.farminginfoplatform.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import org.com.gannan.farminginfoplatform.comm.Global;

public class SharedUtil {

	public static SharedPreferences share;

	public static void getSharedPreference(Context context, String fileName) {
		share = context.getSharedPreferences(fileName, Activity.MODE_PRIVATE);
	}

	// 获取用户名
	public static String getUserName(Context context) {
		getSharedPreference(context, Global.SHARED_USER);
		return share.getString(Global.SHARED_KEY_USERNAME, "");
	}

	// 保存用户名
	public static void saveUserName(Context context, String value) {
		getSharedPreference(context, Global.SHARED_USER);
		Editor edit = share.edit();
		edit.putString(Global.SHARED_KEY_USERNAME, value);
		edit.commit();
	}

	// 获取密码
	public static String getPassword(Context context) {
		getSharedPreference(context, Global.SHARED_USER);
		return share.getString(Global.SHARED_KEY_PASSWORD, "");
	}
	
	// 保存密码
	public static void savePassword(Context context, String value) {
		getSharedPreference(context, Global.SHARED_USER);
		Editor edit = share.edit();
		edit.putString(Global.SHARED_KEY_PASSWORD, value);
		edit.commit();
	}

	// 获取登录标识
	public static Boolean getLoginFlag(Context context) {
		getSharedPreference(context, Global.SHARED_USER);
		return share.getBoolean(Global.SHARED_KEY_LOGINFLAG, false);
	}

	// 保存登录标识
	public static void saveLoginFlag(Context context, Boolean value) {
		getSharedPreference(context, Global.SHARED_USER);
		Editor edit = share.edit();
		edit.putBoolean(Global.SHARED_KEY_LOGINFLAG, value);
		edit.commit();
	}

	// 获取token
	public static String getToken(Context context) {
		getSharedPreference(context, Global.SHARED_USER);
		return share.getString(Global.SHARED_KEY_TOKEN, "");
	}

	// 保存token
	public static void saveToken(Context context, String value) {
		getSharedPreference(context, Global.SHARED_USER);
		Editor edit = share.edit();
		edit.putString(Global.SHARED_KEY_TOKEN, value);
		edit.commit();
	}
	// 获取登录人姓名
	public static String getName(Context context) {
		getSharedPreference(context, Global.SHARED_USER);
		return share.getString(Global.SHARED_KEY_REALNAME, "");
	}

	// 保存登录人姓名
	public static void saveName(Context context, String value) {
		getSharedPreference(context, Global.SHARED_USER);
		Editor edit = share.edit();
		edit.putString(Global.SHARED_KEY_REALNAME, value);
		edit.commit();
	}
	// 获取登录人所属区域name
	public static String getAreaName(Context context) {
		getSharedPreference(context, Global.SHARED_USER);
		return share.getString(Global.SHARED_KEY_AREANAME, "");
	}

	// 保存登录人所属区域name
	public static void saveAreaName(Context context, String value) {
		getSharedPreference(context, Global.SHARED_USER);
		Editor edit = share.edit();
		edit.putString(Global.SHARED_KEY_AREANAME, value);
		edit.commit();
	}
	// 获取登录人所属区域id
	public static String getAreaId(Context context) {
		getSharedPreference(context, Global.SHARED_USER);
		return share.getString(Global.SHARED_KEY_AREAID, "");
	}

	// 保存登录人所属区域id
	public static void saveAreaId(Context context, String value) {
		getSharedPreference(context, Global.SHARED_USER);
		Editor edit = share.edit();
		edit.putString(Global.SHARED_KEY_AREAID, value);
		edit.commit();
	}
	// 获取登录人电话号
	public static String getPhoneNo(Context context) {
		getSharedPreference(context, Global.SHARED_USER);
		return share.getString(Global.SHARED_KEY_PHONENO, "");
	}

	// 保存登录人电话号
	public static void savePhoneNo(Context context, String value) {
		getSharedPreference(context, Global.SHARED_USER);
		Editor edit = share.edit();
		edit.putString(Global.SHARED_KEY_PHONENO, value);
		edit.commit();
	}
	// 获取登录人角色
	public static String getRoleFlag(Context context) {
		getSharedPreference(context, Global.SHARED_USER);
		return share.getString(Global.SHARED_KEY_ROLEFLAG, "");
	}

	// 保存登录人角色
	public static void saveRoleFlag(Context context, String value) {
		getSharedPreference(context, Global.SHARED_USER);
		Editor edit = share.edit();
		edit.putString(Global.SHARED_KEY_ROLEFLAG, value);
		edit.commit();
	}
	// 获取保存密码标识
	public static String getSavePwdFlag(Context context) {
		getSharedPreference(context, Global.SHARED_USER);
		return share.getString(Global.SHARED_KEY_SAVEPWDFLAG, "");
	}

	// 保存保存密码标识
	public static void saveSavePwdFlag(Context context, String value) {
		getSharedPreference(context, Global.SHARED_USER);
		Editor edit = share.edit();
		edit.putString(Global.SHARED_KEY_SAVEPWDFLAG, value);
		edit.commit();
	}

}
