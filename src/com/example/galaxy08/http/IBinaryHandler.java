package com.example.galaxy08.http;

/**
 * 对外公开的回调接口,以使上层与AsyncHttpClient框架分离，使之不依赖AsyncHttpClient的实现
 * @author liuqingfeng
 *
 */
public abstract class IBinaryHandler implements IHandler{
	
	public  void onProgress(int progress, int total){};
	
	public  void onSuccess(int statusCode, String response){};
	
	public void onFailure(Throwable error, String content){};
	
	public abstract void onSuccess(int statusCode, byte[] binaryData);
	
	public abstract void onFailure(Throwable error, byte[] binaryData);
}
