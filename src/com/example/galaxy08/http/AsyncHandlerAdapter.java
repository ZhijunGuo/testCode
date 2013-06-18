package com.example.galaxy08.http;

import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 回调适配器，将公开的回调接口适配为AsyncHttpClient所需要的回调
 * 
 */
public class AsyncHandlerAdapter extends AsyncHttpResponseHandler {

	private IHandler mHandler;

	public AsyncHandlerAdapter(IHandler handler) {
		mHandler = handler;
	}

	@Override
	public void onSuccess(int statusCode,String content) {
		mHandler.onSuccess(statusCode, content);
	}

	@Override
	public void onFailure(Throwable error,String content) {
		mHandler.onFailure(error, content);
	}
	
	@Override
	public void onStart() {
		mHandler.onStart();
	}
	
	@Override
	public void onFinish() {
		mHandler.onFinish();
	}
}
