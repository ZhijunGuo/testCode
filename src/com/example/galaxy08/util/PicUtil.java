/**
 * 
 */
package com.example.galaxy08.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.example.galaxy08.R;
import com.example.galaxy08.SysApplication;
import com.example.galaxy08.finals.PictureStorage;
import com.example.galaxy08.picture.PictureSize;
import com.example.galaxy08.tool.DebugLog;
import com.example.galaxy08.tool.ToastUtil;
import com.example.galaxy08.tool.Tool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.os.Environment;

//import com.jingwei.mobile.SysApplication;

/**
 * 
 * 
 * @author
 * 
 */
public class PicUtil {
	private static File dir = null;
	public static final int CAMERA_COMPRESS_RATIO = 80;
	public static int UNCONSTRAINED = -1;
	public static final int PREVIEW_IMAGE = 1105;
	public static File getPhotoDir() {
		if (dir == null) {
			if (Environment.MEDIA_MOUNTED.equals(Environment
					.getExternalStorageState())) {
				dir = new File(PictureStorage.ARECORD_DIR);
				if (!dir.exists())
					dir.mkdirs();
			}
		}

		return dir;
	}

	public static File getPhotoFile(Context context) {
		File dir;
		String file_name = Tool.nowTime() + ".jpg";
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			dir = new File(PictureStorage.ARECORD_DIR);
			if (!dir.exists()) {
				dir.mkdirs();
			}
		} else {
			ToastUtil
					.showMessage(context, context.getString(R.string.nosdcard));
			return null;
		}
		File file = new File(dir, file_name);
		DebugLog.logd("context= " + context.toString() + " fileName="
				+ file.getAbsolutePath());
		return file;
	}

	public static File prepareMyPhoto(Context context, File file) {
		return prepareMyPhoto(context, file, PictureSize.S_CARD_WIDTH,
				PictureSize.S_CARD_HEIGHT);
	}

	public static File prepareMyPhoto(Context context, File file, int maxWidth,
			int maxHeight) {
		File myFile = null;
		DebugLog.logd("prepareMyPhoto()");
		Bitmap bitmap = null;
		long start = System.currentTimeMillis();
		DebugLog.logd("Cost time", "prepareMyPhoto start");
		try {
			File dir = getPhotoDir();
			if (dir == null) {
				// ToastUtil.makeText(context,
				// context.getString(R.string.nosdcard),
				// 0).show();
				ToastUtil.showMessage(context,
						context.getString(R.string.nosdcard));
				return null;
			}
			String file_name = Tool.nowTime() + ".jpg";
			myFile = new File(dir, file_name);
			// FileInputStream fis = new
			// FileInputStream(file.getAbsolutePath());
			// // BufferedInputStream bis = new BufferedInputStream(fis);
			// byte[] bytes = new byte[fis.available()];
			// fis.read(bytes);
			/*
			 * // for mengtian test begin // getTiffFile(dir, file_name, bytes);
			 * String mengtian_name = file_name.replace(".jpg", ".tiff"); mtFile
			 * = new File(dir, mengtian_name); int getResizeRatio =
			 * BCRCloudAPI.getResizeRatio(bytes); int rtnCode =
			 * JNISDK_TIFF.Binary2TiffByBuffer(bytes, bytes.length,
			 * getResizeRatio, mtFile.getAbsolutePath().getBytes());
			 * DebugLog.loge("test", "rtnCode=" + rtnCode); //for mengtian end
			 */
			bitmap = optimizeBitmapByFile(file, maxWidth, maxHeight);
			if (bitmap == null) {
				bitmap = optimizeBitmapByFile(file, maxWidth / 2, maxHeight / 2);
			}
			long length = file.length();
			DebugLog.logd("test", "****####length =" + length);
			if (bitmap == null) {
				return null;
			}
			// bytes = null;
			// System.gc();

			FileOutputStream out = new FileOutputStream(
					myFile.getAbsolutePath());
			// BufferedOutputStream bos = new BufferedOutputStream(out);

			// bitmap = Tool.rotateBitmap(bitmap);
			// bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);

			if (NetUtil.is2GNetwork(SysApplication.getAppContext())) {
				int quality = 80;

				if (length > 1000 * 1024) {
					quality = 20;
				} else if (length > 800 * 1024) {
					quality = 30;
				} else if ((length > 500 * 1024)) {
					quality = 40;
				} else if ((length > 200 * 1024)) {
					quality = 60;
				} else {
					quality = 80;
				}
				DebugLog.logd("test", "****####quality =" + quality);
				bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
			} else {
				bitmap.compress(Bitmap.CompressFormat.JPEG,
						CAMERA_COMPRESS_RATIO, out);
			}

			// myFile = Tool.rotateBitmap(myFile, 90);

			// bos.flush();
			// bos.close();
			out.close();
			// fis.close();
			file.delete();
			// bos = null;
			out = null;
			// fis = null;
			bitmap.recycle();
			bitmap = null;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bitmap != null && !bitmap.isRecycled()) {
				bitmap.recycle();
				bitmap = null;
			}
		}
		DebugLog.logd("Cost time",
				"prepareMyPhoto costs:" + (System.currentTimeMillis() - start));
		DebugLog.logd("myFile=" + myFile.getAbsolutePath());

		return myFile;
	}

	public static File prepareMyPhoto(Context context, byte[] data,
			int maxWidth, int maxHeight) {
		File myFile = null;
		Bitmap bitmap = null;
		long start = System.currentTimeMillis();
		DebugLog.logd("Cost time", "prepareMyPhoto start");
		try {
			File dir = getPhotoDir();
			if (dir == null) {
				ToastUtil.showMessage(context,
						context.getString(R.string.nosdcard));
				return null;
			}
			String file_name = Tool.nowTime() + ".jpg";
			myFile = new File(dir, file_name);

			try {
				bitmap = optimizeBitmap(data, maxWidth, maxHeight);
				if (bitmap == null) {
					bitmap = optimizeBitmap(data, maxWidth / 2, maxHeight / 2);
				} else {
					int pixs[] = new int[bitmap.getWidth() * bitmap.getHeight()];
					bitmap.getPixels(pixs, 0, bitmap.getWidth() * 53 / 64,
							bitmap.getWidth() * 3 / 80,
							bitmap.getHeight() * 3 / 32,
							bitmap.getWidth() * 53 / 64,
							bitmap.getHeight() * 13 / 16);
					int width = bitmap.getWidth() * 53 / 64;
					int height = bitmap.getHeight() * 13 / 16;
					// CardImage cardimage = LocalOCR.processImage(width,
					// height, pixs);
					// if(cardimage.sign == 1)
					// {
					// bitmap.recycle();
					// bitmap = Bitmap.createBitmap(cardimage.width,
					// cardimage.height, Config.ARGB_8888);
					// bitmap.setPixels(cardimage.img, 0, cardimage.width, 0, 0,
					// cardimage.width, cardimage.height);
					// }
					// else
					// {
					bitmap = Bitmap.createBitmap(bitmap,
							bitmap.getWidth() * 3 / 80,
							bitmap.getHeight() * 3 / 32,
							bitmap.getWidth() * 53 / 64,
							bitmap.getHeight() * 13 / 16);
					// }
				}
			} catch (OutOfMemoryError error) {
				System.gc();
				if (bitmap != null && !bitmap.isRecycled()) {
					bitmap.recycle();
					bitmap = null;
				}
				bitmap = optimizeBitmap(data, maxWidth / 2, maxHeight / 2);
				error.printStackTrace();
			}

			long length = data.length;
			DebugLog.logd("test", "****####length =" + length);
			if (bitmap == null) {
				return null;
			}

			FileOutputStream out = new FileOutputStream(
					myFile.getAbsolutePath());

			if (NetUtil.is2GNetwork(SysApplication.getAppContext())) {
				int quality = 80;

				if (length > 1000 * 1024) {
					quality = 20;
				} else if (length > 800 * 1024) {
					quality = 30;
				} else if ((length > 500 * 1024)) {
					quality = 40;
				} else if ((length > 200 * 1024)) {
					quality = 60;
				} else {
					quality = 80;
				}
				DebugLog.logd("test", "****####quality =" + quality);
				bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
			} else {
				bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
			}
			out.close();
			out = null;
			bitmap.recycle();
			bitmap = null;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bitmap != null && !bitmap.isRecycled()) {
				bitmap.recycle();
				bitmap = null;
			}
		}
		return myFile;
	}

	/**
	 * 根据设定的分辨率对图片进行压缩处理
	 * 
	 * @param maxWidth
	 *            图片的宽度
	 * @param maxHeight
	 *            图片的高度
	 * @return
	 */
	public static Bitmap optimizeBitmap(byte[] resource, int maxWidth,
			int maxHeight) {

		if (resource == null || maxWidth <= 0 || maxHeight <= 0) {
			DebugLog.loge("Tool optimizeBitmap return: resource = " + resource
					+ " maxWidth = " + maxWidth + " maxHeight = " + maxHeight);
			return null;
		}
		Bitmap result = null;
		int length = resource.length;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		result = BitmapFactory.decodeByteArray(resource, 0, length, options);
		int widthRatio = (int) Math.ceil(options.outWidth / maxWidth);
		int heightRatio = (int) Math.ceil(options.outHeight / maxHeight);
		if (widthRatio > 1 || heightRatio > 1) {
			if (widthRatio > heightRatio) {
				options.inSampleSize = widthRatio;
			} else {
				options.inSampleSize = heightRatio;
			}
		}

		options.inJustDecodeBounds = false;
		try {
			result = BitmapFactory
					.decodeByteArray(resource, 0, length, options);
		} catch (OutOfMemoryError e) {
			System.gc();
			e.printStackTrace();
			DebugLog.loge("error=" + e.toString());
			options.inSampleSize *= 4;
			try {
				result = BitmapFactory.decodeByteArray(resource, 0, length,
						options);
			} catch (OutOfMemoryError e1) {
				e.printStackTrace();
				DebugLog.loge("error1 =" + e1.toString());
				System.gc();
			}
		}
		return result;
	}

	/**
	 * 根据设定的分辨率对图片进行压缩处理
	 * 
	 * @param maxWidth
	 *            图片的宽度
	 * @param maxHeight
	 *            图片的高度
	 * @return
	 */
	public static Bitmap optimizeBitmapByFile(File file, int maxWidth,
			int maxHeight) {

		boolean isRotate = false;
		if (file == null || maxWidth <= 0 || maxHeight <= 0) {
			// DebugLog.loge("Tool optimizeBitmap return: resource = " +
			// resource + " maxWidth = " + maxWidth
			// + " maxHeight = " + maxHeight);
			return null;
		}
		Bitmap result = null;
		// int length = resource.length;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		result = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
		int widthRatio = (int) Math.ceil(options.outWidth / maxWidth);
		int heightRatio = (int) Math.ceil(options.outHeight / maxHeight);
		if (widthRatio > 1 || heightRatio > 1) {
			if (widthRatio > heightRatio) {
				options.inSampleSize = widthRatio;
			} else {
				isRotate = true;
				options.inSampleSize = heightRatio;
			}
		}

		options.inJustDecodeBounds = false;
		try {
			result = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
		} catch (OutOfMemoryError e) {
			System.gc();
			e.printStackTrace();
			DebugLog.loge("error=" + e.toString());
			options.inSampleSize *= 3;
			try {
				result = BitmapFactory.decodeFile(file.getAbsolutePath(),
						options);
			} catch (OutOfMemoryError e1) {
				e.printStackTrace();
				DebugLog.loge("error1 =" + e1.toString());
				System.gc();
			}
		}
		try {
			if (isRotate) {
				result = rotateBitmap(result, -90);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 旋转图像至width > height
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Bitmap rotateBitmap(Bitmap bitmap) {
		if (bitmap != null && bitmap.getWidth() < bitmap.getHeight()) {
			final Matrix matrix = new Matrix();
			matrix.setRotate(90);
			try {
				Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0, 0,
						bitmap.getWidth(), bitmap.getHeight(), matrix, true);
				if (bitmap != bitmap2) {
					bitmap.recycle();
					bitmap = bitmap2;
				}
			} catch (OutOfMemoryError ex) {
				System.gc();
				ex.printStackTrace();
				DebugLog.loge("error =" + ex.toString());
			}
		}
		return bitmap;
	}

	/**
	 * 旋转度数
	 * 
	 * @param bitmap
	 * @param ratate
	 *            度数
	 * @return
	 */
	public static Bitmap rotateBitmap(Bitmap bitmap, int ratate) {
		final Matrix matrix = new Matrix();
		matrix.setRotate(ratate);
		try {
			Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0, 0,
					bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			if (bitmap != bitmap2) {
				bitmap.recycle();
				bitmap = bitmap2;
			}
		} catch (OutOfMemoryError ex) {
			ex.printStackTrace();
			DebugLog.loge("error =" + ex.toString());
			System.gc();
		}
		return bitmap;
	}
	
	public static String getChatLargeImage(String image) {
        String result;
        int pos = image.lastIndexOf('.');
        if (pos != -1) {
            result = combineStrings(image.substring(0, pos), "_orig", image.substring(pos));
        }
        else
            result = combineStrings(image, "_orig");
        return result;
    }
	
	public synchronized static String combineStrings(String... args) {
        if (args == null || args.length == 0)
            return "";
        StringBuffer sb = new StringBuffer(args.length);
        for (String arg : args) {
            sb.append(arg);
        }
        return sb.toString();
    }
	
	public static Bitmap makeBitmap(String path, int maxNumOfPixels)
			throws OutOfMemoryError {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		if (options.mCancel || options.outWidth == -1
				|| options.outHeight == -1) {
			return null;
		}
		options.inSampleSize = computeSampleSize(options, 520, maxNumOfPixels);

		options.inJustDecodeBounds = false;

		options.inDither = false;
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		return BitmapFactory.decodeFile(path, options);
	}
	
	
	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}
	
	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		// Log.d(TAG, "ratio:"+w+"x"+h);

		int lowerBound = (maxNumOfPixels == UNCONSTRAINED) ? 1 : (int) Math
				.ceil(Math.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == UNCONSTRAINED) ? 128 : (int) Math
				.min(Math.floor(w / minSideLength), Math.floor(h
						/ minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == UNCONSTRAINED)
				&& (minSideLength == UNCONSTRAINED)) {
			return 1;
		} else if (minSideLength == UNCONSTRAINED) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}
	
	public static Bitmap roundBitmap(Bitmap bitmap, int outputWidth, int outputHeight,float[] radii) {
    	if(outputWidth==0 || outputHeight==0) return null;
        try {
			Bitmap output = Bitmap.createBitmap(outputWidth, outputHeight, Config.ARGB_8888);
			
			Paint paintForRound = new Paint();
			paintForRound.setAntiAlias(true);
			paintForRound.setColor(0xff424242);
			paintForRound.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			
			if(output==null) return null;
			Canvas canvas = new Canvas(output);

			final Rect rect = new Rect(0, 0,outputWidth, outputHeight);
			final RectF rectF = new RectF(rect);

			canvas.drawARGB(0, 0, 0, 0);
			paintForRound.setXfermode(null);
			
			Path path = new Path();
			path.addRoundRect(rectF, radii, Path.Direction.CW);
			canvas.drawPath(path, paintForRound);

			paintForRound.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			
			int bmpWidth = bitmap.getWidth();
			int bmpHeight = bitmap.getHeight();
			
			Rect srcRect;
			
			
			if(bmpWidth >= bmpHeight){
				float srcHeight = bmpHeight;
				float scale =srcHeight/outputHeight;
				float srcWidth = outputWidth*scale;
				srcRect = new Rect( (int)(bmpWidth/2-srcWidth/2),0,(int)(bmpWidth/2+srcWidth/2),bmpHeight);
			}else{
				float srcWidth = bmpWidth;
				float scale =srcWidth/outputWidth;
				float srcHeight = outputHeight*scale;
				srcRect = new Rect(0, (int)(bmpHeight/2-srcHeight/2),bmpWidth,(int)(bmpHeight/2+srcHeight/2));
			}
			canvas.drawBitmap(bitmap,srcRect, rect, paintForRound);
			bitmap=null;
			return output;
		} catch (java.lang.OutOfMemoryError e) {
			DebugLog.logd("CornerWebImageView", "OutOfMemoryError", e);
			return null;
		}
    }
}