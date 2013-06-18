package com.example.galaxy08.entity;
/**
 *  群组 item
 */
public class Group {
	private int groupId;
	private String groupName;
	private String createTime;
	private String groupLastMessageTime;
	private String groupLastMessage;
	private String imagePath;
	
	
	public Group(String name,String groupLastMessageTime,String groupLastMessage){
		this.groupName = name;
		this.groupLastMessageTime = groupLastMessageTime;
		this.groupLastMessage = groupLastMessage;
	}
	
	public Group(){
		
	}
	
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getGroupLastMessageTime() {
		return groupLastMessageTime;
	}
	public void setGroupLastMessageTime(String groupLastMessageTime) {
		this.groupLastMessageTime = groupLastMessageTime;
	}
	public String getGroupLastMessage() {
		return groupLastMessage;
	}
	public void setGroupLastMessage(String groupLastMessage) {
		this.groupLastMessage = groupLastMessage;
	}
	@Override
	public String toString() {
		return "Item [groupId=" + groupId + ", groupName=" + groupName
				+ ", groupLastMessageTime=" + groupLastMessageTime
				+ ", groupLastMessage=" + groupLastMessage + "]";
	}
}
