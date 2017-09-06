package demo.optimizel.dn.com.myqqc60.AccessCamera.Camera.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class BitmapUtils {

	public static Bitmap scaleBitmap(Bitmap bm, float newWidth, float newHeight) {
		// 获得图片的宽高
		int width = bm.getWidth();
		int height = bm.getHeight();
		// 计算缩放比例
		float scaleWidth = newWidth / width;
		float scaleHeight = newHeight / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		bm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);

		return bm;
	}

	public static Bitmap decodeSampledBitmapFromResource(Resources res,
														 int resId, int reqWidth, int reqHeight) {

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inPurgeable = true;
		BitmapFactory.decodeResource(res, resId, options);
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		options.inJustDecodeBounds = false;
		try {
			return BitmapFactory.decodeResource(res, resId, options);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Bitmap decodeSampledBitmapFromFile(String filename,
													 int reqWidth, int reqHeight) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inPurgeable = true;
		BitmapFactory.decodeFile(filename, options);
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		options.inJustDecodeBounds = false;
		try {
			return BitmapFactory.decodeFile(filename, options);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			return null;
		}
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}

			final float totalPixels = width * height;

			final float totalReqPixelsCap = reqWidth * reqHeight * 2;

			while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
				inSampleSize++;
			}
		}
		return inSampleSize;
	}


	public static void savePic(Bitmap bitmap, String picPath,
							   CompressFormat imgType) {
		// 图片叠加处理
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(new File(picPath));
			bitmap.compress(imgType, 100, fos);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 在原图片上写上时间，生成新的图片
	 *
	 * @param orgPic
	 *            原图片路径
	 * @param newPic
	 *            新图片路径
	 * @return bitmap 生成的新图片的bitmap
	 */
	public static Bitmap drawNewPic(String orgPic, String newPic, String text,
									int[] xy) {
		if (!TextUtils.isEmpty(orgPic) && !TextUtils.isEmpty(newPic)) {
			Bitmap bm = BitmapFactory.decodeFile(orgPic);
			int width = bm.getWidth(), hight = bm.getHeight();
			Bitmap newBm = Bitmap.createBitmap(width, hight,
					Config.ARGB_8888); // 建立一个空的BItMap
			Canvas canvas = new Canvas(newBm);// 初始化画布绘制的图像到icon上

			Paint photoPaint = new Paint(); // 建立画笔
			photoPaint.setDither(true); // 获取跟清晰的图像采样
			photoPaint.setFilterBitmap(true);// 过滤一些

			canvas.drawBitmap(bm, 0, 0, photoPaint);// 将photo 缩放或则扩大到
													// dst使用的填充区photoPaint
			if (!TextUtils.isEmpty(text)) {
				Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG
						| Paint.DEV_KERN_TEXT_FLAG);// 设置画笔
				textPaint.setTextSize(20.0f);// 字体大小
				textPaint.setTypeface(Typeface.DEFAULT_BOLD);// 采用默认的宽度
				textPaint.setColor(Color.WHITE);// 采用的颜色
				canvas.drawText(text, xy[0], xy[1], textPaint);// 绘制上去字，开始未知x,y采用那只笔绘制
			}

			canvas.save(Canvas.ALL_SAVE_FLAG);
			canvas.restore();

			// 保存图片到指定路径
			savePic(newBm, newPic, CompressFormat.PNG);
			bm.recycle();
			return newBm;
		} else {
			return null;
		}

	}

	/*
	 * 生成圆角图像，roundPx值越大角度越明显
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		// 得到画布
		Canvas canvas = new Canvas(output);
		// 将画布的四角圆化
		final int color = Color.RED;
		final Paint paint = new Paint();
		// 得到与图像相同大小的区域 由构造的四个值决定区域的位置以及大小
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		// drawRoundRect的第2,3个参数一样则画的是正圆的一角，如果数值不同则是椭圆的一角
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	/*
	 * 图像打开时根据图片文件大小适当的软缩放
	 */
	public static Bitmap decodeFile(String path) {
		if (path == null || path.length() < 1)
			return null;
		File file = new File(path);
//		Bitmap resizeBmp = null;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inPreferredConfig = Config.RGB_565;
		opts.inPurgeable = true;
		opts.inInputShareable = true;
		// 数字越大读出的图片占用的heap越小 不然总是溢出
		if (file.length() < 512*1024) { // 512k
			opts.inSampleSize = 1;
		} else if (file.length() < 1024*1024) { // 1M
			opts.inSampleSize = 2;
		} else if (file.length() < 2*1024*1024) { // 2M
			opts.inSampleSize = 4;
		} else if (file.length() < 4*1024*1024) { // 4M
			opts.inSampleSize = 8;
		} else {
			opts.inSampleSize = 16;
		}
		try {
			return BitmapFactory.decodeFile(file.getPath(), opts);
		} catch (OutOfMemoryError e) {
			return null;
		}
//		resizeBmp = BitmapFactory.decodeFile(file.getPath(), opts);
//		return resizeBmp;
	}
	public static Bitmap decodePngFile(String filePath){
		if (filePath == null || filePath.length() < 1)
			return null;
		Bitmap bm = BitmapFactory.decodeFile(filePath);
        return bm ;
	}

	/*
	 * 将任意尺寸图片压缩后居中裁剪,如将480*640裁剪为480*480
	 */
	public static Bitmap cropBitmapBySize(Bitmap bitmap, int width, int height) {
		int oldWidth = bitmap.getWidth();
		int oldHeight = bitmap.getHeight();
		if (oldWidth != width || oldHeight != height) {
			float oldScale = (float) oldWidth / oldHeight;
			float newScale = (float) width / height;
			if (oldScale < newScale) {
				float newHeight = oldHeight * width / oldWidth;
				bitmap = scaleBitmap(bitmap, width, newHeight);
				bitmap = Bitmap.createBitmap(bitmap, 0,
						(int) (newHeight - height) / 2, width, height);
			} else {
				float newWidth = oldWidth * height / oldHeight;
				bitmap = scaleBitmap(bitmap, newWidth, height);
				bitmap = Bitmap.createBitmap(bitmap,
						(int) (oldWidth - width) / 2, 0, width, height);
			}
		}
		return bitmap;
	}

	/*
	 * 图像放大
	 */
	public static Bitmap bitmapBig(Bitmap bitmap) {
		Matrix matrix = new Matrix();
		matrix.postScale(10.0f, 10.0f); // 长和宽放大缩小的比例
		Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		return resizeBmp;
	}

	/*
	 * 图像缩小
	 */
	public static Bitmap bitmapSmall(Bitmap bitmap) {
		Matrix matrix = new Matrix();
		matrix.postScale(0.1f, 0.1f); // 长和宽放大缩小的比例
		Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		return resizeBmp;
	}

	/*
 * 图像缩小
 */
	public static Bitmap bitmapSmall(Bitmap bitmap, float scale) {
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale); // 长和宽放大缩小的比例
		Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		return resizeBmp;
	}

	/*
	 * 图像虚化
	 */
	public static Bitmap fastBlur(Bitmap sentBitmap, int radius) {
		sentBitmap = bitmapSmall(sentBitmap);

		Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
		if (radius < 1) {
			return (null);
		}
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		int[] pix = new int[w * h];
		bitmap.getPixels(pix, 0, w, 0, 0, w, h);

		int wm = w - 1;
		int hm = h - 1;
		int wh = w * h;
		int div = radius + radius + 1;

		int r[] = new int[wh];
		int g[] = new int[wh];
		int b[] = new int[wh];
		int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
		int vmin[] = new int[Math.max(w, h)];

		int divsum = (div + 1) >> 1;
		divsum *= divsum;
		int dv[] = new int[256 * divsum];
		for (i = 0; i < 256 * divsum; i++) {
			dv[i] = (i / divsum);
		}

		yw = yi = 0;

		int[][] stack = new int[div][3];
		int stackpointer;
		int stackstart;
		int[] sir;
		int rbs;
		int r1 = radius + 1;
		int routsum, goutsum, boutsum;
		int rinsum, ginsum, binsum;

		for (y = 0; y < h; y++) {
			rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
			for (i = -radius; i <= radius; i++) {
				p = pix[yi + Math.min(wm, Math.max(i, 0))];
				sir = stack[i + radius];
				sir[0] = (p & 0xff0000) >> 16;
				sir[1] = (p & 0x00ff00) >> 8;
				sir[2] = (p & 0x0000ff);
				rbs = r1 - Math.abs(i);
				rsum += sir[0] * rbs;
				gsum += sir[1] * rbs;
				bsum += sir[2] * rbs;
				if (i > 0) {
					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];
				} else {
					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];
				}
			}
			stackpointer = radius;

			for (x = 0; x < w; x++) {

				r[yi] = dv[rsum];
				g[yi] = dv[gsum];
				b[yi] = dv[bsum];

				rsum -= routsum;
				gsum -= goutsum;
				bsum -= boutsum;

				stackstart = stackpointer - radius + div;
				sir = stack[stackstart % div];

				routsum -= sir[0];
				goutsum -= sir[1];
				boutsum -= sir[2];

				if (y == 0) {
					vmin[x] = Math.min(x + radius + 1, wm);
				}
				p = pix[yw + vmin[x]];

				sir[0] = (p & 0xff0000) >> 16;
				sir[1] = (p & 0x00ff00) >> 8;
				sir[2] = (p & 0x0000ff);

				rinsum += sir[0];
				ginsum += sir[1];
				binsum += sir[2];

				rsum += rinsum;
				gsum += ginsum;
				bsum += binsum;

				stackpointer = (stackpointer + 1) % div;
				sir = stack[(stackpointer) % div];

				routsum += sir[0];
				goutsum += sir[1];
				boutsum += sir[2];

				rinsum -= sir[0];
				ginsum -= sir[1];
				binsum -= sir[2];

				yi++;
			}
			yw += w;
		}
		for (x = 0; x < w; x++) {
			rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
			yp = -radius * w;
			for (i = -radius; i <= radius; i++) {
				yi = Math.max(0, yp) + x;

				sir = stack[i + radius];

				sir[0] = r[yi];
				sir[1] = g[yi];
				sir[2] = b[yi];

				rbs = r1 - Math.abs(i);

				rsum += r[yi] * rbs;
				gsum += g[yi] * rbs;
				bsum += b[yi] * rbs;

				if (i > 0) {
					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];
				} else {
					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];
				}

				if (i < hm) {
					yp += w;
				}
			}
			yi = x;
			stackpointer = radius;
			for (y = 0; y < h; y++) {
				// Preserve alpha channel: ( 0xff000000 & pix[yi] )
				pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16)
						| (dv[gsum] << 8) | dv[bsum];

				rsum -= routsum;
				gsum -= goutsum;
				bsum -= boutsum;

				stackstart = stackpointer - radius + div;
				sir = stack[stackstart % div];

				routsum -= sir[0];
				goutsum -= sir[1];
				boutsum -= sir[2];

				if (x == 0) {
					vmin[y] = Math.min(y + r1, hm) * w;
				}
				p = x + vmin[y];

				sir[0] = r[p];
				sir[1] = g[p];
				sir[2] = b[p];

				rinsum += sir[0];
				ginsum += sir[1];
				binsum += sir[2];

				rsum += rinsum;
				gsum += ginsum;
				bsum += binsum;

				stackpointer = (stackpointer + 1) % div;
				sir = stack[stackpointer];

				routsum += sir[0];
				goutsum += sir[1];
				boutsum += sir[2];

				rinsum -= sir[0];
				ginsum -= sir[1];
				binsum -= sir[2];

				yi += w;
			}
		}

		bitmap.setPixels(pix, 0, w, 0, 0, w, h);

