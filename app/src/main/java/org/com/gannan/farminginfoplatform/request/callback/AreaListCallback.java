package org.com.gannan.farminginfoplatform.request.callback;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zhy.http.okhttp.callback.Callback;

import org.com.gannan.farminginfoplatform.entity.AreaEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Response;

/**
 * Created by Administrator on 2016/11/25.
 */
public abstract class AreaListCallback extends Callback<List<AreaEntity>> {

    @Override
    public List<AreaEntity> parseNetworkResponse(Response response, int id) {
        List<AreaEntity> list = null;
        try {
            String content = response.body().string();

            JSONObject obj = new JSONObject(content);
            if (obj.has("result") && "0".equals(obj.getString("result")) && obj.has("obj")) {
                //json串解析
                list = JSON.parseObject(obj.getString("obj"),
                        new TypeReference<List<AreaEntity>>() {
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
