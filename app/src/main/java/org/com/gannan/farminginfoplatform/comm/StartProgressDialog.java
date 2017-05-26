package org.com.gannan.farminginfoplatform.comm;

import android.content.Context;

public class StartProgressDialog {
	
	public  CustomProgressDialog progressDialog;
	
	public StartProgressDialog(Context context){
		progressDialog = new CustomProgressDialog(context,"加载中...");  
		 progressDialog.setCancelable(true);// 按返回键是否退出
		 progressDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog是否消失
		 progressDialog.show();
	}
	
	public void stopProgressDialog(){
		progressDialog.dismiss();
	}
	public boolean isShowing(){
		return progressDialog.isShowing();
	}
}
