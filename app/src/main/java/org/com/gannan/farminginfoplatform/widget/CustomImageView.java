package org.com.gannan.farminginfoplatform.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import org.com.gannan.farminginfoplatform.comm.ImageTools;

/**
 * 自定义View，实现圆角，圆形等效果
 */
public class CustomImageView extends ImageView {

	// 图片
	private Bitmap mSrc;
	public CustomImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CustomImageView(Context context) {
		this(context, null);
	}

	public void setCustomImageView(Context context, Bitmap mSrc) {
		this.mSrc = mSrc;
	}

	public CustomImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	/**
	 * 绘制
	 */
	@Override
	protected void onDraw(Canvas canvas) {

		Drawable drawable = getDrawable();
		if (drawable == null) {
			return;
		}
		Bitmap b = null;
		if (drawable instanceof BitmapDrawable) {
			b = ((BitmapDrawable) drawable).getBitmap();
		}else{
			return;
		}
		//图片压缩
		mSrc = ImageTools.imageZoom(b.copy(Config.ARGB_8888, true));
		// 想要设置图片的宽度
		int mWidth = getWidth();
		canvas.drawBitmap(createScaledBitmap(mSrc, mWidth, mWidth), 0, 0, null);
	}
	public  Bitmap createScaledBitmap(Bitmap unscaledBitmap, int dstWidth, int dstHeight) {
		  Rect srcRect = calculateSrcRect(unscaledBitmap.getWidth(), unscaledBitmap.getHeight(), dstWidth, dstHeight);
//		  Rect dstRect = calculateDstRect(unscaledBitmap.getWidth(), unscaledBitmap.getHeight(), dstWidth, dstHeight);//原比例剪切
		 Rect dstRect =  new Rect(0, 0, dstWidth, dstHeight);//最窄边为边长，剪切正方形
		 
		  Bitmap scaledBitmap = Bitmap.createBitmap(dstWidth, dstRect.height(), Config.ARGB_8888);
		   Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
		   //抗锯齿
			paint.setAntiAlias(true);
			Canvas canvas = new Canvas(scaledBitmap);
			 //首先绘制圆形
			canvas.drawCircle(dstWidth / 2, dstWidth / 2, dstWidth / 2,paint);
			 // 使用SRC_IN，参考上面的说明
			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		  canvas.drawBitmap(unscaledBitmap, srcRect, dstRect, paint);
		  return scaledBitmap;
	}
	public  Rect calculateSrcRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight) {
		    final float srcAspect = (float)srcWidth / (float)srcHeight;
		    final float dstAspect = (float)dstWidth / (float)dstHeight;
		    if (srcAspect > dstAspect) {
		      final int srcRectWidth = (int)(srcHeight * dstAspect);
		      final int srcRectLeft = (srcWidth - srcRectWidth) / 2;
		      return new Rect(srcRectLeft, 0, srcRectLeft + srcRectWidth, srcHeight);
		    } else {
		      final int srcRectHeight = (int)(srcWidth / dstAspect);
		      final int scrRectTop = (int)(srcHeight - srcRectHeight) / 2;
		      return new Rect(0, scrRectTop, srcWidth, scrRectTop + srcRectHeight);
		    }
		}
		public static Rect calculateDstRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight) {
		    final float srcAspect = (float)srcWidth / (float)srcHeight;
		    final float dstAspect = (float)dstWidth / (float)dstHeight;
		    if (srcAspect > dstAspect) {
		      return new Rect(0, 0, dstWidth, (int)(dstWidth / srcAspect));
		    } else {
		      return new Rect(0, 0, (int)(dstHeight * srcAspect), dstHeight);
		    }
		}
}
