package com.example.galaxy08.entity;
/*
 * 聊天状态
 */
public class ChatState {
	private String inputType;
	private String context="";
	public String getInputType() {
		return inputType;
	}
	public void setInputType(String inputType) {
		this.inputType = inputType;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
}
