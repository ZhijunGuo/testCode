package com.example.galaxy08.http.model;

import java.io.Serializable;

import org.json.JSONObject;

/**
 * BaseResponse
 * 服务器响应基础类 2013/04/15
 *
 */
public class BaseResponse  implements Serializable{

	private static final long serialVersionUID = -8297984650252432892L;
	//状态
	private int status;
	//信息
	private String message;
	//连接
	private String url;
	//是否是隐藏的？
	private String ishide;


	public String getIshide() {
		return ishide;
	}

	public void setIshide(String ishide) {
		this.ishide = ishide;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}

	public BaseResponse parser(JSONObject jsonObject){
		this.status = jsonObject.optInt("status");
		this.message = jsonObject.optString("message");
		return this;
	}
}
