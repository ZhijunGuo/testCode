package com.example.galaxy08.util;

import java.util.HashSet;

import android.os.Handler;

import com.example.galaxy08.tool.DebugLog;


/**
 * 集合元素只能保存一段时间，超过该时间自动被移除
 * 只能在主线程中创建
 */


public class LifeLimitedtCache<K> {
	private  HashSet<K>  cache;
	int defaultTimeout;
	
	Handler  handler;
	
	public LifeLimitedtCache(){
		cache = new HashSet<K>();
		handler  = new Handler();
		defaultTimeout = 30 * 1000;
	}
	
	public void setDefaultTimeout(int time){
		defaultTimeout = time;
	}
	
	public void put(K k){
		put(k,defaultTimeout);
	}
	
	public void put(final K k, int timeout){
		cache.add(k);
		handler.postDelayed(new Runnable(){

			@Override
			public void run() {
				cache.remove(k);
			}
			
		}, timeout);
	}
	
	public void remove(K k){
		cache.remove(k);
		DebugLog.logd("callback size :" + cache.size());
	}
}
