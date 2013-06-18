/**
 * 
 */
package com.example.galaxy08.message;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import android.os.Handler;
import android.os.Message;

import com.example.galaxy08.SysApplication;
import com.example.galaxy08.app.PreferenceWrapper;
import com.example.galaxy08.message.iq.PingIQ;
import com.example.galaxy08.tool.Common;
import com.example.galaxy08.tool.DebugLog;
import com.example.galaxy08.tool.Tool;

/**
 * 信息保持线程(断线重新登陆)
 */
public class MessageMaintenanceThread extends Thread {

	public static final String PING_ID_PREFIX = "ping_";

	public static final int MESSAGE_RECONNECT = 10101;

	private static MessageMaintenanceThread instance;

	private AtomicBoolean finish;

	private AtomicBoolean pause;

	private AtomicBoolean receive;

	byte[] mLock = new byte[0];

	public static final int PING_TIMEOUT = 10000;
	public static final int PING_DURATION = 60000;
	public static final int PING_DURATION_BACKGROUND = PING_DURATION*10;
//	public static final int OBTAIN_OFFLINE_DURATION = 30000;// 5 minute to obtainoffline message；
//	public static final int OBTAIN_OFFLINE_DURATION_BACKGROUD = OBTAIN_OFFLINE_DURATION*10;// 10 minute to obtainoffline message backgroud；
	public static final int MAX_FAIL_COUNT = 3;

//	private long sendTime;
//
//	private long lastObtainOfflineTime = -1l;

	private AtomicReference<PingIQ> ping;

	private AtomicInteger failCount;
	
	private boolean mNeedPing = false;
	
	private boolean isBackGround = true;

	private MessageMaintenanceThread() {
		finish = new AtomicBoolean(false);
		pause = new AtomicBoolean(false);
		ping = new AtomicReference<PingIQ>();
		receive = new AtomicBoolean(false);
		failCount = new AtomicInteger(0);
		this.setDaemon(true);
		this.setPriority(Thread.MIN_PRIORITY);
		DebugLog.logd("MessageMaintenanceThread", "MessageMaintenanceThread construct");
	}

	public static synchronized MessageMaintenanceThread getInstance() {
		if (instance == null)
			instance = new MessageMaintenanceThread();
		return instance;
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			//重新登录
			case MESSAGE_RECONNECT:
				MessageManager.getInstance().login(SysApplication.application.getApplicationContext());
				break;
			}
		}

	};

	private void sendMessage(int message) {
		handler.sendEmptyMessage(message);
	}

	@Override
	public void run() {
		
		while (!isInterrupted() && !finish.get()) {
			if (pause.get()||!Tool.isNetworkServiceAvailable(SysApplication.application.getApplicationContext())) {
				DebugLog.logd("MessageMaintenanceThread", "pause");
				synchronized (mLock) {
					try {
						mLock.wait();
					} catch (InterruptedException e) {
						DebugLog.logd("MessageMaintenanceThread", "pause", e);
					}
				}
			}
			//判断当前应用是不是在前台进行
			isBackGround = Common.isApplicationBroughtToBackground(SysApplication.application.getApplicationContext());
			//前台 后台 等待时间不一样
			long waittime = !isBackGround?PING_DURATION:PING_DURATION_BACKGROUND;
			DebugLog.logd("MessageMaintenanceThread", "wait time:"+waittime);
			//获取userID
			String userid = PreferenceWrapper.get("userID", null);
			//判断是否登陆
			if (MessageManager.getInstance().isLogin(userid)) {
				if (mNeedPing) {
					//需要ping一下
					String id = PING_ID_PREFIX + System.currentTimeMillis();
					PingIQ iq = new PingIQ(id);
					int seq = PreferenceWrapper.get(Tool.combineStrings(userid,"_", PreferenceWrapper.CHAT_MESSAGE_SEQUENCE), -1);
					if (seq > 0) iq.setAck(seq + "");
					ping.set(iq);
					try {
						MessageManager.getInstance().getConnection().sendPacket(ping.get());
					} catch (Exception e) {
						DebugLog.logd("MessageMaintenanceThread","ping error",e);
						MessageManager.getInstance().disconnect();
						sendMessage(MESSAGE_RECONNECT);
					}
					DebugLog.logd("MessageMaintenanceThread","sent ping:" + iq.toXML());
					receive.set(false); 
					mNeedPing &= false;
				} 
//				waittime = pingDuration;
				DebugLog.logd("MessageMaintenanceThread", "PING_TIMEOUT, waittime:" + waittime);
			} else {
				sendMessage(MESSAGE_RECONNECT);
//				waittime = PING_DURATION;
			}

			synchronized (mLock) {
				try {
					DebugLog.logd("MessageMaintenanceThread", "lock wait, waittime:" + waittime);
					mLock.wait(waittime);
				} catch (InterruptedException e) {
					DebugLog.logd("MessageMaintenanceThread", "pause", e);
				}
			}

		}
	}

	public void finish() {
		finish.set(true);
		instance = null;
	}

	@Override
	public synchronized void start() {
		if (pause.get())
			pause.set(false);
		if (getState() == State.NEW) {
			super.start();
		} else if (getState() == State.WAITING || getState() == State.TIMED_WAITING) {
			synchronized (mLock) {
				mLock.notify();
			}
		}
	}
	
	public void back2Front(){
		DebugLog.logd("MessageMaintenanceThread", "back2Front isBackGround:"+isBackGround);
		if(isBackGround){
			start();
		}
	}

	public synchronized void pause() {
		pause.set(true);
	}

	public void receiverPong(String id) {
		DebugLog.logd("MessageMaintenanceThread", "receiv pong id:" + id);
		if (ping.get() != null && ping.get().getPacketID().equals(id)) {
			DebugLog.logd("MessageMaintenanceThread", "receiv pong success");
			failCount.set(0);
			DebugLog.logd("MessageMaintenanceThread", "failCount reset:"+ failCount.get());
			receive.set(true);
			start();
		}
	}
	
	public void needPing(){
		mNeedPing |= true;
	}

}
