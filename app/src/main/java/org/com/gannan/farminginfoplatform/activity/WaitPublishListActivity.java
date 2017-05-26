package org.com.gannan.farminginfoplatform.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.com.gannan.farminginfoplatform.R;
import org.com.gannan.farminginfoplatform.adapter.WaitPublishAdapter;
import org.com.gannan.farminginfoplatform.adapter.WaitPublishItemDelegate;
import org.com.gannan.farminginfoplatform.baseadapter.base.MultiItemTypeAdapter;
import org.com.gannan.farminginfoplatform.entity.NewsEntity;
import org.com.gannan.farminginfoplatform.request.BuySellListRequest;
import org.com.gannan.farminginfoplatform.request.DealPublishRequest;
import org.com.gannan.farminginfoplatform.utils.AnimUtil;
import org.com.gannan.farminginfoplatform.utils.DividerItemDecoration;
import org.com.gannan.farminginfoplatform.utils.SharedUtil;
import org.easycoding.mobile.android.annotation.InjectView;
import org.easycoding.mobile.android.annotation.Injector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/03/6.
 */

public class WaitPublishListActivity extends AppCompatActivity implements BuySellListRequest.RefreshComplete,
        WaitPublishItemDelegate.CheckBoxListener {

    @InjectView(id = R.id.title_tv)
    private TextView titleTv;
    @InjectView(id = R.id.textView1)
    private TextView deal;
    @InjectView(id = R.id.recyclerView)
    private RecyclerView recyclerView;
    @InjectView(id = R.id.imageView1)
    private ImageView back;
    private WaitPublishAdapter adapter;
    private List<NewsEntity> list = new ArrayList<>();
    private BuySellListRequest listRequest;
    private PopupWindow popup;
    private TextView textView2;
    private boolean loadMore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_publish_list);
        Injector.injectAll(this);
        initList();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        deal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("批处理".equals(deal.getText().toString())) {
                    deal.setText("取消");
                    createPop();
                    showPop();
                } else {
                    deal.setText("批处理");
                    hidePop();
                }
                for (int i = 0; i < list.size(); i++) {
                    NewsEntity n = list.get(i);
                    int isCheck = n.getIsCheck();
                    if (isCheck == 0) {
                        n.setIsCheck(1);
                    } else if (isCheck == 1 || isCheck == 2) {
                        n.setIsCheck(0);
                    }
                    list.set(i, n);
                }
                adapter.notifyDataSetChanged();
            }
        });
        titleTv.setText(getString(R.string.str_msg138));

    }

    public void initList() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        adapter = new WaitPublishAdapter(this, list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(WaitPublishListActivity.this, PublishDetailActivity.class);
                intent.putExtra("id", list.get(position).getId());
                intent.putExtra("position", position);
                intent.putExtra("requestCode", 103);
                intent.putExtra("categoryType", list.get(position).getCategoryType());
                startActivityForResult(intent, 103);

                AnimUtil.changePageIn(WaitPublishListActivity.this);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        HashMap<String, String> map = new HashMap<>();
        map.put("token", SharedUtil.getToken(this));
        map.put("actName", "getWaitPublishList");
        //网络获取内容数据
        listRequest = new BuySellListRequest(this, map);
        listRequest.setRefreshComplete(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 103) {
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

    public void createPop() {
        if (popup == null) {
            View layoutView = LayoutInflater.from(this).inflate(R.layout.popupwindow_deal, null);
            TextView textView1 = (TextView) layoutView.findViewById(R.id.textView1);
            textView2 = (TextView) layoutView.findViewById(R.id.textView2);
            TextView textView3 = (TextView) layoutView.findViewById(R.id.textView3);
            textView2.setText("共0条");
            textView1.setOnClickListener(new View.OnClickListener() {//批量发布
                @Override
                public void onClick(View v) {
                  boolean flag=false;
                        StringBuilder ids = new StringBuilder();
                        StringBuilder positions = new StringBuilder();
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getIsCheck() == 2) {
                                ids.append("," + list.get(i).getId());
                                positions.append("," + i);
                                flag=true;
                            }
                        }
                    if(flag) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("articleIds", ids.toString());
                        map.put("token", SharedUtil.getToken(WaitPublishListActivity.this));
                        new DealPublishRequest(WaitPublishListActivity.this, map, positions.toString());
                    }
                }
            });
            textView3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < list.size(); i++) {
                        NewsEntity n = list.get(i);
                        int isCheck = n.getIsCheck();
                        if (isCheck != 2)n.setIsCheck(2);
                        list.set(i, n);
                    }
                    adapter.notifyDataSetChanged();
                    setNum();
                }
            });
            popup = new PopupWindow(layoutView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, false);
            ColorDrawable drawable = new ColorDrawable(0xe0000000);
            popup.setBackgroundDrawable(drawable);
            popup.setAnimationStyle(R.style.popupWindowShow);
        }
    }

    public void showPop() {
        if (popup != null) {
            popup.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        }
    }

    public void hidePop() {
        if (popup != null && popup.isShowing()) {
            popup.dismiss();
        }
    }
    public void dealResult(String positions) {
        String[] p=positions.split(",");
        List<NewsEntity> list1 = new ArrayList<>();
        for (int i = 1; i <p.length ; i++) {
            list1.add(list.get(Integer.parseInt(p[i])));
        }
        list.removeAll(list1);
        adapter.notifyDataSetChanged();
    }
    //下拉刷新数据
    @Override
    public void pullToRefresh(List<NewsEntity> lst) {
        hidePop();
        deal.setText("批处理");
        list.clear();
        list.addAll(lst);

        adapter.notifyDataSetChanged();

    }

    @Override
    public void loadMore(List<NewsEntity> lst) {
        loadMore=true;
        list.addAll(lst);
        adapter.notifyDataSetChanged();
        loadMore=false;
        // TODO: 2017/3/8 需测试  loadMore
    }

    @Override
    public void clearList() {
    }

    @Override
    public void changeListValue(int position, boolean isChecked) {
        if(popup!=null&&popup.isShowing()&&!loadMore) {
            NewsEntity n = list.get(position);
            if (isChecked) {
                n.setIsCheck(2);
            } else {
                n.setIsCheck(1);
            }
            list.set(position, n);
            setNum();
        }
       
    }

    public void setNum() {
        int num = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getIsCheck()==2) {
                num++;
            }
        }
        textView2.setText("共"+num+"条");
    }
}
