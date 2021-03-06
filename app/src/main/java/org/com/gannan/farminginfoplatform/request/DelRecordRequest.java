package org.com.gannan.farminginfoplatform.request;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.com.gannan.farminginfoplatform.comm.Global;
import org.com.gannan.farminginfoplatform.comm.StartProgressDialog;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Request;

import static android.R.attr.tag;

/**
 * Created by Administrator on 2016/11/25.
 */

public class DelRecordRequest {

    private Activity activity;
    private HashMap<String, String> map;
    private StartProgressDialog progressDialog;

    public DelRecordRequest(Activity activity, HashMap<String, String> map) {
        this.activity = activity;
        this.map = map;
        http();
    }

    private void http() {
        String url = Global.SERVER_URL + "ph/delNew";
        OkHttpUtils
                .post()
                .params(map)
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.has("result") && "0".equals(obj.getString("result"))) {

                                int position = activity.getIntent().getIntExtra("position", 0);
                                int requestCode = activity.getIntent().getIntExtra("requestCode", 0);
                                Intent intent = new Intent();
                                intent.putExtra("position", position);
                                activity.setResult(requestCode, intent);
                                activity.finish();

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
