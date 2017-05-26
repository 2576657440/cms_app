package org.com.gannan.farminginfoplatform.request;

import android.app.Activity;
import android.util.Log;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.com.gannan.farminginfoplatform.R;
import org.com.gannan.farminginfoplatform.activity.WaitPublishListActivity;
import org.com.gannan.farminginfoplatform.comm.Global;
import org.com.gannan.farminginfoplatform.comm.StartProgressDialog;
import org.com.gannan.farminginfoplatform.comm.UtilsDialog;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Request;

import static android.R.attr.tag;

/**
 * Created by Administrator on 2016/12/01.
 */

public class DealPublishRequest {

    private Activity activity;
    private StartProgressDialog progressDialog;
    private String ids;

    public DealPublishRequest(Activity activity, HashMap<String, String> map, String ids) {
        this.activity = activity;
        this.ids = ids;
        http(map);
    }

    private void http(final HashMap<String, String> map) {
        String url = Global.SERVER_URL + "ph/dealPublish";
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
                                ((WaitPublishListActivity) activity).dealResult(ids);
                            } else if (obj.has("result") && "2".equals(obj.getString("result"))) {
                                UtilsDialog utilsDialog = UtilsDialog.getInstance();
                                utilsDialog.showExitNoticeDialog(activity, activity.getString
                                        (R.string.str_msg222), obj.getString("msg"));

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
