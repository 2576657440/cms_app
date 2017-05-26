package org.com.gannan.farminginfoplatform.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.com.gannan.farminginfoplatform.R;

@SuppressLint("InflateParams")
public class PopupShow {

	private PopupWindow popup;
	private boolean isShow=true;
	private Activity activity;

	public PopupShow(Activity activity) {
		this.activity=activity;
		View layoutView=LayoutInflater.from(activity).inflate(R.layout.pop_pic_show, null);

		popup=new PopupWindow(layoutView,LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT,false);
		ColorDrawable drawable=new ColorDrawable(0xe0000000);
		popup.setBackgroundDrawable(drawable);
		popup.setAnimationStyle(R.style.popupWindowShow);

		popup.showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);

		TextView text=(TextView) layoutView.findViewById(R.id.textView1);
		text.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isShow){
					popup.dismiss();
					isShow=false;
				}else{
					popup.showAtLocation(PopupShow.this.activity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
					isShow=true;
				}
			}
		});
	}

	
	public void dismiss(){
		if(popup!=null&&popup.isShowing()){
			popup.dismiss();
		}
	}
	
}
