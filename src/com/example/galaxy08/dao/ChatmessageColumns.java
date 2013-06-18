package com.example.galaxy08.dao;

//import com.example.galaxy08.entity.ChatMessage;

/*
 * chatmessage(私聊信息)
 * _id    编号
 * user_id  信息的拥有者id
 * group_id             信息所属群组
 * sender_id  信息发送者id
 * receiver_id  信息的接受者id
 * body   信息的主体
 * time   信息发送的时间
 * type   信息的类型(0文本，1图片，2语音)
 * status  信息的状态(0草稿，1发送成功，2发送失败)
 * time_stamp  消息的时间戳
 * sequence  消息的顺序
 * audio_time  语音消息的时长
 *
 * <1> type=0 body=文本具体内容
 * <2> type=1 body=图片文件的地址
 * <3> type=2 body=语音文件的地址
 *   *
 * 查询某群组内的聊天信息:(group_id相同)
 */
/*
CREATE TABLE [tb_chatmessage] (
		  [_id] INTEGER PRIMARY KEY AUTOINCREMENT, 
		  [userid] CHAR(32), 
		  [targetid] CHAR(32), 
		  [mediatype] INTEGER, 
		  [content] TEXT, 
		  [sendtime] INTEGER, 
		  [receivetime] INTEGER, 
		  [sequence] INTEGER, 
		  [status] INTEGER, 
		  [media_cache] TEXT, 
		  [audio_time] INTEGER, 
		  [name] TEXT, 
		  [avatar] TEXT, 
		  [type] INTEGER);
		  */
public class ChatmessageColumns {
	public static final String _ID = "_id";//userid
	public static final String USERID = "userid";//userid
	public static final String TARGETID = "targetid";//targetid
	public static final String TYPE = "type";//type
	public static final String MEDIATYPE = "mediatype";//mediatype
	public static final String CONTENT = "content";//content
	public static final String TIMESTAMP = "timestamp";
	//public static final String SNEDTIME = "sendtime";//sendtime
	//public static final String RECEIVETIME = "receivetime";//receivetime
	public static final String SEQUENCE = "sequence";//sequence
	public static final String STATUS = "status";//status
	public static final String MEDIA_CACHE = "media_cache";//media_cache
	public static final String AUDIO_TIME = "audio_time";//audio_time
	public static final String NAME="name";//name
	public static final String AVATAR = "avatar";//avatar
	
}
