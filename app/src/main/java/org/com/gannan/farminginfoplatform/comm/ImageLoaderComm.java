package org.com.gannan.farminginfoplatform.comm;

import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.com.gannan.farminginfoplatform.R;

import java.io.File;

/**
 * Created by Administrator on 2016/12/2.
 */

public class ImageLoaderComm {

    public static ImageLoaderConfiguration getImageConfig(Context context) {
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, "images");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(context)
                .memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽
                .threadPoolSize(3)//线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(30 * 1024 * 1024)) //被使用次数少的先被删除
//                .memoryCache(new WeakMemoryCache()) //弱引用
                .memoryCacheSize(10 * 1024 * 1024)
//                .diskCacheSize(50 * 1024 * 1024)
//                .diskCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
//                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .diskCacheFileCount(100) //缓存的文件数量
                .diskCache(new UnlimitedDiskCache(cacheDir))//自定义缓存路径
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(context, 30 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                .writeDebugLogs() // Remove for release app
                .build();
        return config;
    }

    public static DisplayImageOptions getImageOptions() {
        DisplayImageOptions options;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.color.diyMainBg) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.color.diyMainBg)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.color.diyMainBg)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
//                .decodingOptions(new DecodingOptions())//设置图片的解码配置
                .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
//                .displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
                .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
                .build();
        return options;
    }
    public static DisplayImageOptions getRoundedImageOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.def) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.def)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.def)  //设置图片加载/解码过程中错误时候显示的图片
                .displayer(new RoundedBitmapDisplayer(30))//是否设置为圆角，弧度为多少

                .build();
        return options;
    }
}
