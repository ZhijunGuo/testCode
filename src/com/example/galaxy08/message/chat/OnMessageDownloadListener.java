package com.example.galaxy08.message.chat;

import com.example.galaxy08.entity.ChatMessage;

/**
 * OnMessageDownloadListener 接口，拉取(下载)消息时调用
 * 2013/04/15
 */
public interface OnMessageDownloadListener {
	public void onDownloadStart(ChatMessage message);

	public void onDownloadFinish(ChatMessage message, boolean success);
}
