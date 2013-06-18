package com.example.galaxy08.dao;

import android.net.Uri;

public class GalaxyProviderInfo {
	/*
	 * 8个表
	 */
	public static final String TABLE_CHAT_MESSAGE = "tb_chatmessage";
	public static final String TABLE_ATTENTION = "tb_attention";
	public static final String TABLE_CHATSESSION = "tb_chatsession";
	public static final String TABLE_COMMENT = "tb_comment";
	public static final String TABLE_GROUP = "tb_chatgroup";
	public static final String TABLE_FEED = "tb_feed";
	public static final String TABLE_TASK = "tb_task";
	public static final String TABLE_USER = "tb_user";
	public static final String TABLE_DEPARTMENT = "tb_department";
	//记录最新的动态信息
	public static final String TABLE_FEEDSESSION = "tb_feedsession";
	
	public static final String AUTHORITY = "com.example.galaxy";
	//1
	public static final String SINGLE_CHAT_PATH = "chat/#";
	public static final String MULTI_CHAT_PATH = "chat";
	//2
	public static final String SINGLE_USER_PATH = "user/#";
	public static final String MULTI_USER_PATH = "user";
	//3
	public static final String SINGLE_FEED_PATH = "feed/#";
	public static final String MULTI_FEED_PATH = "feed";
	//4
	public static final String SINGLE_GROUP_PATH = "group/#";
	public static final String MULTI_GROUP_PATH = "group";
	//5
	public static final String SINGLE_COMMENT_PATH = "comment/#";
	public static final String MULTI_COMMENT_PATH = "comment";
	//6
	public static final String SINGLE_ATTENTION_PATH = "attention/#";
	public static final String MULTI_ATTENTION_PATH = "attention";
	//7
	public static final String SINGLE_TASK_PATH = "task/#";
	public static final String MULTI_TASK_PATH = "task";
	
	public static final String SINGLE_DEMENT_PATH = "task/#";
	public static final String MULTI_DEMENT_PATH = "task";
	//8
	public static final String SINGLE_CHATSESSION_PATH = "chatsession/#";
	public static final String MULTI_CHATSESSION_PATH = "chatsession";
	//9
	public static final String SINGLE_FEEDSESSION_PATH = "feedsession/#";
	public static final String MULTI_FEEDSESSION_PATH = "feedsession";
	
	public static final Uri CHATMESSAGE_URI = Uri.parse("content://"
			+ AUTHORITY + "/"+MULTI_CHAT_PATH);
	public static final Uri USER_URI = Uri.parse("content://"
			+ AUTHORITY + "/"+MULTI_USER_PATH);
	public static final Uri DEPARTMENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/"+MULTI_DEMENT_PATH);
	public static final Uri GROUP_URI = Uri.parse("content://"
			+ AUTHORITY + "/"+MULTI_USER_PATH);
	//会话表
	public static final Uri CHAT_URI = Uri.parse("content://"
			+ AUTHORITY + "/"+MULTI_CHATSESSION_PATH);
	//feedsession
	public static final Uri FEEDSESSION_URI = Uri.parse("content://"
			+ AUTHORITY + "/"+MULTI_FEEDSESSION_PATH);
	
	//feed表
	public static final Uri FEED_URI = Uri.parse("content://"
			+ AUTHORITY + "/"+MULTI_FEED_PATH);
}
