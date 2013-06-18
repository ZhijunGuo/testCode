package com.example.galaxy08.http;

/**
 * 对外公开的回调接口,以使上层与AsyncHttpClient框架分离，使之不依赖AsyncHttpClient的实现
 */
public interface IHandler {
	
	public  void onStart();
	
	public  void onProgress(int progress, int total);
	
	public  void onSuccess(int statusCode, String response);
	
	public void onFailure(Throwable error, String content);
	
	public  void onFinish();
}
