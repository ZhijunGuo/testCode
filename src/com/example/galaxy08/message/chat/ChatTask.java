/**
 * 
 */
package com.example.galaxy08.message.chat;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jivesoftware.smack.packet.Message;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.example.galaxy08.SysApplication;
import com.example.galaxy08.app.PreferenceWrapper;
import com.example.galaxy08.entity.ChatMessage;
import com.example.galaxy08.entity.ChatMessage.MESSAGE_STATUS;
import com.example.galaxy08.http.GalaxyApi;
import com.example.galaxy08.http.model.GalaxyResponse;
import com.example.galaxy08.message.MessageHandler;
import com.example.galaxy08.message.MessageManager;
import com.example.galaxy08.tool.DebugLog;
import com.example.galaxy08.tool.SystemInfo;
import com.example.galaxy08.tool.Tool;


/**
 * ChatTask 聊天任务类  2013/04/15
 * 负责：操作数据库 发送消息
 */
public class ChatTask implements Runnable{

	private Chat chat;

	private ChatMessage message;
	
	private static SystemInfo mSystemInfo;

	private OnMessageSendListener mOnMessageSendListener;
	
	private AtomicBoolean cancel = new AtomicBoolean(false);

	public ChatTask(Chat chat, ChatMessage message,
			OnMessageSendListener onMessageSendListener) {
		super();
		this.chat = chat;
		this.message = message;
		this.mOnMessageSendListener = onMessageSendListener;
		mSystemInfo = new SystemInfo(SysApplication.application.getApplicationContext());
        mSystemInfo.init();
	}

	public Chat getChat() {
		return chat;
	}

	public void setChat(Chat chat) {
		this.chat = chat;
	}

	public ChatMessage getMessage() {
		return message;
	}

	public void setMessage(ChatMessage message) {
		this.message = message;
	}

	public OnMessageSendListener getOnMessageSendListener() {
		return mOnMessageSendListener;
	}

	public void setOnMessageSendListener(OnMessageSendListener mOnMessageSendListener) {
		this.mOnMessageSendListener = mOnMessageSendListener;
	}

	public void run() {
		//开始发送消息
		handler.sendEmptyMessage(MESSAGE_SEND_START);
		if(message.getId()>-1l){
			//数据库相关的操作 暂时不需要
			//ChatMessages.remove(SysApplication.application.getApplicationContext(), message.getId());
		}
		//ChatMessages.insert(SysApplication.application.getApplicationContext(), message);
		message.setTimestamp(System.currentTimeMillis());
		ChatThread.getInstance().addMessageCallback(this);
		MESSAGE_STATUS status = send(message);
		if(cancel.get()) {
			DebugLog.logd("ChatTask", "task canceled");
			return;
		}
		message.setStatus(status);
		if (status != MESSAGE_STATUS.STATUS_SEND && status!= MESSAGE_STATUS.STATUS_INVALID) {
			ChatThread.getInstance().removeMessageCallback(this);
			handler.sendEmptyMessage(MESSAGE_SEND_FINISH);
		}
	}
	
	public String getMessageUniqeId(){
		return parseMessageUniqeId(this.message.getId());
	}
	
	public static final String MESSAGE_UNIQUE_SEPERATOR = "_";
	
	public static String parseMessageUniqeId(long id){
		
		return Tool.combineStrings(mSystemInfo.getUniqid(),MESSAGE_UNIQUE_SEPERATOR+id);
	}
	
	public static String parseMessageId(String uniqeId){
		if(TextUtils.isEmpty(uniqeId)) return "";
		String prefix = Tool.combineStrings(mSystemInfo.getUniqid(),MESSAGE_UNIQUE_SEPERATOR);
		int prefix_length = prefix.length();
		if(uniqeId.startsWith(prefix) && uniqeId.length() > prefix_length){
			return uniqeId.substring(prefix_length);
		}else{
			return uniqeId;
		}
	}
	
	public long getTimestamp(){
		return message.getTimestamp();
	}
	
	private final int MESSAGE_SEND_START = 192;
	private final int MESSAGE_SEND_FINISH = 193;
	
