package org.com.gannan.farminginfoplatform.fragment;

import android.animation.AnimatorSet;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.com.gannan.farminginfoplatform.R;
import org.com.gannan.farminginfoplatform.activity.NewsDetailActivity;
import org.com.gannan.farminginfoplatform.activity.SearchActivity;
import org.com.gannan.farminginfoplatform.adapter.NewsAdapter;
import org.com.gannan.farminginfoplatform.baseadapter.base.MultiItemTypeAdapter;
import org.com.gannan.farminginfoplatform.entity.ClassifyEntity;
import org.com.gannan.farminginfoplatform.entity.NewsEntity;
import org.com.gannan.farminginfoplatform.player.PlayerActivity;
import org.com.gannan.farminginfoplatform.request.ClassifyListRequest;
import org.com.gannan.farminginfoplatform.request.NewsListRequest;
import org.com.gannan.farminginfoplatform.request.RollNewsRequest;
import org.com.gannan.farminginfoplatform.request.WeatherRequest;
import org.com.gannan.farminginfoplatform.utils.AnimUtil;
import org.com.gannan.farminginfoplatform.utils.DividerItemDecoration;
import org.com.gannan.farminginfoplatform.utils.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by Administrator on 2016/11/15.
 */
public class HomeFragment extends Fragment implements NewsListRequest.RefreshComplete {

    private View view;
    private View line;
    private HorizontalScrollView horScrollView;
    private AnimatorSet animatorSet;
    private LinearLayout linear;
    private RecyclerView recyclerView;
    private EditText homeSearch_edit;
    private NewsAdapter adapter;
    private List<NewsEntity> list = new ArrayList<>();
    private NewsListRequest newsListRequest;
    private PopupWindow pwMyPopWindow;
    private ListView lvPopupList;
    private LinearLayout nothing;
    public List<ClassifyEntity> listNews = new ArrayList<>();
    public List<ClassifyEntity> listOther = new ArrayList<>();
    public List<ClassifyEntity> listOtherChild = new ArrayList<>();
    public List<ClassifyEntity> AllList = new ArrayList<>();
    public List<ClassifyEntity> newsAndOther = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        horScrollView = (HorizontalScrollView) view.findViewById(R.id.HorScrollView1);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        homeSearch_edit = (EditText) view.findViewById(R.id.homeSearch_edit);
        nothing = (LinearLayout) view.findViewById(R.id.nothing);
        line = (View) view.findViewById(R.id.view5);

