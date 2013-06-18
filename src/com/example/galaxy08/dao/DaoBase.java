package com.example.galaxy08.dao;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DaoBase extends SQLiteOpenHelper {

	public SQLiteDatabase mDatabase = null;

	public static String DATABASE_NAME = "galaxy.db";

	public final static int DATABASE_VERSION = 100;

	private Context mContext;

	public SharedPreferences sharedPreferences;

	public String userId;

	public DaoBase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		mContext = context;
		// sharedPreferences = context.getSharedPreferences("myloveSetting",
		// Context.MODE_PRIVATE);
		// userId = sharedPreferences.getString("userID", "0");
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		/*
		 * [_id] INTEGER PRIMARY KEY AUTOINCREMENT, [userid] CHAR(32),
		 * [targetid] CHAR(32), [mediatype] INTEGER, [content] TEXT, [sendtime]
		 * INTEGER, [receivetime] INTEGER, [sequence] INTEGER, [status] INTEGER,
		 * [media_cache] TEXT, [audio_time] INTEGER, [name] TEXT, [avatar] TEXT,
		 * [type] INTEGER);
		 */
		String chatmessage_sql = "create table IF NOT EXISTS "
				+ GalaxyProviderInfo.TABLE_CHAT_MESSAGE + "("
				+ "_id INTEGER PRIMARY KEY," + "userid TEXT,"
				+ "targetid TEXT," + "type INTEGER," + "mediatype INTEGER,"
				+ "content TEXT," + "timestamp INTEGER," + "sendtime INTEGER,"
				+ "receivetime INTEGER," + "sequence INTEGER,"
				+ "status INTEGER," + "media_cache TEXT,"
				+ "audio_time INTEGER," + "name TEXT," + "avatar TEXT);";

		/**
		 * * user(用户信息表) _id 数据编号 user_id 用户编号 user_name 用户名称 img_path 用户头像路径
		 * position 用户的职位 department 用户所在的部门 mobile 用户的手机号 telephony 用户的座机号
		 * email 用户的电子邮件 address 用户的地址 group_id 用户所属的群组id level
		 * 用户的等级(建议使用等级标准区分用户) type 用户的可见属性, 可以和level结合使用
		 */
		String user_sql = "create table IF NOT EXISTS "
				+ GalaxyProviderInfo.TABLE_USER + "("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "user_id char(32) DEFAULT ('0'), "
				+ "user_name char(64) DEFAULT (''),"
				+ "gender INTEGER DEFAULT 1,"
				+ "img_path TEXT DEFAULT (''),"
				
				+ "position char(32) DEFAULT ('')," 
				+ "department char(64) DEFAULT ('')," 
				+ "company char(255) DEFAULT (''), "
				
				+ "address char(255) DEFAULT (''), "
				+ "mobile char(32) DEFAULT (''),"
				+ "telephony char(32) DEFAULT (''),"
				+ "email char(64) DEFAULT (''),"
				
				+ "group_id char(32) DEFAULT (''),"
				+ "level INTEGER,"
				+ "type INTEGER,"
				
				+ "owner_id CHAR(32),"
				
				+ "followcount INTEGER DEFAULT 0,"
				+ "fanscount INTEGER DEFAULT 0);";
		/*
		 * [_id] INTEGER PRIMARY KEY AUTOINCREMENT, [groupid] char(32) NOT NULL,
		 * [groupname] char(32) UNIQUE ON CONFLICT FAIL, [createtime] integer,
		 * [avatar] TEXT);
		 */
		String group_sql = "create table IF NOT EXISTS "
				+ GalaxyProviderInfo.TABLE_GROUP + "("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "groupid char(32) NOT NULL,"
				+ "groupname char(32) UNIQUE ON CONFLICT FAIL,"
				+ "createtime integer," + "avatar TEXT);";
		/*
		 * task(任务表) task_id 任务编号 user_id 任务所属用户 task_name 任务名称 level 任务的级别(0普通
		 * 1重要 2紧急) start_time 开始时间 end_time 截止时间 assigner_id 任务的汇报对象id(分配者)
		 * actor_id 任务的执行者id remark 任务备注 status 任务状态(0未开始 1进行中 2完成 3上报)
		 * 
		 * <1> 查询需要自己执行的任务：user_id==actor_id <2> 查询自己分配的任务： user_id==assigner_id
		 */
		String task_sql = "create table IF NOT EXISTS "
				+ GalaxyProviderInfo.TABLE_TASK
				+ "("
				+ "task_id integer primary key autoincrement,"
				+ "user_id char(32) not null,"
				+ "task_name char(64) not null,"// 这个属性需要限制文字的数目吗？
				+ "level integer not null," + "start_time char(32) not null,"
				+ "end_time char(32) not null,"
				+ "assigner_id char(32) not null,"
				+ "actor_id char(32) not null," + "remark TEXT,"
				+ "status integer not null)";
		/**
		 * 部门表
		 */
		String department_sql = "create table IF NOT EXISTS "
				+ GalaxyProviderInfo.TABLE_DEPARTMENT
				+ "("
				+ "_id integer primary key autoincrement,"
				+ "dement_id CHAR(32) not null,"
				+ "dement_name CHAR(64) DEFAULT (''),"
				+ "owner_id CHAR(32) not null,"
				+ "level integer not null)"; 
				
		/*
		 * feed(动态信息表)
		 * 
		 * feed_id 动态信息编号 user_id 动态信息的所有者 publisher_id 信息发布者id content 信息的内容
		 * time 发布的时间 type 信息的类型(0文本 1图片 是否需要支持语音?) relay_count 转发的次数
		 * comment_count 评论的次数 level 信息的等级(0普通 1重要 2紧急) status 信息的状态 allow_level
		 * 操作(接收、转发或者评论)该信息用户的最低级别
		 */
		String feed_sql = "create table IF NOT EXISTS "
				+ GalaxyProviderInfo.TABLE_FEED + "("
				+ "feed_id integer primary key autoincrement,"
				+ "user_id char(32) not null,"
				+ "publisher_id char(32) not null,"
				+ "publisher_name char(32) not null,"
				+ "publisher_avatar text default (''),"
				+ "feedsession_id char(32) not null,"
				+ "content char(32) not null," 
				+ "time char(32) not null,"
				+ "type integer not null," 
				+ "relay_count integer default 0,"
				+ "comment_count integer default 0,"
				+ "level integer not null," 
				+ "status integer default 0,"
				+ "allow_level integer not null)";
		/*
		 * comment(动态信息评论表) comment_id 该评论编号 feed_id 评论所属的动态信息 publisher_id
		 * 评论的发布者 content 评论的内容 time 评论发表的时间 level 该评论的状态(0只允许发布者进行操作(查看，回复)
		 * 1允许同等级用户操作 2公开)
		 */
		String comment_sql = "create table IF NOT EXISTS "
				+ GalaxyProviderInfo.TABLE_COMMENT + "("
				+ "comment_id integer primary key autoincrement,"
				+ "feed_id char(32) not null,"
				+ "publisher_id char(32) not null," + "content TEXT not null,"
				+ "time char(32) not null," + "level integer not null)";
		/*
		 * attention(我关注的人 表) _id 编号 user_id 数据的所有者 attention_id 所关注的人的id
		 */
		String attention_sql = "create table IF NOT EXISTS "
				+ GalaxyProviderInfo.TABLE_ATTENTION + "("
				+ "_id integer primary key autoincrement,"
				+ "user_id char(32) not null,"
				+ "attention_id char(32) not null);";

		/*
		 * CREATE TABLE "tb_chatsession" ( [_id] INTEGER NOT NULL PRIMARY KEY
		 * AUTOINCREMENT, [targetid] CHAR(32), [type] CHAR(32), [lastupdate]
		 * INTEGER, [createtime] INTEGER);
		 */
		String chatsession_sql = "create table IF NOT EXISTS "
				+ GalaxyProviderInfo.TABLE_CHATSESSION + "("
				+ "_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
				+ "targetid CHAR(32)," 
				+ "userid CHAR(32)," 
				+ "type INTEGER,"
				+ "lastupdate CHAR(32) DEFAULT ('')," 
				+ "createtime CHAR(32) DEFAULT (''));";
		/*
		 * CREATE TABLE "tb_chatgroupuser" ( [_id] INTEGER PRIMARY KEY
		 * AUTOINCREMENT, [userid] CHAR(32), [groupid] CHAR(32), [targetid]
		 * CHAR(32), [type] INTEGER(32));
		 */
		String chatgroupuser_sql = "create table IF NOT EXISTS "
				+ GalaxyProviderInfo.TABLE_ATTENTION + "("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT," + "userid CHAR(32),"
				+ "groupid CHAR(32)," + "targetid CHAR(32),"
				+ "type INTEGER(32));";
		/**
		 * 存储 动态 信息
		 */
		String feedsession_sql = "create table IF NOT EXISTS "
				+ GalaxyProviderInfo.TABLE_FEEDSESSION + "("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "userid CHAR(32) DEFAULT (''),"
				+ "feedsid CHAR(32) DEFAULT ('')," 
				+ "feedsname CHAR(32) DEFAULT ('')," 
				+ "lastupdate CHAR(32) DEFAULT ('')," 
				+ "feedsavatar TEXT DEFAULT ('')," 
				+ "type INTEGER);";
		try {
			db.execSQL(chatmessage_sql);
		} catch (SQLException e) {
		}
		try {
			db.execSQL(user_sql);
		} catch (SQLException e) {
		}
		try {
			db.execSQL(group_sql);
		} catch (SQLException e) {
		}
		try {
			db.execSQL(task_sql);
		} catch (SQLException e) {
		}
		try {
			db.execSQL(feed_sql);
		} catch (SQLException e) {
		}
		try {
			db.execSQL(comment_sql);
		} catch (SQLException e) {
		}
		try {
			db.execSQL(attention_sql);
		} catch (SQLException e) {
		}
		try {
			db.execSQL(chatsession_sql);
		} catch (SQLException e) {
		}
//		try {
//			db.execSQL(chatgroupuser_sql);
//		} catch (SQLException e) {
//		}
		try {
			db.execSQL(department_sql);
		} catch (SQLException e) {
		}
		try {
			db.execSQL(feedsession_sql);
		} catch (SQLException e) {
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}
