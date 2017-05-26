package org.com.gannan.farminginfoplatform.request;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.com.gannan.farminginfoplatform.R;
import org.com.gannan.farminginfoplatform.comm.Global;
import org.com.gannan.farminginfoplatform.comm.UtilsDialog;
import org.com.gannan.farminginfoplatform.utils.SharedUtil;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Request;

import static android.R.attr.tag;

/**
 * Created by Administrator on 2016/11/25.
 */

public class GetAuditCountRequest {

    private Activity activity;
    private View buySellTv;
    private View newsTv;
    private View pubnumTv;
//    private StartProgressDialog progressDialog;
    private RefreshCount refreshCount;

    public GetAuditCountRequest(Activity activity, View buySellTv, View newsTv,View pubnumTv) {
        this.activity = activity;
        this.buySellTv = buySellTv;
        this.newsTv = newsTv;
        this.pubnumTv = pubnumTv;
        http();
    }

    private void http() {
        String url = Global.SERVER_URL + "ph/getWaitAuditCount?token=" + SharedUtil.getToken(activity);
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

                                             if (buySellTv != null) {
                                                 if (jsonObject.has("bscount") && jsonObject.getInt("bscount") > 0) {
                                                     //供求待审核个数
                                                     refreshCount.changeNum(0,jsonObject.getInt("bscount"));

                                                 } else {
                                                     refreshCount.removeNum(0);
                                                 }
                                             }
                                             if (newsTv != null) {

                                                 if (jsonObject.has("othercount") && jsonObject.getInt("othercount") > 0) {
                                                     //新闻待审核个数
                                                     refreshCount.changeNum(1,jsonObject.getInt("othercount"));

                                                 } else {
                                                     refreshCount.removeNum(1);
                                                 }

                                             }
                                             if (pubnumTv != null) {

                                                 if (jsonObject.has("waitPublish") && jsonObject.getInt("waitPublish") > 0) {
                                                     //新闻待审核个数
                                                     refreshCount.changeNum(2,jsonObject.getInt("waitPublish"));

                                                 } else {
                                                     refreshCount.removeNum(2);
                                                 }

                                             }

                                         }
                                     } else if (obj.has("result") && "2".equals(obj.getString("result"))) {
                                         UtilsDialog utilsDialog =UtilsDialog.getInstance();
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
//                                 progressDialog = new StartProgressDialog(activity);
                             }

                             @Override
                             public void onAfter(int id) {
                                 super.onAfter(id);
//                                 progressDialog.stopProgressDialog();
                             }

                             @Override
                             public void onError(Call call, Exception e, int id) {
                                 Log.i(Global.TAG, tag + ":" + e.toString());
                             }

                         }

                );
    }

    public interface RefreshCount {
        void changeNum(int flag,int num);

        void removeNum(int flag);
    }

    public RefreshCount getRefreshCount() {
        return refreshCount;
    }

    public void setRefreshCount(RefreshCount refreshCount) {
        this.refreshCount = refreshCount;
    }
}
