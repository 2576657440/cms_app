package org.com.gannan.farminginfoplatform.request;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.com.gannan.farminginfoplatform.R;
import org.com.gannan.farminginfoplatform.comm.Global;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Request;

import static android.R.attr.tag;

/**
 * Created by Administrator on 2016/11/26.
 */

public class WeatherRequest {

    private Activity activity;
    private TextView airCondition;
    private TextView weather;
    private TextView temperature;
    private LinearLayout airLinear;
    public WeatherRequest(final Activity activity, View view) {
        this.activity = activity;
//        airCondition = (TextView) view.findViewById(R.id.airCondition);
        temperature = (TextView) view.findViewById(R.id.temperature);
        weather = (TextView) view.findViewById(R.id.weather);
        airLinear = (LinearLayout) view.findViewById(R.id.airLinear);
        airLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri=Uri.parse("http://e.weather.com.cn/d/index/101050204.shtml");
                Intent intent =new Intent(Intent.ACTION_VIEW,uri);
                activity.startActivity(intent);
            }
        });
        http();
    }

    private void http() {
        String url = "http://apicloud.mob.com/v1/weather/query?key=1aab6d19ac4a6&city=甘南&province=黑龙江";
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.has("retCode") && "200".equals(obj.getString("retCode"))) {

                                JSONArray jsonArray=new JSONArray(obj.get("result").toString());
                                JSONObject obj1 = new JSONObject(jsonArray.get(0).toString());
                               // airCondition.setText(obj1.getString("airCondition")+":"+obj1.getString("pollutionIndex"));
                                JSONArray jArray=new JSONArray(obj1.get("future").toString());
                                JSONObject obj2 = new JSONObject(jArray.get(0).toString());

                                temperature.setText(obj2.getString("temperature"));
                                weather.setText(obj1.getString("weather")+","+obj1.getString("airCondition")+":"+obj1.getString("pollutionIndex"));

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

}
