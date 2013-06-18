package com.example.galaxy08.entity;

import com.example.galaxy08.app.PreferenceWrapper;

import android.content.ContentValues;

public class ChatSession {
	private User targetUser;
	
	private String targetId;
	
	private int type;//类型 群聊 还是 私聊
	
	private String lastupdate;//最后更新时间
	private String createTime;//创建时间
	
	private ChatMessage message;//最后一条信息
	
	private String userid;
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public ChatMessage getMessage() {
		return message;
	}
	public void setMessage(ChatMessage message) {
		this.message = message;
	}
	public String getTargetId() {
		return targetId;
	}
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public User getTargetUser() {
		return targetUser;
	}
	public void setTargetUser(User targetUser) {
		this.targetUser = targetUser;
	}
	public String getlastupdate() {
		return lastupdate;
	}
	public void setlastupdate(String lastupdate) {
		this.lastupdate = lastupdate;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	public ContentValues getContentValues(){
		ContentValues cv = new ContentValues();
		cv.put("targetid", targetId);
		cv.put("userid", userid);
		cv.put("type", type);
		cv.put("lastupdate", lastupdate);
		cv.put("createtime", createTime);
		return cv;
	}
	
}
