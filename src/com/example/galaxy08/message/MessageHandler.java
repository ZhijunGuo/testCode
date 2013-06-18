package com.example.galaxy08.message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.example.galaxy08.SysApplication;
import com.example.galaxy08.app.PreferenceWrapper;
import com.example.galaxy08.entity.ChatMessage;
import com.example.galaxy08.entity.ChatMessage.MESSAGE_MEDIA_TYPE;
import com.example.galaxy08.entity.ChatMessage.MESSAGE_STATUS;
import com.example.galaxy08.entity.ChatMessage.MESSAGE_TYPE;
import com.example.galaxy08.http.GalaxyApi;
import com.example.galaxy08.http.model.GalaxyResponse;
import com.example.galaxy08.http.model.ResponseMessageBean;
import com.example.galaxy08.message.MessageParserFactory.ChatMessageParser;
import com.example.galaxy08.message.MessageParserFactory.IMessageParser;
import com.example.galaxy08.message.MessageParserFactory.MessageBeanParser;
import com.example.galaxy08.message.MessageParserFactory.MessageParser;
import com.example.galaxy08.message.chat.ChatThread;
import com.example.galaxy08.tool.DebugLog;
import com.example.galaxy08.tool.Tool;

/**
 * 处理 Message
 */

public class MessageHandler extends Thread {

	public static final String INTENT_KEY_MESSAGE = "INTENT_KEY_MESSAGE";
	public static final String BROADCAST_ACTION_NEW_MESSAGE = "com.galaxy.card.action.newmessage";
	public static final int MESSAGE_HANDLE_CAPACITY = 5;

	private Map<String, MessageListener> messageListenerMap;

	private List<IMessageParser> messageQueue;

	byte[] mLock = new byte[0];

	private static MessageHandler instance = null;
	
	private ExecutorService mAddExecutor;
	
	private MessageHandler() {
		//监听消息 Map
		messageListenerMap = new HashMap<String, MessageListener>();
		messageQueue = Collections.synchronizedList(new LinkedList<IMessageParser>());
		mAddExecutor = Executors.newSingleThreadExecutor();
		userid = PreferenceWrapper.get(PreferenceWrapper.USER_ID, "0");
		sequence = PreferenceWrapper.get(Tool.combineStrings(userid, "_",PreferenceWrapper.CHAT_MESSAGE_SEQUENCE), 0);
		setPriority(Thread.MIN_PRIORITY);
		DebugLog.logd("MessageHandler", "message handler construct with userid:"+userid );
	}

	public synchronized static MessageHandler getInstance() {
		if (instance == null)
			instance = new MessageHandler();
		instance.update();
		return instance;
	}
	
	private void update(){
		userid = PreferenceWrapper.get(PreferenceWrapper.USER_ID, "0");
		sequence = PreferenceWrapper.get(Tool.combineStrings(userid, "_",PreferenceWrapper.CHAT_MESSAGE_SEQUENCE), 0);
	}

	public void handleMessage(String account, Message message) {
//		message.setType(Math.random()%2==1?Type.received:Type.error);
		DebugLog.logd("MessageHandler", "receive message" + message.toXML());
		if (message.getType() == Type.received|| message.getType() == Type.error){
			ChatThread.getInstance().receiveMessageCallback(message, message.getType() == Type.received);
		} else if(message.getType()== Type.chat || message.getType()== Type.normal || message.getType()==Type.headline){//新添加headline的处理
			addMessageTask(new MessageParser(message, account));
			MessageMaintenanceThread.getInstance().needPing();
		}
	}

	public void handleMessage(ChatMessage message) {
		addMessageTask(new ChatMessageParser(message));
	}
	
	public void handleMessage(List<IMessageParser> parsers){
		addMessageTask(parsers);
	}

	public void handleMessage(String account, List<ResponseMessageBean> messages) {
		if (messages != null && messages.size() > 0) {
			List<IMessageParser> parsers = new ArrayList<IMessageParser>(messages.size());
			for (ResponseMessageBean bean : messages) {
				parsers.add(new MessageBeanParser(bean, account));
			}
			addMessageTask(parsers);
		}
	}

	private int sequence;

	private List<ChatMessage> resultList;

	private boolean isUpdateSequence = false;
	private String userid;

	private AtomicBoolean stop = new AtomicBoolean(false);

//	private boolean needUpdate = false;
//	
//	private boolean isUpdated = false;
//	
//	private boolean needQueryCard = false;
//	
//	private boolean needQueryJwMessage = false;

