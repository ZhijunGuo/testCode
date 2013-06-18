package com.example.galaxy08.http.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
/**
 * 后台Service
 * 1 接受服务器聊天信息
 */
public class WorkService extends Service{

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
	}

}
