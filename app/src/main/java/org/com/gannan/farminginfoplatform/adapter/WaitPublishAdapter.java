package org.com.gannan.farminginfoplatform.adapter;

import android.content.Context;

import org.com.gannan.farminginfoplatform.activity.WaitPublishListActivity;
import org.com.gannan.farminginfoplatform.baseadapter.base.MultiItemTypeAdapter;
import org.com.gannan.farminginfoplatform.entity.NewsEntity;

import java.util.List;

/**
 * Created by Administrator on 2016/11/15.
 */

public class WaitPublishAdapter extends MultiItemTypeAdapter<NewsEntity> {

    public WaitPublishAdapter(Context context, List<NewsEntity> data) {
        super(context, data);
        WaitPublishItemDelegate waitPublishItemDelegate= new WaitPublishItemDelegate();
        waitPublishItemDelegate.setCheckBoxListener((WaitPublishListActivity)context);
        addItemViewDelegate(waitPublishItemDelegate);
    }


}
