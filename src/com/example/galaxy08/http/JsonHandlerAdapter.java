package com.example.galaxy08.http;

import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;

public class JsonHandlerAdapter extends JsonHttpResponseHandler {

	private IJsonHandler mHandler;

	public JsonHandlerAdapter(IJsonHandler handler) {
		mHandler = handler;
	}

	@Override
	public void onSuccess(int statusCode, JSONObject jsonData) {
		mHandler.onSuccess(statusCode, jsonData);
	}

	@Override
	public void onFailure(Throwable error, JSONObject jsonData) {
		mHandler.onFailure(error, jsonData);
	}

	
	@Override
	public void onFailure(Throwable error, String content) {
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