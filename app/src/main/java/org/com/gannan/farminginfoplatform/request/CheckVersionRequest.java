package org.com.gannan.farminginfoplatform.request;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import org.com.gannan.farminginfoplatform.widget.BadgeView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.com.gannan.farminginfoplatform.R;
import org.com.gannan.farminginfoplatform.comm.Global;
import org.com.gannan.farminginfoplatform.comm.StartProgressDialog;
import org.com.gannan.farminginfoplatform.comm.UpgradeDownManager;
import org.com.gannan.farminginfoplatform.comm.UtilsDialog;
import org.com.gannan.farminginfoplatform.utils.Util;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Request;

import static android.R.attr.tag;

/**
 * Created by Administrator on 2016/11/25.
 */

public class CheckVersionRequest {

    private Activity activity;
    private View view;
    private StartProgressDialog progressDialog;

    public CheckVersionRequest(Activity activity, View view) {
        this.activity = activity;
        this.view = view;
        http();
    }

    private void http() {
        String url = Global.SERVER_URL + "ph/versionCheck";
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
                                    int versionNum = jsonObject.getInt("id");
                                    int localVersion = Util.getVersionCode(activity);
                                    if (versionNum > localVersion) {
                                        if (view == null) {
                                            //手动检查
                                            String describe = "";
                                            if (jsonObject.has("describe")) {
                                                describe = jsonObject.getString("describe");
                                            }
                                            String url = jsonObject.getString("url");
                                            //显示更新的对话框
                                            showAlertDialog(Global.URL + url, describe);
                                        }else{
                                            //自动检测
                                            BadgeView badgeView=new BadgeView(activity);
                                            badgeView.setText("new");
//                                            Drawable drawable=new ColorDrawable(activity.getResources().getColor(R.color.diyGreenColor));
                                            badgeView.setBackground(10,activity.getResources().getColor(R.color.diy_red));
                                            badgeView.setTargetView(view);
                                            badgeView.setBadgeGravity(Gravity.CENTER_VERTICAL|Gravity.RIGHT);

                                        }
                                    } else {
                                        if (view == null) {
                                            Toast.makeText(activity, "已经是最新版本了", Toast.LENGTH_LONG).show();
                                        }
                                    }

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

    private void showAlertDialog(final String url, String describe) {
        final UtilsDialog dialog = new UtilsDialog();
        dialog.setOnClickListenerOk(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                UpgradeDownManager downloadManager = new UpgradeDownManager(activity, url);
                downloadManager.loadFile();
            }
        });
        dialog.showNoticeDialog(activity, activity.getString(R.string.str_msg214), describe);
    }
}
