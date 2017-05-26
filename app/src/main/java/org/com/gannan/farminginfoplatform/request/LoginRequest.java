package org.com.gannan.farminginfoplatform.request;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.com.gannan.farminginfoplatform.comm.Global;
import org.com.gannan.farminginfoplatform.comm.StartProgressDialog;
import org.com.gannan.farminginfoplatform.fragment.LoginFragment;
import org.com.gannan.farminginfoplatform.utils.SharedUtil;
import org.com.gannan.farminginfoplatform.utils.Util;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import okhttp3.Call;
import okhttp3.Request;

import static android.R.attr.tag;
import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2016/11/26.
 */

public class LoginRequest {

    private LoginFragment fragment;
    private Activity activity;
    private StartProgressDialog progressDialog;

    public LoginRequest(LoginFragment fragment, HashMap<String, String> map) {
        this.fragment = fragment;
        activity = fragment.getActivity();
        http(map);
    }

    private void http(final HashMap<String, String> map) {
        String url = Global.SERVER_URL + "ph/userLogin";
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
                                SharedUtil.saveLoginFlag(activity, true);
                                SharedUtil.saveUserName(activity, map.get("userName"));
                                SharedUtil.savePassword(activity, map.get("passWord"));
                                JSONObject obj1 = new JSONObject(obj.get("obj").toString());
                                JSONObject obj2 = new JSONObject(obj1.get("userandrole").toString());

                                if (obj2.has("enname")) {
                                    SharedUtil.saveRoleFlag(activity, obj2.getString("enname"));
                                }
                                if (obj2.has("name")) {
                                    SharedUtil.saveName(activity, obj2.getString("name"));
                                }
                                if (obj2.has("areaname")) {
                                    SharedUtil.saveAreaName(activity, obj2.getString("areaname"));
                                }
                                if (obj2.has("mobile")) {
                                    SharedUtil.savePhoneNo(activity, obj2.getString("mobile"));
                                }
                                if (obj1.has("token")) {
                                    SharedUtil.saveToken(activity, obj1.getString("token"));
                                }
                                if (obj1.has("uid") && Util.isValidTagAndAlias(obj1.getString("uid"))) {
                                    mHandler.sendMessage(mHandler.obtainMessage(0, obj1.getString("uid")));
                                }
                                activity.setResult(RESULT_OK);
                                Log.i("LoginRequest", "onResponse: RESULT_OK");
                                activity.finish();
                            } else if (obj.has("result") && "1".equals(obj.getString("result"))) {
                                fragment.setHintText(obj.getString("msg"));
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

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            JPushInterface.setAliasAndTags(activity, (String) msg.obj, null, mAliasCallback);
        }
    };
    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
            }
//            Toast.makeText(activity, logs, Toast.LENGTH_LONG).show();
        }

    };
}
