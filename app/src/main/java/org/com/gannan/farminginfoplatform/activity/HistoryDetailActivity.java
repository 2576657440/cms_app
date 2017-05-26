package org.com.gannan.farminginfoplatform.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import org.com.gannan.farminginfoplatform.request.DelRecordRequest;
import org.com.gannan.farminginfoplatform.request.NewsDetailRequest;
import org.com.gannan.farminginfoplatform.utils.SharedUtil;
import org.easycoding.mobile.android.annotation.InjectView;
import org.easycoding.mobile.android.annotation.Injector;

import java.util.HashMap;

import static org.com.gannan.farminginfoplatform.R.id.goBack;

/**
 * Created by Administrator on 2016/12/01.
 */

public class HistoryDetailActivity extends AppCompatActivity {

    @InjectView(id = R.id.title_tv)
    private TextView titleTv;
    @InjectView(id = R.id.myInfoLayout)
    private LinearLayout myInfoLayout;
    @InjectView(id = R.id.imageView1)
    private ImageView back;
    @InjectView(id = R.id.imageView3)
    private TextView more;
    private AlertDialog dialog;
    public String state="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audit_detail);
        Injector.injectAll(this);
        titleTv.setText("内容详情");
        final int type = getIntent().getIntExtra("categoryType", 2);
        if (type == 2) {
            myInfoLayout.setVisibility(View.GONE);
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == 2) {//新闻信息
                    showNoticeDialog();
                }else{//供求信息
                    if("4".equals(state)||"6".equals(state)){
                        openSelectDialog();
                    }else {
                        showNoticeDialog();
                    }
                }
            }
        });

        //获取详细信息,如果为1就是待审核信息或历史记录传，为“”查看已发布详情
        new NewsDetailRequest(this,"1");

    }
    //弹出选择对话框
    private void openSelectDialog() {

        //自定义Context,添加主题
        Context dialogContext = new ContextThemeWrapper(this, android.R.style.Theme_Holo_Light_Dialog_MinWidth);
        String[] choiceItems = new String[4];
        choiceItems[0] = "交易成功";
        choiceItems[1] = "再次发布";
        choiceItems[2] = "删除";
        choiceItems[3] = "取消";
        ListAdapter adapter = new ArrayAdapter<>(dialogContext, android.R.layout.simple_list_item_1, choiceItems);

        AlertDialog.Builder builder = new AlertDialog.Builder(dialogContext);
        builder.setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                HashMap<String,String> map;
                switch (which) {
                    case 0:
                        map=new HashMap<>();
                        map.put("articleId",getIntent().getStringExtra("id"));
                        map.put("token", SharedUtil.getToken(HistoryDetailActivity.this));
                        map.put("state","0");
                        new AudioRequest(HistoryDetailActivity.this,map,0);
                        break;
                    case 1:
                        map=new HashMap<>();
                        map.put("articleId",getIntent().getStringExtra("id"));
                        map.put("token", SharedUtil.getToken(HistoryDetailActivity.this));
                        map.put("state","4");
                        new AudioRequest(HistoryDetailActivity.this,map,4);
                        break;
                    case 2:
                        map=new HashMap<>();
                        map.put("id",getIntent().getStringExtra("id"));
                        map.put("token", SharedUtil.getToken(HistoryDetailActivity.this));
                        new DelRecordRequest(HistoryDetailActivity.this,map);
                        break;

                }
                dialog.dismiss();
            }
        });
        builder.create().show();

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

        dialog.setContentView(R.layout.dialog_audit);
        TextView titleTv = (TextView) dialog.findViewById(R.id.dialog_title);
        final EditText reasonTxt = (EditText) dialog.findViewById(R.id.message);
        TextView okBtn = (TextView) dialog.findViewById(R.id.ok);
        TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
        TextView del = (TextView) dialog.findViewById(goBack);
        titleTv.setText("记录操作");
        del.setText("删除");
        reasonTxt.setEnabled(false);
        reasonTxt.setHint("");

        reasonTxt.setText(getIntent().getStringExtra("remarks"));
        okBtn.setVisibility(View.GONE);

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,String> map=new HashMap<String, String>();
                map.put("id",getIntent().getStringExtra("id"));
                map.put("token", SharedUtil.getToken(HistoryDetailActivity.this));
                new DelRecordRequest(HistoryDetailActivity.this,map);
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
