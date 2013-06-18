/**
 * 
 */
package com.example.galaxy08.message;


import org.jivesoftware.smack.packet.Message;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.example.galaxy08.SysApplication;
import com.example.galaxy08.entity.ChatMessage;
import com.example.galaxy08.entity.ChatMessage.MESSAGE_MEDIA_TYPE;
import com.example.galaxy08.http.model.ResponseMessageBean;
import com.example.galaxy08.tool.DebugLog;

/**
 *
 */
public class MessageParserFactory {

	public interface IMessageParser{
		public ChatMessage parse();
		public void process();
	}
	
	public static IMessageParser getMessageParser(String account, Object message){
		//目前主处理标准信息
		if(message instanceof Message){
			return new MessageParser((Message)message,account);
		}
		else if(message instanceof ResponseMessageBean){
			return new MessageBeanParser((ResponseMessageBean)message, account);
		}
		else if(message instanceof ChatMessage){
			return new ChatMessageParser((ChatMessage)message);
		}else
			return null;
	}
	
	public static class MessageParser implements IMessageParser{
		
		public Message message;
		public String userid;
		private ChatMessage chat;
		
		public MessageParser(Message message, String userid) {
			super();
			this.message = message;
			this.userid = MessageManager.getUserid(userid);
		}

		@Override
		public ChatMessage parse() {
			chat = ChatMessage.parse(message, userid);
			DebugLog.logd("XMPP", "here 1");
			return chat;
		}
		//这是干什么用的呢？
		@Override
		public void process() {
			
			MESSAGE_MEDIA_TYPE mediaType = chat.getMediatype();
			String contentMessage = chat.getContent();
			
			Context context = SysApplication.application.getApplicationContext();
			if (mediaType == MESSAGE_MEDIA_TYPE.MEDIA_CARD
					&& !TextUtils.isEmpty(contentMessage)) {
//				try {
//					JSONObject json = new JSONObject(contentMessage);
//					Card card = NetMethods.getInstance(JwApplication.getAppContext()).handleChatShareServerCard(json);
////					Card card = NetMethods.getInstance(JwApplication.getAppContext()).handleServerCardWithouBubble(json);
//					String cardid = card.getCardID();
//					if(!TextUtils.isEmpty(cardid) && !"0".equals(cardid))
//						chat.setCardid(cardid);
//				} catch (Exception e) {
//					DebugLog.logd("XMPP", "parse message card type exception",
//							e);
//				}
			} else if ((mediaType == MESSAGE_MEDIA_TYPE.MEDIA_CARD_CONFIRM || mediaType == MESSAGE_MEDIA_TYPE.MEDIA_CARD_CONFIRM_NOTIFICATION)
					&& !TextUtils.isEmpty(contentMessage)) {
//				try {
//					JSONObject json = new JSONObject(contentMessage);
//					json.putOpt("store", "true");
//					Card card;
//					if(mediaType == MESSAGE_MEDIA_TYPE.MEDIA_CARD_CONFIRM){
//						card = NetMethods.getInstance(JwApplication.getAppContext()).handleServerCard(json);
//					}else{
//						card = NetMethods.getInstance(JwApplication.getAppContext()).handleServerCardWithouBubble(json);
//					}
//					contentMessage = json.optString("message");
//					String cardid = card.getCardID();
//					if(!TextUtils.isEmpty(cardid) && !"0".equals(cardid))
//						chat.setCardid(cardid);
//				} catch (Exception e) {
//					DebugLog.logd("XMPP", "parse message card type exception",
//							e);
//				}
			} else if (mediaType == MESSAGE_MEDIA_TYPE.MEDIA_CARD_UPDATE && !TextUtils.isEmpty(contentMessage)) {
//				try {
//					JSONObject json = new JSONObject(contentMessage);
//					if (json.has("id") && !json.isNull("id")) {
//						String cardid = json.optString("id");
//						//IndexCardCache.getInstance(userid).updateCardUpdateByCardid(cardid);
//						Cards.updateCardUpdateByCardid(JwApplication.getAppContext(), userid, cardid);
//						chat.setCardid(cardid);
//					}
//				} catch (Exception e) {
//					DebugLog.logd("XMPP",
//							"parse message card update exception", e);
//				}
			} else if(mediaType == MESSAGE_MEDIA_TYPE.MEDIA_CARDVERIFY_INITIAL){
//				try {
//					JSONObject json = new JSONObject(contentMessage);
//					if(json.has("status") && 0== json.optInt("status") && json.has("data") && !json.isNull("data")){
//						JSONObject cardJson = json.optJSONObject("data");
//						NetMethods.getInstance(context).handleServerInitCard(cardJson);
//					}
//				} catch (Exception e) {
//					DebugLog.logd("XMPP", "parse message card update exception", e);
//				}
			}
			chat.setContent(contentMessage);
		}
		
	}
	
	public static class MessageBeanParser implements IMessageParser{
		
		public ResponseMessageBean message;
		public String userid;
		private ChatMessage chat;
		
		public MessageBeanParser(ResponseMessageBean message, String userid) {
			super();
			this.message = message;
			this.userid = MessageManager.getUserid(userid);
		}
		public boolean isRead(){
			return message!=null?"1".equals(message.getReadFlag()):false;
		}

		@Override
		public ChatMessage parse() {
			chat = ChatMessage.parse(message, userid);
			return chat;
		}

