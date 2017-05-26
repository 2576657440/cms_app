package org.com.gannan.farminginfoplatform.adapter;

import android.content.Context;

import org.com.gannan.farminginfoplatform.baseadapter.base.MultiItemTypeAdapter;
import org.com.gannan.farminginfoplatform.entity.NewsEntity;

import java.util.List;

/**
 * Created by Administrator on 2016/11/15.
 */

public class NewsAdapter extends MultiItemTypeAdapter<NewsEntity> {

    public NewsAdapter(Context context, List<NewsEntity> data) {
        super(context, data);
        addItemViewDelegate(0,new NewsItemDelegate());
        addItemViewDelegate(1,new AudioItemDelegate());
    }


}
