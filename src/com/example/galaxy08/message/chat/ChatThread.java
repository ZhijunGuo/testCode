/**
 * 
 */
package com.example.galaxy08.message.chat;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jivesoftware.smack.packet.Message;

import com.example.galaxy08.entity.ChatMessage;
import com.example.galaxy08.entity.ChatMessage.MESSAGE_MEDIA_TYPE;
import com.example.galaxy08.entity.ChatMessage.MESSAGE_STATUS;
import com.example.galaxy08.tool.DebugLog;

/**
 * ChatThread 聊天工作线程类  2013/04/15
 * 功能：轮询任务队列，取出队列中的任务，执行任务
 */
public class ChatThread extends Thread {

	private Queue<ChatTask> taskQueue;

	private Map<String, ChatTask> callbackMap;

	private static ChatThread instance;

	byte[] mLock = new byte[0];
	
	public static final long MESSAGE_TIMEOUT = 30000;
	
	public static final long MEDIA_MESSAGE_TIMEOUT = 60000;
	
	private AtomicBoolean finish;
	
	private static ExecutorService executor;
	
	private ChatThread() {
		taskQueue = new ConcurrentLinkedQueue<ChatTask>();
		callbackMap = Collections.synchronizedMap(new HashMap<String, ChatTask>());
		finish = new AtomicBoolean(false);
		executor = Executors.newCachedThreadPool();
		DebugLog.logd("ChatThread", "ChatThread construct");
	}

	public synchronized static ChatThread getInstance() {
		if (instance == null)
			instance = new ChatThread();
		return instance;
	}

	@Override
	public void run() {
		while (!Thread.interrupted() && !finish.get()) {
			if (taskQueue.size() == 0 && callbackMap.size() == 0) {
				DebugLog.logd("ChatThread", "wait");
				synchronized (mLock) {
					try {
						mLock.wait();
					} catch (InterruptedException e) {
						DebugLog.logd("ChatThread", "synchronized (mLock)", e);
					}
				}
			} else if (taskQueue.size() == 0 && callbackMap.size() > 0) {
				DebugLog.logd("ChatThread", "wait 1 second");
				synchronized (mLock) {
					try {
						mLock.wait(1000);
					} catch (InterruptedException e) {
						DebugLog.logd("ChatThread", "synchronized (mLock)", e);
					}
				}
			}
			ChatTask task = taskQueue.poll();
			if (task != null)
				executor.execute(task);
			for(Iterator<Entry<String,ChatTask>> itor = callbackMap.entrySet().iterator(); itor.hasNext(); ){
				Entry<String,ChatTask> entry = itor.next();
				ChatTask callbackTask = entry.getValue();
				ChatMessage cm = callbackTask.getMessage();
				//上传文件超时时间60s，普通通过消息30
				long timeout = cm.getMediatype()==MESSAGE_MEDIA_TYPE.MEDIA_AUDIO||cm.getMediatype()== MESSAGE_MEDIA_TYPE.MEDIA_IMAGE?MEDIA_MESSAGE_TIMEOUT:MESSAGE_TIMEOUT;
				if(System.currentTimeMillis()-cm.getTimestamp() > timeout){
					DebugLog.logd("ChatThread", "chatmessage timeout:"+timeout+",with id:"+cm.getId()+", with media type:"+cm.getMediatype());
					itor.remove();
					cm.setStatus(MESSAGE_STATUS.STATUS_SEND_FAIL);
					callbackTask.cancel();
					callbackTask.onMessageSendFinish(false);
				}
			}
		}
	}

	public void addTask(ChatTask task) {
		taskQueue.offer(task);
		DebugLog.logd("ChatThread", "notify");
		start();
	}
	
	@Override
	public synchronized void start() {
		State state = getState();
		DebugLog.logd("ChatThread", "thread state:"+state);
		if (state == State.NEW) {
			super.start();
		} else if(state == State.WAITING|| state == State.TIMED_WAITING){
			synchronized (mLock) {
				mLock.notify();
			}
		}
	}

	public void addMessageCallback(ChatTask task) {
		callbackMap.put(task.getMessageUniqeId(), task);
		start();
	}

	public void removeMessageCallback(ChatTask task) {
		String uniqeId = task.getMessageUniqeId();
		if (callbackMap.containsKey(uniqeId)) {
			DebugLog.logd("ChatThread", "removeMessageCallback with id:"+uniqeId);
			callbackMap.remove(uniqeId);
		}
	}
	
	public void updateMessageCallback(ChatMessage message){
		if(message==null || message.getId()>-1l) return;
		String uniqeId = ChatTask.parseMessageUniqeId(message.getId());
		if(callbackMap.containsKey(uniqeId)){
			callbackMap.get(uniqeId).setMessage(message);
		}
	}
	
	public void receiveMessageCallback(Message callback, boolean success){
		String uniqeId = callback.getPacketID();
		DebugLog.logd("ChatThread", "receiveMessageCallback, id:"+ uniqeId);
		if(callbackMap.containsKey(uniqeId)){
			ChatTask task = callbackMap.remove(uniqeId);
			DebugLog.logd("ChatThread", "receiveMessageCallback id:"+uniqeId+",success:"+success);
			task.getMessage().setStatus(success?MESSAGE_STATUS.STATUS_SEND_SUCCESS:MESSAGE_STATUS.STATUS_SEND_FAIL);
//			String ts = callback.getTimestamp();
//			if(!TextUtils.isEmpty(ts) && TextUtils.isDigitsOnly(ts)){
//				task.getMessage().setTimestamp(Long.parseLong(ts));
//			}
			task.onMessageSendFinish(success);
		}
	}
	
	public boolean containMessageCallBack(ChatMessage message){
		if(message==null|| message.getId()== -1l) return false;
		String uniqeId = ChatTask.parseMessageUniqeId(message.getId());
		return callbackMap.containsKey(uniqeId);
	}

	
	
	public void finish(){
		finish.set(true);
		executor.shutdown();
		instance = null;
		DebugLog.logd("ChatThread", "ChatThread finish");
	}

}
