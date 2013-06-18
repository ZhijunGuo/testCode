package com.example.galaxy08.http.model;

/**
 * ResponseMessageBean 服务器响应实体类 2013/04/15
 */
public class ResponseMessageBean {

	private String body;
	private String sender;
	private String receiver;
	private String seq;
	private String createTimeLong;
	private String type;
	private String readFlag;
	private String msgType;

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}
    
	public String getReadFlag() {
		return readFlag;
	}

	public void setReadFlag(String readFlag) {
		this.readFlag = readFlag;
	}

	public String getCreateTimeLong() {
		return createTimeLong;
	}

	public void setCreateTimeLong(String createTimeLong) {
		this.createTimeLong = createTimeLong;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	
}
