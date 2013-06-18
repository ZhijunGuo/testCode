package com.example.galaxy08.http;

import com.loopj.android.http.BinaryHttpResponseHandler;

public class BinnaryHandlerAdapter extends BinaryHttpResponseHandler {

	private IBinaryHandler  mHandler;
	
	public BinnaryHandlerAdapter(IBinaryHandler handler){
		mHandler = handler;
	}

	@Override
	public void onSuccess(int statusCode, byte[] binaryData) {
		mHandler.onSuccess(statusCode, binaryData);
	}

	@Override
	public void onFailure(Throwable error, byte[] binaryData) {
		mHandler.onFailure(error, binaryData);
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
