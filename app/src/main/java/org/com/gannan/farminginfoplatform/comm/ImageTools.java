package org.com.gannan.farminginfoplatform.comm;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 *
 *
 */
public final class ImageTools {
	/**
	 * 设置图片缩小比例
	 * @param bitmap
	 * @return
	 */
	public static Bitmap imageZoom(Bitmap bitmap) { 
        //图片允许最大空间1MB 
        int maxSize =1024; //单位：KB
        //将字节换成KB 
        int mid = bitmapSize(bitmap); 
        //判断bitmap占用空间是否大于允许最大空间  如果大于则压缩 小于则不压缩 
        if (mid > maxSize) {
            //需要缩小的倍数
            int i = mid / maxSize; 
             //zoomBitmap(bitmap, bitmap.getWidth() / Math.sqrt(i), bitmap.getHeight() / Math.sqrt(i)); 
             double width=bitmap.getWidth() / Math.sqrt(i);
             double height=bitmap.getHeight() / Math.sqrt(i);
             int w = bitmap.getWidth();
     		int h = bitmap.getHeight();
     		Matrix matrix = new Matrix();
     		float scaleWidth = ((float) width / w);
     		float scaleHeight = ((float) height / h);       
     		matrix.postScale(scaleWidth, scaleHeight);
     		bitmap=Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        }
       return bitmap;
        
	} 
	/**
	 * 设置图片宽高大小
	 * @param bitmap
	 * @param width
	 * @param height
	 * @return
	 */
	public static void zoomBitmap(Bitmap bitmap, double width, double height) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) width / w);
		float scaleHeight = ((float) height / h);       
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitMap = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		
	}
	/**
	 * 图片重命名
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String createPhotoFileName() {
		String fileName = "";
		Date date = new Date(System.currentTimeMillis());  //系统当前时间
		String dateFormat = new SimpleDateFormat("yyyyMMddHHmmss").format(date);
		for(int i = 0; i < 6; i ++) {
			dateFormat += String.valueOf((int)(Math.random()*10));
		}
		fileName = dateFormat+".jpg";
		return fileName;
	}
	/**
	 * 获取文件路径
	 * @param uri
	 * @param activity
	 * @return
	 */
	 public static String uri2filePath(Uri uri,Activity activity)  
	    {  
	        String[] projection = {MediaStore.Images.Media.DATA };  
	        @SuppressWarnings("deprecation")
			Cursor cursor = activity.managedQuery(uri, projection, null, null, null);  
	        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);  
	        cursor.moveToFirst();  
	        String path =  cursor.getString(column_index);
	        return path;  
	    }  
	 /**
	  * 保存图片到本应用下
	  * @param bitmap
	  * @param activity
	  */
	public static void savePicture(Bitmap bitmap,String fileName,Activity activity) {
		
			FileOutputStream fos =null;
			try {
				//直接写入名称即可，没有会被自动创建；私有：只有本应用才能访问，重新内容写入会被覆盖
				fos = activity.openFileOutput(fileName, Context.MODE_PRIVATE); 
				bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);// 把图片写入指定文件夹中
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if(null != fos) {
						fos.flush();
						fos.close();
						fos = null;
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	  /**
     * 保存文件到指定路径
     * @param bitmap
     * @param fileName
     */
    public static void saveFile(Bitmap bitmap, String fileName,String path,Activity activity){
    	BufferedOutputStream bos=null;
    	try {
        File dirFile = new File(path);
        if(!dirFile.exists()){
            dirFile.mkdir();
        }
        File picFile = new File(path,fileName);
         bos= new BufferedOutputStream(new FileOutputStream(picFile));
         bitmap.compress(Bitmap.CompressFormat.JPEG, 60, bos);
         bos.flush();
		bos.close();
		bos = null;
			//发送广播，更新相册
//         Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//         Uri uri = Uri.fromFile(picFile);
//         intent.setData(uri);
//         activity.sendBroadcast(intent);
    	} catch (Exception e) {
			e.printStackTrace();
		}
    }
/**
 * 图片占内存字节大小
 * @param data
 * @return
 */
	@SuppressLint("NewApi")
	public static int bitmapSize(Bitmap data) {
		
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
			return data.getRowBytes() * data.getHeight()/1024;
		} else {
			return data.getByteCount()/1024;
		}
	}

	public long getSDFreeSize(){  
	     //取得SD卡文件路径  
	     File path = Environment.getExternalStorageDirectory();   
	     StatFs sf = new StatFs(path.getPath());   
	     //获取单个数据块的大小(Byte)  
	     long blockSize = sf.getBlockSize();   
	     //空闲的数据块的数量  
	     long freeBlocks = sf.getAvailableBlocks();  
	     //返回SD卡空闲大小  
	     //return freeBlocks * blockSize;  //单位Byte  
	     //return (freeBlocks * blockSize)/1024;   //单位KB  
	     return (freeBlocks * blockSize)/1024 /1024; //单位MB  
	   }      
	public long getSDAllSize(){  
	     //取得SD卡文件路径  
	     File path = Environment.getExternalStorageDirectory();   
	     StatFs sf = new StatFs(path.getPath());   
	     //获取单个数据块的大小(Byte)  
	     long blockSize = sf.getBlockSize();   
	     //获取所有数据块数  
	     long allBlocks = sf.getBlockCount();  
	     //返回SD卡大小  
	     //return allBlocks * blockSize; //单位Byte  
	     //return (allBlocks * blockSize)/1024; //单位KB  
	     return (allBlocks * blockSize)/1024/1024; //单位MB  
	   }      
}