//		bitmap = bitmapBig(bitmap);
		return (bitmap);
	}

	/*
	 * 图像压缩
	 */
	public static Bitmap compressImage(Bitmap image, int fileSize) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(CompressFormat.PNG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > fileSize&&options>0) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(CompressFormat.PNG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	/***
	 * 裁剪图片成圆形
	 * @param bitmap
	 * @return
	 */
	public static Bitmap toRoundBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}

		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right,
				(int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top,
				(int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);

		paint.setAntiAlias(true);

		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);
		return output;
	}

	public static int readPictureDegree(String path) {
       int degree  = 0;
       try {
           ExifInterface exifInterface = new ExifInterface(path);
           int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
           switch (orientation) {
           case ExifInterface.ORIENTATION_ROTATE_90:
                   degree = 90;
                   break;
           case ExifInterface.ORIENTATION_ROTATE_180:
                   degree = 180;
                   break;
           case ExifInterface.ORIENTATION_ROTATE_270:
                   degree = 270;
                   break;
           }
       } catch (IOException e) {
           e.printStackTrace();
       }
       return degree;
   }

	public static Bitmap rotaingImageView(int angle , Bitmap bitmap) {
        //旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		bitmap.recycle();
        return resizedBitmap;
    }


	public static byte[] bitmap2Bytes(final Bitmap bmp, final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}
		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	@SuppressWarnings("null")
