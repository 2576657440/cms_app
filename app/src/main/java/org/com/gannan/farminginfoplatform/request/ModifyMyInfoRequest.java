package org.com.gannan.farminginfoplatform.request;

import android.app.Activity;
import android.util.Log;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.com.gannan.farminginfoplatform.activity.MyInfoActivity;
import org.com.gannan.farminginfoplatform.comm.Global;
import org.com.gannan.farminginfoplatform.comm.StartProgressDialog;
import org.com.gannan.farminginfoplatform.utils.SharedUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Request;

import static android.R.attr.tag;

/**
 * Created by Administrator on 2016/11/26.
 */

public class ModifyMyInfoRequest {

    private Activity activity;
    private StartProgressDialog progressDialog;

    public ModifyMyInfoRequest(Activity activity, HashMap<String, String> map) {
        this.activity = activity;
        http(map);
    }

    private void http(final HashMap<String, String> map) {
        String url = Global.SERVER_URL + "ph/updateMemeberInfo";
        OkHttpUtils
                .post()
                .url(url)
                .params(map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.has("result") && "0".equals(obj.getString("result"))) {

                                SharedUtil.saveAreaName(activity,((MyInfoActivity)activity).areaName);
                                SharedUtil.saveAreaId(activity,map.get("oId"));
                                SharedUtil.saveName(activity,map.get("name"));
                                SharedUtil.savePhoneNo(activity,map.get("mobile"));

                                activity.finish();
                            } else if (obj.has("result") && "1".equals(obj.getString("result"))) {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

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

                });
    }

}