        homeSearch_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                getActivity().startActivity(intent);
                AnimUtil.changePageIn(getActivity());
            }
        });
        //获取栏目列表
        new ClassifyListRequest(this, "");
        //获取天气信息
        new WeatherRequest(getActivity(), view);
        //获取滚动字幕
        new RollNewsRequest(getActivity(), view);

        initNewsList();
        return view;
    }


    public void initNewsList() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));

        adapter = new NewsAdapter(getContext(), list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if(list.get(position).getItemType()==0){//资讯信息
                    Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                    intent.putExtra("id", list.get(position).getId());
                    intent.putExtra("categoryType", list.get(position).getCategoryType());
                    getActivity().startActivity(intent);
                    AnimUtil.changePageIn(getActivity());
                }else  if(list.get(position).getItemType()==1){//视频信息
                    Intent intent = new Intent(getActivity(), PlayerActivity.class);
                    intent.putExtra("id", list.get(position).getId());
                    intent.putExtra("image", list.get(position).getImage());
                    intent.putExtra("video", list.get(position).getVideo());
                    getActivity().startActivity(intent);
                    AnimUtil.changePageIn(getActivity());
                }

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

    }

    /**
     * 初始化栏目数据
     */
    public void initColumn(final List<ClassifyEntity> list) {

        AllList = list;
        //网络获取内容数据
        newsListRequest = new NewsListRequest(getActivity());
        HashMap<String, String> map = newsListRequest.getMap();
        map.put("categoryId", "");
        newsListRequest.setMap(map);
        newsListRequest.setRefreshComplete(this);
        newsListRequest.http(false);

        linear = new LinearLayout(getActivity());
        linear.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_VERTICAL;

        int lrtextPad = (int) Util.dpToPx(getActivity(), 8);
        int tbtextPad = (int) Util.dpToPx(getActivity(), 2);
        int lrMargin = (int) Util.dpToPx(getActivity(), 4);
        int tbMargin = (int) Util.dpToPx(getActivity(), 4);
//        linear.setPadding(lrpx, tbpx, lrpx, tbpx);

        linear.setLayoutParams(params);
        for (int i = 0; i < list.size(); i++) {
            final String type = list.get(i).getType();
            if ("0".equals(type)) {//0为供求，2为新闻,3为供求二级列表,4为外链
                listOther.add(list.get(i));
                newsAndOther.add(list.get(i));
            } else if ("2".equals(type)) {
                listNews.add(list.get(i));
                newsAndOther.add(list.get(i));
            } else if ("3".equals(type)) {
                listOtherChild.add(list.get(i));
            }
            if ("0".equals(type) || "2".equals(type) || "4".equals(type)) {
                final TextView text = new TextView(getActivity());
                text.setText(list.get(i).getName());
                text.setTextSize(16);
                text.setTextColor(getResources().getColor(R.color.diy_title));
                text.setTag(i + 1);
                text.setPadding(lrtextPad, tbtextPad, lrtextPad, tbtextPad);
                LinearLayout.LayoutParams paramsTxt = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                paramsTxt.leftMargin = lrMargin;
                paramsTxt.rightMargin = lrMargin;
                paramsTxt.topMargin = tbMargin;
                paramsTxt.bottomMargin = tbMargin;
                text.setLayoutParams(paramsTxt);
                final String id = list.get(i).getId();

                text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ("0".equals(type)) {
                            if (pwMyPopWindow != null) {
                                if (pwMyPopWindow.isShowing()) {
                                    pwMyPopWindow.dismiss();// 关闭
                                } else {
                                    int margin = getResources().getDimensionPixelSize(R.dimen.popup_padding_left);
                                    pwMyPopWindow.showAsDropDown(line, margin, 0);// 显示
                                }
                            }
                        } else if ("4".equals(type)) {
                            Intent intent = new Intent();
                            intent.setAction("android.intent.action.VIEW");
                            Uri content_url = Uri.parse("http://baidu.com");
//                            Uri content_url = Uri.parse("http://mp.weixin.qq.com/mp/getmasssendmsg?__biz=MzI0NzUxNjk3OA==&wechat_redirect");
                            intent.setData(content_url);
                            startActivity(intent);
                        } else {
                            reset(v);
                            setClickAnim(text, 1.0f, 1.0f);
                            text.setTextColor(getResources().getColor(R.color.diy_green));
                            text.setBackgroundResource(R.drawable.background_red_line_rounded);
                            HashMap<String, String> map = newsListRequest.getMap();
                            if (id != null) map.put("categoryId", id);
                            newsListRequest.setMap(map);
                            newsListRequest.http(false);
                        }
                    }
                });
                linear.addView(text);
            }
        }
        TextView firstTv = (TextView) linear.getChildAt(0);
        firstTv.setTextColor(getResources().getColor(R.color.diy_green));
        firstTv.setBackgroundResource(R.drawable.background_red_line_rounded);
        setClickAnim(firstTv, 1.0f, 1.0f);
        horScrollView.addView(linear);
        //初始化二级列表
        iniPopupWindow();
    }

    private void iniPopupWindow() {
        final List<Map<String, Object>> moreList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("name", "全部");
        map.put("id", "");
        moreList.add(map);
        for (int i = 0; i < listOtherChild.size(); i++) {
            map = new HashMap<>();
            map.put("name", listOtherChild.get(i).getName());
            map.put("id", listOtherChild.get(i).getId());
            moreList.add(map);
        }
        //获取系统应用
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.task_detail_popupwindow, null); //获取listView
        lvPopupList = (ListView) layout.findViewById(R.id.lv_popup_list);
        pwMyPopWindow = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        lvPopupList.setAdapter(new SimpleAdapter(getContext(), moreList,
                R.layout.list_item_popupwindow, new String[]{"name"}, new int[]{R.id.tv_list_item}));
        lvPopupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //点击事件处理
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                pwMyPopWindow.dismiss();
                HashMap<String, String> map = newsListRequest.getMap();
                map.put("categoryId", (String) moreList.get(position).get("id"));
                newsListRequest.setMap(map);
                newsListRequest.http(false);

                TextView firstTv = (TextView) linear.getChildAt(0);
                reset(firstTv);
                firstTv.setTextColor(getResources().getColor(R.color.diy_green));
                firstTv.setBackgroundResource(R.drawable.background_red_line_rounded);
                setClickAnim(firstTv, 1.0f, 1.0f);
            }
        });
        ColorDrawable drawable = new ColorDrawable(getResources().getColor(R.color.diy_content));
        pwMyPopWindow.setBackgroundDrawable(drawable);
        pwMyPopWindow.setOutsideTouchable(true);

    }

    /**
     * 重置栏目字体效果
     *
     * @param v
     */
    private void reset(View v) {
        for (int i = 0; i < linear.getChildCount(); i++) {
            TextView tv = (TextView) linear.getChildAt(i);
            if (v.getTag() != tv.getTag()) {
                setClickAnim(tv, 1.0f, 1.0f);
                tv.setBackgroundColor(getActivity().getResources().getColor(R.color.diy_transparent));
                tv.setTextColor(getResources().getColor(R.color.diy_title));
            }
        }
    }

    /**
     * 栏目点击动画效果
     *
     * @param text
     * @param from
     * @param to
     */
    private void setClickAnim(TextView text, float from, float to) {

//        ObjectAnimator objectAnimX = ObjectAnimator.ofFloat(text, "scaleX", from, to);
//        ObjectAnimator objectAnimY = ObjectAnimator.ofFloat(text, "scaleY", from, to);
//        animatorSet = new AnimatorSet();
//        animatorSet.setDuration(300);
//        animatorSet.playTogether(objectAnimX, objectAnimY);
//        animatorSet.start();
    }

    //下拉刷新数据
    @Override
    public void pullToRefresh(List<NewsEntity> lst) {
        list.clear();
//        NewsEntity newsEntity=new NewsEntity();
//        newsEntity.setTitle("我就想试试好不好使行不行啊啊！");
//        newsEntity.setUpdateDate("2017-08-09 16:22");
//        newsEntity.setVideo("http://baobab.wdjcdn.com/145076769089714.mp4");
//        newsEntity.setStickTop("1");
//        newsEntity.setItemType(1);
//        lst.add(newsEntity);
        list.addAll(0,lst);
        adapter.notifyDataSetChanged();
        nothing.setVisibility(View.GONE);
    }

    @Override
    public void loadMore(List<NewsEntity> lst) {
        list.addAll(lst);
        adapter.notifyDataSetChanged();
        nothing.setVisibility(View.GONE);
    }

    @Override
    public void clearList() {
        list.clear();
        adapter.notifyDataSetChanged();
        nothing.setVisibility(View.VISIBLE);
    }

    public View getRootView() {
        return view;
    }

}
