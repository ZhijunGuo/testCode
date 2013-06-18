

package com.example.galaxy08.dao;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;


public class GalaxyProvider extends ContentProvider{

	private static final int CODE_MULTI_CHAT = 1;
	private static final int CODE_SINGLE_CHAT = 2;
	private static final int CODE_MULTI_USER = 3;
	private static final int CODE_SINGLE_USER = 4;
	private static final int CODE_MULTI_GROUP = 5;
	private static final int CODE_SINGLE_GROUP = 6;
	private static final int CODE_MULTI_FEED = 7;
	private static final int CODE_SINGLE_FEED = 8;
	private static final int CODE_MULTI_COMMENT = 9;
	private static final int CODE_SINGLE_COMMENT = 10;
	private static final int CODE_MULTI_ATTENTION = 11;
	private static final int CODE_SINGLE_ATTENTION = 12;
	private static final int CODE_SINGLE_TASK = 13;
	private static final int CODE_MULTI_TASK = 14;
	private static final int CODE_SINGLE_DEPARTMENT = 15;
	private static final int CODE_MULTI_DEPARTMENT = 16;
	private static final int CODE_SINGLE_CHATSESSION = 17;
	private static final int CODE_MULTI_CHATSESSION= 18;
	private static final int CODE_SINGLE_FEEDSESSION = 19;
	private static final int CODE_MULTI_FEEDSESSION= 20;

	private static UriMatcher matcher;
	static {
		matcher = new UriMatcher(UriMatcher.NO_MATCH);
		//1 聊天信息表
		matcher.addURI(GalaxyProviderInfo.AUTHORITY,
				GalaxyProviderInfo.SINGLE_CHAT_PATH, CODE_SINGLE_CHAT);
		matcher.addURI(GalaxyProviderInfo.AUTHORITY,
				GalaxyProviderInfo.MULTI_CHAT_PATH, CODE_MULTI_CHAT);
		//2 用户信息表
		matcher.addURI(GalaxyProviderInfo.AUTHORITY,
				GalaxyProviderInfo.SINGLE_USER_PATH, CODE_SINGLE_USER);
		matcher.addURI(GalaxyProviderInfo.AUTHORITY,
				GalaxyProviderInfo.MULTI_USER_PATH, CODE_MULTI_USER);
		//3 群组表
		matcher.addURI(GalaxyProviderInfo.AUTHORITY,
				GalaxyProviderInfo.SINGLE_GROUP_PATH, CODE_SINGLE_GROUP);
		matcher.addURI(GalaxyProviderInfo.AUTHORITY,
				GalaxyProviderInfo.MULTI_GROUP_PATH, CODE_MULTI_GROUP);
		//4 动态信息表
		matcher.addURI(GalaxyProviderInfo.AUTHORITY,
				GalaxyProviderInfo.SINGLE_FEED_PATH, CODE_SINGLE_FEED);
		matcher.addURI(GalaxyProviderInfo.AUTHORITY,
				GalaxyProviderInfo.MULTI_FEED_PATH, CODE_MULTI_FEED);
		//5 评论表
		matcher.addURI(GalaxyProviderInfo.AUTHORITY,
				GalaxyProviderInfo.SINGLE_COMMENT_PATH, CODE_SINGLE_COMMENT);
		matcher.addURI(GalaxyProviderInfo.AUTHORITY,
				GalaxyProviderInfo.MULTI_COMMENT_PATH, CODE_MULTI_COMMENT);
		//6 我的关注
		matcher.addURI(GalaxyProviderInfo.AUTHORITY,
				GalaxyProviderInfo.SINGLE_ATTENTION_PATH, CODE_SINGLE_ATTENTION);
		matcher.addURI(GalaxyProviderInfo.AUTHORITY,
				GalaxyProviderInfo.MULTI_ATTENTION_PATH, CODE_MULTI_ATTENTION);
		//7 任务表
		matcher.addURI(GalaxyProviderInfo.AUTHORITY,
				GalaxyProviderInfo.SINGLE_TASK_PATH, CODE_SINGLE_TASK);
		matcher.addURI(GalaxyProviderInfo.AUTHORITY,
				GalaxyProviderInfo.MULTI_TASK_PATH, CODE_MULTI_TASK);
		//8 部门表
		matcher.addURI(GalaxyProviderInfo.AUTHORITY,
				GalaxyProviderInfo.SINGLE_DEMENT_PATH, CODE_SINGLE_DEPARTMENT);
		matcher.addURI(GalaxyProviderInfo.AUTHORITY,
				GalaxyProviderInfo.MULTI_DEMENT_PATH, CODE_MULTI_DEPARTMENT);
		//9 会话信息
		matcher.addURI(GalaxyProviderInfo.AUTHORITY,
				GalaxyProviderInfo.SINGLE_CHATSESSION_PATH, CODE_SINGLE_CHATSESSION);
		matcher.addURI(GalaxyProviderInfo.AUTHORITY,
				GalaxyProviderInfo.MULTI_CHATSESSION_PATH, CODE_MULTI_CHATSESSION);
		//10 FEED信息
		matcher.addURI(GalaxyProviderInfo.AUTHORITY,
				GalaxyProviderInfo.SINGLE_FEEDSESSION_PATH, CODE_SINGLE_FEEDSESSION);
		matcher.addURI(GalaxyProviderInfo.AUTHORITY,
				GalaxyProviderInfo.MULTI_FEEDSESSION_PATH, CODE_MULTI_FEEDSESSION);
	}

