package com.example.galaxy08.entity;

import java.io.Serializable;

import android.content.Context;

import com.example.galaxy08.tool.DebugLog;

public class GalaxyMessage implements Serializable{
	/**
     * 
     */
    private static final long serialVersionUID = 635450851087720555L;
    
    private static Context context;
	public String Content;
	public String sourceId;
	public String sourceName;
	public String sourceCompany;
	public String sourceTitle;
	public String sourcePhoto;
	public String sourcePhotoLocalPath;
	public String msg_id;
	public String type;	
	public String dateline;
	public String isread;
	public String cardId;
	public String userId;
	public String unReadcount;
//	public String name;
//	public String matchField;
	public String totalCount;
	public String contactCount;
	public String contactedCount;
	public String colleague;
	public String from;
	
	public static String TYPE_COLLECTED         = "1";
	public static String TYPE_SWAP_CARD         = "8";
	public static String TYPE_EXCHANGE_SUCCESS  = "12";
	public static String TYPE_CARD_UPDATE  = "13";
	public static String TYPE_NEW_JW_USER  = "14";
	public static String TYPE_RECOMMEND = "15";
	//public static String TYPE_ALREADY_REQUEST  = "19";
	public static String TYPE_NOTICE_CARD_MATCHED_FROM_CONTACTS = "16";
	public static String TYPE_HAS_CARD = "17";
	public static String TYPE_RECOMMEND_COLLEAGUE = "18";
	public static String TYPE_COLLEAGUE_CARD = "19";
	public static String TYPE_LOOKWHOUSEMSG = "56897";   //看看谁在用 消息类型。 为了数据库查询。无实际应用意义
	
	/**
	 * 
	 */
	public static String REFER_CARD_DETAIL = "1";                        //名片详情 
	public static String REFER_MESSAGE_RECOMMEND = "2";                  //消息推荐
	public static String REFER_CARD_UPDATE = "3";                        //名片更新 
	public static String REFER_NEARBY = "4";                             //附近的人
	public static String REFER_SHAKE = "5";                              //摇一摇 
	public static String REFER_CARDS = "6";                              //从名片夹添加
	public static String REFER_PHONE_NUM = "7";                          //按手机号添加
	public static String REFER_COLLECTION = "8";                         //从收藏我的名片添加 
	public static String REFER_COLLECTION_ME = "9";                      //收藏我的名片
	public static String REEFR_PHONE_CONTACT = "10";                     //手机通讯录添加 
	
	
//	MessageCenter ms = new MessageCenter();
//	public static String TYPE_MSG_COLLECTED         = context.getString(R.string.msgCardCollect);//
//	public static String TYPE_MSG_EXCHANGE          = context.getString(R.string.msgCardExchang);
//	public static String TYPE_MSG_EXCHANGE_AGREE    = context.getString(R.string.msgCardExAgree);
//	public static String TYPE_MSG_EXCHANGE_DISAGREE = context.getString(R.string.msgCardExRefuse);
//	public static String TYPE_MSG_OBTAIN            = context.getString(R.string.msgCardExchang);
//	public static String TYPE_MSG_OBTAIN_AGREE      = context.getString(R.string.msgCardGetAgree);
//	public static String TYPE_MSG_OBTAIN_DISAGREE   = context.getString(R.string.msgCardGetRefuse);
	
	public void printMsg() {
		DebugLog.logd("Content=" + Content);
		DebugLog.logd("sourceId=" + sourceId);
		DebugLog.logd("sourceName=" + sourceName);
		DebugLog.logd("sourceCompany=" + sourceCompany);
		DebugLog.logd("sourceTitle=" + sourceTitle);
		DebugLog.logd("sourcePhoto=" + sourcePhoto);
		DebugLog.logd("sourcePhotoLocalPath=" + sourcePhotoLocalPath);
		DebugLog.logd("msg_id=" + msg_id);
		DebugLog.logd("type=" + type);
		DebugLog.logd("dateline=" + dateline);
		DebugLog.logd("isread=" + isread);
		DebugLog.logd("cardId=" + cardId);
		DebugLog.logd("userId=" + userId);
	}
	
	public class MessageColumns {
		public static final String ID           = "_id";     //1
		public static final String CONTENT      = "Content";     //1
		public static final String SOURCE_ID    = "sourceId";     //1
		public static final String SOURCE_NAME  = "sourceName";     //1
		public static final String SOURCE_COMPANY  = "sourceCompany";     //1
		public static final String SOURCE_TITLE  = "sourceTitle";     //1
		public static final String SOURCE_PHOTO  = "sourcePhoto";     //1
		public static final String SOURCE_PHOTO_LOCALPATH  = "sourcePhotoLocalPath";     //1
		public static final String MSG_ID       = "id";     //1
		public static final String TYPE         = "type";     //1
		public static final String DATE_LINE    = "dateline";     //1
		public static final String IS_READ    = "isread";     //1
		public static final String CARD_ID    = "cardId";     //1
		public static final String USER_ID    = "userid";
		//public static final String UNREAD_COUNT    = "unReadcount";
		public static final String TOTAL_COUNT = "totalCount";
		public static final String CONTACT_COUNT    = "contactCount";
		public static final String CONTACTED_COUNT    = "contactedCount";
		public static final String FROM    = "messageSource";
	}
	
}
