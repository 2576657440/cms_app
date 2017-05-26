package org.com.gannan.farminginfoplatform.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.com.gannan.farminginfoplatform.R;
import org.com.gannan.farminginfoplatform.request.ModifyPwRequest;
import org.com.gannan.farminginfoplatform.utils.SharedUtil;
import org.easycoding.mobile.android.annotation.InjectView;
import org.easycoding.mobile.android.annotation.Injector;

import java.util.HashMap;

//import android.os.PersistableBundle;

/**
 * Created by Administrator on 2016/11/15.
 */

public class PasswordActivity extends AppCompatActivity {

    @InjectView(id = R.id.editText1)
    private EditText editText1;
    @InjectView(id = R.id.editText2)
    private EditText editText2;
    @InjectView(id = R.id.editText3)
    private EditText editText3;
    @InjectView(id = R.id.textview_hint)
    private TextView hintTv;
    @InjectView(id = R.id.btn_confirm)
    private Button confirmBtn;
    @InjectView(id = R.id.imageView1)
    private ImageView backBtn;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        Injector.injectAll(this);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check()) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("userName", SharedUtil.getUserName(PasswordActivity.this));
                    map.put("oldPassword", editText1.getText().toString().trim());
                    map.put("newPassword", editText2.getText().toString().trim());

                    new ModifyPwRequest(PasswordActivity.this, map);
                }
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void setHintText(String str) {
        hintTv.setText(str);
    }
    private boolean check() {
        boolean flag = true;
        String oldPassword = editText1.getText().toString().trim();// 原密码
        String newPassword = editText2.getText().toString().trim();//新密码
        String comPassword = editText3.getText().toString().trim();//确认新密码
        if (TextUtils.isEmpty(oldPassword)) {
            flag = false;
            hintTv.setText(getResources().getString(R.string.hint11));
        } else if (TextUtils.isEmpty(newPassword)) {
            flag = false;
            hintTv.setText(getResources().getString(R.string.hint12));
        } else if (newPassword.length() < 6) {
            flag = false;
            hintTv.setText(getResources().getString(R.string.hint4));
        } else if (TextUtils.isEmpty(comPassword)) {
            flag = false;
            hintTv.setText(getResources().getString(R.string.hint3));
        } else if (!oldPassword.equals(SharedUtil.getPassword(PasswordActivity.this))) {
            flag = false;
            hintTv.setText(getResources().getString(R.string.hint13));
        } else if (!comPassword.equals(newPassword)) {
            flag = false;
            hintTv.setText(getResources().getString(R.string.hint14));
        }
        return flag;
    }
}
