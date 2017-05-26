package org.com.gannan.farminginfoplatform.request;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.com.gannan.farminginfoplatform.R;
import org.com.gannan.farminginfoplatform.comm.Global;
import org.com.gannan.farminginfoplatform.comm.StartProgressDialog;
import org.com.gannan.farminginfoplatform.utils.DateUtils;
import org.easycoding.mobile.android.annotation.InjectView;
import org.easycoding.mobile.android.annotation.Injector;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Request;

import static android.R.attr.tag;

/**
 * Created by Administrator on 2016/03/10.
 */

public class AudioNewsDetailRequest {

    private Activity activity;
    private StartProgressDialog progressDialog;
    @InjectView(id = R.id.linearlayout1)
    private LinearLayout picLayout;
    @InjectView(id = R.id.textView1)
    private TextView textView1;
    @InjectView(id = R.id.textView2)
    private TextView textView2;
    @InjectView(id = R.id.textView3)
    private TextView textView3;
    @InjectView(id = R.id.textView4)
    private TextView textView4;
    @InjectView(id = R.id.textView12)
    private TextView textView12;

    @InjectView(id = R.id.imageView2)
    private ImageView imageView2;
    private String flag;
    public AudioNewsDetailRequest(Activity activity, String flag) {
        this.activity = activity;
        this.flag = flag;
        Injector.injectAll(this, activity);
        http();
    }
    private void http() {
        String url = Global.SERVER_URL + "ph/getNewDetail?id=" + activity.getIntent().getStringExtra("id") + "&flag=" + flag;
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.has("result") && "0".equals(obj.getString("result"))) {
                                if (obj.has("obj")) {
                                    //获取相应的下载信息
                                    JSONObject jsonObject = (JSONObject) obj.get("obj");
                                    if (jsonObject.has("title"))
                                        textView1.setText(jsonObject.getString("title"));
                                    if (jsonObject.has("updateDate"))
                                        textView2.setText(DateUtils.dateFormatDateTime(jsonObject.getString("updateDate")));
                                    if (jsonObject.has("hits")) {
                                        textView3.setText(jsonObject.getString("hits"));
                                        imageView2.setVisibility(View.VISIBLE);
                                    } else {
                                        textView3.setText("0");
                                        imageView2.setVisibility(View.VISIBLE);
                                    }
                                    if (jsonObject.has("infoArea"))
                                        textView12.setText(jsonObject.getString("infoArea"));
                                    if (jsonObject.has("content"))
                                        textView4.setText(jsonObject.getString("content"));

                                } else if (obj.has("result") && "1".equals(obj.getString("result"))) {

                                }
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
