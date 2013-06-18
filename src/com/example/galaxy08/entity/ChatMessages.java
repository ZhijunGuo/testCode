/**
 * 
 */
package com.example.galaxy08.entity;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.database.Cursor;
import android.util.Log;

import com.example.galaxy08.SysApplication;
import com.example.galaxy08.chat.ChatActivity;
import com.example.galaxy08.dao.ChatmessageColumns;
import com.example.galaxy08.dao.GalaxyProviderInfo;
import com.example.galaxy08.entity.ChatMessage.MESSAGE_MEDIA_TYPE;
import com.example.galaxy08.entity.ChatMessage.MESSAGE_STATUS;
import com.example.galaxy08.entity.ChatMessage.MESSAGE_TYPE;

//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//import android.content.ContentProviderOperation;
//import android.content.ContentUris;
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.net.Uri;
//
//import com.example.galaxy08.entity.ChatMessage.MESSAGE_MEDIA_TYPE;
//import com.example.galaxy08.entity.ChatMessage.MESSAGE_STATUS;
//import com.example.galaxy08.entity.ChatMessage.MESSAGE_TYPE;
//import com.example.galaxy08.entity.GalaxyMessage.MessageColumns;
//import com.example.galaxy08.http.RequestParames;
//import com.example.galaxy08.tool.DebugLog;
//import com.example.galaxy08.tool.Tool;


public class ChatMessages {
	
