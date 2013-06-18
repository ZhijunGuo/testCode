package com.example.galaxy08.http;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * AsyncHttpClient 代理
 * 
 */
public class JwHttpClient {

	public static final String TAG = "JwHttpClient";
	
	private static AsyncHttpClient mHttpClient = new AsyncHttpClient();

	private static ParamsInterceptor mParamsInterceptor;
	
	private  static String mToken;
	
	public static void setUserAgent(String userAgent) {
		mHttpClient.setUserAgent(userAgent);
	}

	public static void setToken(String token){
		mToken = token;
	}
	
//	public static void setDebugLog(boolean enable,String tag){
//		mHttpClient.setDebugLog(enable, tag);
//	}
//	
//	public static void setResponseCache(File directory, int maxSize) throws IOException {
//		mHttpClient.setResponseCache(new HttpResponseCache(new File(""), 1 * 1024 * 1024));
//	}

	public static HttpClient getHttpClient(){
		return mHttpClient.getHttpClient();
	}
	public static void get(String url, RequestParams params, IHandler handler) {
		applyParamsInterceptor(url,"get",params);
		mHttpClient.get(url, params, getRealyHandler(handler));
	}
	
//	public static <T extends ResponseType>T getSync(String url, RequestParams params, Parser<T> parser) throws Exception {
//		applyParamsInterceptor(url,"get",params);
//		String content = EntityUtils.toString(mHttpClient.getSync(url, params).getEntity());
//		AsyncHttpClient.log(content);
//		return parser.parser(new JSONObject(content));
//	}
	
//	public static <T extends ResponseType>T postSync(String url, RequestParams params, Parser<T> parser) throws Exception {
//		applyParamsInterceptor(url,"post",params);
//		String content = EntityUtils.toString(mHttpClient.postSync(url, params).getEntity());
//		//AsyncHttpClient.log(content);
//		
//		if(parser != null){
//			return parser.parser(new JSONObject(content));
//		}else{
//			return null;
//		}
//		
//	}
    
	public static void post(String url, RequestParams params, IHandler handler) {
		applyParamsInterceptor(url,"post",params);
		Log.i("GalaxyApi", url.toString()+"?"+params.toString());
		mHttpClient.post(url, params, getRealyHandler(handler));
	}

//	public static InputStream downloadSync(String url, RequestParams params) throws Exception{
//		HttpResponse response = mHttpClient.getSync(url, params);
//		if(response.getStatusLine().getStatusCode() == 200){
//			return response.getEntity().getContent();
//		}else{
//			throw new IOException("download failed!");
//		}
//	}
	
	public static void setAppendParams(final List<BasicNameValuePair> params){
		
		setParamsInterceptor(new ParamsInterceptor(){

			@Override
			public void beforeRequest(String url, String method,
					RequestParams requestParams) {
				if(params != null && !params.isEmpty()){
					for(NameValuePair value : params){
						requestParams.put(value.getName(), value.getValue());
					}
				}
			}
		});
	}
	
	private static void applyParamsInterceptor(String url, String method, RequestParams params){
		params.put(RequestParames.TOKEN, mToken);
		if(mParamsInterceptor != null){
			mParamsInterceptor.beforeRequest(url, method, params);
		}
	}
	
	/**
	 * 拦截器，用来在发送请求时添加额外的参数
	 */
	public static void setParamsInterceptor(ParamsInterceptor paramsInterceptor){
		mParamsInterceptor = paramsInterceptor;
		
	}
	/**
	 * 将IHandler接口转化为AsyncHttpResponseHandler;
	 * 
	 * @param handler
	 * @return
	 */
	private static AsyncHttpResponseHandler getRealyHandler(IHandler handler) {
		if (handler instanceof IJsonHandler) {
			return new JsonHandlerAdapter((IJsonHandler) handler);
		} else if (handler instanceof IBinaryHandler) {
			return new BinnaryHandlerAdapter((IBinaryHandler) handler);
		} else if (handler instanceof IHandler) {
			return new AsyncHandlerAdapter(handler);
		} else {
			throw new UnknownError("wrong handler type");
		}
	}
	
	public static  interface ParamsInterceptor{
		public void beforeRequest(String url, String method, RequestParams params);
	}
}

