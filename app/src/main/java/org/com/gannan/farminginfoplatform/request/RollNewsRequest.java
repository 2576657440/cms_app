package org.com.gannan.farminginfoplatform.request;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.github.guilhermesgb.marqueeto.LabelledMarqueeEditText;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.com.gannan.farminginfoplatform.R;
import org.com.gannan.farminginfoplatform.comm.Global;
import org.com.gannan.farminginfoplatform.entity.NewsEntity;
import org.com.gannan.farminginfoplatform.widget.MarqueeView;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

import static android.R.attr.tag;

/**
 * Created by Administrator on 2016/11/26.
 */

public class RollNewsRequest {

    private Activity activity;
    private View view;
    private MarqueeView mv;
    private LabelledMarqueeEditText tvMarquee;
//    String str = "摘要：1上的纠纷快速点击付款就看看看就看见";
    public RollNewsRequest(Activity activity, View view) {
        this.activity = activity;
        this.view = view;
        http();
    }

    private void http() {
        String url = Global.SERVER_URL + "ph/getRollnewsList";
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            List<NewsEntity> list = null;
                            JSONObject obj = new JSONObject(response);
                            if (obj.has("result") && "0".equals(obj.getString("result"))) {
                                if(obj.has("obj")){
                                    //json串解析
                                    list = JSON.parseObject(obj.getString("obj"),
                                            new TypeReference<List<NewsEntity>>() {
                                            });
                                    if(list!=null&&list.size()>0){
                                        setView(list);
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
                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.i(Global.TAG, tag + ":" + e.toString());
                    }

                });
    }
    private void setView(List<NewsEntity> list) {
        StringBuffer sbf=new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            sbf.append("      "+list.get(i).getContent());
        }
        tvMarquee = (LabelledMarqueeEditText) view.findViewById(R.id.textMarquee);
        tvMarquee.setText(sbf.toString());
        tvMarquee.setVisibility(View.VISIBLE);
    }

}
