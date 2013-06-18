package com.example.galaxy08;

import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jivesoftware.smack.ChatManager;

import android.app.Application;
import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;

import com.example.galaxy08.app.PreferenceWrapper;
import com.example.galaxy08.finals.SysConstants;
import com.example.galaxy08.http.GalaxyHttpClient;
import com.example.galaxy08.message.MessageMaintenanceThread;
import com.example.galaxy08.tool.DebugLog;
import com.example.galaxy08.tool.SystemInfo;

public class SysApplication extends Application {

	// public XMPPConnection conn = null;

	public static SysApplication application;

	public ChatManager manager;

	public ExecutorService threadPool;

	private final static int UPLOAD_REQUEST_CODE = 789315137;

	public static Context mContext;

	private String mUserId;

	private String mUserName;

	private String mCompany;

	private int mGYMsgCount = -1;

	private int mGYMsgCenterCount = -1;

	private int mGYMsgReminderCount = -1;

	private static Handler mMsgHandler;

	private String mTimestamp;

	// public static GYApplication mGYApplication;

	private boolean mbTakeSuccess;

	private boolean mIsFromTab = false;

	private int mLoginState;

	public static boolean isChangeLanguage = false;

	public static boolean isVisable = false;

	private static SystemInfo mSystemInfo;

	private ContentObserver mObserver;

	//public String tipFindCardUserFromContacts;

	// 键为旧的cardId 值为新的cardId 防止详情页或编辑页出现数据空白的情况发生
	//private HashMap<String, String> old2NewCardIdsMap;

	@Override
	public void onCreate() {
		DebugLog.logd("GYApplication", "onCreate");
		application = this;
		mContext = this;
		super.onCreate();

		SysConstants.isEnglishVersion = Locale.CHINA.equals(getResources()
				.getConfiguration().locale) ? false : true;

		DebugLog.logd("isEnglishVersion:" + SysConstants.isEnglishVersion);

		PreferenceWrapper.initialize(this);
		mSystemInfo = new SystemInfo(this);
		mSystemInfo.init();

		DebugLog.logd("GYApplication onCreate()");
		setupHttpClient();
		
		threadPool = Executors.newFixedThreadPool(3); 
	}

	private void setupHttpClient() {
		GalaxyHttpClient.setupClient(mSystemInfo.getVersion());
		GalaxyHttpClient.enableGZIPEncoding();
		GalaxyHttpClient.updateProxySettings(getApplicationContext());
	}

	/********************** 监控前后台变化 ************************************/

	private static boolean tempVisable = false;

	private static boolean constantVisable = false;

	private static Handler checkBackHandler = new Handler();

	public static void activityResume() {
		tempVisable = true;
		final boolean changed = tempVisable != constantVisable;
		if (changed) {
			checkBackHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					if (tempVisable == true && constantVisable != tempVisable) {
						constantVisable = tempVisable;
						onVisableChanged(true);
					}
				}
			}, 1 * 1000);
		}
	}

	public static void activityPaused() {
		tempVisable = false;
		final boolean changed = tempVisable != constantVisable;
		if (changed) {
			checkBackHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					if (tempVisable == false && constantVisable != tempVisable) {
						constantVisable = tempVisable;
						onVisableChanged(false);
					}
				}
			}, 1 * 1000);
		}
	}

	private static long startTime;

	// 运行与UI线程中，切勿做耗时操作
	private static void onVisableChanged(boolean visable) {
		if (visable) {
			startTime = System.currentTimeMillis();
			MessageMaintenanceThread.getInstance().back2Front();
		} else {
			long used = System.currentTimeMillis() - startTime;
			// Behaviour.addUsedTimeAction(Math.round(used / 1000.0), mContext);
		}

	}

	/********************** 监控前后台变化 end ↑ ************************************/

	@Override
	public void onTerminate() {
		super.onTerminate();
		DebugLog.loge("GYApplication onTerminate()");
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		DebugLog.loge("GYApplication onLowMemory()");
	}

	public static Context getAppContext() {
		return mContext;
	}

	public void setUserId(String userId) {
		mUserId = userId;
	}

	public String getUserId() {
		return mUserId;
	}

	public void setUserName(String userName) {
		mUserName = userName;
	}

	public String getUserName() {
		return mUserName;
	}

	public String getCompany() {
		return mCompany;
	}

	public void setCompany(String company) {
		mCompany = company;
	}

	public int getGYMsgReminderCount() {
		return mGYMsgReminderCount;
	}

	public void setGYMsgReminderCountt(int GYMsgReminderCount) {
		mGYMsgReminderCount = GYMsgReminderCount;
	}

	public int getGYMsgCenterCount() {
		return mGYMsgCenterCount;
	}

	public void setGYMsgCenterCount(int GYMsgCenterCount) {
		mGYMsgCenterCount = GYMsgCenterCount;
	}

	public int getGYMsgCount() {
		return mGYMsgCount;
	}

	public void setGYMsgCount(int GYMsgCount) {
		mGYMsgCount = GYMsgCount;
	}

	public void setMsgTabHandler(Handler handler) {
		mMsgHandler = handler;
	}

	public Handler getMsgTabHandler() {
		return mMsgHandler;
	}

	public String getTimeStamp() {
		return mTimestamp;
	}

	public void setTimeStamp(String timeStamp) {
		timeStamp = mTimestamp;
	}

	public void setIsFromTab(boolean isFromTab) {
		mIsFromTab = isFromTab;
	}

	public boolean getIsFromTab() {
		return mIsFromTab;
	}

//	public boolean isTakeMycardSuccess() {
//		return mbTakeSuccess;
//	}

//	public void setTakeMycardSuccess(boolean takeSuccess) {
//		mbTakeSuccess = takeSuccess;
//	}

	public int getmLoginState() {
		return mLoginState;
	}

	public void setmLoginState(int mLoginState) {
		this.mLoginState = mLoginState;
	}

	public static SystemInfo getSystemInfo() {
		return mSystemInfo;
	}

	public static String current_user = null;
	
}
