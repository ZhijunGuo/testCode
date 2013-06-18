package com.example.galaxy08.chat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

public class BitmapUtils {
	public static Bitmap loadBitmap(byte[] data, int width, int height) {
		Bitmap bm = null;
		Options opts = new Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(data, 0, data.length, opts);
		int x = opts.outWidth / width;
		int y = opts.outHeight / height;
		opts.inSampleSize = x > y ? x : y;
		opts.inJustDecodeBounds = false;
		bm = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
		return bm;
	}
	
	public static void save(Bitmap bm,String savePath){
		if(bm!=null && savePath!=null){
			File file = new File(savePath);
			if(!file.getParentFile().exists()){
				file.getParentFile().mkdirs();
			}
			
			if(!file.exists()){
				try {
					file.createNewFile();
					bm.compress(CompressFormat.JPEG, 100, new FileOutputStream(file));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	public static Bitmap loadBitmap(String path){
		return BitmapFactory.decodeFile(path);
	}
}
