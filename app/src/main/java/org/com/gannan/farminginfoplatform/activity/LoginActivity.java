package org.com.gannan.farminginfoplatform.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.com.gannan.farminginfoplatform.R;
import org.com.gannan.farminginfoplatform.fragment.LoginFragment;
import org.com.gannan.farminginfoplatform.fragment.RegistFragment;
import org.easycoding.mobile.android.annotation.InjectView;
import org.easycoding.mobile.android.annotation.Injector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/9.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @InjectView(id = R.id.viewPager)
    private ViewPager viewPager;

    @InjectView(id = R.id.tab_regist_top)
    private TextView tab_regist_top;

    @InjectView(id = R.id.close)
    private TextView tab_close;

    @InjectView(id = R.id.tab_login_top)
    private TextView tab_login_top;


    private List<Fragment> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Injector.injectAll(this);
        init();
        addListener();
    }

    private void init() {
        LoginFragment loginFragment = new LoginFragment();
        RegistFragment registFragment = new RegistFragment();
        list = new ArrayList<>();
        list.add(loginFragment);
        list.add(registFragment);

        PagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return list.get(position);
            }

            @Override
            public int getCount() {
                return list.size();
            }
        };
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                onSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void onSelected(int index) {
        resetInit();
        switch (index) {
            case 0:
                tab_login_top.setText(R.string.str_msg122);
                tab_login_top.setTextColor(getResources().getColor(R.color.diyGreenColor));
                break;
            case 1:
                tab_regist_top.setText(R.string.str_msg123);
                tab_regist_top.setTextColor(getResources().getColor(R.color.diyGreenColor));
                break;
        }
    }

    private void resetInit() {
        tab_login_top.setText(R.string.str_msg122);
        tab_login_top.setTextColor(getResources().getColor(R.color.diy_content));
        tab_regist_top.setText(R.string.str_msg123);
        tab_regist_top.setTextColor(getResources().getColor(R.color.diy_content));
    }


    private void addListener() {
        tab_close.setOnClickListener(this);
        tab_login_top.setOnClickListener(this);
        tab_regist_top.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close:
                finish();
                break;
            case R.id.tab_login_top:
                viewPager.setCurrentItem(0, true);
                break;
            case R.id.tab_regist_top:
                viewPager.setCurrentItem(1, true);
                break;
        }
    }
}