package org.com.gannan.farminginfoplatform.selectiempic.util;

import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**    
 */
public class CheckImageLoaderConfiguration {
	
	private static final int discCacheLimitTime = 3600*24*15;

	public static void checkImageLoaderConfiguration(Context context){
		if(!UniversalImageLoadTool.checkImageLoader()){
			// This configuration tuning is custom. You can tune every option, you may tune some of them,
			// or you can create default configuration by
			// ImageLoaderConfiguration.createDefault(this);
			// method.
			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
					.threadPriority(Thread.NORM_PRIORITY)
					.denyCacheImageMultipleSizesInMemory()
					.discCacheFileNameGenerator(new Md5FileNameGenerator())
					.diskCacheSize(discCacheLimitTime)
					.tasksProcessingOrder(QueueProcessingType.LIFO)
					.build();
			// Initialize ImageLoader with configuration.
			ImageLoader.getInstance().init(config);
		}
	}
}
