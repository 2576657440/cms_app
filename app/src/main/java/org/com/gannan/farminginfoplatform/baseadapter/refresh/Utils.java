package org.com.gannan.farminginfoplatform.baseadapter.refresh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import org.com.gannan.farminginfoplatform.R;


public class Utils {

    public static View createSimpleLoadingTip(Context context) {
        final View view = LayoutInflater.from(context).inflate(R.layout.cube_ptr_simple_loading, null);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(-2, -2);
        lp.setMargins(0, 0, 0, PtrLocalDisplay.dp2px(4));
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lp.addRule(RelativeLayout.CENTER_VERTICAL);
        view.setLayoutParams(lp);
        view.setVisibility(View.INVISIBLE);
        return view;
    }
}
