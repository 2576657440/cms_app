package org.com.gannan.farminginfoplatform.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.com.gannan.farminginfoplatform.R;
import org.com.gannan.farminginfoplatform.request.AudioRequest;
import org.com.gannan.farminginfoplatform.request.NewsDetailRequest;
import org.com.gannan.farminginfoplatform.utils.AnimUtil;
import org.com.gannan.farminginfoplatform.utils.SharedUtil;
import org.easycoding.mobile.android.annotation.InjectView;
import org.easycoding.mobile.android.annotation.Injector;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/11/24.
 */

public class AuditDetailActivity extends AppCompatActivity {

    @InjectView(id = R.id.title_tv)
    private TextView titleTv;
    @InjectView(id = R.id.myInfoLayout)
    private LinearLayout myInfoLayout;
    @InjectView(id = R.id.imageView1)
    private ImageView back;
    @InjectView(id = R.id.imageView3)
    private TextView more;
    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audit_detail);
        Injector.injectAll(this);
        titleTv.setText(R.string.str_msg130);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSelectDialog();
            }
        });
        int type = getIntent().getIntExtra("categoryType", 2);
        if (type == 2) {
            myInfoLayout.setVisibility(View.GONE);
        }
        //获取详细信息,如果为1就是待审核信息或历史记录传，为“”查看已发布详情
        new NewsDetailRequest(this,"1");

    }

    //弹出选择对话框
    private void openSelectDialog() {

        //自定义Context,添加主题
        Context dialogContext = new ContextThemeWrapper(this, android.R.style.Theme_Holo_Light_Dialog_MinWidth);
        String[] choiceItems = new String[4];
        choiceItems[0] = "通过";
        choiceItems[1] = "驳回";
        choiceItems[2] = "修改";
        choiceItems[3] = "取消";
        ListAdapter adapter = new ArrayAdapter<>(dialogContext, android.R.layout.simple_list_item_1, choiceItems);

        AlertDialog.Builder builder = new AlertDialog.Builder(dialogContext);
        builder.setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        HashMap<String,String> map=new HashMap<String, String>();
                        map.put("articleId",getIntent().getStringExtra("id"));
                        map.put("token", SharedUtil.getToken(AuditDetailActivity.this));
                        if("infoadmin".equals(SharedUtil.getRoleFlag(AuditDetailActivity.this))){
                            map.put("state","2");
                        }else if("dept".equals(SharedUtil.getRoleFlag(AuditDetailActivity.this))){
                            map.put("state","3");
                        }
                        new AudioRequest(AuditDetailActivity.this,map,Integer.parseInt(map.get("state")));
                        break;
                    case 1:
                        showNoticeDialog();
                        break;
                    case 2:
                        Intent intent = new Intent(AuditDetailActivity.this, ModifyDetailActivity.class);
                        intent.putExtra("id",getIntent().getStringExtra("id"));
                        intent.putExtra("requestCode",105);
                        startActivityForResult(intent,105);
                        AnimUtil.changePageIn(AuditDetailActivity.this);
                        break;
                }
                dialog.dismiss();
            }
        });
        builder.create().show();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==105){
             new NewsDetailRequest(this,"1");//信息内容被修改后刷新当前页

            Intent intent=new Intent();
            intent.putExtra("refresh",201);//刷新列表
            setResult(getIntent().getIntExtra("requestCode",0),intent);
        }
    }
    /*
    * 提示对话框
    */
    public void showNoticeDialog() {
        dialog = new AlertDialog.Builder(this).create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();

        int screenWidth=getResources().getDisplayMetrics().widthPixels;
        WindowManager.LayoutParams params=dialog.getWindow().getAttributes();
        params.width= (int) (screenWidth*0.8f);
        Window window=dialog.getWindow();
        window.setAttributes(params);
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.setContentView(R.layout.dialog_audit);
        TextView titleTv = (TextView) dialog.findViewById(R.id.dialog_title);
        final EditText reasonTxt = (EditText) dialog.findViewById(R.id.message);
        TextView okBtn = (TextView) dialog.findViewById(R.id.ok);
        TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
        TextView goBack = (TextView) dialog.findViewById(R.id.goBack);
        goBack.setVisibility(View.GONE);
        okBtn.setText("确定");
        titleTv.setText("确定要驳回当前信息么？");
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,String> map=new HashMap<String, String>();
                map.put("articleId",getIntent().getStringExtra("id"));
                map.put("token", SharedUtil.getToken(AuditDetailActivity.this));
                map.put("remarks", reasonTxt.getText().toString());
                map.put("state","5");
                new AudioRequest(AuditDetailActivity.this,map,5);
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }
}