	private Handler handler = new Handler(Looper.getMainLooper()) {

		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MESSAGE_SEND_START:
				if (null != mOnMessageSendListener)
					mOnMessageSendListener.onMessageSendStart(message);
				break;
			case MESSAGE_SEND_FINISH:
				onMessageSendFinish(message !=null && message.getStatus() == MESSAGE_STATUS.STATUS_SEND_SUCCESS);
				break;
			}
		}
	};
	
	public void onMessageSendFinish(boolean success){
		MessageHandler.getInstance().handleMessage(message);
		if (null != mOnMessageSendListener)
			mOnMessageSendListener.onMessageSendFinish(message,success);
	}
	
	MESSAGE_STATUS send(ChatMessage message){
		DebugLog.logd("XMPP", "sendMessage start");
		try {
			String userid = message.getUserid();
			Message msg = ChatMessage.parse(message);
			switch(message.getMediatype()){
			case MEDIA_AUDIO:
				if(TextUtils.isEmpty(message.getContent()) && !TextUtils.isEmpty(message.getMediaCache())){
					DebugLog.logd("XMPP", "upload file:"+message.getMediaCache());
					GalaxyResponse response = GalaxyApi.uploadChatAudio(userid, new File(message.getMediaCache()));
					if(response != null && "0".equals(response.getStatus())){
						String recordurl = response.getUrl();
						message.setContent(recordurl);
						msg.setBody(recordurl,"audio");
					}else
						throw new RuntimeException("send audio card fail:"+message.getUserid());
				}
				break;
			case MEDIA_IMAGE:
				if(TextUtils.isEmpty(message.getContent()) && !TextUtils.isEmpty(message.getMediaCache())){
					DebugLog.logd("XMPP", "upload file:"+message.getMediaCache());
					String recordurl = GalaxyApi.uploadChatImage(userid, new File(message.getMediaCache()));
					message.setContent(recordurl);
					msg.setBody(recordurl,"image");
				}
				break;

			default:
				break;
			}
			DebugLog.logd("XMPP", msg.toXML());
			if(cancel.get()) return MESSAGE_STATUS.STATUS_INVALID;
			if(MessageManager.getInstance().isLogin(userid)){
				int seq = PreferenceWrapper.get(Tool.combineStrings(userid, "_",PreferenceWrapper.CHAT_MESSAGE_SEQUENCE), -1);
				if(seq>0) msg.setAck(seq+"");
				MessageManager.getInstance().getConnection().sendPacket(msg);
			}else{
				DebugLog.logd("XMPP", "server not start yet, send message with http");
				boolean result = ChatTask.sendXmppMessage(message.getUserid(),message.getTargetid(), msg.getBodyType(), msg.getBody(), msg.getDuration()+"");
				return result?MESSAGE_STATUS.STATUS_SEND_SUCCESS:MESSAGE_STATUS.STATUS_SEND_FAIL;
			}
			DebugLog.logd("XMPP", "sendMessage success");
			return MESSAGE_STATUS.STATUS_SEND;
		} catch (Exception e) {
			//DebugLog.logd("Chat", "send message exception", e);
			DebugLog.logd("XMPP", "sendMessage fail",e);
			return MESSAGE_STATUS.STATUS_SEND_FAIL;
		}
	}

	public void cancel(){
		this.cancel.set(true);
	}
	
	private static final int MAX_RETRY_COUNT = 3;
	public static final String HOST = "http://mobile.jingwei.com/";
	public static final String CHAT_SEND_MESSAGE = HOST + "letter/send";
	public static boolean sendXmppMessage(String userid, String to, String type, String body, String duration) {
        List<NameValuePair> list = new ArrayList<NameValuePair>(4);
        list.add(new BasicNameValuePair("userId", userid));
        list.add(new BasicNameValuePair("to", to));
        list.add(new BasicNameValuePair("type", type));
        list.add(new BasicNameValuePair("body", body));
        list.add(new BasicNameValuePair("duration", duration));

        for (int retry = 0; retry < MAX_RETRY_COUNT; retry++) {
            try {
            	//????????
                //JingweiResponse response = doPost(CHAT_SEND_MESSAGE, list, JingweiResponse.class);
                //return response != null && "0".equals(response.getStatus());
            	return true;
            } catch (Exception e) {
                //DebugLog.logd(TAG, "sendXmppMessage error", e);
                //DebugLog.logd(TAG, "retry count:" + retry);
            }
        }
        return false;
    }
	
}
