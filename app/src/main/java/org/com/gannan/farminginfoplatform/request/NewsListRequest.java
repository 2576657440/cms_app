package org.com.gannan.farminginfoplatform.request;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.com.gannan.farminginfoplatform.MainActivity;
import org.com.gannan.farminginfoplatform.R;
import org.com.gannan.farminginfoplatform.baseadapter.refresh.PtrClassicFrameLayout;
import org.com.gannan.farminginfoplatform.baseadapter.refresh.PtrDefaultHandler;
import org.com.gannan.farminginfoplatform.baseadapter.refresh.PtrFrameLayout;
import org.com.gannan.farminginfoplatform.baseadapter.refresh.PtrHandler;
import org.com.gannan.farminginfoplatform.comm.Global;
import org.com.gannan.farminginfoplatform.comm.StartProgressDialog;
import org.com.gannan.farminginfoplatform.entity.NewsEntity;
import org.com.gannan.farminginfoplatform.fragment.HomeFragment;
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

public class NewsListRequest {

    private Activity activity;
    private StartProgressDialog progressDialog;
    private int pageNo=1;
    private int pageAll;
    private RecyclerView recyclerView;
    public PtrClassicFrameLayout ptrFrameLayout;
    public RefreshComplete refreshComplete;
    private HashMap<String, String> map = new HashMap<>();

    public NewsListRequest(Activity activity) {
        this.activity = activity;
        setRefresh();
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
                                if (list != null && list.size() > 0) {
                                    if(flag){
                                        refreshComplete.loadMore(list);
                                    }else{
                                        refreshComplete.pullToRefresh(list);
                                    }
                                }else{
                                    refreshComplete.clearList();
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
                        if (ptrFrameLayout != null && ptrFrameLayout.isRefreshing()) {
                            ptrFrameLayout.refreshComplete();
                        }
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
        HomeFragment fragment=null;
        List<Fragment> list=((MainActivity)activity).getSupportFragmentManager().getFragments();
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i) instanceof HomeFragment){
                fragment= (HomeFragment) list.get(i);
            }
        }
        recyclerView = (RecyclerView) fragment.getRootView().findViewById(R.id.recyclerView);
        ptrFrameLayout = (PtrClassicFrameLayout) fragment.getRootView().findViewById(R.id.fragment_PtrLayout);
        //下拉刷新
        ptrFrameLayout.setPullToRefresh(true);
        ptrFrameLayout.setLastUpdateTimeRelateObject(this);
        ptrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pageNo=1;
                http(false);
            }
        });
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
                    http(true);
                }
            }
        });
    }


    public interface RefreshComplete {
        void pullToRefresh(List<NewsEntity> list);
        void loadMore(List<NewsEntity> list);
        void clearList();
    }

    public void setRefreshComplete(RefreshComplete refreshComplete) {
        this.refreshComplete = refreshComplete;
    }

    public HashMap<String, String> getMap() {
        return map;
    }

    public void setMap(HashMap<String, String> map) {
        this.map = map;
    }
}
