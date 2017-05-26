package org.com.gannan.farminginfoplatform.request;

import android.app.Activity;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.com.gannan.farminginfoplatform.R;
import org.com.gannan.farminginfoplatform.activity.HistoryDetailActivity;
import org.com.gannan.farminginfoplatform.activity.ViewImageActivity;
import org.com.gannan.farminginfoplatform.comm.Global;
import org.com.gannan.farminginfoplatform.comm.ImageLoaderComm;
import org.com.gannan.farminginfoplatform.comm.StartProgressDialog;
import org.com.gannan.farminginfoplatform.utils.AnimUtil;
import org.com.gannan.farminginfoplatform.utils.DateUtils;
import org.com.gannan.farminginfoplatform.utils.Util;
import org.easycoding.mobile.android.annotation.InjectView;
import org.easycoding.mobile.android.annotation.Injector;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Request;

import static android.R.attr.tag;

/**
 * Created by Administrator on 2016/11/30.
 */

public class NewsDetailRequest {

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
    @InjectView(id = R.id.textView5)
    private TextView textView5;
    @InjectView(id = R.id.textView6)
    private TextView textView6;
    @InjectView(id = R.id.textView7)
    private TextView textView7;
    @InjectView(id = R.id.textView8)
    private TextView textView8;
    @InjectView(id = R.id.textView9)
    private TextView textView9;
    @InjectView(id = R.id.textView12)
    private TextView textView12;

    @InjectView(id = R.id.imageView2)
    private ImageView imageView2;
    private String flag;
    public NewsDetailRequest(Activity activity, String flag) {
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
                                    if (jsonObject.has("mobil"))
                                        textView5.setText(activity.getResources().getString(R.string.str_msg216) + "   " + jsonObject.getString("mobil"));
                                    if (jsonObject.has("address"))
                                        textView6.setText(activity.getResources().getString(R.string.str_msg217) + "   " + jsonObject.getString("address"));
                                    if (jsonObject.has("contacts"))
                                        textView7.setText(activity.getResources().getString(R.string.str_msg218) + "   " + jsonObject.getString("contacts"));
                                    if (jsonObject.has("expiryDate"))
                                        textView8.setText(activity.getResources().getString(R.string.str_msg219) + "   " + DateUtils.dateFormatDIY(jsonObject.getString("expiryDate")));

                                    if ("".equals(flag)) {//免责声明
                                        textView9.setText(activity.getString(R.string.str_msg134));
                                    }
                                    if(activity instanceof HistoryDetailActivity){
                                        if (jsonObject.has("state"))
                                        ((HistoryDetailActivity)activity).state=jsonObject.getString("state");
                                    }

                                    setInfo(jsonObject);
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

    private void setInfo(JSONObject obj) {


        try {
            if (!obj.has("image")) {
                return;
            }
            final String imagePathStr = obj.getString("image");
            if (imagePathStr == null || "".equals(imagePathStr)) {
                return;
            }
            int count = picLayout.getChildCount();
            for (int i = count; i >= 1; i--) {
                View view = picLayout.getChildAt(i);
                if(view instanceof ImageView){
                    picLayout.removeView(view);
                }
            }
            final String[] path = imagePathStr.split("\\|");
            for (int i = 1; i < path.length; i++) {
                int topMargin = (int) Util.dpToPx(activity, 20);

                int height = (int) Util.dpToPx(activity, ViewGroup.LayoutParams.MATCH_PARENT);

                Display display=activity.getWindowManager().getDefaultDisplay();
                DisplayMetrics metrics=new DisplayMetrics();
                display.getMetrics(metrics);
                int screenHeight = ((int) (metrics.widthPixels * 0.8));

                LinearLayout.LayoutParams params =
                        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, screenHeight);
                params.topMargin = topMargin;
                ImageView pic = new ImageView(activity);
                pic.setTag(i - 1);
                pic.setScaleType(ImageView.ScaleType.CENTER_CROP);
                //下载图片
                ImageLoader.getInstance().displayImage(Global.URL + path[i], pic, ImageLoaderComm.getImageOptions());

                pic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳转到大图浏览页
                        Intent intent = new Intent(activity, ViewImageActivity.class);
                        intent.putExtra("position", (int) v.getTag());
                        intent.putExtra("pageAll", path.length);
                        intent.putExtra("imagePathStr", imagePathStr);
                        activity.startActivity(intent);
                        AnimUtil.changePageIn(activity);
                    }
                });
                picLayout.addView(pic, params);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
