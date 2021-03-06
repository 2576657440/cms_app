package org.com.gannan.farminginfoplatform.request;

import android.app.Activity;
import android.util.Log;

import com.zhy.http.okhttp.OkHttpUtils;

import org.com.gannan.farminginfoplatform.activity.ModifyDetailActivity;
import org.com.gannan.farminginfoplatform.comm.Global;
import org.com.gannan.farminginfoplatform.comm.StartProgressDialog;
import org.com.gannan.farminginfoplatform.entity.ClassifyEntity;
import org.com.gannan.farminginfoplatform.request.callback.ClassifyListCallback;

import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

import static android.R.attr.tag;

/**
 * Created by Administrator on 2016/11/25.
 */

public class MClassifyListRequest {

    private Activity activity;
    private String type;
    private StartProgressDialog progressDialog;

    public MClassifyListRequest(Activity activity, String type) {
        this.activity = activity;
        this.type = type;
        http();
    }

    private void http() {
        String url = Global.SERVER_URL + "ph/getCategory?type="+type;
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new ClassifyListCallback() {
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
                    public void onResponse(List<ClassifyEntity> list, int id) {
                        if(list!=null&&list.size()>0){
                            if(activity instanceof ModifyDetailActivity){
                                ((ModifyDetailActivity) activity).setTypeList(list);
                            }
                        }
                    }
                });
    }

}