		@Override
		public void process() {
//			MESSAGE_MEDIA_TYPE mediaType = chat.getMediatype();
//			String contentMessage = chat.getContent();
//			Context context = SysApplication.application.getApplicationContext();
//			if (mediaType == MESSAGE_MEDIA_TYPE.MEDIA_CARD
//					&& !TextUtils.isEmpty(contentMessage)) {
//				try {
//					JSONObject json = new JSONObject(contentMessage);
//					String cardid = json.optString("id");
//					if(!TextUtils.isEmpty(cardid) && !"0".equals(cardid))
//						chat.setCardid(cardid);
//				} catch (Exception e) {
//					DebugLog.logd("XMPP", "parse message card type exception",e);
//				}
//			} else if ((mediaType == MESSAGE_MEDIA_TYPE.MEDIA_CARD_CONFIRM || mediaType == MESSAGE_MEDIA_TYPE.MEDIA_CARD_CONFIRM_NOTIFICATION)
//					&& !TextUtils.isEmpty(contentMessage)) {
//				try {
//					JSONObject json = new JSONObject(contentMessage);
//					contentMessage = json.optString("message");
//					String cardid = json.optString("id");
//					if(!TextUtils.isEmpty(cardid) && !"0".equals(cardid))
//						chat.setCardid(cardid);
//				} catch (Exception e) {
//					DebugLog.logd("XMPP", "parse message card type exception",
//							e);
//				}
//			} else if (mediaType == MESSAGE_MEDIA_TYPE.MEDIA_CARD_UPDATE
//					&& !TextUtils.isEmpty(contentMessage)) {
//				try {
//					JSONObject json = new JSONObject(contentMessage);
//					if (json.has("id") && !json.isNull("id")) {
//						String cardid = json.optString("id");
////						IndexCardCache.getInstance(userid).updateCardUpdateByCardid(cardid);
////						Cards.updateCardUpdateByCardid(JwApplication.getAppContext(), userid, cardid);
//						chat.setCardid(cardid);
//					}
//				} catch (Exception e) {
//					DebugLog.logd("XMPP",
//							"parse message card update exception", e);
//				}
//			}else if(mediaType == MESSAGE_MEDIA_TYPE.MEDIA_CARDVERIFY_INITIAL){
//				try {
//					JSONObject json = new JSONObject(contentMessage);
//					if(json.has("status") && 0== json.optInt("status") && json.has("data") && !json.isNull("data")){
//						JSONObject cardJson = json.optJSONObject("data");
//						NetMethods.getInstance(context).handleServerInitCard(cardJson);
//					}
//				} catch (Exception e) {
//					DebugLog.logd("XMPP", "parse message card update exception", e);
//				}
//			}
//			chat.setContent(contentMessage);
		}
	}
	
	public static class ChatMessageParser implements IMessageParser{
		
		private ChatMessage message;

		public ChatMessageParser(ChatMessage message){
			this.message = message;
		}
		
		@Override
		public ChatMessage parse() {
			return message;
		}

		@Override
		public void process() {
			
		}
		
	}
	
	public static class UpdateMessageParser implements IMessageParser{
		private ChatMessage old;
		private ChatMessage message;
		
		public UpdateMessageParser(ChatMessage old){
			this.old = old;
		}

		@Override
		public ChatMessage parse() {
			if(old != null) {
				message = ChatMessage.parse(old.getContent(),old.getUserid());
				message.setId(old.getId());
				message.setSequence(old.getSequence());
				message.setTimestamp(old.getTimestamp());
				message.setMediaCache(old.getMediaCache());
			}
			return message;
		}

		@Override
		public void process() {
			if(message ==null) return;
			MESSAGE_MEDIA_TYPE mediaType = message.getMediatype();
			String contentMessage = message.getContent();
			if (mediaType == MESSAGE_MEDIA_TYPE.MEDIA_CARD
					&& !TextUtils.isEmpty(contentMessage)) {
				try {
					JSONObject json = new JSONObject(contentMessage);
					String cardid = json.optString("id");
					//if(!TextUtils.isEmpty(cardid) && !"0".equals(cardid))
						//message.setCardid(cardid);
				} catch (Exception e) {
					DebugLog.logd("XMPP", "parse message card type exception",
							e);
				}
			} else if ((mediaType == MESSAGE_MEDIA_TYPE.MEDIA_CARD_CONFIRM || mediaType == MESSAGE_MEDIA_TYPE.MEDIA_CARD_CONFIRM_NOTIFICATION)
					&& !TextUtils.isEmpty(contentMessage)) {
				try {
					JSONObject json = new JSONObject(contentMessage);
					contentMessage = json.optString("message");
					String cardid = json.optString("id");
					//if(!TextUtils.isEmpty(cardid) && !"0".equals(cardid))
						//message.setCardid(cardid);
				} catch (Exception e) {
					DebugLog.logd("XMPP", "parse message card type exception",
							e);
				}
			} else if (mediaType == MESSAGE_MEDIA_TYPE.MEDIA_CARD_UPDATE
					&& !TextUtils.isEmpty(contentMessage)) {
				try {
					JSONObject json = new JSONObject(contentMessage);
					if (json.has("cardId") && json.has("message")) {
						contentMessage = json.optString("message");
					}
				} catch (Exception e) {
					DebugLog.logd("XMPP","parse message card update exception", e);
				}
			}
			message.setContent(contentMessage);
		}
	}
}
