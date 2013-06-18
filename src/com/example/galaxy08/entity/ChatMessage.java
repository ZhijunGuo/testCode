/**
 * 
 */
package com.example.galaxy08.entity;

import java.io.StringReader;
import java.util.Map;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smack.packet.Packet;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.example.galaxy08.dao.ChatmessageColumns;
import com.example.galaxy08.http.model.ResponseMessageBean;
import com.example.galaxy08.message.MessageManager;
import com.example.galaxy08.message.MessageParserFactory.IMessageParser;
import com.example.galaxy08.message.MessageParserFactory.UpdateMessageParser;
import com.example.galaxy08.message.chat.ChatTask;
import com.example.galaxy08.tool.DebugLog;
import com.example.galaxy08.tool.Tool;

/**
 * ChatMessage 聊天信息实体类 2013/04/15
 */
public class ChatMessage implements Parcelable {

	public static final int CHAT_MESSAGE_VERSION = 1;

	public static final String TAG = "ChatMessage";

	/**
	 * 无效类型，发送，接受
	 */
	public static enum MESSAGE_TYPE {
		MESSAGE_INVALID, MESSAGE_SEND, MESSAGE_RECEIVE
	}

	/**
	 * 
	 * 无效类型，文字类型，图片类型，音频类型,视频类型,名片，添加联系人确认，分享名片，名片更新，名片交换，小秘书html5,名片请求确认通知，push
	 */
	public enum MESSAGE_MEDIA_TYPE {
		MEDIA_INVALID, 
		MEDIA_TEXT, 
		MEDIA_IMAGE, 
		MEDIA_AUDIO, 
		MEDIA_VIDEO, 
		MEDIA_CARD, 
		MEDIA_CARD_CONFIRM, 
		MEDIA_CARD_UPDATE, 
		MEDIA_CARD_SWAP,
		MEDIA_HTML5,
		MEDIA_CARD_CONFIRM_NOTIFICATION,
		MEDIA_PUSH,
		MEDIA_GREET,
		MEDIA_GREET_REPLY,
		MEDIA_PUSH_GREET,
		MEDIA_CARDVERIFY_OCR,
		MEDIA_CARDVERIFY_INITIAL,
		MEDIA_CARDVERIFY_ONCE,
		MEDIA_SECRETARY_COMPLETE,
		//添加新的通知类型：20130220
		MEDIA_HEADLINE_LETTER_LIST,//message to obtain offline
		MEDIA_HEADLINE_QUERY_UPDATE,//message to query update
		MEDIA_HEADLINE_MSG_COUNT//message to query new message
	}

	/**
	 * 
	 * 无效状态， MESSAGE_TYPE = MESSAGE_SEND时的发送状态，包括：
	 * 为正在发送状态，发送成功状态，发送失败状态,存储不发送状态,上传； MESSAGE_TYPE = MESSAGE_RECEIVE
	 * 时的接受状态包括,STATUS_READ,STATUS_UNREAD，STATUS_DOWNLOAD 下载;
	 */
	public enum MESSAGE_STATUS {
		STATUS_INVALID, STATUS_SEND, STATUS_SEND_SUCCESS, STATUS_SEND_FAIL, STATUS_STORE, STATUS_READ, STATUS_UNREAD, STATUS_UPLOAD, STATUS_DOWNLOAD, STATUS_DOWNLOAD_FAIL, STATUS_SEALED
	}

