package com.example.galaxy08.dao;
/*
 * comment(动态信息评论表)
 * comment_id 该评论编号
 * feed_id  评论所属的动态信息
 * publisher_id 评论的发布者
 * content  评论的内容
 * time   评论发表的时间
 * level  该评论的状态(0只允许发布者进行操作(查看，回复) 1允许同等级用户操作
 *      2公开)
 */
public class CommentColumns {
	public static final String _ID="comment_id";
	public static final String FEED_ID="feed_id";
	public static final String PUBLISHER_ID="publisher_id";
	public static final String CONTENT="content";
	public static final String TIME="time";
	public static final String LEVEL="level";
}
