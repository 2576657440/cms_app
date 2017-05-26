package org.com.gannan.farminginfoplatform.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.com.gannan.farminginfoplatform.R;
import org.com.gannan.farminginfoplatform.adapter.NewsAdapter;
import org.com.gannan.farminginfoplatform.baseadapter.base.MultiItemTypeAdapter;
import org.com.gannan.farminginfoplatform.entity.NewsEntity;
import org.com.gannan.farminginfoplatform.request.BuySellListRequest;
import org.com.gannan.farminginfoplatform.utils.AnimUtil;
import org.com.gannan.farminginfoplatform.utils.DividerItemDecoration;
import org.com.gannan.farminginfoplatform.utils.SharedUtil;
import org.easycoding.mobile.android.annotation.InjectView;
import org.easycoding.mobile.android.annotation.Injector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/12/1.
 */

public class BuySellAuditListActivity extends AppCompatActivity implements BuySellListRequest.RefreshComplete {


    @InjectView(id = R.id.title_tv)
    private TextView titleTv;
    @InjectView(id = R.id.imageView1)
    private ImageView back;
    @InjectView(id = R.id.recyclerView)
    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private List<NewsEntity> list = new ArrayList<>();
    private BuySellListRequest listRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_sell_list);
        Injector.injectAll(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleTv.setText(getString(R.string.str_msg107));
        initList();
    }
    public void initList() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        adapter = new NewsAdapter(this, list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(BuySellAuditListActivity.this, AuditDetailActivity.class);
                intent.putExtra("id",list.get(position).getId());
                intent.putExtra("position",position);
                intent.putExtra("requestCode",101);
                intent.putExtra("categoryType",list.get(position).getCategoryType());
                startActivityForResult(intent,101);
                AnimUtil.changePageIn(BuySellAuditListActivity.this);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        HashMap<String, String> map= new HashMap<>();
        map.put("flag",getIntent().getStringExtra("flag"));
        map.put("token", SharedUtil.getToken(this));
        map.put("actName", "getWaitAuditList");
        //网络获取内容数据
        listRequest = new BuySellListRequest(this,map);
        listRequest.setRefreshComplete(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==101){
            int refresh= data.getIntExtra("refresh",0);
            if(refresh==201){
                listRequest.http(false);//刷新当前页
            }else {
                int position=data.getIntExtra("position",0);
                list.remove(position);
                adapter.notifyDataSetChanged();
            }
        }
    }

    //下拉刷新数据
    @Override
    public void pullToRefresh(List<NewsEntity> lst) {
        list.clear();
        list.addAll(lst);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void loadMore(List<NewsEntity> lst) {
        list.addAll(lst);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void clearList() {
    }

}
