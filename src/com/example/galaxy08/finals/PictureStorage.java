/**
 * PictureStorage.java
 * 2012-2-9
 */
package com.example.galaxy08.finals;

import java.io.File;

import android.os.Environment;

/**
 * 
 */
public interface PictureStorage {
	public static final String ARECORD_NAME = "Android" + File.separator
			+ "data" + File.separator + "com.jingwei.card" + File.separator
			+ "files";
	public static final String SECOND_NAME = "pic";
	public static final String CACHE_DIR_NAME = "pic_cache";
	public static final String BLESS_DIR_NAME = "bless_pic_cache";
	public static final String HTML_DIR_NAME = "bless_html_cache";
	public static final String DB_DIR_NAME = "db";
	public static final String JSON_CACHE_DIR_NAME = "json_cache";
	public static final String JSON_CACHE_FILE_NAME = "query_new.cache";
	public static final String AUDIO_DIR_NAME ="audio";
	public static final String OCR_DB = "ocrdb";
	public static final String QRCODE_NAME = "qrpic";

	public static final String ARECORD_DIR = Environment
			.getExternalStorageDirectory()
			+ File.separator
			+ ARECORD_NAME
			+ File.separator + SECOND_NAME + File.separator;

	public static final String ARECORD_CACHE_DIR = Environment
			.getExternalStorageDirectory()
			+ File.separator
			+ ARECORD_NAME
			+ File.separator + CACHE_DIR_NAME + File.separator;
	public static final String BLESS_CACHE_DIR = Environment
			.getExternalStorageDirectory()
			+ File.separator
			+ ARECORD_NAME
			+ File.separator + BLESS_DIR_NAME + File.separator;
	
	public static final String HTML_CACHE_DIR = Environment
			.getExternalStorageDirectory()
			+ File.separator
			+ ARECORD_NAME
			+ File.separator + HTML_DIR_NAME + File.separator;

	public static final String ARECORD_DB_DIR = File.separator + ARECORD_NAME
			+ File.separator + DB_DIR_NAME + File.separator;

	public static final String ARECORD_JSON_CACHE_DIR = File.separator + ARECORD_NAME
			+ File.separator + JSON_CACHE_DIR_NAME + File.separator;
	
	public static final String ARECORD_AUDIO_DIR = Environment
			.getExternalStorageDirectory() +
			File.separator + ARECORD_NAME
			+ File.separator + AUDIO_DIR_NAME + File.separator;
	public static final String OCR_DB_DIR =  Environment
	.getExternalStorageDirectory()
	+ File.separator
	+ ARECORD_NAME
	+ File.separator + OCR_DB + File.separator;
	public static final String QRCODE_IMAGE_DIR = Environment
			.getExternalStorageDirectory()
			+ File.separator
			+ ARECORD_NAME
			+ File.separator + QRCODE_NAME + File.separator;
	public static final String OCR_ITEM_DIR =  Environment
	.getExternalStorageDirectory()
	+ File.separator
	+ ARECORD_NAME
	+ File.separator + "itempic" + File.separator;		
//	public static final String JSON_CACHE_FILE_PATH = JwApplication.mContext.getCacheDir()+ File.separator + JSON_CACHE_FILE_NAME;

}
