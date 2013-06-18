package com.example.galaxy08.dao;
/*
 * feed(动态信息表)
 *
 * feed_id  动态信息编号
 * user_id  动态信息的所有者
 * publisher_id 信息发布者id
 * content  信息的内容
 * time   发布的时间
 * type   信息的类型(0文本 1图片 是否需要支持语音?)
 * relay_count 转发的次数
 * comment_count 评论的次数
 * level  信息的等级(0普通  1重要 2紧急)
 * status  信息的状态
 * allow_level 操作(接收、转发或者评论)该信息用户的最低级别
 */
public class FeedColumns {
	public static final String _ID="feed_id";
	public static final String USER_ID="user_id";
	public static final String PUBLISHER_ID="publisher_id";
	public static final String PUBLISHER_NAME="publisher_name";
	public static final String PUBLISHER_AVATAR="publisher_avatar";
	public static final String CONTENT="content";
	public static final String TIME="time";
	public static final String TYPE="type";
	public static final String RELAY_COUNT="relay_count";
	public static final String COMMENT_COUNT="comment_count";
	public static final String LEVEL="level";
	public static final String STATUS="status";
	public static final String ALLOW_LEVEL="allow_level";
	public static final String FEEDSESSION_ID="feedsession_id";
}
