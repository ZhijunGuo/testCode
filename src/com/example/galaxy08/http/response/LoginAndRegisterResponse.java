package com.example.galaxy08.http.response;

import org.json.JSONObject;

import com.example.galaxy08.http.ResponseType;
import com.example.galaxy08.http.model.BaseResponse;

@SuppressWarnings("serial")
public class LoginAndRegisterResponse extends BaseResponse implements ResponseType, Parser<LoginAndRegisterResponse>{
	
	private String token;
	private long userid;
	
	private int userStatus;
	private String statusDesc;
	
	@Override
	public LoginAndRegisterResponse parser(JSONObject jsonObject) {
		
		if(jsonObject==null)
			return null;
		
		setStatus(jsonObject.optInt("status"));
		setMessage(jsonObject.optString("message"));
		
		JSONObject dataObject = jsonObject.optJSONObject("data");
		if(dataObject!=null){
			if(getStatus()==0){
				token = dataObject.optString("token");
				userid = dataObject.optLong("userId");
			}else if(getStatus()==8){
				userStatus = dataObject.optInt("user_status");
				statusDesc = dataObject.optString("status_desc");
			}
		}
		return this;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}
	
	
	public int getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(int userStatus) {
		this.userStatus = userStatus;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	@Override
	public String toString() {
		return "{status"+getStatus()+" message:\""+getMessage()+"\"}";
	}
	
}