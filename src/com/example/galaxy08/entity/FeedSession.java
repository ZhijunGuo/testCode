package com.example.galaxy08.entity;

import java.io.Serializable;

import com.example.galaxy08.dao.FeedSessionColumns;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

/**
 * 
 * @author 郭智军
 *
 */
public class FeedSession implements Serializable,Parcelable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 451075326498031468L;
	/**
	 * id 动态组名称(个人名称)
	 * 动态组头像
	 * 最后更新feed的数量
	 * 最新的一条feed
	 * 类型(个人feed还是群组feed)
	 */
	private String feedsession_id;
	private String feedsession_name;
	private String feedsession_avatar;
	private int messageCount;
	private String lastUpdate;
	private String user_id;
	private int type;
	private Feed lastFeed;
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getFeedsession_id() {
		return feedsession_id;
	}
	public void setFeedsession_id(String feedsession_id) {
		this.feedsession_id = feedsession_id;
	}
	public String getFeedsession_name() {
		return feedsession_name;
	}
	public void setFeedsession_name(String feedsession_name) {
		this.feedsession_name = feedsession_name;
	}
	public String getFeedsession_avatar() {
		return feedsession_avatar;
	}
	public void setFeedsession_avatar(String feedsession_avatar) {
		this.feedsession_avatar = feedsession_avatar;
	}
	public int getMessageCount() {
		return messageCount;
	}
	public void setMessageCount(int messageCount) {
		this.messageCount = messageCount;
	}
	
	public Feed getLastFeed() {
		return lastFeed;
	}
	public void setLastFeed(Feed lastFeed) {
		this.lastFeed = lastFeed;
	}
	public String getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public ContentValues getContentValues(){
		ContentValues cv = new ContentValues();
		cv.put(FeedSessionColumns.FEEDSID, feedsession_id);
		cv.put(FeedSessionColumns.FEEDSNAME, feedsession_name);
		cv.put(FeedSessionColumns.FEEDSAVATAR, feedsession_avatar);
		cv.put(FeedSessionColumns.USERID, user_id);
		cv.put(FeedSessionColumns.TYPE, type);
		return cv;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(feedsession_id);
		dest.writeString(feedsession_name);
		dest.writeString(feedsession_avatar);
		dest.writeInt(messageCount);
		dest.writeString(lastUpdate);
		dest.writeString(user_id);
		dest.writeInt(type);
	}
	
	public static final Parcelable.Creator<FeedSession> CREATOR = new Creator<FeedSession>() {

		@Override
		public FeedSession createFromParcel(Parcel p) {
			FeedSession f = new FeedSession();
			f.setFeedsession_id(p.readString());
			f.setFeedsession_name(p.readString());
			f.setFeedsession_avatar(p.readString());
			f.setMessageCount(p.readInt());
			
			f.setLastUpdate(p.readString());
			f.setUser_id(p.readString());
			f.setType(p.readInt());
			return f;
		}

		@Override
		public FeedSession[] newArray(int size) {
			return new FeedSession[size];
		}
		
	};
	
}
