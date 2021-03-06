package org.com.gannan.farminginfoplatform.selectiempic.util;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

public class UniversalImageLoadTool {

	private static ImageLoader imageLoader = ImageLoader.getInstance();
	
	public static ImageLoader getImageLoader(){
		return imageLoader;
	}
	public static boolean checkImageLoader(){
		return imageLoader.isInited();
	}
	
	public static void disPlay(String uri, ImageAware imageAware,int default_pic){
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.showImageOnLoading(default_pic)
		.showImageForEmptyUri(default_pic)
		.showImageOnFail(default_pic)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.displayer(new SimpleBitmapDisplayer())
		.build();

		imageLoader.displayImage(uri, imageAware, options);
	}
	public static void disPlay(String uri, ImageView imageView, int default_pic){
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(default_pic)
				.showImageForEmptyUri(default_pic)
				.showImageOnFail(default_pic)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new SimpleBitmapDisplayer())
				.build();

		imageLoader.displayImage(uri, imageView, options);
	}
	public static void clear(){
		imageLoader.clearMemoryCache();		
		imageLoader.clearDiskCache();
	}
	
	public static void resume(){
		imageLoader.resume();
	}
	/**
	 * 暂停加载
	 */
	public static void pause(){
		imageLoader.pause();
	}
	/**
	 * 停止加载
	 */
	public static void stop(){
		imageLoader.stop();
	}
	/**
	 * 销毁加载
	 */
	public static void destroy() {
		imageLoader.destroy();
	}
}
