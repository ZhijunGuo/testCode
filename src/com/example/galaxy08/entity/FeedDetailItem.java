package com.example.galaxy08.entity;

public class FeedDetailItem {
	private int id;
	private String content;
	private int transmitCount;
	private int reviewCount;
	private String name;
	private String time;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getTransmitCount() {
		return transmitCount;
	}
	public void setTransmitCount(int transmitCount) {
		this.transmitCount = transmitCount;
	}
	public int getReviewCount() {
		return reviewCount;
	}
	public void setReviewCount(int reviewCount) {
		this.reviewCount = reviewCount;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
}
