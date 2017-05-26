package org.com.gannan.farminginfoplatform.request.callback;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zhy.http.okhttp.callback.Callback;

import org.com.gannan.farminginfoplatform.entity.NewsEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Response;

/**
 * Created by Administrator on 2016/11/27.
 */
public abstract class NewsListCallback extends Callback<List<NewsEntity>> {

    @Override
    public List<NewsEntity> parseNetworkResponse(Response response, int id) {
        List<NewsEntity> list = null;
        try {
            String content = response.body().string();

            JSONObject obj = new JSONObject(content);
            if (obj.has("result")&&"0".equals(obj.getString("result")) &&obj.has("obj")) {
                //json串解析
                list = JSON.parseObject(obj.getString("obj"),
                        new TypeReference<List<NewsEntity>>() {
                        });
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }


}
