package org.com.gannan.farminginfoplatform.request;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.com.gannan.farminginfoplatform.R;
import org.com.gannan.farminginfoplatform.comm.Global;
import org.com.gannan.farminginfoplatform.comm.StartProgressDialog;
import org.com.gannan.farminginfoplatform.entity.NewsEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

import static android.R.attr.tag;

/**
 * Created by Administrator on 2016/11/27.
 */

public class SearchListRequest {

    private Activity activity;
    private StartProgressDialog progressDialog;
    private int pageNo=1;
    private int pageAll;
    private RecyclerView recyclerView;
    private HashMap<String, String> map;
    private SearchComplete searchComplete;

    public SearchListRequest(Activity activity,String key,boolean flag) {
        this.activity = activity;
        map = new HashMap<>();
        map.put("title", key);
        setRefresh();
        http(flag);
    }

    public void http(final boolean flag) {

        map.put("pageSize", "20");
        map.put("pageNo", pageNo + "");
        String url = Global.SERVER_URL + "ph/getArticle";
        OkHttpUtils
                .post()
                .url(url)
                .params(map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            List<NewsEntity> list = null;
                            JSONObject obj = new JSONObject(response);
                            if (obj.has("result")&&"0".equals(obj.getString("result"))) {
                                if(obj.has("obj")){
                                    //json串解析
                                    list = JSON.parseObject(obj.getString("obj"),
                                            new TypeReference<List<NewsEntity>>() {
                                            });
                                }
                                if(flag){
                                    if (list != null && list.size() > 0) {
                                        searchComplete.result(list);
                                    }else{
                                        searchComplete.nothing();
                                    }
                                }else{
                                    searchComplete.loadMore(list);
                                }

                                pageAll=obj.getInt("lastPage");
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

    /**
     * 设置刷新监听器及头部布局
     */
    public void setRefresh() {
        recyclerView = (RecyclerView) activity.findViewById(R.id.recyclerView);
        //加载更多
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                int itemCount = linearLayoutManager.getItemCount();

                if (pageAll>pageNo&&lastVisibleItem >= itemCount - 1 && dy > 0) {
                    pageNo++;
                    http(false);
                }
            }
        });
    }


    public interface SearchComplete {
        void result(List<NewsEntity> list);
        void loadMore(List<NewsEntity> list);
        void nothing();
    }

    public void setSearchComplete(SearchComplete searchComplete) {
        this.searchComplete = searchComplete;
    }
}
