package org.com.gannan.farminginfoplatform.selectiempic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import org.com.gannan.farminginfoplatform.R;
import org.com.gannan.farminginfoplatform.selectiempic.bean.PhotoInfo;
import org.com.gannan.farminginfoplatform.selectiempic.imageaware.RotateImageViewAware;
import org.com.gannan.farminginfoplatform.selectiempic.main.MyApplication;
import org.com.gannan.farminginfoplatform.selectiempic.util.ThumbnailsUtil;
import org.com.gannan.farminginfoplatform.selectiempic.util.UniversalImageLoadTool;

import java.util.List;


/**
 * 相片适配器
 * 
 */
public class PhotoAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<PhotoInfo> list;
	private ViewHolder viewHolder;
	private GridView gridView;
	private int width = MyApplication.getDisplayMetrics().widthPixels/3;

	public PhotoAdapter(Context context,List<PhotoInfo> list,GridView gridView){
		mInflater = LayoutInflater.from(context);
		this.list = list;
		this.gridView = gridView;
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
	/**
	 * 刷新view
	 * @param index
	 */
	public void refreshView(int index){
		int visiblePos = gridView.getFirstVisiblePosition();
		View view = gridView.getChildAt(index-visiblePos);
		ViewHolder holder = (ViewHolder)view.getTag();

		if(list.get(index).isChoose()){
			holder.selectImage.setImageResource(R.mipmap.pic_selected);
		}else{
			holder.selectImage.setImageResource(R.mipmap.pic_normal);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_selectphoto, null);
			viewHolder.image = (ImageView)convertView.findViewById(R.id.imageView);
			viewHolder.selectImage = (ImageView)convertView.findViewById(R.id.selectImage);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if(list.get(position).isChoose()){
			viewHolder.selectImage.setImageResource(R.mipmap.pic_selected);
		}else{
			viewHolder.selectImage.setImageResource(R.mipmap.pic_normal);
		}
		LayoutParams layoutParams = viewHolder.image.getLayoutParams();
		layoutParams.width = width;
		layoutParams.height = width;
		viewHolder.image.setLayoutParams(layoutParams);
		final PhotoInfo photoInfo = list.get(position);
		if(photoInfo!=null){
			UniversalImageLoadTool.disPlay(ThumbnailsUtil.MapgetHashValue(photoInfo.getImage_id(),photoInfo.getPath_file()),
					new RotateImageViewAware(viewHolder.image,photoInfo.getPath_absolute()), R.mipmap.pic_defalt_bg);
//			UniversalImageLoadTool.disPlay(ThumbnailsUtil.MapgetHashValue(photoInfo.getImage_id(),photoInfo.getPath_file()),
//					viewHolder.image, R.drawable.common_defalt_bg);
		}
		return convertView;
	}
	public class ViewHolder{
		public ImageView image;
		public ImageView selectImage;
	}
}
