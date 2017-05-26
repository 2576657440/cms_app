package org.com.gannan.farminginfoplatform.adapter;

import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.com.gannan.farminginfoplatform.R;
import org.com.gannan.farminginfoplatform.baseadapter.base.ItemViewDelegate;
import org.com.gannan.farminginfoplatform.baseadapter.base.ViewHolder;
import org.com.gannan.farminginfoplatform.comm.Global;
import org.com.gannan.farminginfoplatform.comm.ImageLoaderComm;
import org.com.gannan.farminginfoplatform.entity.NewsEntity;
import org.com.gannan.farminginfoplatform.utils.DateUtils;

/**
 * Created by Administrator on 2016/11/16.
 */

public class HistoryItemDelegate implements ItemViewDelegate<NewsEntity> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_history_list;
    }

    @Override
    public int forViewType(NewsEntity item, int position) {
        return item.getItemType();
    }

    @Override
    public void convert(ViewHolder holder, NewsEntity newsEntity, int position) {
        holder.setText(R.id.textView1, newsEntity.getTitle());
        holder.setText(R.id.textView2,newsEntity.getContent());
        holder.setText(R.id.textView3, newsEntity.getInfoArea());
        holder.setText(R.id.textView4, DateUtils.dateFormatDIY(newsEntity.getUpdateDate()));
        if ("0".equals(newsEntity.getState())) {
            holder.setText(R.id.textView5, "交易成功");
        } else if ("1".equals(newsEntity.getState())||"2".equals(newsEntity.getState())||"3".equals(newsEntity.getState())) {
            holder.setText(R.id.textView5, "待审核");
        } else if ("4".equals(newsEntity.getState())) {
            holder.setText(R.id.textView5, "已发布");
        } else if ("5".equals(newsEntity.getState())) {
            holder.setText(R.id.textView5, "被驳回");
        } else if ("6".equals(newsEntity.getState())) {
            holder.setText(R.id.textView5, "已下架");
        }

        if (newsEntity.getImage() != null && !"".equals(newsEntity.getImage())) {
            String[] path = newsEntity.getImage().split("\\|");
            if (Global.URL + path[1] != null) {
                ImageLoader.getInstance().displayImage(Global.URL+path[1],
                        (ImageView) holder.getView(R.id.imageView1), ImageLoaderComm.getImageOptions());

            }
            holder.setVisible(R.id.imageView1, true);
        } else {
            holder.setVisible(R.id.imageView1, false);
        }
    }

}