//	public static void url2Bitmap(String url, final UrlToBitmapInfo bitmapInfo){
//		new AsyncTask<String, Void, Bitmap>(){
//			@Override
//			protected Bitmap doInBackground(String... params) {
//				Bitmap bitmap = null;
//				URL mUrl = null;
//				HttpURLConnection conn = null;
//				try {
//					mUrl = new URL(params[0]);
//					conn = (HttpURLConnection) mUrl.openConnection();
//				} catch (MalformedURLException e) {
//					e.printStackTrace();
//				}catch (IOException e) {
//					e.printStackTrace();
//				}
//				conn.setConnectTimeout(5000);
//				InputStream is = null;
//				try {
//					is = conn.getInputStream();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//				bitmap= BitmapFactory.decodeStream(is);
//				bitmap = BitmapUtils.compressImage(bitmap, 16);
//				return bitmap;
//			}
//
//			@SuppressWarnings("null")
//			@Override
//			protected void onPostExecute(Bitmap result) {
//				bitmapInfo.setBitmap(result);
//			}
//		}.execute(url);
//	}
	public static Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
		}

	/**
	 * 按正方形裁切图片
	 */
	public static Bitmap ImageCrop(Bitmap bitmap) {
		int w = bitmap.getWidth(); // 得到图片的宽，高
		int h = bitmap.getHeight();

		int wh = w > h ? h : w;// 裁切后所取的正方形区域边长

		int retX = w > h ? (w - h) / 2 : 0;//基于原图，取正方形左上角x坐标
		int retY = w > h ? 0 : (h - w) / 2;

		//下面这句是关键
		return Bitmap.createBitmap(bitmap, retX, retY, wh, wh, null, false);
	}
}