	@Override
	public void run() {
		resultList = new LinkedList<ChatMessage>();
		while (!Thread.interrupted() && !stop.get()) {
			if (messageQueue.size() == 0 || resultList.size()>MESSAGE_HANDLE_CAPACITY) {
				Context context = SysApplication.application.getApplicationContext();
				//MainActivity.UpdateOfflineMsgCount(); 0412注释
				if (sequence != -1 && isUpdateSequence) {
					PreferenceWrapper.put(Tool.combineStrings(userid, "_", PreferenceWrapper.CHAT_MESSAGE_SEQUENCE), sequence);
					PreferenceWrapper.commit();
					isUpdateSequence = false;
				}
//				if(needQueryCard){
//					context.startService(new Intent(context, MessageService.class));
//					needQueryCard = false;
//				}
//				if (needUpdate && !isUpdated) {
//					//NetMethods.getInstance(context).queryUpdate(context);
//					DebugLog.logd("MessageHandler", "MainActivity.UpdateGUIQuiet()");
//				}
				//server request to query jwmessage 0412注释
//				if(needQueryJwMessage && MainActivity.mMainInstance!=null){
//					MainActivity.mMainInstance.loadMessage(false);
//					DebugLog.logd("MessageHandler", "MainActivity.mMainInstance.loadMessage(false);");
//				}
//				needQueryJwMessage &= false;
//				needUpdate &= false;
//				isUpdated &= false;
				if (resultList != null && resultList.size() > 0){
					handleNewMessage(resultList);
					resultList.clear();
				}
				if (messageQueue.size() == 0) {
					DebugLog.logd("MessageHandler", "wait");
					synchronized (mLock) {
						try {
							mLock.wait();
						} catch (InterruptedException e) {
							DebugLog.logd("MessageHandler", "mlock wait interrupted", e);
						}
					}
				}
			}else{
				IMessageParser parser = messageQueue.remove(0);
				ChatMessage message = parser.parse();
				if(message==null) continue;
				DebugLog.logd("MessageHandler", "handle message:"+message.toString());
				if (message.getType() == MESSAGE_TYPE.MESSAGE_RECEIVE) {
	
					// 只记录接受的消息的sequence
					int seqDistance = message.getSequence() - sequence;
					DebugLog.logd("MessageHandler", "receive sequence:"+message.getSequence()+ ",distance:"+ seqDistance);
					if (seqDistance > 0) {
						int oldSeq = sequence;
						sequence = message.getSequence();
						isUpdateSequence = true;
						//只对xmpp消息进行seq断开判断，如果拉去离线消息失败，则将新消息删去
						if (parser instanceof MessageParser && ((seqDistance > 1 && oldSeq != 0) || message.getMediatype()== ChatMessage.MESSAGE_MEDIA_TYPE.MEDIA_HEADLINE_LETTER_LIST)) {
							//这里需要拉取离线
							DebugLog.logd("MessageHandler","receive sequence distance > 1, need obtainofflineMessage.");
							int count = obtainOfflineMessage(message.getUserid(), oldSeq, seqDistance);
							DebugLog.logd("MessageHandler","obtainofflineMessage count:"+count);
	//						if(count>0){
							continue;
	//						}
						}
					}
					
					if (!TextUtils.isEmpty(message.getOs()) && !message.getOs().toLowerCase().contains("android")) continue;
					
	//				//无效类型直接忽略
	//				if(message.getMediatype()== MESSAGE_MEDIA_TYPE.MEDIA_INVALID) continue;
					
					//联系人判断
	//				if( !"0".equals(message.getTargetid()) 
					//&& message.getMediatype() != MESSAGE_MEDIA_TYPE.MEDIA_CARD && 
//					message.getMediatype()!= MESSAGE_MEDIA_TYPE.MEDIA_CARD_SWAP 
//							&& message.getMediatype()!= MESSAGE_MEDIA_TYPE.MEDIA_CARD_UPDATE 
//							&& message.getMediatype()!= MESSAGE_MEDIA_TYPE.MEDIA_GREET 
//							&& message.getMediatype()!= MESSAGE_MEDIA_TYPE.MEDIA_GREET_REPLY){
	//					boolean isContact = Cards.isContact(JwApplication.getAppContext(), message.getUserid(), message.getTargetid());
	//					boolean isUserType = message.getMediatype()== MESSAGE_MEDIA_TYPE.MEDIA_CARD_CONFIRM||message.getMediatype()== MESSAGE_MEDIA_TYPE.MEDIA_CARD_CONFIRM_NOTIFICATION;
	//					if((!isContact && !isUserType) || (isContact && isUserType)){
	//						DebugLog.logd("MessageHandler", "message ignored :"+message.toString());
	//						DebugLog.logd("MessageHandler", "isContact:"+isContact+",isUserType:"+isUserType);
	//						continue;//非名片类消息 且 非联系人消息不处理 或者 已是联系人的添加成功名片 统统过滤掉
	//					}
	//				}
					
					//添加非群发消息类消息的文本类消息过滤
//					if(!"0".equals(message.getTargetid()) 
//							&& message.getMediatype()== MESSAGE_MEDIA_TYPE.MEDIA_TEXT 
//							&& TextUtils.isEmpty(message.getName()) 
//							//&& !Cards.isCardHasName(JwApplication.getAppContext(), message.getUserid(), message.getTargetid())
//							){
//						DebugLog.logd("MessageHandler", "ignore message for no card found"+message.toString());
//						continue;
//					}
					
					//对与离线消息去除联系人判断功能，只剩下如果已经事联系人则忽略添加联系人消息
//					if(parser instanceof MessageBeanParser &&!"0".equals(message.getTargetid()) &&(message.getMediatype()== MESSAGE_MEDIA_TYPE.MEDIA_CARD_CONFIRM||message.getMediatype()== MESSAGE_MEDIA_TYPE.MEDIA_CARD_CONFIRM_NOTIFICATION) 
//							&& ChatMessages.isLastMessageCardNotify(JwApplication.getAppContext(), message.getUserid(), message.getTargetid())){
//						DebugLog.logd("MessageHandler", "card confirm message ignored :"+message.toString());
//						continue;//非名片类消息 且 非联系人消息不处理 或者 已是联系人的添加成功名片 统统过滤掉
//					}
	
					//push判断
					if( (message.getMediatype() == MESSAGE_MEDIA_TYPE.MEDIA_PUSH||message.getMediatype() == MESSAGE_MEDIA_TYPE.MEDIA_PUSH_GREET) && "0".equals(message.getTargetid())){
						DebugLog.logd("MessageHandler", "receive push message:"+message.toString());
						message.setStatus(ChatMessage.MESSAGE_STATUS.STATUS_UNREAD);
						resultList.add(message);
						continue;
					}
					
					//忽略已有更新
					//if(message.getMediatype()== MESSAGE_MEDIA_TYPE.MEDIA_CARD_UPDATE && null!= ChatMessages.queryChatMessage(JwApplication.getAppContext(),message.getUserid(), message.getTargetid(), message.getMediatype()))
//					if(message.getMediatype()== MESSAGE_MEDIA_TYPE.MEDIA_CARD_UPDATE && 0 < JwMessages.queryUpdateMessage(JwApplication.getAppContext(), message.getUserid(), message.getTargetid()))
//						continue;
					
					parser.process();
					
//					if(message.getMediatype().name().startsWith("MEDIA_CARDVERIFY")){
//						needQueryCard = true;
//						continue;
//					}
//					
//					//server notify to query update
//					if(message.getMediatype() == ChatMessage.MESSAGE_MEDIA_TYPE.MEDIA_HEADLINE_QUERY_UPDATE){
//						needUpdate = true;
//						continue;
//					}
//					
//					if(message.getMediatype()== ChatMessage.MESSAGE_MEDIA_TYPE.MEDIA_HEADLINE_MSG_COUNT){
//						needQueryJwMessage = true;
//						continue;
//					}
					
					if("0".equals(message.getTargetid()) && !TextUtils.isEmpty(message.getAvatar())){
						String userid  = message.getUserid();
//						String cardid = Card.getSecretoryCarid(userid);
//						//如果数据库中和服务器返回不同，则update小秘书头像
//						int row = Cards.updateSecretoryAvatar(JwApplication.getAppContext(), userid,message.getAvatar());
//						//如果数据库中头像更新，则内存中更新小秘书头像
//						//if(row>0) 
//							//IndexCardCache.getInstance(message.getUserid()).updateCardAvatarByCardid(cardid, message.getAvatar());
//						DebugLog.logd("MessageHandler", "update secretory avatar with row:"+row);
					}
					
					MessageListener messageListener = messageListenerMap.get(message.getTargetid());
					if (messageListener != null) {
						message.setStatus(MESSAGE_STATUS.STATUS_READ);
						sendMessage(MESSAGE_HANDLE_BY_LISTENER, message);
					} else {
						if (message.getMediatype() == ChatMessage.MESSAGE_MEDIA_TYPE.MEDIA_CARD_CONFIRM_NOTIFICATION) {
							// MessageActivity.agreeMessage = false;
							message.setStatus(MESSAGE_STATUS.STATUS_READ);
						} else{
							boolean readFlag = false; 
							if(parser instanceof MessageBeanParser){
								MessageBeanParser mbParser = (MessageBeanParser)parser;
								readFlag= mbParser.isRead();
							}
							message.setStatus(readFlag?MESSAGE_STATUS.STATUS_READ:MESSAGE_STATUS.STATUS_UNREAD);
						
						}
					}
					//if (ChatMessages.queryReceiveMessage(JwApplication.mContext,message.getUserid(), String.valueOf(message.getSequence())) == 0) {
//						DebugLog.logd("MessageHandler", "receive push message:"+message.toString());
//						//离线消息的及时query update
//						if (!needUpdate && message.getMediatype().name().startsWith("MEDIA_CARD"))	{
//							if(parser instanceof MessageBeanParser && !isUpdated){
//								DebugLog.logd("MessageHandler", "offline message pre query update");
//								NetMethods.getInstance(JwApplication.getAppContext()).queryUpdate(JwApplication.getAppContext());
//								isUpdated = true;
//							} else	{
//								needUpdate = true;
//							}
//						}
//						
//						//预下载音频文件
//						if(message.getMediatype() == MESSAGE_MEDIA_TYPE.MEDIA_AUDIO && TextUtils.isEmpty(message.getMediaCache()) && !TextUtils.isEmpty(message.getContent())){
////							String cachePath = Common.getAudioFile(JwApplication.getAppContext()).getAbsolutePath();
////							boolean result = false;
////							try {
////								if(!TextUtils.isEmpty(cachePath)){
////									DebugLog.logd("MessageHandler", "pre download audio file:"+cachePath);
////									result = JwHttpClient.executeHttpGetCache(message.getContent(), null, cachePath);
////								}
////							} catch (Exception e) {
////								DebugLog.logd("MessageHandler","download audio file exception", e);
////							}
////							if(result)
////								message.setMediaCache(cachePath);//下载成功插入缓存
////							if(message.getStatus() == MESSAGE_STATUS.STATUS_READ) 
////								message.setStatus(MESSAGE_STATUS.STATUS_SEALED);
//						}
						
						//预下载语音祝福音频文件
						if((message.getMediatype() == MESSAGE_MEDIA_TYPE.MEDIA_GREET ||message.getMediatype()== MESSAGE_MEDIA_TYPE.MEDIA_GREET_REPLY) && !TextUtils.isEmpty(message.getContent())){
							
//							try {
//								JSONObject json = new JSONObject(message.getContent());
//								if(json.has("voiceUrl") && !json.isNull("voiceUrl")){
//									String voiceUrl = json.optString("voiceUrl"); 
//									String localVoiceUrl = Common.getAudioCacheFile(JwApplication.getAppContext(), voiceUrl).getAbsolutePath();
//									DebugLog.logd("MessageHandler", "pre download audio file:"+localVoiceUrl);
//									boolean downloadResult = JwHttpClient.executeHttpGetCache(voiceUrl, null, localVoiceUrl);
//									DebugLog.logd("MessageHandler","download greet audio result:"+downloadResult);
//								}
//							} catch (Exception e) {
//								DebugLog.logd("MessageHandler","download greet audio file exception", e);
//							}
						}
						
						if(message.getMediatype() == MESSAGE_MEDIA_TYPE.MEDIA_CARD_UPDATE )
							message = saveUpdateMessage(message);
						//else
							//ChatMessages.insert(JwApplication.mContext, message);
					} else {
						continue;
					}
//				} else if (message.getType() == MESSAGE_TYPE.MESSAGE_SEND){
//	//				parser.process();
//					if (message.getId() > -1l) {
//						ChatMessages.update(JwApplication.mContext, message);
//						DebugLog.logd("MessageHandlerTask","update id:" + message.getId());
//					} else {
//						ChatMessages.insert(JwApplication.mContext, message);					
//						DebugLog.logd("MessageHandlerTask",
//								"insert id:" + message.getId());
//					}
//				}
				resultList.add(message);
			}
		}
	}
	