	private DaoBase helper;

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int count = 0;
		String where = null;
		String tblName = null;
		switch (matcher.match(uri)) {
		//1
		case CODE_MULTI_CHAT:
			where = selection;
			tblName = GalaxyProviderInfo.TABLE_CHAT_MESSAGE;
			break;
		case CODE_SINGLE_CHAT:
			tblName = GalaxyProviderInfo.TABLE_CHAT_MESSAGE;
			where = ChatmessageColumns._ID + "=" + uri.getLastPathSegment();
			if (selection != null)
				where += " and (" + selection + ")";
			break;
			//2
		case CODE_SINGLE_USER:
			tblName = GalaxyProviderInfo.TABLE_USER;
			where = UserColumns._ID + "=" + uri.getLastPathSegment();
			if (selection != null)
				where += " and (" + selection + ")";
			break;
		case CODE_MULTI_USER:
			where = selection;
			tblName = GalaxyProviderInfo.TABLE_USER;
			break;
			//3
		case CODE_SINGLE_GROUP:
			tblName = GalaxyProviderInfo.TABLE_GROUP;
			where = GroupColumns._ID + "=" + uri.getLastPathSegment();
			if (selection != null)
				where += " and (" + selection + ")";
			break;
		case CODE_MULTI_GROUP:
			where = selection;
			tblName = GalaxyProviderInfo.TABLE_GROUP;
			break;
			//4
		case CODE_SINGLE_FEED:
			tblName = GalaxyProviderInfo.TABLE_FEED;
			where = FeedColumns._ID + "=" + uri.getLastPathSegment();
			if (selection != null)
				where += " and (" + selection + ")";
			break;
		case CODE_MULTI_FEED:
			where = selection;
			tblName = GalaxyProviderInfo.TABLE_FEED;
			break;
			//5
		case CODE_SINGLE_ATTENTION:
			tblName = GalaxyProviderInfo.TABLE_ATTENTION;
			where = AttentionColumns._ID + "=" + uri.getLastPathSegment();
			if (selection != null)
				where += " and (" + selection + ")";
			break;
		case CODE_MULTI_ATTENTION:
			where = selection;
			tblName = GalaxyProviderInfo.TABLE_ATTENTION;
			break;
			//6
		case CODE_SINGLE_COMMENT:
			tblName = GalaxyProviderInfo.TABLE_COMMENT;
			where = CommentColumns._ID + "=" + uri.getLastPathSegment();
			if (selection != null)
				where += " and (" + selection + ")";
			break;
		case CODE_MULTI_COMMENT:
			where = selection;
			tblName = GalaxyProviderInfo.TABLE_ATTENTION;
			break;
			//7
		case CODE_SINGLE_TASK:
			tblName = GalaxyProviderInfo.TABLE_TASK;
			where = TaskColumns._ID + "=" + uri.getLastPathSegment();
			if (selection != null)
				where += " and (" + selection + ")";
			break;
		case CODE_MULTI_TASK:
			where = selection;
			tblName = GalaxyProviderInfo.TABLE_TASK;
			break;
			//8
		case CODE_SINGLE_DEPARTMENT:
			tblName = GalaxyProviderInfo.TABLE_DEPARTMENT;
			where = DepartmentColumns._ID + "=" + uri.getLastPathSegment();
			if (selection != null)
				where += " and (" + selection + ")";
			break;
		case CODE_MULTI_DEPARTMENT:
			where = selection;
			tblName = GalaxyProviderInfo.TABLE_DEPARTMENT;
			break;
			//9 会话信息
		case CODE_SINGLE_CHATSESSION:
			tblName = GalaxyProviderInfo.TABLE_CHATSESSION;
			where = ChatSessionColumns._ID + "=" + uri.getLastPathSegment();
			if (selection != null)
				where += " and (" + selection + ")";
			break;
		case CODE_MULTI_CHATSESSION:
			where = selection;
			tblName = GalaxyProviderInfo.TABLE_CHATSESSION;
			break;
			//10 feed信息
		case CODE_SINGLE_FEEDSESSION:
			tblName = GalaxyProviderInfo.TABLE_FEEDSESSION;
			where = FeedSessionColumns._ID + "=" + uri.getLastPathSegment();
			if (selection != null)
				where += " and (" + selection + ")";
			break;
		case CODE_MULTI_FEEDSESSION:
			where = selection;
			tblName = GalaxyProviderInfo.TABLE_FEEDSESSION;
			break;
		default:
			throw new IllegalArgumentException("uri:" + uri);
		}

