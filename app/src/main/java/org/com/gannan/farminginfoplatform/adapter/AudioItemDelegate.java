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

public class AudioItemDelegate implements ItemViewDelegate<NewsEntity> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_news_audio_list;
    }

    @Override
    public int forViewType(NewsEntity item, int position) {
        return item.getItemType();
    }

    @Override
    public void convert(ViewHolder holder, NewsEntity newsEntity, int position) {
        holder.setText(R.id.textView1,newsEntity.getTitle());//标题
//        holder.setText(R.id.textView3,newsEntity.getInfoArea());
        holder.setText(R.id.textView3, DateUtils.dateFormatDIY(newsEntity.getUpdateDate()));//日期
        //（0：已经置顶；1：未置顶）
        if(newsEntity.getStickTop()!=null&&"0".equals(newsEntity.getStickTop())){
            holder.setText(R.id.textView2,"置顶");
            holder.setVisible(R.id.textView2,true);
        }else {
            holder.setText(R.id.textView2,"");
            holder.setVisible(R.id.textView2,false);
        }
        if(newsEntity.getImage()!=null&&!"".equals(newsEntity.getImage())){
            String[] path=newsEntity.getImage().split("\\|");
            if(Global.URL+path[1]!=null){
                ImageLoader.getInstance().displayImage(Global.URL+path[1],
                        (ImageView) holder.getView(R.id.imageView1), ImageLoaderComm.getImageOptions());
            }
        }
    }

}