	private ChatMessage saveUpdateMessage(ChatMessage cm){
		if(cm == null)
			return cm;
		String content = cm.getContent();
		String sourceId = cm.getTargetid();
		String dateLine = String.valueOf(cm.getTimestamp());
		String seq = String.valueOf(cm.getSequence());
		String sourceName = null;
		String cardId = null;
		String sourceCompany = null;
		String sourceTitle = null;
		String sourcePhoto = null;
		JSONObject json;
//		try {
//			json = new JSONObject(content);
//			String[] projection = new String[]{"lastname", "firstname", "username", "cardid", "company", "position", "photoRemotePath"};
//		    String selectionchat = MessageColumns.USER_ID + "=? AND " + RequestParames.TARGET_ID + "=?";
//		    String[] selectionArgschat = new String[] { userid, sourceId };
//		    Cursor c = Cards.query(JwApplication.getAppContext(), projection, selectionchat, selectionArgschat, null);
//		    if(c != null){
//				if(c.getCount() > 0){
//					for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
//						sourceName = TextUtils.isEmpty(c.getString(0)+c.getString(1))?c.getString(2):(c.getString(0)+c.getString(1));
//						cardId = c.getString(3);
//						sourceCompany = c.getString(4);
//						sourceTitle = c.getString(5);
//						sourcePhoto = c.getString(6);
//					}
//				}
//			}
//		    if(TextUtils.isEmpty(cardId)){
//		    	cardId = cm.getTargetCardid();
//		    }
//		    if(TextUtils.isEmpty(sourceName)){
//				sourceName = json.optString("lastName") + json.optString("firstName");
//				if(TextUtils.isEmpty(sourceName)){
//					if(!TextUtils.isEmpty(json.optString("middleNameEn")))
//						sourceName = json.optString("firstNameEn") + " " + json.optString("middleNameEn") + " " + json.optString("lastNameEn");
//					else
//						sourceName = json.optString("firstNameEn") + " " + json.optString("lastNameEn");
//				}
//		    }
//		    if(TextUtils.isEmpty(sourceCompany)){
//		    	sourceCompany = json.optString("company");
//		    }
//		    if(TextUtils.isEmpty(sourceTitle)){
//		    	sourceTitle = json.optString("title");
//		    }
//		    if(TextUtils.isEmpty(sourcePhoto)){
//		    	sourcePhoto = cm.getAvatar();
//		    }
//		    cm.setName(sourceName);
//			String totalCount = json.optString("totalCount");
//			String contactCount = json.optString("contactCount");
//			String contactedCount = json.optString("contactedCount");
//			ContentValues cv = new ContentValues();
//			cv.put(MessageColumns.USER_ID, cm.getUserid());
//			cv.put(MessageColumns.CONTENT, JwApplication.mContext.getString(R.string.cardupdate_new));
//	        cv.put(MessageColumns.SOURCE_ID, sourceId);
//	        cv.put(MessageColumns.SOURCE_NAME, sourceName);
//	        cv.put(MessageColumns.SOURCE_COMPANY, sourceCompany);
//	        cv.put(MessageColumns.SOURCE_TITLE, sourceTitle);
//	        cv.put(MessageColumns.TYPE, JwMessage.TYPE_CARD_UPDATE);
//	        cv.put(MessageColumns.SOURCE_PHOTO, sourcePhoto);
//	        cv.put(MessageColumns.SOURCE_PHOTO_LOCALPATH, "");
//	        cv.put(MessageColumns.CARD_ID, cardId);
//	        cv.put(MessageColumns.DATE_LINE, dateLine);
//	        cv.put(MessageColumns.IS_READ, "0");
//	        cv.put(MessageColumns.MSG_ID, seq);
//	        cv.put(MessageColumns.TOTAL_COUNT, totalCount);
//	        cv.put(MessageColumns.CONTACT_COUNT, contactCount);
//	        cv.put(MessageColumns.CONTACTED_COUNT, contactedCount);
//	        JwMessages.insertMessage(JwApplication.mContext, cv);
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return cm;
	}

