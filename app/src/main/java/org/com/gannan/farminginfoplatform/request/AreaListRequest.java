package org.com.gannan.farminginfoplatform.request;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.zhy.http.okhttp.OkHttpUtils;

import org.com.gannan.farminginfoplatform.comm.Global;
import org.com.gannan.farminginfoplatform.comm.StartProgressDialog;
import org.com.gannan.farminginfoplatform.entity.AreaEntity;
import org.com.gannan.farminginfoplatform.request.callback.AreaListCallback;

import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

import static android.R.attr.tag;

/**
 * Created by Administrator on 2016/11/25.
 */

public class AreaListRequest {

    private Activity activity;
    private StartProgressDialog progressDialog;
    public ChoiceItemsListener listener;

    public AreaListRequest(Activity activity) {
        this.activity = activity;
        http();
    }

    private void http() {
        String url = Global.SERVER_URL + "ph/getOffice";
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new AreaListCallback() {
                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        progressDialog = new StartProgressDialog(activity);
                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        progressDialog.stopProgressDialog();
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.i(Global.TAG, tag + ":" + e.toString());
                    }

                    @Override
                    public void onResponse(List<AreaEntity> list, int id) {
                        if (list != null && list.size() > 0) {

                            openSelectDialog(list);
                        }
                    }
                });
    }

    //弹出选择对话框
    private void openSelectDialog(final List<AreaEntity> list) {

        //自定义Context,添加主题
        Context dialogContext = new ContextThemeWrapper(activity, android.R.style.Theme_Holo_Light_Dialog_MinWidth);
        String[] choiceItems = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            choiceItems[i] = list.get(i).getName();
        }
        ListAdapter adapter = new ArrayAdapter<String>(dialogContext, android.R.layout.simple_list_item_1, choiceItems);

        AlertDialog.Builder builder = new AlertDialog.Builder(dialogContext);
        builder.setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                String name = list.get(position).getName();
                String id = list.get(position).getId();
                if(listener!=null){
                    listener.passValue(id,name);
                }
                dialog.dismiss();
            }
        });
        builder.create().show();

    }

    public interface ChoiceItemsListener {
        void passValue(String areaId, String areaName);
    }

    public ChoiceItemsListener getListener() {
        return listener;
    }

    public void setListener(ChoiceItemsListener listener) {
        this.listener = listener;
    }

}
