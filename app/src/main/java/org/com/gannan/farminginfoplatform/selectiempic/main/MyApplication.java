package org.com.gannan.farminginfoplatform.selectiempic.main;


import android.app.Application;
import android.util.DisplayMetrics;

public class MyApplication extends Application {

	private static DisplayMetrics dm = new DisplayMetrics();   
	
	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	public static DisplayMetrics getDisplayMetrics(){
		return dm;
	}
}
