package org.com.gannan.farminginfoplatform.utils;

import android.app.Activity;

import org.com.gannan.farminginfoplatform.R;


public class AnimUtil {
	
	/**
	 * 进入跳转效果
	 * @param activity
	 */
	public static void changePageIn(Activity activity){
		activity.overridePendingTransition(R.anim.in_right, R.anim.out_left);
	}
	
	/**
	 * 退出跳转效果
	 * @param activity
	 */
	public static void changePageOut(Activity activity){
		activity.finish();
		activity.overridePendingTransition(R.anim.in_left,R.anim.out_right);
	}
}


/*
 * 左进：overridePendingTransition(R.anim.in_left, R.anim.out_right);
 * 右进：overridePendingTransition(R.anim.in_right, R.anim.out_left);
 * */
