package org.com.gannan.farminginfoplatform.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.com.gannan.farminginfoplatform.R;
import org.com.gannan.farminginfoplatform.request.LoginRequest;
import org.com.gannan.farminginfoplatform.utils.SharedUtil;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/11/15.
 */

public class LoginFragment extends Fragment {

    private View view;
    private EditText userName;
    private EditText password;
    private Button loginBtn;
    private TextView hintText;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_login,container,false);
        userName = (EditText)view.findViewById(R.id.userName);
        password = (EditText)view.findViewById(R.id.password);
        hintText = (TextView)view.findViewById(R.id.hintText);
        loginBtn = (Button)view.findViewById(R.id.login);
        String name=SharedUtil.getUserName(getContext());
        if(!"".equals(SharedUtil.getUserName(getContext()))){
            userName.setText(name);
        }
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = userName.getText().toString();
                String pw = password.getText().toString();
                if(TextUtils.isEmpty(username)||TextUtils.isEmpty(pw)){
                    hintText.setText(getResources().getString(R.string.hint10));
                }else{
                    hintText.setText("");
                    HashMap<String,String> map=new HashMap<>();
                    map.put("userName",username);
                    map.put("passWord",pw);
                    //登录
                    new LoginRequest(LoginFragment.this,map);
                }
            }
        });

        return view;
    }
    public void setHintText(String str) {
        hintText.setText(str);
    }

}
