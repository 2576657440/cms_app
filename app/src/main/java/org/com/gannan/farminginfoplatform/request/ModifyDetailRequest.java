package org.com.gannan.farminginfoplatform.request;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.com.gannan.farminginfoplatform.R;
import org.com.gannan.farminginfoplatform.activity.ModifyDetailActivity;
import org.com.gannan.farminginfoplatform.comm.Global;
import org.com.gannan.farminginfoplatform.comm.ImageLoaderComm;
import org.com.gannan.farminginfoplatform.comm.StartProgressDialog;
import org.com.gannan.farminginfoplatform.utils.DateUtils;
import org.com.gannan.farminginfoplatform.utils.Util;
import org.easycoding.mobile.android.annotation.InjectView;
import org.easycoding.mobile.android.annotation.Injector;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Request;

import static android.R.attr.tag;
import static org.com.gannan.farminginfoplatform.R.id.textView1;
import static org.com.gannan.farminginfoplatform.R.id.textView2;

/**
 * Created by Administrator on 2016/11/30.
 */

public class ModifyDetailRequest {

    private Activity activity;
    private StartProgressDialog progressDialog;
    @InjectView(id = R.id.editText1)
    private EditText title;
    @InjectView(id = R.id.editText2)
    private EditText content;
    @InjectView(id = R.id.editText3)
    private EditText tel;
    @InjectView(id = R.id.editText4)
    private EditText address;
    @InjectView(id = R.id.editText5)
    private EditText linkman;
    @InjectView(id = R.id.editText6)
    private EditText infoArea;
    @InjectView(id = textView1)
    private TextView dateTv;
    @InjectView(id = textView2)
    private TextView typeTv;
    @InjectView(id = R.id.linearlayout2)
    private LinearLayout linearLayout2;//有效期
    @InjectView(id = R.id.dLinear)
    private LinearLayout dLinear;//个人信息版块
    @InjectView(id = R.id.view4)
    private View view4;
    @InjectView(id = R.id.picLayout)
    private LinearLayout picLay;
    private String flag;

    public ModifyDetailRequest(Activity activity, String flag) {
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
                                        title.setText(jsonObject.getString("title"));
                                    if (jsonObject.has("content"))
                                        content.setText(jsonObject.getString("content"));
                                    if (jsonObject.has("mobil") && !"".equals(jsonObject.getString("mobil"))) {
                                        dLinear.setVisibility(View.VISIBLE);
                                        linearLayout2.setVisibility(View.GONE);
                                        view4.setVisibility(View.GONE);
                                        tel.setText(jsonObject.getString("mobil"));
                                        if (jsonObject.has("address"))
                                            address.setText(jsonObject.getString("address"));
                                        if (jsonObject.has("contacts"))
                                            linkman.setText(jsonObject.getString("contacts"));
                                        if (jsonObject.has("infoArea"))
                                            infoArea.setText(jsonObject.getString("infoArea"));
                                    } else {
                                        dLinear.setVisibility(View.GONE);
                                        linearLayout2.setVisibility(View.VISIBLE);
                                        view4.setVisibility(View.VISIBLE);
                                        if (jsonObject.has("expiryDate"))
                                            dateTv.setText(DateUtils.dateFormatDIY(jsonObject.getString("expiryDate")));
                                    }
                                    if (jsonObject.has("cName"))
                                        typeTv.setText(jsonObject.getString("cName"));
                                    if (jsonObject.has("categoryId"))
                                        ((ModifyDetailActivity) activity).setTypeId(jsonObject.getString("categoryId"));
                                    if (jsonObject.has("categoryType"))
                                        ((ModifyDetailActivity) activity).type=jsonObject.getString("categoryType");
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
            final String[] path = imagePathStr.split("\\|");
            for (int i = 1; i < path.length; i++) {
                int parentw = (int) Util.dpToPx(activity, 100);
                int width = (int) Util.dpToPx(activity, 85);
                int btnWidth = (int) Util.dpToPx(activity, 25);
                FrameLayout.LayoutParams p = new FrameLayout.LayoutParams(parentw, parentw);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, width);
                params.gravity = Gravity.BOTTOM | Gravity.LEFT;

                FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(btnWidth, btnWidth);
                param.gravity = Gravity.TOP | Gravity.RIGHT;

                final FrameLayout frame = new FrameLayout(activity);
                frame.setLayoutParams(p);
                String[] pt = path[i].split("/");
//                ((ModifyDetailActivity) activity).path = pt[0] + "/" + pt[1] + "/" + pt[2] + "/" + pt[3] + "/" + pt[4] + "/" + pt[5] + "/";
                ((ModifyDetailActivity) activity).pathMap.put(pt[8], pt[6] + "/" + pt[7] + "/" + pt[8]);
                frame.setTag(pt[8]);
                ImageView pic = new ImageView(activity);
                pic.setScaleType(ImageView.ScaleType.CENTER_CROP);
                //下载图片
                ImageLoader.getInstance().displayImage(Global.URL + path[i], pic, ImageLoaderComm.getImageOptions());

                ImageView delBtn = new ImageView(activity);
                delBtn.setImageResource(R.mipmap.btn_del);
                delBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        picLay.removeView(frame);
                        ((ModifyDetailActivity) activity).pathMap.remove(frame.getTag());
                    }
                });
                frame.addView(pic, params);
                frame.addView(delBtn, param);

                picLay.addView(frame);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