		SQLiteDatabase db = helper.getWritableDatabase();
		count = db.delete(tblName, where, selectionArgs);
		if (count > 0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}
		db.close();
		return count;
	}
	@Override
	public String getType(Uri uri) {
		switch (matcher.match(uri)) {
		//1
		case CODE_MULTI_CHAT:
			return "vnd.android.cursor.dir/chatmessage";
		case CODE_SINGLE_CHAT:
			return "vnd.android.cursor.item/chatmessage";
			//2
		case CODE_SINGLE_USER:
			return "vnd.android.cursor.item/user";
		case CODE_MULTI_USER:
			return "vnd.android.cursor.dir/user";
			//3
		case CODE_SINGLE_GROUP:
			return "vnd.android.cursor.item/group";
		case CODE_MULTI_GROUP:
			return "vnd.android.cursor.dir/group";
			//4
		case CODE_SINGLE_FEED:
			return "vnd.android.cursor.item/feed";
		case CODE_MULTI_FEED:
			return "vnd.android.cursor.dir/feed";
			//5
		case CODE_SINGLE_ATTENTION:
			return "vnd.android.cursor.item/attention";
		case CODE_MULTI_ATTENTION:
			return "vnd.android.cursor.dir/attention";
			//6
		case CODE_SINGLE_COMMENT:
			return "vnd.android.cursor.item/comment";
		case CODE_MULTI_COMMENT:
			return "vnd.android.cursor.dir/comment";
			//7
		case CODE_SINGLE_TASK:
			return "vnd.android.cursor.item/task";
		case CODE_MULTI_TASK:
			return "vnd.android.cursor.dir/task";
			//8
		case CODE_SINGLE_DEPARTMENT:
			return "vnd.android.cursor.item/department";
		case CODE_MULTI_DEPARTMENT:
			return "vnd.android.cursor.dir/department";
			//9
		case CODE_SINGLE_CHATSESSION:
			return "vnd.android.cursor.item/chatsession";
		case CODE_MULTI_CHATSESSION:
			return "vnd.android.cursor.dir/chatsession";
			//10
		case CODE_SINGLE_FEEDSESSION:
			return "vnd.android.cursor.item/feedsession";
		case CODE_MULTI_FEEDSESSION:
			return "vnd.android.cursor.dir/feedsession";
		default:
			throw new IllegalArgumentException("uri:" + uri);
		}
	}
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		String tblName = null;
		switch (matcher.match(uri)) {
		//1
		case CODE_SINGLE_CHAT:
			tblName = GalaxyProviderInfo.TABLE_CHAT_MESSAGE;
			break;
		case CODE_MULTI_CHAT:
			tblName = GalaxyProviderInfo.TABLE_CHAT_MESSAGE;
			break;
			//2
		case CODE_SINGLE_USER:
			tblName = GalaxyProviderInfo.TABLE_USER;
			break;
		case CODE_MULTI_USER:
			tblName = GalaxyProviderInfo.TABLE_USER;
			break;
			//3
		case CODE_SINGLE_GROUP:
			tblName = GalaxyProviderInfo.TABLE_GROUP;
			break;
		case CODE_MULTI_GROUP:
			tblName = GalaxyProviderInfo.TABLE_GROUP;
			break;
			//4
		case CODE_SINGLE_FEED:
			tblName = GalaxyProviderInfo.TABLE_FEED;
			break;
		case CODE_MULTI_FEED:
			tblName = GalaxyProviderInfo.TABLE_FEED;
			break;
			//5
		case CODE_SINGLE_ATTENTION:
			tblName = GalaxyProviderInfo.TABLE_ATTENTION;
			break;
		case CODE_MULTI_ATTENTION:
			tblName = GalaxyProviderInfo.TABLE_ATTENTION;
			break;
			//6
		case CODE_SINGLE_COMMENT:
			tblName = GalaxyProviderInfo.TABLE_COMMENT;
			break;
		case CODE_MULTI_COMMENT:
			tblName = GalaxyProviderInfo.TABLE_COMMENT;
			break;
			//7
		case CODE_SINGLE_TASK:
			tblName = GalaxyProviderInfo.TABLE_TASK;
			break;
		case CODE_MULTI_TASK:
			tblName = GalaxyProviderInfo.TABLE_TASK;
			break;
			//8
		case CODE_SINGLE_DEPARTMENT:
			tblName = GalaxyProviderInfo.TABLE_DEPARTMENT;
			break;
		case CODE_MULTI_DEPARTMENT:
			tblName = GalaxyProviderInfo.TABLE_DEPARTMENT;
			break;
			//9
		case CODE_SINGLE_CHATSESSION:
			tblName = GalaxyProviderInfo.TABLE_CHATSESSION;
			break;
		case CODE_MULTI_CHATSESSION:
			tblName = GalaxyProviderInfo.TABLE_CHATSESSION;
			break;
			//10
		case CODE_SINGLE_FEEDSESSION:
			tblName = GalaxyProviderInfo.TABLE_FEEDSESSION;
			break;
		case CODE_MULTI_FEEDSESSION:
			tblName = GalaxyProviderInfo.TABLE_FEEDSESSION;
			break;
		default:
			throw new IllegalArgumentException("uri:" + uri);
		}
		SQLiteDatabase db = helper.getWritableDatabase();
		long rowId = db.insert(tblName, null, values);
		if (rowId != -1) {
			db.close();
			getContext().getContentResolver().notifyChange(uri, null);
			return ContentUris.withAppendedId(uri, rowId);
		}
		return null;
	}
	@Override
	public boolean onCreate() {
		helper = new DaoBase(getContext());
		if (helper != null)
			return true;
		return false;
	}
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Cursor c = null;
		String where = null;
		String tblName = null;
		switch (matcher.match(uri)) {
		//聊天记录表
		case CODE_MULTI_CHAT:
			where = selection;
			tblName = GalaxyProviderInfo.TABLE_CHAT_MESSAGE;
			break;
		case CODE_SINGLE_CHAT:
			tblName = GalaxyProviderInfo.TABLE_CHAT_MESSAGE;
			where = ChatmessageColumns._ID + "=" + uri.getLastPathSegment();
			if (selection != null)
				where += " and (" + selection + ")";
			break;
		//用户信息表
		case CODE_SINGLE_USER:
			tblName = GalaxyProviderInfo.TABLE_USER;
			where = UserColumns._ID + "=" + uri.getLastPathSegment();
			if (selection != null)
				where += " and (" + selection + ")";
			break;
		case CODE_MULTI_USER:
			where = selection;
			tblName = GalaxyProviderInfo.TABLE_USER;
			break;
		//群组信息表
		case CODE_SINGLE_GROUP:
			tblName = GalaxyProviderInfo.TABLE_GROUP;
			where = GroupColumns._ID + "=" + uri.getLastPathSegment();
			if (selection != null)
				where += " and (" + selection + ")";
			break;
		case CODE_MULTI_GROUP:
			where = selection;
			tblName = GalaxyProviderInfo.TABLE_GROUP;
			break;
		//动态信息表
		case CODE_SINGLE_FEED:
			tblName = GalaxyProviderInfo.TABLE_FEED;
			where = FeedColumns._ID + "=" + uri.getLastPathSegment();
			if (selection != null)
				where += " and (" + selection + ")";
			break;
		case CODE_MULTI_FEED:
			where = selection;
			tblName = GalaxyProviderInfo.TABLE_FEED;
			break;
		//我的关注
		case CODE_SINGLE_ATTENTION:
			tblName = GalaxyProviderInfo.TABLE_ATTENTION;
			where = AttentionColumns._ID + "=" + uri.getLastPathSegment();
			if (selection != null)
				where += " and (" + selection + ")";
			break;
		case CODE_MULTI_ATTENTION:
			where = selection;
			tblName = GalaxyProviderInfo.TABLE_ATTENTION;
			break;
			//6
		case CODE_SINGLE_COMMENT:
			tblName = GalaxyProviderInfo.TABLE_COMMENT;
			where = AttentionColumns._ID + "=" + uri.getLastPathSegment();
			if (selection != null)
				where += " and (" + selection + ")";
			break;
		case CODE_MULTI_COMMENT:
			where = selection;
			tblName = GalaxyProviderInfo.TABLE_COMMENT;
			break;
			//7
		case CODE_SINGLE_TASK:
			tblName = GalaxyProviderInfo.TABLE_TASK;
			where = TaskColumns._ID + "=" + uri.getLastPathSegment();
			if (selection != null)
				where += " and (" + selection + ")";
			break;
		case CODE_MULTI_TASK:
			where = selection;
			tblName = GalaxyProviderInfo.TABLE_TASK;
			break;
			//8
		case CODE_SINGLE_DEPARTMENT:
			tblName = GalaxyProviderInfo.TABLE_DEPARTMENT;
			where = DepartmentColumns._ID + "=" + uri.getLastPathSegment();
			if (selection != null)
				where += " and (" + selection + ")";
			break;
		case CODE_MULTI_DEPARTMENT:
			where = selection;
			tblName = GalaxyProviderInfo.TABLE_DEPARTMENT;
			break;
			//9
		case CODE_SINGLE_CHATSESSION:
			tblName = GalaxyProviderInfo.TABLE_CHATSESSION;
			where = ChatSessionColumns._ID + "=" + uri.getLastPathSegment();
			if (selection != null)
				where += " and (" + selection + ")";
			break;
		case CODE_MULTI_CHATSESSION:
			where = selection;
			tblName = GalaxyProviderInfo.TABLE_CHATSESSION;
			break;
			//9
		case CODE_SINGLE_FEEDSESSION:
			tblName = GalaxyProviderInfo.TABLE_CHATSESSION;
			where = FeedSessionColumns._ID + "=" + uri.getLastPathSegment();
			if (selection != null)
				where += " and (" + selection + ")";
			break;
		case CODE_MULTI_FEEDSESSION:
			where = selection;
			tblName = GalaxyProviderInfo.TABLE_FEEDSESSION;
			break;
		default:
			throw new IllegalArgumentException("uri" + uri);
		}

		SQLiteDatabase db = helper.getReadableDatabase();
		c = db.query(tblName, projection, where, selectionArgs, null, null,
				sortOrder);

		return c;
	}
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int count = 0;
		String where = null;
		String tblName = null;
		switch (matcher.match(uri)) {
		case CODE_MULTI_CHAT:
			where = selection;
			tblName = GalaxyProviderInfo.TABLE_CHAT_MESSAGE;
			break;
		case CODE_SINGLE_CHAT:
			tblName = GalaxyProviderInfo.TABLE_CHAT_MESSAGE;
			where = ChatmessageColumns._ID + "=" + uri.getLastPathSegment();
			if (selection != null)
				where += " and (" + selection + ")";
			break;
		case CODE_SINGLE_USER:
			tblName = GalaxyProviderInfo.TABLE_USER;
			where = UserColumns._ID + "=" + uri.getLastPathSegment();
			if (selection != null)
				where += " and (" + selection + ")";
			break;
		case CODE_MULTI_USER:
			where = selection;
			tblName = GalaxyProviderInfo.TABLE_USER;
			break;
		case CODE_SINGLE_GROUP:
			tblName = GalaxyProviderInfo.TABLE_GROUP;
			where = GroupColumns._ID + "=" + uri.getLastPathSegment();
			if (selection != null)
				where += " and (" + selection + ")";
			break;
		case CODE_MULTI_GROUP:
			where = selection;
			tblName = GalaxyProviderInfo.TABLE_GROUP;
			break;
		case CODE_SINGLE_FEED:
			tblName = GalaxyProviderInfo.TABLE_FEED;
			where = FeedColumns._ID + "=" + uri.getLastPathSegment();
			if (selection != null)
				where += " and (" + selection + ")";
			break;
		case CODE_MULTI_FEED:
			where = selection;
			tblName = GalaxyProviderInfo.TABLE_FEED;
			break;
		case CODE_SINGLE_ATTENTION:
			tblName = GalaxyProviderInfo.TABLE_ATTENTION;
			where = AttentionColumns._ID + "=" + uri.getLastPathSegment();
			if (selection != null)
				where += " and (" + selection + ")";
			break;
		case CODE_MULTI_ATTENTION:
			where = selection;
			tblName = GalaxyProviderInfo.TABLE_ATTENTION;
			break;
			//6
		case CODE_SINGLE_COMMENT:
			tblName = GalaxyProviderInfo.TABLE_COMMENT;
			where = CommentColumns._ID + "=" + uri.getLastPathSegment();
			if (selection != null)
				where += " and (" + selection + ")";
			break;
		case CODE_MULTI_COMMENT:
			where = selection;
			tblName = GalaxyProviderInfo.TABLE_COMMENT;
			break;
			//7
		case CODE_SINGLE_TASK:
			tblName = GalaxyProviderInfo.TABLE_TASK;
			where = TaskColumns._ID + "=" + uri.getLastPathSegment();
			if (selection != null)
				where += " and (" + selection + ")";
			break;
		case CODE_MULTI_TASK:
			where = selection;
			tblName = GalaxyProviderInfo.TABLE_TASK;
			break;
			//8
		case CODE_SINGLE_DEPARTMENT:
			tblName = GalaxyProviderInfo.TABLE_DEPARTMENT;
			where = TaskColumns._ID + "=" + uri.getLastPathSegment();
			if (selection != null)
				where += " and (" + selection + ")";
			break;
		case CODE_MULTI_DEPARTMENT:
			where = selection;
			tblName = GalaxyProviderInfo.TABLE_DEPARTMENT;
			break;
			//9
		case CODE_SINGLE_CHATSESSION:
			tblName = GalaxyProviderInfo.TABLE_CHATSESSION;
			where = ChatSessionColumns._ID + "=" + uri.getLastPathSegment();
			if (selection != null)
				where += " and (" + selection + ")";
			break;
		case CODE_MULTI_CHATSESSION:
			where = selection;
			tblName = GalaxyProviderInfo.TABLE_CHATSESSION;
			break;
			//10
		case CODE_SINGLE_FEEDSESSION:
			tblName = GalaxyProviderInfo.TABLE_CHATSESSION;
			where = FeedSessionColumns._ID + "=" + uri.getLastPathSegment();
			if (selection != null)
				where += " and (" + selection + ")";
			break;
		case CODE_MULTI_FEEDSESSION:
			where = selection;
			tblName = GalaxyProviderInfo.TABLE_FEEDSESSION;
			break;
		default:
			throw new IllegalArgumentException("uri" + uri);
		}

		SQLiteDatabase db = helper.getWritableDatabase();
		count = db.update(tblName, values, where, selectionArgs);
		if (count > 0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}
		db.close();

		return count;
	}
}

