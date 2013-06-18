/**
 * 
 */
package com.example.galaxy08.message.chat;

import com.example.galaxy08.entity.ChatMessage;

/**
 *OnMessageSendListener 消息发送接口 2013/04/15
 */
public interface OnMessageSendListener {
	public void onMessageSendStart(ChatMessage message);
	public void onMessageSendFinish(ChatMessage message, boolean success);
}
