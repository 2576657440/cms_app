package org.com.gannan.farminginfoplatform.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.com.gannan.farminginfoplatform.R;
import org.com.gannan.farminginfoplatform.request.NewsDetailRequest;
import org.easycoding.mobile.android.annotation.InjectView;
import org.easycoding.mobile.android.annotation.Injector;

/**
 * Created by Administrator on 2016/11/24.
 */

public class NewsDetailActivity extends AppCompatActivity {

    @InjectView(id = R.id.title_tv)
    private TextView titleTv;
    @InjectView(id = R.id.myInfoLayout)
    private LinearLayout myInfoLayout;
    @InjectView(id = R.id.imageView1)
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        Injector.injectAll(this);
        titleTv.setText("内容详情");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        int type = getIntent().getIntExtra("categoryType", 2);
        if (type == 2)  myInfoLayout.setVisibility(View.GONE);
        //获取详细信息，如果为1就是待审核信息或历史记录传，为“”查看已发布详情
        new NewsDetailRequest(this,"");
    }

}
