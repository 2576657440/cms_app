package org.com.gannan.farminginfoplatform.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.com.gannan.farminginfoplatform.R;
import org.com.gannan.farminginfoplatform.adapter.NewsAdapter;
import org.com.gannan.farminginfoplatform.baseadapter.base.MultiItemTypeAdapter;
import org.com.gannan.farminginfoplatform.dbservice.DbManager;
import org.com.gannan.farminginfoplatform.dbservice.UseSqlite;
import org.com.gannan.farminginfoplatform.entity.NewsEntity;
import org.com.gannan.farminginfoplatform.request.SearchListRequest;
import org.com.gannan.farminginfoplatform.utils.AnimUtil;
import org.com.gannan.farminginfoplatform.utils.DividerItemDecoration;
import org.com.gannan.farminginfoplatform.utils.Util;
import org.easycoding.mobile.android.annotation.InjectView;
import org.easycoding.mobile.android.annotation.Injector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/9.
 */

public class SearchActivity extends AppCompatActivity implements View.OnClickListener, SearchListRequest.SearchComplete {

    @InjectView(id = R.id.searcher_back)
    private ImageView search_back;
    @InjectView(id = R.id.searcher_edit)
    private EditText searcher_edit;
    @InjectView(id = R.id.search_delete)
    private TextView search_delete;
    @InjectView(id = R.id.search_listView_tips)
    private ListView search_listView_tips;
    @InjectView(id = R.id.search_btn)
    private TextView search_btn;
    @InjectView(id = R.id.nothing)
    private LinearLayout nothing;
    @InjectView(id = R.id.recyclerView)
    private RecyclerView recyclerView;

    private MyAdapter myAdapter;
    private List<Map<String, Object>> list;
    private List<NewsEntity> listData = new ArrayList<>();
    private NewsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Injector.injectAll(this);

        init();
        addListener();
    }

    private void addListener() {
        search_delete.setOnClickListener(this);
        search_btn.setOnClickListener(this);
        search_back.setOnClickListener(this);
        search_listView_tips.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text = list.get(position).get("the_key").toString();
                searcher_edit.setText(text);
                search();
            }
        });
    }

    private void init() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        adapter = new NewsAdapter(this, listData);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(SearchActivity.this, NewsDetailActivity.class);
                intent.putExtra("id", listData.get(position).getId());
                intent.putExtra("categoryType", listData.get(position).getCategoryType());
                startActivity(intent);
                AnimUtil.changePageIn(SearchActivity.this);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });


        DbManager dbManager = new DbManager(SearchActivity.this);
        list = dbManager.dblistInfo(UseSqlite.SEARCH_RECORD_TABLE_NAME, "", null, "create_time desc", "");
        if (list != null && list.size() > 0) {
            myAdapter = new MyAdapter(list);
            search_listView_tips.setAdapter(myAdapter);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.searcher_back:
                finish();
                break;
            case R.id.search_btn:
                //检索
                search();
                break;
            case R.id.search_delete://删除历史记录
                DbManager dbManager = new DbManager(SearchActivity.this);
                dbManager.dbClearTable(UseSqlite.SEARCH_RECORD_TABLE_NAME);
                if (myAdapter != null) {
                    list.clear();
                    myAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    private void search() {
        String theKey = searcher_edit.getText().toString().trim();
        if (!TextUtils.isEmpty(theKey)) {
            DbManager dbManager = new DbManager(SearchActivity.this);
            //为避免记录重复的关键字，先删除，后添加。
            String[] whereArgs = {theKey};
            dbManager.dbDelObj(UseSqlite.SEARCH_RECORD_TABLE_NAME, "the_key=?", whereArgs);
            //添加
            ContentValues values = new ContentValues();
            values.put("the_key", searcher_edit.getText().toString());
            values.put("create_time", Util.getCurrentDate());
            dbManager.dbinsert(values, UseSqlite.SEARCH_RECORD_TABLE_NAME);

            //网络获取内容数据
            SearchListRequest listRequest = new SearchListRequest(this, theKey,true);
            listRequest.setSearchComplete(this);

        }
    }

    @Override
    public void result(List<NewsEntity> lst) {
        listData.clear();
        listData.addAll(lst);
        adapter.notifyDataSetChanged();
        search_listView_tips.setVisibility(View.GONE);
        search_delete.setVisibility(View.GONE);
        nothing.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void loadMore(List<NewsEntity> lst) {
        listData.addAll(lst);
        adapter.notifyDataSetChanged();
        search_listView_tips.setVisibility(View.GONE);
        search_delete.setVisibility(View.GONE);
        nothing.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void nothing() {
        listData.clear();
        adapter.notifyDataSetChanged();
        search_listView_tips.setVisibility(View.GONE);
        search_delete.setVisibility(View.GONE);
        nothing.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    class MyAdapter extends BaseAdapter {
        private List<Map<String, Object>> list;
        private LayoutInflater inflater;

        public MyAdapter(List<Map<String, Object>> list) {
            this.list = list;
            inflater = LayoutInflater.from(SearchActivity.this);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();

                convertView = inflater.inflate(R.layout.item_search_list, null);
                holder.textView1 = (TextView) convertView.findViewById(R.id.textView1);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.textView1.setText(list.get(position).get("the_key").toString());
            return convertView;
        }

        public class ViewHolder {

            public TextView textView1;
        }
    }
}
