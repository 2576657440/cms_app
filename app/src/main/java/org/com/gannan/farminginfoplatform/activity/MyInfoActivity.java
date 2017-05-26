package org.com.gannan.farminginfoplatform.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.com.gannan.farminginfoplatform.R;
import org.com.gannan.farminginfoplatform.request.AreaListRequest;
import org.com.gannan.farminginfoplatform.request.ModifyMyInfoRequest;
import org.com.gannan.farminginfoplatform.utils.SharedUtil;
import org.easycoding.mobile.android.annotation.InjectView;
import org.easycoding.mobile.android.annotation.Injector;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/1/17.
 */

public class MyInfoActivity extends AppCompatActivity implements View.OnClickListener,
        AreaListRequest.ChoiceItemsListener {

    @InjectView(id = R.id.textView1)
    private TextView areaTv;
    @InjectView(id = R.id.editText1)
    private EditText reallyName;
    @InjectView(id = R.id.editText2)
    private EditText phoneNo;
    @InjectView(id = R.id.linearlayout1)
    private LinearLayout areaLayout;
    @InjectView(id = R.id.textview_hint)
    private TextView hintTv;
    @InjectView(id = R.id.btn_confirm)
    private Button confirmBtn;
    @InjectView(id = R.id.imageView1)
    private ImageView backBtn;
    private AreaListRequest areaRequest;
    private String areaId;
    public String areaName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        Injector.injectAll(this);
        confirmBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        areaLayout.setOnClickListener(this);

        areaTv.setText(SharedUtil.getAreaName(this));
        areaName=SharedUtil.getAreaName(this);
        areaId=SharedUtil.getAreaId(this);
        reallyName.setText(SharedUtil.getName(this));
        phoneNo.setText(SharedUtil.getPhoneNo(this));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView1:
                finish();
                break;
            case R.id.linearlayout1:
                //获取区域列表
                areaRequest = new AreaListRequest(this);
                areaRequest.setListener(this);
                break;
            case R.id.btn_confirm:
                if (check()) {
                    hintTv.setText("");
                    HashMap<String, String> map = new HashMap<>();
                    map.put("oId", areaId);
                    map.put("name", reallyName.getText().toString());
                    map.put("mobile", phoneNo.getText().toString());
                    map.put("token", SharedUtil.getToken(this));
                    //修改个人信息
                    new ModifyMyInfoRequest(this, map);
                }
                break;
        }
    }

    private boolean check() {
        boolean flag = true;
        if ("".equals(areaTv.getText().toString())) {
            flag = false;
            hintTv.setText(getResources().getString(R.string.hint6));
        } else if ("".equals(reallyName.getText().toString())) {
            flag = false;
            hintTv.setText(getResources().getString(R.string.hint7));
        } else if ("".equals(phoneNo.getText().toString())) {
            flag = false;
            hintTv.setText(getResources().getString(R.string.hint8));
        } else if (phoneNo.getText().toString().length() != 11) {
            flag = false;
            hintTv.setText(getResources().getString(R.string.hint9));
        }
        return flag;
    }

    public void setHintText(String str) {
        hintTv.setText(str);
    }

    @Override
    public void passValue(String areaId, String areaName) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
                areaTv.setText(areaName);
//            }
//        });

        this.areaId = areaId;
        this.areaName = areaName;
    }
}
