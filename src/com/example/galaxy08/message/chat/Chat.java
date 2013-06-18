/**
 * 
 */
package com.example.galaxy08.message.chat;

import com.example.galaxy08.entity.ChatMessage;
import com.example.galaxy08.entity.ChatMessage.MESSAGE_MEDIA_TYPE;
import com.example.galaxy08.entity.ChatMessage.MESSAGE_STATUS;
import com.example.galaxy08.entity.ChatMessage.MESSAGE_TYPE;

/**
 *Chat 聊天相关方法的集合  2013/04/15
 */
public class Chat{
	
	private String userid;
	private String targetid;
	
	public Chat(String userid, String targetid) {
		super();
		this.targetid = targetid;
		this.userid =userid;
	}
	/*
	 * sendMessage: 发送文本信息
	 */
	public ChatMessage sendMessage(String message, OnMessageSendListener onMessageSendListener){
		ChatMessage cm = new ChatMessage(-1l, userid,  targetid,
				MESSAGE_TYPE.MESSAGE_SEND, MESSAGE_MEDIA_TYPE.MEDIA_TEXT, message,
				System.currentTimeMillis(), 0, MESSAGE_STATUS.STATUS_SEND,"",0,null,null,null);
		ChatThread.getInstance().addTask(new ChatTask(this,cm,onMessageSendListener));
		return cm;
	}
	
	public ChatMessage sendAudio(String localPath, int audioTime,OnMessageSendListener onMessageSendListener){
		ChatMessage cm = new ChatMessage(-1l, userid, targetid,
				MESSAGE_TYPE.MESSAGE_SEND, MESSAGE_MEDIA_TYPE.MEDIA_AUDIO, null,
				System.currentTimeMillis(), 0, MESSAGE_STATUS.STATUS_SEND,localPath,audioTime,null,null,null);
		ChatThread.getInstance().addTask(new ChatTask(this,cm, onMessageSendListener));
		return cm;
	}
	
	public ChatMessage sendImage(String localPath, OnMessageSendListener onMessageSendListener){
		ChatMessage cm = new ChatMessage(-1l, userid, targetid,
				MESSAGE_TYPE.MESSAGE_SEND, MESSAGE_MEDIA_TYPE.MEDIA_IMAGE, null,
				System.currentTimeMillis(), 0, MESSAGE_STATUS.STATUS_SEND,localPath,0,null,null,null);
		ChatThread.getInstance().addTask(new ChatTask(this,cm, onMessageSendListener));
		return cm;
	}
	
	public ChatMessage sendCard(String cardid, OnMessageSendListener onMessageSendListener){
		ChatMessage cm = new ChatMessage(-1l, userid, targetid,
				MESSAGE_TYPE.MESSAGE_SEND, MESSAGE_MEDIA_TYPE.MEDIA_CARD, null,
				System.currentTimeMillis(), 0, MESSAGE_STATUS.STATUS_SEND,null,0,null,null,null);
		ChatThread.getInstance().addTask(new ChatTask(this,cm, onMessageSendListener));
		return cm;
	}
	
	public ChatMessage sendMessage(ChatMessage message, OnMessageSendListener onMessageSendListener){
		message.setTargetid(targetid);
		message.setStatus(MESSAGE_STATUS.STATUS_SEND);
		ChatThread.getInstance().addTask(new ChatTask(this,message,onMessageSendListener));
		return message;
	}
	
	public ChatMessage sendBless(String message, String mediaCache, int audioTime, OnMessageSendListener onMessageSendListener){
		ChatMessage cm = new ChatMessage(-1l, userid, targetid,
				MESSAGE_TYPE.MESSAGE_SEND, MESSAGE_MEDIA_TYPE.MEDIA_GREET, message,
				System.currentTimeMillis(), 0, MESSAGE_STATUS.STATUS_SEND,mediaCache,audioTime,null,null,null);
		ChatThread.getInstance().addTask(new ChatTask(this,cm,onMessageSendListener));
		return cm;
	}
	
	public ChatMessage sendBlessReply(String message, String mediaCache, int audioTime, OnMessageSendListener onMessageSendListener){
		ChatMessage cm = new ChatMessage(-1l, userid, targetid,
				MESSAGE_TYPE.MESSAGE_SEND, MESSAGE_MEDIA_TYPE.MEDIA_GREET_REPLY, message,
				System.currentTimeMillis(), 0, MESSAGE_STATUS.STATUS_SEND,mediaCache,audioTime,null,null,null);
		ChatThread.getInstance().addTask(new ChatTask(this,cm,onMessageSendListener));
		return cm;
	}
	
	}
