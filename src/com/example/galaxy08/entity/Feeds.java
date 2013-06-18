package com.example.galaxy08.entity;

import java.util.ArrayList;

import com.example.galaxy08.SysApplication;
import com.example.galaxy08.dao.FeedColumns;
import com.example.galaxy08.dao.GalaxyProviderInfo;

import android.content.ContentResolver;
import android.database.Cursor;

/**
 * 数据库操作类
 * @author 郭智军
 */
public class Feeds {
	/**
	 * 根据 feedsid userid 查询feed
	 */
	public static ArrayList<Feed> getFeedInfoDB(String feedsid,String userid){
		ArrayList<Feed> fs = null;
		//Feed f = null;
		ContentResolver cr = SysApplication.application.getContentResolver();
		Cursor cursor = cr.query(GalaxyProviderInfo.FEED_URI, 
				new String[]{
				FeedColumns.CONTENT,
				FeedColumns.PUBLISHER_ID,
				FeedColumns.PUBLISHER_NAME,
				FeedColumns.PUBLISHER_AVATAR,
				FeedColumns.TIME,
				FeedColumns.TYPE,
				FeedColumns.LEVEL,
				FeedColumns.STATUS,
		},FeedColumns.FEEDSESSION_ID+"=? and "+ FeedColumns.USER_ID+"=?", 
		new String[]{
			feedsid,userid
		}
		, FeedColumns.TIME+" desc");
		if(cursor!=null){
			fs = new ArrayList<Feed>();
			while(cursor.moveToNext()){
				Feed f = new Feed();
				f.setContent(cursor.getString(cursor.getColumnIndex(FeedColumns.CONTENT)));
				f.setTime(cursor.getString(cursor.getColumnIndex(FeedColumns.TIME)));
				f.setPublisher_id(cursor.getString(cursor.getColumnIndex(FeedColumns.PUBLISHER_ID)));
				f.setPublisher_name(cursor.getString(cursor.getColumnIndex(FeedColumns.PUBLISHER_NAME)));
				f.setPublisher_avatar(cursor.getString(cursor.getColumnIndex(FeedColumns.PUBLISHER_AVATAR)));
				f.setType(cursor.getInt(cursor.getColumnIndex(FeedColumns.TYPE)));
				f.setLevel(cursor.getInt(cursor.getColumnIndex(FeedColumns.LEVEL)));
				f.setStatus(cursor.getInt(cursor.getColumnIndex(FeedColumns.STATUS)));
				fs.add(f);
			}
			cursor.close();
		}
		return fs;
	}
}