	//add single thread executor in case of message queue locked
	private void addMessageTask(IMessageParser parser) {
		mAddExecutor.execute(new AddRunnable(parser));
	}
	//add single thread executor in case of message queue locked
	private void addMessageTask(List<IMessageParser> parsers) {
		mAddExecutor.execute(new AddRunnable(parsers));
	}

	@Override
	public synchronized void start() {
		if (this.getState() == State.NEW) {
			super.start();
			DebugLog.logd("MessageHandler", "start");
		} else {
			synchronized (mLock) {
				mLock.notify();
			}
			DebugLog.logd("MessageHandler", "notify");
		}
	}

	private final int MESSAGE_HANDLE_BY_LISTENER = 101;

	private void sendMessage(int what, ChatMessage message) {
		handler.sendMessage(handler.obtainMessage(what, message));
	}

	private Handler handler = new Handler(Looper.getMainLooper()) {

		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MESSAGE_HANDLE_BY_LISTENER:
				if (msg.obj instanceof ChatMessage) {
					ChatMessage message = (ChatMessage) msg.obj;
					MessageListener messageListener = messageListenerMap
							.get(message.getTargetid());
					if (messageListener != null)
						messageListener.handleMessage(message);
				}
				break;
			}
		}

	};

	public void finish() {
		this.stop.set(true);
		start();
		instance = null;
		DebugLog.logd("MessageHandler", "message handler finish" );
	}

	public void addMessageListener(String targetid, MessageListener listener) {
		messageListenerMap.put(targetid, listener);
	}

	public void removeMessageListener(String targetid) {
		if (messageListenerMap.containsKey(targetid))
			messageListenerMap.remove(targetid);
	}

	public Map<String, MessageListener> getMessageListener() {
		return messageListenerMap;
	}

	public interface MessageListener {
		public void handleMessage(ChatMessage message);
	}
	//收到新消息后进行广播
	public void handleNewMessage(List<ChatMessage> messages) {
		if (messages == null || messages.size() == 0)
			return;

		ArrayList<ChatMessage> list = new ArrayList<ChatMessage>(
				messages.size());
		for (ChatMessage message : messages) {
			if (message.getType() == MESSAGE_TYPE.MESSAGE_RECEIVE) {
				MessageListener messageListener = messageListenerMap
						.get(message.getTargetid());
				if (messageListener != null)
					sendMessage(MESSAGE_HANDLE_BY_LISTENER, message);
			}
			list.add(message);
		}
		Intent broadcast = new Intent(BROADCAST_ACTION_NEW_MESSAGE);
		broadcast.putParcelableArrayListExtra(INTENT_KEY_MESSAGE, list);
		SysApplication.application.sendBroadcast(broadcast);
		DebugLog.logd("MessageHandler", "sendBroadcast complete");
	}
	
	/**
	 * synchronous method to obtain off line message and insert into message queue
	 * @param userid
	 * @param sequence
	 * @param count
	 * @return
	 */
	public int obtainOfflineMessage(String userid, int sequence, int count) {
		int result = 0;
		try {
			synchronized (messageQueue) {
				DebugLog.logd("MessageHandler","obtainOfflineMessage messageQueue locked");
				GalaxyResponse response = GalaxyApi.obtainOfflineMessage(userid, sequence, count);
				List<ResponseMessageBean> letters = response.getData().getLetters();
				if (letters != null && letters.size() != 0) {
					List<IMessageParser> parsers = new ArrayList<IMessageParser>(letters.size());
					for (ResponseMessageBean bean : letters) {
						parsers.add(new MessageBeanParser(bean, userid));
					}
					result = parsers.size();
					DebugLog.logd("MessageHandler","obtainOfflineMessage success with size:"+ parsers.size());
					messageQueue.addAll(messageQueue.size(), parsers);
				}
			}
		} catch (Exception e) {
			DebugLog.logd("MessageHandler", "obtainOfflineMessage error", e);
			result = 0;
		} finally {
			if(result>0){
				MessageHandler.this.start();
			}
			DebugLog.logd("MessageHandler","obtainOfflineMessage messageQueue unlocked");
		}

		return result;
	}
	
	/**
	 * add queue by executor service
	 *
	 */
	public class AddRunnable implements Runnable{
		
		private List<IMessageParser> parsers;
		
		public AddRunnable(List<IMessageParser> parsers) {
			super();
			this.parsers = parsers;
		}

		public AddRunnable(IMessageParser parser) {
			super();
			//生成只读的单一元素
			this.parsers = Collections.singletonList(parser);
		}

		@Override
		public void run() {
			DebugLog.logd("MessageHandler", "AddRunnable add messageQueue start");
			messageQueue.addAll(parsers);
			DebugLog.logd("MessageHandler", "AddRunnable add messageQueue end");
			MessageHandler.this.start();
		}
		
	}

}
