package org.com.gannan.farminginfoplatform.adapter;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

public class WaitPublishItemDelegate implements ItemViewDelegate<NewsEntity> {
    public CheckBoxListener checkBoxListener;

    public interface CheckBoxListener {
        void changeListValue(int position, boolean isChecked);
    }

    public CheckBoxListener getCheckBoxListener() {
        return checkBoxListener;
    }

    public void setCheckBoxListener(CheckBoxListener checkBoxListener) {
        this.checkBoxListener = checkBoxListener;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_wait_publish_list;
    }

    @Override
    public int forViewType(NewsEntity item, int position) {
        return item.getItemType();
    }

    @Override
    public void convert(ViewHolder holder, NewsEntity newsEntity, final int position) {
        holder.setText(R.id.textView1, newsEntity.getTitle());
        holder.setText(R.id.textView2, newsEntity.getContent());
        holder.setText(R.id.textView3, newsEntity.getInfoArea());
        holder.setText(R.id.textView4, DateUtils.dateFormatDIY(newsEntity.getUpdateDate()));
        CheckBox checkBox = holder.getView(R.id.checkBox1);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkBoxListener != null) {
                    checkBoxListener.changeListValue(position, isChecked);
                }
            }
        });

        if (newsEntity.getIsCheck() == 0) {
            checkBox.setVisibility(View.GONE);
            checkBox.setChecked(false);
        } else if (newsEntity.getIsCheck() == 1) {
            checkBox.setVisibility(View.VISIBLE);
            checkBox.setChecked(false);
        } else if (newsEntity.getIsCheck() == 2) {
            checkBox.setChecked(true);
            checkBox.setVisibility(View.VISIBLE);
        }
        if (newsEntity.getImage() != null && !"".equals(newsEntity.getImage())) {
            String[] path = newsEntity.getImage().split("\\|");
            if (Global.URL + path[1] != null) {
                ImageLoader.getInstance().displayImage(Global.URL + path[1],
                        (ImageView) holder.getView(R.id.imageView1), ImageLoaderComm.getImageOptions());
            }
            holder.setVisible(R.id.imageView1, true);
        } else {
            holder.setVisible(R.id.imageView1, false);
        }
    }

}