	// ////////////////////////////
	// Parcelable apis
	// ////////////////////////////
	public static final Parcelable.Creator<ChatMessage> CREATOR = new Parcelable.Creator<ChatMessage>() {
		public ChatMessage createFromParcel(Parcel p) {
			return new ChatMessage(p);
		}

		public ChatMessage[] newArray(int size) {
			return new ChatMessage[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeLong(id);
		parcel.writeString(userid);
		parcel.writeString(targetid);
		parcel.writeInt(type == null ? 0 : type.ordinal());
		parcel.writeInt(mediatype.ordinal());
		parcel.writeString(content);
		parcel.writeLong(timestamp);
		parcel.writeInt(sequence);
		parcel.writeInt(status == null ? 0 : status.ordinal());
		parcel.writeString(mediaCache);
		parcel.writeInt(audioTime);
		parcel.writeString(name);
		parcel.writeString(avatar);
		parcel.writeString(targetCardid);
	}

	private long id;
	private String userid;
	//private String cardid;
	private String targetid;
	private MESSAGE_TYPE type = MESSAGE_TYPE.MESSAGE_INVALID;// 0:send 1:receive
	private MESSAGE_MEDIA_TYPE mediatype = MESSAGE_MEDIA_TYPE.MEDIA_INVALID;// 0:text
	// 1:image
	// 2:audio
	private String content;// text when mediatype==0, file source path when
	// mediatype==1 or mediatype==2
	private long timestamp;// server time
	private int sequence;
	private MESSAGE_STATUS status;
	private String mediaCache;
	private int audioTime;// 录音时间
	private String name;
	private String avatar;
	private String targetCardid;
	
	private String os;//receive only;
	//用来标记 已读(1)/未读(0)
	private int readFlag;
	//用来区别 群聊消息 和 私聊消息
	private String ts;

	/**
	 * 
	 */
	public ChatMessage() {

	}

	public ChatMessage(Parcel p) {
		this.id = p.readLong();
		this.userid = p.readString();
		//this.cardid = p.readString();
		this.targetid = p.readString();
		this.type = MESSAGE_TYPE.values()[p.readInt()];
		this.mediatype = MESSAGE_MEDIA_TYPE.values()[p.readInt()];
		this.content = p.readString();
		this.timestamp = p.readLong();
		this.sequence = p.readInt();
		this.status = MESSAGE_STATUS.values()[p.readInt()];
		this.mediaCache = p.readString();
		this.audioTime = p.readInt();
		this.name = p.readString();
		this.avatar = p.readString();
		this.targetCardid = p.readString();		
	}

	public ChatMessage(long id, String userid, String targetid,
			MESSAGE_TYPE type, MESSAGE_MEDIA_TYPE mediatype, String content,
			long timestamp, int sequence, MESSAGE_STATUS status,
			String mediaCache, int audioTime, String name, String avatar,
			String targetCardid) {
		super();
		this.id = id;
		this.userid = userid;
		//this.cardid = cardid;
		this.targetid = targetid;
		this.type = type;
		this.mediatype = mediatype;
		this.content = content;
		this.timestamp = timestamp;
		this.sequence = sequence;
		this.status = status;
		this.mediaCache = mediaCache;
		this.audioTime = audioTime;
		this.name = name;
		this.avatar = avatar;
		this.targetCardid = targetCardid;
	}

//	public ChatMessage(Cursor cursor) {
//		this.id = cursor.getLong(Columns.ID_INDEX);
//		this.userid = cursor.getString(Columns.USERID_INDEX);
//		//this.cardid = cursor.getString(Columns.CARDID_INDEX);
//		this.targetid = cursor.getString(Columns.TARGETID_INDEX);
//		this.type = MESSAGE_TYPE.values()[cursor.getInt(Columns.TYPE_INDEX)];
//		this.mediatype = MESSAGE_MEDIA_TYPE.values()[cursor
//		                                             .getInt(Columns.MEDIATYPE_INDEX)];
//		this.content = cursor.getString(Columns.CONTENT_INDEX);
//		this.timestamp = cursor.getLong(Columns.TIMESTAMP_INDEX);
//		this.sequence = cursor.getInt(Columns.SEQUENCE_INDEX);
//		this.status = MESSAGE_STATUS.values()[cursor
//		                                      .getInt(Columns.STATUS_INDEX)];
//		this.mediaCache = cursor.getString(Columns.MEDIA_CACHE_INDEX);
//		this.audioTime = cursor.getInt(Columns.AUDIO_TIME_INDEX);
//		this.name = cursor.getString(Columns.NAME_INDEX);
//		this.avatar = cursor.getString(Columns.AVATAR_INDEX);
//		this.targetCardid = cursor.getString(Columns.TARGET_CARDID_INDEX);
//	}

	public ChatMessage(Cursor cursor, int i) {
		this.targetid = cursor.getString(0);
		this.content = cursor.getString(1);
		this.timestamp = cursor.getLong(2);
		this.type = ChatMessage.MESSAGE_TYPE.values()[cursor.getInt(3)];
		this.status = ChatMessage.MESSAGE_STATUS.values()[cursor.getInt(4)];
		this.mediatype = ChatMessage.MESSAGE_MEDIA_TYPE.values()[cursor
		                                                         .getInt(5)];
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	//	public String getCardid() {
	//		return cardid;
	//	}
	//
	//	public void setCardid(String cardid) {
	//		this.cardid = cardid;
	//	}

	public String getTargetid() {
		return targetid;
	}

	public void setTargetid(String targetid) {
		this.targetid = targetid;
	}

	public MESSAGE_TYPE getType() {
		return type;
	}

	public void setType(MESSAGE_TYPE type) {
		this.type = type;
	}

	public MESSAGE_MEDIA_TYPE getMediatype() {
		return mediatype;
	}

	public void setMediatype(MESSAGE_MEDIA_TYPE mediatype) {
		this.mediatype = mediatype;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public MESSAGE_STATUS getStatus() {
		return status;
	}

	public void setStatus(MESSAGE_STATUS status) {
		this.status = status;
	}

	public String getMediaCache() {
		return mediaCache;
	}

	public void setMediaCache(String mediaCache) {
		this.mediaCache = mediaCache;
	}

	public int getAudioTime() {
		return audioTime;
	}

	public void setAudioTime(int audioTime) {
		this.audioTime = audioTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getTargetCardid() {
		return targetCardid;
	}

	public void setTargetCardid(String targetCardid) {
		this.targetCardid = targetCardid;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public int getReadFlag() {
		return readFlag;
	}

	public void setReadFlag(int readFlag) {
		this.readFlag = readFlag;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	public ContentValues getContentValues() {
		ContentValues values = new ContentValues();
		values.put(ChatmessageColumns.USERID, getUserid());
		values.put(ChatmessageColumns.TARGETID, getTargetid());
		values.put(ChatmessageColumns.TYPE, type == null ? 0 : getType().ordinal());
		values.put(ChatmessageColumns.MEDIATYPE, getMediatype().ordinal());
		values.put(ChatmessageColumns.CONTENT, getContent());
		values.put(ChatmessageColumns.TIMESTAMP, getTimestamp());
		values.put(ChatmessageColumns.SEQUENCE, getSequence());
		values.put(ChatmessageColumns.STATUS, status == null ? 0 : getStatus().ordinal());
		values.put(ChatmessageColumns.MEDIA_CACHE, getMediaCache());
		values.put(ChatmessageColumns.AUDIO_TIME, getAudioTime());
		values.put(ChatmessageColumns.NAME, getName());
		values.put(ChatmessageColumns.AVATAR, getAvatar());
		return values;
	}

	public static ChatMessage parse(Message message, String account) {
		//		DebugLog.logd(TAG, "parse message:"+message.toXML());
		ChatMessage chat = null;
		if (message != null && !TextUtils.isEmpty(account)) {
			chat = new ChatMessage();
			String userid = MessageManager.getUserid(account);
			chat.setUserid(userid);
			String targetid;
			if (message.getFrom() != null && message.getFrom().startsWith(userid)) {
				targetid = message.getTo();
				chat.setType(MESSAGE_TYPE.MESSAGE_SEND);
			} else {
				targetid = message.getFrom();
				chat.setType(MESSAGE_TYPE.MESSAGE_RECEIVE);
			}
			String contentMessage = message.getBody();
			MESSAGE_MEDIA_TYPE mediaType = parseMessageMediaType(message);
			chat.setOs(message.getOs());
			chat.setMediatype(mediaType);
			//如果是无效类型，则保存所由消息，留待升级时更新
			chat.setContent(mediaType== MESSAGE_MEDIA_TYPE.MEDIA_INVALID?message.toXML():contentMessage);
			chat.setId(-1l);
			chat.setTargetid(MessageManager.getUserid(targetid));
			chat.setTimestamp(!TextUtils.isEmpty(message.getTimestamp())
					&& TextUtils.isDigitsOnly(message.getTimestamp()) ? Long
							.parseLong(message.getTimestamp()) : 0);
			//			chat.setTimestamp(System.currentTimeMillis());
			chat.setSequence(!TextUtils.isEmpty(message.getSequence())
					&& TextUtils.isDigitsOnly(message.getSequence()) ? Integer
							.parseInt(message.getSequence()) : 0);
			chat.setAudioTime(message.getDuration());
			chat.setStatus(MESSAGE_STATUS.STATUS_INVALID);
			chat.setName(message.getName());
			chat.setAvatar(message.getAvatar());
			chat.setTargetCardid(message.getCardid());
		}
		return chat;
	}

	public static Message parse(ChatMessage chat) {
		Message message = new Message();
		if (chat.getType() == MESSAGE_TYPE.MESSAGE_RECEIVE) {
			message.setTo(MessageManager.getChatAccount(chat.getUserid()));
			message.setFrom(MessageManager.getChatAccount(chat.getTargetid()));
		} else if (chat.getType() == MESSAGE_TYPE.MESSAGE_SEND) {
			message.setTo(MessageManager.getChatAccount(chat.getTargetid()));
			message.setFrom(MessageManager.getChatAccount(chat.getUserid()));
		} else
			throw new RuntimeException("ChatMessage error message type");
		message.setBody(chat.getContent(), parseMessageBodyType(chat.getMediatype()));
		message.setSequence(chat.getSequence() + "");
		message.setTimestamp(chat.getTimestamp() + "");
		message.setDuration(chat.getAudioTime());
		message.setType(Type.chat);
		message.setVersion("1.0.0");
		message.setPacketID(ChatTask.parseMessageUniqeId(chat.getId()));
		return message;
	}

	public static final String BODY_TYPE_TEXT = "text";
	public static final String BODY_TYPE_AUDIO = "audio";
	public static final String BODY_TYPE_IMAGE = "image";
	public static final String BODY_TYPE_CARD = "card";
	public static final String BODY_ACTION_CONFIRM = "confirm";
	public static final String BODY_ACTION_SHARE = "share";
	public static final String BODY_ACTION_UPDATE = "update";
	public static final String BODY_ACTION_SWAP = "swapcard";
	public static final String BODY_TYPE_HTML5 = "html5";
	public static final String BODY_TYPE_PUSH = "push";
	public static final String BODY_TYPE_GREET="greet";
	public static final String BODY_ACTION_GREET_SEND = "send";
	public static final String BODY_ACTION_GREET_REPLY = "reply";
	public static final String BODY_ACTION_NORMAL = "normal";
	public static final String BODY_TYPE_CARDVERIFY = "cardverify";
	public static final String BODY_ACTION_CARDVERIFY_OCR = "ocr";
	public static final String BODY_ACTION_CARDVERIFY_INITIAL = "initial";
	public static final String BODY_ACTION_CARDVERIFY_ONCE = "once";
	public static final String BODY_ACTION_SECRETARY_COMPLETE = "complete";
	public static final String BODY_TYPE_HTTP = "http";
	public static final String BODY_ACTION_HTTP_LETTER_LIST = "letter";
	public static final String BODY_ACTION_HTTP_QUERY_UPDATE = "queryupdate";
	public static final String BODY_ACTION_HTTP_MSG_COUNT = "newmessage";

	/**
	 * 发送未添加MEDIA_CARD相关类型
	 * 
	 * @param type
	 * @return
	 */
	private static String parseMessageBodyType(MESSAGE_MEDIA_TYPE type) {
		String bodyType = "";
		switch (type) {
		case MEDIA_TEXT:
			bodyType = BODY_TYPE_TEXT;
			break;
		case MEDIA_AUDIO:
			bodyType = BODY_TYPE_AUDIO;
			break;
		case MEDIA_IMAGE:
			bodyType = BODY_TYPE_IMAGE;
			break;
		case MEDIA_CARD:
			bodyType = BODY_TYPE_CARD;
			break;
		case MEDIA_GREET:
			bodyType = BODY_TYPE_GREET;
		default:
			break;
		}
		return bodyType;
	}

	public static MESSAGE_MEDIA_TYPE parseMessageMediaType(Message message) {
		MESSAGE_MEDIA_TYPE type = MESSAGE_MEDIA_TYPE.MEDIA_INVALID;
		String bodyType = message.getBodyType();
		if (!TextUtils.isEmpty(bodyType)) {
			if(message.getType()== Type.headline){
				if(BODY_TYPE_HTTP.equals(bodyType)){
					String action = message.getAction();
					if( BODY_ACTION_HTTP_LETTER_LIST.equals(action)){
						//type = MESSAGE_MEDIA_TYPE.MEDIA_HEADLINE_LETTER_LIST;
					}else if(BODY_ACTION_HTTP_MSG_COUNT.equals(action)){
						//type = MESSAGE_MEDIA_TYPE.MEDIA_HEADLINE_MSG_COUNT;
					}else if(BODY_ACTION_HTTP_QUERY_UPDATE.equals(action)){
						//type = MESSAGE_MEDIA_TYPE.MEDIA_HEADLINE_QUERY_UPDATE;
					}
				}
			}else{
				if (BODY_TYPE_TEXT.equals(bodyType)){
					String action = message.getAction();
					if(!TextUtils.isEmpty(action) && BODY_ACTION_SECRETARY_COMPLETE.equals(action)){
						type = MESSAGE_MEDIA_TYPE.MEDIA_SECRETARY_COMPLETE;
					}else{
						type = MESSAGE_MEDIA_TYPE.MEDIA_TEXT;
					}
				}
				else if (BODY_TYPE_AUDIO.equals(bodyType))
					type = MESSAGE_MEDIA_TYPE.MEDIA_AUDIO;
				else if (BODY_TYPE_IMAGE.equals(bodyType))
					type = MESSAGE_MEDIA_TYPE.MEDIA_IMAGE;
				else if (BODY_TYPE_CARD.equals(bodyType)) {
					String action = message.getAction();
					if (BODY_ACTION_CONFIRM.equals(action)) {
						String source = message.getSource();
						type = !TextUtils.isEmpty(source) && "my".equals(source) ? MESSAGE_MEDIA_TYPE.MEDIA_CARD_CONFIRM_NOTIFICATION
								: MESSAGE_MEDIA_TYPE.MEDIA_CARD_CONFIRM;
					} else if (BODY_ACTION_SHARE.equals(action)) {
						type = MESSAGE_MEDIA_TYPE.MEDIA_CARD;
					} else if (BODY_ACTION_UPDATE.equals(action)) {
						type = MESSAGE_MEDIA_TYPE.MEDIA_CARD_UPDATE;
					} else if (BODY_ACTION_SWAP.equals(action)) {
						type = MESSAGE_MEDIA_TYPE.MEDIA_CARD_SWAP;
					}
				} else if (BODY_TYPE_HTML5.equals(bodyType))
					type = MESSAGE_MEDIA_TYPE.MEDIA_HTML5;
				else if (BODY_TYPE_PUSH.equals(bodyType)) {
					String action = message.getAction();
					if (BODY_TYPE_GREET.equals(action)) {
						type = MESSAGE_MEDIA_TYPE.MEDIA_PUSH_GREET;
					} else {
						type = MESSAGE_MEDIA_TYPE.MEDIA_PUSH;
					}
				} else if (BODY_TYPE_GREET.equals(bodyType)) {
					String action = message.getAction();
					if (BODY_ACTION_GREET_SEND.equals(action)) {
						type = MESSAGE_MEDIA_TYPE.MEDIA_GREET;
					} else if (BODY_ACTION_GREET_REPLY.equals(action)) {
						type = MESSAGE_MEDIA_TYPE.MEDIA_GREET_REPLY;
					}
				} else if(BODY_TYPE_CARDVERIFY.equals(bodyType)){
					String action = message.getAction();
					if(BODY_ACTION_CARDVERIFY_OCR.equals(action)){
						type = MESSAGE_MEDIA_TYPE.MEDIA_CARDVERIFY_OCR;
					} else if(BODY_ACTION_CARDVERIFY_INITIAL.equals(action)){
						type = MESSAGE_MEDIA_TYPE.MEDIA_CARDVERIFY_INITIAL;
					} else if(BODY_ACTION_CARDVERIFY_ONCE.equals(action)){
						type = MESSAGE_MEDIA_TYPE.MEDIA_CARDVERIFY_ONCE;
					}
				}
			}
		}
		return type;
	}

	public static XmlPullParser parser;
	static {
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			parser = factory.newPullParser();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
	}

	public static ChatMessage parse(ResponseMessageBean bean, String account) {
		Message message = new Message();
		message.setFrom(bean.getSender());
		message.setTo(bean.getReceiver());
		message.setSequence(bean.getSeq());
		message.setTimestamp(bean.getCreateTimeLong());
		message.setType(Type.fromString(bean.getMsgType()));
		getXmlContent(message, bean.getBody());
		ChatMessage cm = parse(message, account);
		cm.setStatus(MESSAGE_STATUS.STATUS_UNREAD);
		return cm;
	}

	private static void getXmlContent(Message message, String xmlContent) {
		if (parser != null) {
			try {
				parser.setInput(new StringReader(xmlContent));
				int eventType = parser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					if (eventType == XmlPullParser.START_TAG) {
						String elementName = parser.getName();
						if ("body".equals(elementName)) {
							String xmlLang = getLanguageAttribute(parser);
							String bodyType = parser.getAttributeValue("", "type");
							message.setAction(parser.getAttributeValue("","action"));
							message.setOs(parser.getAttributeValue("", "os"));
							message.setVersion(parser.getAttributeValue("",
									"version"));
							message.setName(parser.getAttributeValue("", "name"));
							message.setAvatar(parser.getAttributeValue("","avatar"));
							message.setSource(parser.getAttributeValue("","source"));
							message.setCardid(parser.getAttributeValue("", "cardid"));
							String duration = parser.getAttributeValue("","duration");
							if (!TextUtils.isEmpty(duration) && TextUtils.isDigitsOnly(duration))
								message.setDuration(Integer.parseInt(duration));
							DebugLog.logd("bodyType:" + bodyType
									+ ",duration:" + duration);
							message.addBody(xmlLang, parser.nextText(), bodyType);
						}
					} else if (eventType == XmlPullParser.END_TAG) {
						DebugLog.logd("End tag " + parser.getName());
					}
					eventType = parser.next();
				}
			} catch (Exception e) {
				DebugLog.logd("ChatMessage", "XmlPullParser", e);
			}
		}
	}

	private static String getLanguageAttribute(XmlPullParser parser) {
		for (int i = 0; i < parser.getAttributeCount(); i++) {
			String attributeName = parser.getAttributeName(i);
			if ("xml:lang".equals(attributeName)
					|| ("lang".equals(attributeName) && "xml".equals(parser
							.getAttributePrefix(i)))) {
				return parser.getAttributeValue(i);
			}
		}
		return null;
	}

	public static ChatMessage parser(String userid, String targetid, String templateId, String templateType, String message, 
			String voiceUrl, String voicePath, int audioTime, String picUrl, MESSAGE_STATUS status){
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"templateId\":").append(templateId).append(',');
		sb.append("\"templateType\":").append(templateType).append(',');
		sb.append("\"message\":\"").append(Tool.transStr(message)).append("\",");
		if(voiceUrl != null){
			sb.append("\"voiceUrl\":\"").append(Tool.transStr(voiceUrl)).append("\",");
		}
		sb.append("\"audioTime\":").append(audioTime).append(',');
		sb.append("\"picUrl\":\"").append(picUrl).append("\"}");
		ChatMessage cm = new ChatMessage(-1l, userid, targetid,
				MESSAGE_TYPE.MESSAGE_SEND, MESSAGE_MEDIA_TYPE.MEDIA_GREET, sb.toString(),
				System.currentTimeMillis(), 0, status,voicePath,audioTime,null,null,null);
		return cm;
	}


	public static void updateDatabase(){
		//数据库暂时不需要
		//		List<ChatMessage> messages = ChatMessages.query(
		//				SysApplication.application.getApplicationContext(), MESSAGE_MEDIA_TYPE.MEDIA_INVALID);
		//		if(messages==null || messages.size()==0) return;
		//		List<ChatMessage> updates = new ArrayList<ChatMessage>(messages.size());
		//		for(ChatMessage cm : messages){
		//			ChatMessage update = updateInvaidMessage(cm);
		//			if(update!=null && update.getMediatype() != MESSAGE_MEDIA_TYPE.MEDIA_INVALID) updates.add(update);
		//		}
		//		if(updates.size() > 0) ChatMessages.bulkUpdate(JwApplication.getAppContext(), updates); 
	}

	private static ChatMessage updateInvaidMessage(ChatMessage message){
		if(message ==null|| message.getMediatype()!= MESSAGE_MEDIA_TYPE.MEDIA_INVALID || TextUtils.isEmpty(message.getUserid())) return null;
		IMessageParser parser = new UpdateMessageParser(message);
		ChatMessage result = parser.parse();
		parser.process();
		return result;
	}

	public static ChatMessage parse(String messageString, String userid){
		if(TextUtils.isEmpty(messageString)) return null;
		try {
			parser.setInput(new StringReader(messageString));
			parser.next();
			Message msg = parseMessage(parser);
			ChatMessage result = ChatMessage.parse(msg, userid);
			return result;
		} catch (Exception e) {
			DebugLog.logd("ChatMessage", "updateInvaidMessage error", e);
			return null;
		}
	}

	/**
	 * Parses a message packet.
	 *
	 * @param parser the XML parser, positioned at the start of a message packet.
	 * @return a Message packet.
	 * @throws Exception if an exception occurs while parsing the packet.
	 */
	public static Message parseMessage(XmlPullParser parser) throws Exception {
		Message message = new Message();
		String id = parser.getAttributeValue("", "id");
		message.setPacketID(id == null ? Packet.ID_NOT_AVAILABLE : id);
		message.setTo(parser.getAttributeValue("", "to"));
		message.setFrom(parser.getAttributeValue("", "from"));
		message.setType(Message.Type.fromString(parser.getAttributeValue("", "type")));
		message.setSequence(parser.getAttributeValue("", "seq"));
		message.setTimestamp(parser.getAttributeValue("", "ts"));
		String language = getLanguageAttribute(parser);
		if (language != null && !"".equals(language.trim())) {
			message.setLanguage(language);
		}

		// Parse sub-elements. We include extra logic to make sure the values
		// are only read once. This is because it's possible for the names to appear
		// in arbitrary sub-elements.
		boolean done = false;
		String subject = null;
		String body;
		String thread = null;
		Map<String, Object> properties = null;
		while (!done) {
			int eventType = parser.next();
			if (eventType == XmlPullParser.START_TAG) {
				String elementName = parser.getName();
				String namespace = parser.getNamespace();
				if (elementName.equals("subject")) {
					if (subject == null) {
						subject = parser.nextText();
					}
				}
				else if (elementName.equals("body")) {
					String xmlLang = getLanguageAttribute(parser);
					String bodyType = parser.getAttributeValue("", "type");
					message.setAction(parser.getAttributeValue("", "action"));
					message.setOs(parser.getAttributeValue("", "os"));
					message.setVersion(parser.getAttributeValue("", "version"));
					message.setCardid(parser.getAttributeValue("", "cardid"));
					message.setName(parser.getAttributeValue("", "name"));
					message.setAvatar(parser.getAttributeValue("", "avatar"));
					message.setSource(parser.getAttributeValue("", "source"));
					String duration = parser.getAttributeValue("", "duration");
					if(!TextUtils.isEmpty(duration) && TextUtils.isDigitsOnly(duration))
						message.setDuration(Integer.parseInt(duration));
					System.out.println("bodyType:"+bodyType+",duration:"+duration+",name:"+message.getName()+",avatar:"+message.getAvatar()+",cardid:"+message.getCardid());
					body = parser.nextText();
					message.addBody(xmlLang, body, bodyType);
				}
				else if (elementName.equals("thread")) {
					if (thread == null) {
						thread = parser.nextText();
					}
				}
			}
			else if (eventType == XmlPullParser.END_TAG) {
				if (parser.getName().equals("message")) {
					done = true;
				}
			}
		}
		message.setSubject(subject);
		message.setThread(thread);
		return message;
	}
	
	public void parser(JSONObject obj){
		setContent(obj.optString("body"));
		setUserid(obj.optString("sender"));//发送者id
		setTargetid(obj.optString("recever"));//接收者id
		setSequence(obj.optInt("seq"));
		setTimestamp(obj.optLong("createTimeLong"));
		int type = obj.optInt("type");
		switch (type) {
		case 0:
			
			break;

		default:
			break;
		}
		String ts_temp = obj.optString("ts");
		if(ts_temp!=null){
			setTs(ts_temp);
		}else{
			setReadFlag(0);
		}
	}

	@Override
	public String toString() {
		return "ChatMessage [id=" + id + ", userid=" + userid + ", cardid="
				+  ", targetid=" + targetid + ", type=" + type
				+ ", mediatype=" + mediatype + ", content=" + content
				+ ", timestamp=" + timestamp + ", sequence=" + sequence
				+ ", status=" + status + ", mediaCache=" + mediaCache
				+ ", audioTime=" + audioTime + ", name=" + name + ", avatar="
				+ avatar + ", targetCardid=" + targetCardid + ", os=" + os
				+ "]";
	}
}
