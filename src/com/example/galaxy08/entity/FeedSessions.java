package com.example.galaxy08.entity;

import java.util.ArrayList;

import com.example.galaxy08.SysApplication;
import com.example.galaxy08.app.PreferenceWrapper;
import com.example.galaxy08.dao.FeedSessionColumns;
import com.example.galaxy08.dao.GalaxyProviderInfo;

import android.content.ContentResolver;
import android.database.Cursor;

/**
 * 数据库操作类
 * @author guozhijun
 *
 */
public class FeedSessions {
	public static ArrayList<FeedSession> getFeedSessionInfoDB(){
		ArrayList<FeedSession> fs = null;
		ContentResolver cr = SysApplication.application.getContentResolver();
		Cursor cursor = cr.query(GalaxyProviderInfo.FEEDSESSION_URI,
				new String[]{
				FeedSessionColumns.FEEDSID,
				FeedSessionColumns.FEEDSNAME,
				FeedSessionColumns.FEEDSAVATAR,
				FeedSessionColumns.TYPE,
				FeedSessionColumns.LASTUPDATE,
			} , FeedSessionColumns.USERID+"=?", 
			new String[]{
				PreferenceWrapper.get(PreferenceWrapper.USER_ID, "")
		}, FeedSessionColumns.LASTUPDATE+" desc");
		if(cursor!=null){
			fs = new ArrayList<FeedSession>();
			while(cursor.moveToNext()){
				FeedSession f = new FeedSession();
				f.setFeedsession_name(cursor.getString(cursor.getColumnIndex(FeedSessionColumns.FEEDSNAME)));
				f.setFeedsession_avatar(cursor.getString(cursor.getColumnIndex(FeedSessionColumns.FEEDSAVATAR)));
				f.setFeedsession_id(cursor.getString(cursor.getColumnIndex(FeedSessionColumns.FEEDSID)));
				f.setType(cursor.getInt(cursor.getColumnIndex(FeedSessionColumns.TYPE)));
				f.setLastUpdate(cursor.getString(cursor.getColumnIndex(FeedSessionColumns.LASTUPDATE)));
				fs.add(f);
			}
			cursor.close();
		}
		/**
		 * 然后获取 每个分享群组的最后一条feed信息
		 */
		if(fs!=null){
			for(FeedSession f:fs){
				ArrayList<Feed> targetFs = Feeds.getFeedInfoDB(f.getFeedsession_id(),
						PreferenceWrapper.get(PreferenceWrapper.USER_ID, ""));
				if(targetFs!=null&&targetFs.size()>0){
					f.setLastFeed(targetFs.get(0));
				}
			}
		}
		return fs;
	}
}
