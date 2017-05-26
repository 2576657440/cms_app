package org.com.gannan.farminginfoplatform.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.com.gannan.farminginfoplatform.R;
import org.com.gannan.farminginfoplatform.request.AreaListRequest;
import org.com.gannan.farminginfoplatform.request.RegistRequest;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/11/15.
 */

public class RegistFragment extends Fragment implements View.OnClickListener, AreaListRequest.ChoiceItemsListener {

    private View view;
    private LinearLayout areaLayout;
    private EditText userName;
    private EditText password;
    private EditText confirmPw;
    private EditText reallyName;
    private EditText phoneNo;
    private TextView areaTv;
    private String areaId;
    public String areaName;
    private TextView hintTv;
    private Button btnRegister;
    private AreaListRequest request;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_regist, container, false);
        areaLayout = (LinearLayout) view.findViewById(R.id.linearlayout1);
        userName = (EditText) view.findViewById(R.id.editText1);
        password = (EditText) view.findViewById(R.id.editText2);
        confirmPw = (EditText) view.findViewById(R.id.editText3);
        reallyName = (EditText) view.findViewById(R.id.editText4);
        phoneNo = (EditText) view.findViewById(R.id.editText5);
        areaTv = (TextView) view.findViewById(R.id.textView1);
        hintTv = (TextView) view.findViewById(R.id.textView2);
        btnRegister = (Button) view.findViewById(R.id.buttonView1);

        areaLayout.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linearlayout1:
                //获取区域列表
                request = new AreaListRequest(getActivity());
                request.setListener(this);
                break;
            case R.id.buttonView1:
                //提交数据
                if (check()) {
                    hintTv.setText("");
                    HashMap<String, String> map = new HashMap<>();
                    map.put("loginName", userName.getText().toString());
                    map.put("password", password.getText().toString());
                    map.put("oId", areaId);
                    map.put("name", reallyName.getText().toString());
                    map.put("mobile", phoneNo.getText().toString());

                    new RegistRequest(this, map);
                }
                break;
        }
    }

    public void setHintText(String str) {
        hintTv.setText(str);
    }

    private boolean check() {
        boolean flag = true;
        if ("".equals(userName.getText().toString())) {
            flag = false;
            hintTv.setText(getResources().getString(R.string.hint1));
        } else if ("".equals(password.getText().toString())) {
            flag = false;
            hintTv.setText(getResources().getString(R.string.hint2));
        } else if (password.getText().toString().length() < 6) {
            flag = false;
            hintTv.setText(getResources().getString(R.string.hint4));
        } else if ("".equals(confirmPw.getText().toString())) {
            flag = false;
            hintTv.setText(getResources().getString(R.string.hint3));
        } else if (!confirmPw.getText().toString().equals(password.getText().toString())) {
            flag = false;
            hintTv.setText(getResources().getString(R.string.hint5));
        } else if ("".equals(areaTv.getText().toString())) {
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

    @Override
    public void passValue(String areaId, String areaName) {
        areaTv.setText(areaName);
        this.areaId = areaId;
        this.areaName = areaName;
    }
}
