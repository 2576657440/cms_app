package org.com.gannan.farminginfoplatform.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.TextView;

import org.com.gannan.farminginfoplatform.R;
import org.com.gannan.farminginfoplatform.comm.Global;
import org.com.gannan.farminginfoplatform.viewimage.BasePagerAdapter;
import org.com.gannan.farminginfoplatform.viewimage.GalleryViewPager;
import org.com.gannan.farminginfoplatform.viewimage.UrlPagerAdapter;
import org.easycoding.mobile.android.annotation.InjectView;
import org.easycoding.mobile.android.annotation.Injector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/27.
 */

public class ViewImageActivity extends AppCompatActivity {
    @InjectView(id = R.id.viewer)
    private GalleryViewPager mViewPager;
    @InjectView(id = R.id.textView1)
    private TextView pageSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_view_image);
        Injector.injectAll(this);

        String imagePathStr = getIntent().getStringExtra("imagePathStr");
        int position = getIntent().getIntExtra("position", 0);
        int pageAll = getIntent().getIntExtra("pageAll", 0);
        pageSize.setText(position + "/" + pageAll);

        List<String> urls = new ArrayList<String>();

       String[] imagePath= imagePathStr.split("\\|");
        for (int i = 1; i <imagePath.length ; i++) {
            urls.add(Global.URL+imagePath[i]);
        }
        final int length = urls.size();
        UrlPagerAdapter pagerAdapter = new UrlPagerAdapter(this, urls);
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setCurrentItem(position);
        pagerAdapter.setOnItemChangeListener(new BasePagerAdapter.OnItemChangeListener() {
            @Override
            public void onItemChange(int currentPosition) {
                pageSize.setText(currentPosition + 1 + "/" + length);
            }
        });
    }

}