	/**
	 * 查询 当前用户 与 目标用户之间的聊天记录
	 */
	public static ArrayList<ChatMessage> getMessages(String userid,String targetid,int count){
		ArrayList<ChatMessage> messages_temp = null;
		ContentResolver cr = SysApplication.application.getContentResolver();
		Cursor cursor = cr.query(GalaxyProviderInfo.CHATMESSAGE_URI,
				new String[] { ChatmessageColumns._ID,
				ChatmessageColumns.CONTENT,
				ChatmessageColumns.USERID,
				ChatmessageColumns.TIMESTAMP,
				ChatmessageColumns.TARGETID,
				ChatmessageColumns.AUDIO_TIME,
				ChatmessageColumns.MEDIATYPE,
				ChatmessageColumns.MEDIA_CACHE,
				ChatmessageColumns.TYPE }, "("
						+ ChatmessageColumns.USERID + "=? and "
						+ ChatmessageColumns.TARGETID + "=?) or ("
						+ ChatmessageColumns.USERID + "=? and "
						+ ChatmessageColumns.TARGETID + "=?) ",
						new String[] { userid, targetid, targetid, userid },
						ChatmessageColumns.TIMESTAMP + " desc limit " + count
						+ ",10");
		if (cursor != null) {
			if (!cursor.moveToLast()) {
				cursor.close();
				return null;
			}
			Log.i("thread", "queryThread--start");
			messages_temp = new ArrayList<ChatMessage>();
			do {
				ChatMessage oneChat = new ChatMessage();
				oneChat.setId(cursor.getLong(cursor
						.getColumnIndex(ChatmessageColumns._ID)));
				oneChat.setUserid(cursor.getString(cursor
						.getColumnIndex(ChatmessageColumns.USERID)));
				oneChat.setTargetid(cursor.getString(cursor
						.getColumnIndex(ChatmessageColumns.TARGETID)));
				oneChat.setContent(cursor.getString(cursor
						.getColumnIndex(ChatmessageColumns.CONTENT)));
				oneChat.setTimestamp(cursor.getLong(cursor
						.getColumnIndex(ChatmessageColumns.TIMESTAMP)));
				oneChat.setMediaCache(cursor.getString(cursor
						.getColumnIndex(ChatmessageColumns.MEDIA_CACHE)));
				oneChat.setAudioTime(cursor.getInt(cursor
						.getColumnIndex(ChatmessageColumns.AUDIO_TIME)));

				oneChat.setStatus(MESSAGE_STATUS.STATUS_SEND_SUCCESS);

				switch (cursor.getInt(cursor
						.getColumnIndex(ChatmessageColumns.MEDIATYPE))) {
						case 0:
							oneChat.setMediatype(MESSAGE_MEDIA_TYPE.MEDIA_INVALID);
							break;
						case 1:
							oneChat.setMediatype(MESSAGE_MEDIA_TYPE.MEDIA_TEXT);
							break;
						case 2:
							oneChat.setMediatype(MESSAGE_MEDIA_TYPE.MEDIA_IMAGE);
							break;
						case 3:
							oneChat.setMediatype(MESSAGE_MEDIA_TYPE.MEDIA_AUDIO);
							break;

						default:
							break;
				}
				switch (cursor.getInt(cursor
						.getColumnIndex(ChatmessageColumns.TYPE))) {
						case 0:
							oneChat.setType(MESSAGE_TYPE.MESSAGE_INVALID);
							break;
						case 1:
							oneChat.setType(MESSAGE_TYPE.MESSAGE_SEND);
							break;
						case 2:
							oneChat.setType(MESSAGE_TYPE.MESSAGE_RECEIVE);
							break;
						default:
							break;
				}
				messages_temp.add(oneChat);
			} while (cursor.moveToPrevious());
			cursor.close();
		}
		return messages_temp;
		
	}

	
//	private static int update(Context context, ContentValues values, String where, String[] selectionArgs) {
//		return context.getContentResolver().update(ChatMessage.Columns.CHAT_MESSAGE_URI, values, where, selectionArgs);
//	}
//
//	public static int delete(Context context, String where, String[] selectionArgs) {
//		return context.getContentResolver().delete(ChatMessage.Columns.CHAT_MESSAGE_URI, where, selectionArgs);
//	}
//	
//	public static int deleteClerkText(Context context, String where, String[] selectionArgs) {
//		return context.getContentResolver().delete(ChatMessage.Columns.CHAT_MESSAGE_URI, where, selectionArgs);
//	}
//
//	private static long insert(Context context, ContentValues values) {
//		long id;
//		try{
//			Uri uri = context.getContentResolver().insert(ChatMessage.Columns.CHAT_MESSAGE_URI, values);
//			id = ContentUris.parseId(uri);
//		}catch(Exception e){
//			id = -1;
//		}
//		return id;
//	}
//	
//	public static int bulkInsert(Context context, List<ChatMessage> list){
//		if(list == null || list.size() == 0) return 0;
//		return context.getContentResolver().bulkInsert(ChatMessage.Columns.CHAT_MESSAGE_URI, getContentValues(list));
//	}
//	
//	private static ContentValues[] getContentValues(List<ChatMessage> list){
//		int count = list.size();
//		ContentValues[] values = new ContentValues[count];
//		for(int i = 0; i < count; i++){
//			values[i] = list.get(i).getContentValues();
//		}
//		return values;
//	}
//	
//	private static Cursor getQueryCursor(Context context, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
//		Cursor cursor = context.getContentResolver().query(ChatMessage.Columns.CHAT_MESSAGE_URI, projection, selection, selectionArgs, sortOrder);
//		return cursor;
//	}
//	
//	private static List<ChatMessage> queryFromCursor(Cursor cursor){
//		List<ChatMessage> list;
//		if (cursor != null) {
//			list = new ArrayList<ChatMessage>(cursor.getCount());
//			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
//				list.add(new ChatMessage(cursor));
//			}
//			cursor.close();
//		} else {
//			list = Collections.emptyList();
//		}
//		return list;
//	}
//	
//	private static List<ChatMessage> queryCursor(Cursor cursor){
//		List<ChatMessage> list;
//		if (cursor != null) {
//			list = new ArrayList<ChatMessage>(cursor.getCount());
//			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
//				list.add(new ChatMessage(cursor, 0));
//			}
//			cursor.close();
//		} else {
//			list = Collections.emptyList();
//		}
//		return list;
//	}
//	
//	public static long insert(Context context, ChatMessage message){
//		long id = insert(context,message.getContentValues());
//		message.setId(id);
//		return id;
//	}
//	
//	public static List<ChatMessage> query(Context context, String[] projection, String selection, String[] selectionArgs, String sortOrder){
//		Cursor cursor = getQueryCursor(context, projection, selection, selectionArgs, sortOrder);
//		return queryFromCursor(cursor);
//	}
//	
//	public static List<ChatMessage> query(Context context, String selection, String[] selectionArgs){
//		return query(context, ChatMessage.Columns.QUERY_COLUMNS, selection, selectionArgs, ChatMessage.Columns.DEFAULT_SORT_ORDER);
//	}
//	
//	public static List<ChatMessage> query(Context context, String selection, String[] selectionArgs, String orderby){
//		return query(context, ChatMessage.Columns.QUERY_COLUMNS, selection, selectionArgs, orderby);
//	}
//	
//	public static List<ChatMessage> query(Context context, String userid, String targetid){
//		String selection = "";
//		if("0".equals(targetid)){
////			selection = Tool.combineStrings(ChatMessage.Columns.USERID," = ? and ",ChatMessage.Columns.TARGETID," = ? and ",ChatMessage.Columns.MEDIATYPE," = ?");			
////			String[] selectionArgs = {userid,targetid,ChatMessage.MESSAGE_MEDIA_TYPE.MEDIA_TEXT.ordinal()+""};
//			selection=Tool.combineStrings(ChatMessage.Columns.USERID," = ? and ",ChatMessage.Columns.TARGETID," = ?");
//			String[] selectionArgs={userid,targetid};
//			return query(context,selection,selectionArgs);
//		}else{
//			selection = Tool.combineStrings(ChatMessage.Columns.USERID," = ? and ",ChatMessage.Columns.TARGETID," = ?");
//			String[] selectionArgs = {userid,targetid};
//			return query(context,selection,selectionArgs);
//		}
//		
//	}
//	/**
//	 * 加载更多
//	 * @param context
//	 * @param userid
//	 * @param targetid
//	 * @return
//	 */
//	public static List<ChatMessage> querymore(Context context, String userid, String targetid,String messageId){
////		if(targetid.equals("0")){
////			String selection = Tool.combineStrings(ChatMessage.Columns.USERID," = ? and ",ChatMessage.Columns.TARGETID," = ? and _id"+"< ?  and ",ChatMessage.Columns.MEDIATYPE," = ?");
////			String[] selectionArgs = {userid,targetid,messageId,ChatMessage.MESSAGE_MEDIA_TYPE.MEDIA_TEXT.ordinal()+""};
////			return query(context,selection,selectionArgs);
////		}else{
//			String selection = Tool.combineStrings(ChatMessage.Columns.USERID," = ? and ",ChatMessage.Columns.TARGETID," = ? and _id"+"< ?");
//			String[] selectionArgs = {userid,targetid,messageId};
//			return query(context,selection,selectionArgs);
////		}
//		
//		
//	}
//	
//	public static List<ChatMessage> queryNewMessage(Context context, String userid, String targetid){
//		String[] projection = {ChatMessage.Columns.TARGETID, ChatMessage.Columns.CONTENT, "max("+ChatMessage.Columns.TIMESTAMP+")"
//								, ChatMessage.Columns.TYPE, ChatMessage.Columns.STATUS, ChatMessage.Columns.MEDIATYPE};
//		String selection = Tool.combineStrings(ChatMessage.Columns.USERID," = ? and ",ChatMessage.Columns.TARGETID," = ?");
//		String[] selectionArgs = {userid,targetid};
//		Cursor cursor = getQueryCursor(context, projection, selection, selectionArgs, ChatMessage.Columns.DEFAULT_SORT_ORDER);
//		return queryCursor(cursor);
//	}
//	
//	public static int updateMediaCache(Context context, String mediaCache, long id){
//		Uri uri =ContentUris.withAppendedId(ChatMessage.Columns.CHAT_MESSAGE_URI,id);
//		ContentValues values = new ContentValues();
//		values.put(ChatMessage.Columns.MEDIA_CACHE, mediaCache);
//		return context.getContentResolver().update(uri, values, null, null);
//	}
//	
////	public static int updateMessageStatus(Context context, String mUserID, String mTargetID){
////		String where = ChatMessage.Columns.USERID + "=? and " + ChatMessage.Columns.TARGETID + "=? and " + ChatMessage.Columns.STATUS + "=?";
////		String[] selectionArgs = new String[]{mUserID, mTargetID, String.valueOf(ChatMessage.MESSAGE_STATUS.STATUS_UNREAD.ordinal())};
////		ContentValues values = new ContentValues();		
////		values.put(ChatMessage.Columns.STATUS, ChatMessage.MESSAGE_STATUS.STATUS_READ.ordinal());
////		return update(context, values, where, selectionArgs);
////	}
//	
//	public static int updateMessageStatus(Context context, String mUserID, String mTargetID){
//		String where1 = ChatMessage.Columns.USERID + "=? and " + ChatMessage.Columns.TARGETID + "=? and " + ChatMessage.Columns.STATUS + "=? and " + ChatMessage.Columns.MEDIATYPE + "<>?";
//		String[] selectionArgs = new String[]{mUserID, mTargetID, String.valueOf(ChatMessage.MESSAGE_STATUS.STATUS_UNREAD.ordinal()), String.valueOf(ChatMessage.MESSAGE_MEDIA_TYPE.MEDIA_AUDIO.ordinal())};
//		ContentValues values1 = new ContentValues();		
//		values1.put(ChatMessage.Columns.STATUS, ChatMessage.MESSAGE_STATUS.STATUS_READ.ordinal());
//		int update1 = update(context, values1, where1, selectionArgs);
//		
//		String where2 = ChatMessage.Columns.USERID + "=? and " + ChatMessage.Columns.TARGETID + "=? and " + ChatMessage.Columns.STATUS + "=? and " + ChatMessage.Columns.MEDIATYPE + "=?";
//		ContentValues values2 = new ContentValues();		
//		values2.put(ChatMessage.Columns.STATUS, ChatMessage.MESSAGE_STATUS.STATUS_SEALED.ordinal());
//		int update2 = update(context, values2, where2, selectionArgs);
//		
//		int update = update1 + update2;
//		return update;
//	}
//	
//	public static int updateSingleMessageStatus(Context context, String mUserID, String mMessageId,int status){
//		String where = ChatMessage.Columns.USERID + "=? and _id=?" ;
//		String[] selectionArgs = new String[]{mUserID, mMessageId};
//		ContentValues values = new ContentValues();		
//		values.put(ChatMessage.Columns.STATUS, status);
//		return update(context, values, where, selectionArgs);
//	}
//	
//	public static int update(Context context, ChatMessage message){
//		return update(context,message.getContentValues(), Tool.combineStrings(ChatMessage.Columns._ID,"=",String.valueOf(message.getId())),null);
//	}
//	
//	public static int queryReceiveMessage(Context context, String userid, String sequence){
//		String[] projection = {"count(_id)"};
//		String selection = Tool.combineStrings(ChatMessage.Columns.USERID," =? and ",ChatMessage.Columns.TYPE," = ? and ", ChatMessage.Columns.SEQUENCE," = ?");
//		String[] selectionArgs = new String[]{userid, MESSAGE_TYPE.MESSAGE_RECEIVE.ordinal()+"", sequence};
//		return getSingleData(context, projection, selection, selectionArgs, "",0);
//	}
//	
////	private static String getSingleData(Context context, String[] projection,
////			String selection, String[] selectionArgs, String sortOrder, String defaultValue) {
////		String result = defaultValue;
////		Cursor cursor = getQueryCursor(context, projection, selection,
////				selectionArgs, sortOrder);
////		if (cursor != null) {
////			if (cursor.moveToFirst())
////				result = cursor.getString(0);
////			cursor.close();
////		}
////		return result;
////	}
//	
//	private static int getSingleData(Context context, String[] projection,
//			String selection, String[] selectionArgs, String sortOrder, int defaultValue) {
//		int result = defaultValue;
//		Cursor cursor = getQueryCursor(context, projection, selection,
//				selectionArgs, sortOrder);
//		if (cursor != null) {
//			if (cursor.moveToFirst())
//				result = cursor.getInt(0);
//			cursor.close();
//		}
//		return result;
//	}
//	
//	public static int remove(Context context, long id){
//		if(context ==null || id<0) return 0;
//		Uri uri = ContentUris.withAppendedId(ChatMessage.Columns.CHAT_MESSAGE_URI, id);
//		return context.getContentResolver().delete(uri, null, null);
//	}
//	
//	public static Cursor getCardUpdateContent(Context context, String userId, String targetId){
//		String[] projection = new String[]{"content"};
//		String selection = MessageColumns.USER_ID + "=" + userId + " AND " + RequestParames.TARGET_ID + "=" + targetId + " AND " 
//							+ ChatMessage.Columns.MEDIATYPE + "=" + ChatMessage.MESSAGE_MEDIA_TYPE.MEDIA_CARD_UPDATE.ordinal();
//		return getQueryCursor(context, projection, selection, null, null); 
//	}
//	public static Cursor getCardShareContent(Context context, String userId, String targetId){
//		String[] projection = new String[]{"content"};
//		String selection = MessageColumns.USER_ID + "=" + userId + " AND " + RequestParames.TARGET_ID + "=" + targetId + " AND " 
//							+ ChatMessage.Columns.MEDIATYPE + "=" + ChatMessage.MESSAGE_MEDIA_TYPE.MEDIA_CARD.ordinal();
//		return getQueryCursor(context, projection, selection, null, null); 
//	}
//	public static int deleteMessages(Context context, String userid, String targetid){
//		String selections = ChatMessage.Columns.TARGETID + "=? and " + ChatMessage.Columns.USERID + "=?";
//		String[] selectionArgs = {targetid, userid};
//		return ChatMessages.delete(context, selections,selectionArgs);
//	}
//	
//	public static int updateSendFailMessage(Context context, List<Long> ids){
//		if(ids == null || ids.size()==0) return 0;
//		StringBuilder sb = new StringBuilder();
//		for(Long id : ids){ sb.append(id).append(',');}
//		sb.deleteCharAt(sb.length()-1);
//		String selection = ChatMessage.Columns._ID +" in (?)";
//		String[] selectionArgs = {sb.toString()};
//		ContentValues values = new ContentValues();
//		values.put(ChatMessage.Columns.STATUS, MESSAGE_STATUS.STATUS_SEND_FAIL.ordinal());
//		return ChatMessages.update(context, values, selection, selectionArgs);
//	}
//	
//	public static List<ChatMessage> query(Context context, MESSAGE_MEDIA_TYPE mediaType){
//		if(mediaType==null) return Collections.emptyList();
//		String selection = Tool.combineStrings(ChatMessage.Columns.MEDIATYPE," = ?");
//		String[] selectionArgs = {mediaType.ordinal()+""};
//		return ChatMessages.query(context, ChatMessage.Columns.QUERY_COLUMNS, selection, selectionArgs, null);
//	}
//	
//	public static int bulkUpdate(Context context, List<ChatMessage> messages){
//		if(messages==null || messages.size()==0) return 0;
//		ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>(messages.size());
//		for(ChatMessage cm : messages){
//			String selection = Tool.combineStrings(ChatMessage.Columns._ID,"=?");
//			String[] selectionArgs = { cm.getId()+""};
//			ContentValues values = cm.getContentValues();
//			ContentProviderOperation operation = ContentProviderOperation.newUpdate(ChatMessage.Columns.CHAT_MESSAGE_URI).withValues(values).withSelection(selection, selectionArgs).build();
//			operations.add(operation);
//		}
//		try {
////			ContentProviderResult[] results = context.getContentResolver().applyBatch(JwProvider.AUTHORITY, operations);
////			return results ==null?0:results.length;
//			return 0;
//		} catch (Exception e) {
//			DebugLog.logd("ChatMessages", "bulk update error",e);
//			return 0;
//		}
//	}
//	
//	public static int bulkUpdateSequence(Context context, List<ChatMessage> messages){
//		if(messages==null || messages.size()==0) return 0;
//		ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>(messages.size());
//		for(ChatMessage cm : messages){
//			String selection = Tool.combineStrings(ChatMessage.Columns.USERID,"= ? and ", ChatMessage.Columns.SEQUENCE, " = ?");
//			String[] selectionArgs = { cm.getUserid(),cm.getSequence()+""};
//			ContentValues values = cm.getContentValues();
//			ContentProviderOperation operation = ContentProviderOperation.newUpdate(ChatMessage.Columns.CHAT_MESSAGE_URI).withValues(values).withSelection(selection, selectionArgs).build();
//			operations.add(operation);
//		}
//		try {
////			ContentProviderResult[] results = context.getContentResolver().applyBatch(JwProvider.AUTHORITY, operations);
////			return results ==null?0:results.length;
//			return 0;
//		} catch (Exception e) {
//			DebugLog.logd("ChatMessages", "bulk update error",e);
//			return 0;
//		}
//	}
//	
//	public static ChatMessage queryNameAvatar(Context context, String selection, String[] selectionArgs){
//		 String[] projection = new String[]{"name","avatar","target_cardid"};
//		 ChatMessage cm = null;
//		 Cursor cur = getQueryCursor(context,projection,selection,selectionArgs,null);
//		 if(cur != null){
//		 for (cur.moveToFirst(); !cur.isAfterLast();){
//			 	cm = new ChatMessage();
//				cm.setName(cur.getString(0));
//				cm.setAvatar(cur.getString(1));
//				cm.setTargetCardid(cur.getString(2));
//				break;
//		}
//		 cur.close();
//		}
//		return cm;
//	}
//	
//	public static int queryMinSequence(Context context, String userid){
//		String[] projection={"min(sequence)"};
//		String selection = Tool.combineStrings(ChatMessage.Columns.USERID,"= ? and ",ChatMessage.Columns.SEQUENCE," > 0");
//		String[] selectionArgs = {userid};
//		return getSingleData(context, projection, selection, selectionArgs, null, 0);
//	}
//	
//	public static ChatMessage queryChatMessage(Context context, String userid, String targetid, MESSAGE_MEDIA_TYPE mediaType){
//		if(mediaType==null|| mediaType== MESSAGE_MEDIA_TYPE.MEDIA_INVALID) return null;
//		String selection = Tool.combineStrings(ChatMessage.Columns.USERID,
//				"=? and ",
//				ChatMessage.Columns.TARGETID,
//				"=? and ",
//				ChatMessage.Columns.MEDIATYPE,
//				"=?");
//		String[] selectionArgs = {userid, targetid, mediaType.ordinal()+""};
//		Cursor  cursor = getQueryCursor(context, ChatMessage.Columns.QUERY_COLUMNS, selection, selectionArgs, null);
//		if(cursor ==null) return null;
//		ChatMessage cm = null;
//		if(cursor.moveToFirst()){
//			cm = new ChatMessage(cursor);
//		}
//		cursor.close();
//		return cm;
//	}
//	
//	/**
//	 * 
//	 * @param context
//	 * @param oldUser old user id
//	 * @param newUser new user id
//	 * @return
//	 */
//	public static int updateChatUser(Context context, String oldUser, String newUser){
//		String selection = Tool.combineStrings(ChatMessage.Columns.USERID,"=?");
//		String[] selectionArgs = {oldUser};
//		ContentValues values = new ContentValues();
//		values.put(ChatMessage.Columns.USERID, newUser);
//		return update(context, values, selection, selectionArgs);
//	}
//	
//	
//	public static boolean isLastMessageCardNotify(Context context, String userid, String targetid){
//		String selection = "userid=? and targetid=?";
//		String[] selectionArgs = {userid, targetid};
//		String orderby ="_id desc limit 1";
//		List<ChatMessage> messages = query(context, selection, selectionArgs,orderby);
//		if(messages!=null && messages.size()==1){
//			MESSAGE_MEDIA_TYPE type = messages.get(0).getMediatype();
//			return type==MESSAGE_MEDIA_TYPE.MEDIA_CARD_CONFIRM||type==MESSAGE_MEDIA_TYPE.MEDIA_CARD_CONFIRM_NOTIFICATION;
//		}
//		return false;
//	}
}
