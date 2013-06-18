package com.example.galaxy08.entity;

import java.io.Serializable;

import org.json.JSONObject;


/**
 * 公司的邀请
 * 郭智军
 * 2013/05/31
 */
public class Invite implements Serializable{

	private static final long serialVersionUID = -2661057100192131878L;
	//compnay name
	private String company;
	//company id
	private String cid;
	//用户姓名
	private String name;
	//position
	private String title;
	
	private boolean isChecked;
	
	public Invite(){
		
	}
	
	public Invite(String company, String cid, String name, String title) {
		super();
		this.company = company;
		this.cid = cid;
		this.name = name;
		this.title = title;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public void parser(JSONObject obj){
		setCompany(obj.optString("company"));
		setCid(String.valueOf(obj.optInt("cid")));
		setName(obj.optString("name"));
		setTitle(obj.optString("title"));
	}
	
}
