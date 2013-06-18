package com.example.galaxy08.entity;

/*
 *  动态实体类
 *  author: 郭智军
 *  2013/05/23
 */

import com.example.galaxy08.dao.FeedColumns;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

@SuppressWarnings("serial")
public class Feed implements Parcelable {
	
	private int feed_id;
	private String publisher_id;
	private String publisher_name;
	private String publisher_avatar;
	private String userid;
	private String content;
	private String time;
	private int type;
	private int comment_count;
	private int relay_count;
	private int level;
	private int status;
	private int allow_level;
	private String feedsession_id;//该feed属于哪一个分享组
	
	public String getPublisher_name() {
		return publisher_name;
	}

	public void setPublisher_name(String publisher_name) {
		this.publisher_name = publisher_name;
	}

	public String getPublisher_avatar() {
		return publisher_avatar;
	}

	public void setPublisher_avatar(String publisher_avatar) {
		this.publisher_avatar = publisher_avatar;
	}

	public String getFeedsession_id() {
		return feedsession_id;
	}

	public void setFeedsession_id(String feedsession_id) {
		this.feedsession_id = feedsession_id;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public int getFeed_id() {
		return feed_id;
	}

	public void setFeed_id(int feed_id) {
		this.feed_id = feed_id;
	}

	public String getPublisher_id() {
		return publisher_id;
	}

	public void setPublisher_id(String publisher_id) {
		this.publisher_id = publisher_id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getComment_count() {
		return comment_count;
	}

	public void setComment_count(int comment_count) {
		this.comment_count = comment_count;
	}

	public int getRelay_count() {
		return relay_count;
	}

	public void setRelay_count(int relay_count) {
		this.relay_count = relay_count;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getAllow_level() {
		return allow_level;
	}

	public void setAllow_level(int allow_level) {
		this.allow_level = allow_level;
	}

	@Override
	public int describeContents() {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		// TODO 自动生成的方法存根
		
	}
	
	public ContentValues getContentValues(){
		ContentValues cv = new ContentValues();
		cv.put(FeedColumns.ALLOW_LEVEL, allow_level);
		cv.put(FeedColumns.COMMENT_COUNT, comment_count);
		cv.put(FeedColumns.CONTENT, content);
		cv.put(FeedColumns.FEEDSESSION_ID, feedsession_id);
		cv.put(FeedColumns.LEVEL, level);
		cv.put(FeedColumns.STATUS, status);
		cv.put(FeedColumns.PUBLISHER_ID, publisher_id);
		cv.put(FeedColumns.PUBLISHER_NAME, publisher_name);
		cv.put(FeedColumns.PUBLISHER_AVATAR, publisher_avatar);
		cv.put(FeedColumns.RELAY_COUNT, relay_count);
		cv.put(FeedColumns.TIME, time);
		cv.put(FeedColumns.TYPE, type);
		cv.put(FeedColumns.USER_ID, userid);
		return cv;
	}
	
}
