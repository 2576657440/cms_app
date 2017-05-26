package org.com.gannan.farminginfoplatform.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;

import org.com.gannan.farminginfoplatform.MainActivity;
import org.com.gannan.farminginfoplatform.R;
import org.com.gannan.farminginfoplatform.utils.AnimUtil;

public class SplashActivity extends Activity{
	@SuppressLint("HandlerLeak")
	private Handler mMainHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Intent intent = new Intent(SplashActivity.this,MainActivity.class);
			startActivity(intent);
			AnimUtil.changePageIn(SplashActivity.this);
			finish();
		}
	};
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏显示
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		getWindow().setBackgroundDrawableResource(R.mipmap.sp);
		mMainHandler.sendEmptyMessageDelayed(0, 3 * 1000);//延迟3秒
		
	}
}
