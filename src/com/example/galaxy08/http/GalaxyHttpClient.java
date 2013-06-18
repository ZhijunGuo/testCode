package com.example.galaxy08.http;

import java.io.File;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.NoConnectionReuseStrategy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.text.TextUtils;

import com.example.galaxy08.http.exception.GalaxyHttpException;
import com.example.galaxy08.http.model.BaseResponse;
import com.example.galaxy08.tool.DebugLog;
import com.example.galaxy08.tool.Tool;
import com.google.gson.Gson;

/**
 * JwHttpClient 客户端类(需要进行改造) 2013/04/15
 */
public class GalaxyHttpClient {

	public static final String TAG = "GalaxyClient";

	//应该是设置的路由？
	private static  String USER_AGENT = "GalaxyBizCard/%s";
	//最大连接数
	private static final int DEFAULT_MAX_CONNECTIONS = 4 * 1000;
	//连接超时？
	private static final int CONNECT_TIMEOUT = 15 * 1000;
	//socket 超时(4.5s?)
	private static final int SOCKET_TIMEOUT = 45 * 1000;

	// 总超时时间(3.0s?)
	private static final int TIMEOUT = 30 * 1000;

	private static DefaultHttpClient mHttpClient;

	//线程池
	private static ScheduledThreadPoolExecutor mTimeoutSchedule;

	public static void setupClient(String codeVersion){
		//创建一个线程池
		mTimeoutSchedule = new ScheduledThreadPoolExecutor(1);
		USER_AGENT = String.format(USER_AGENT, codeVersion);
		try {
			KeyStore trustStore;
			trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(null, null);
			/*
			 * 分层的套接字工厂可以创建在已经存在的普通套接字之上的分层套接字。
			 * 套接字分层主要通过代理来创建安全的套接字。
			 * HttpClient附带实现了SSL/TLS分层的SSLSocketFactory。
			 * 请注意HttpClient不使用任何自定义加密功能。
			 * 它完全依赖于标准的Java密码学（JCE）和安全套接字（JSEE）扩展。
			 */
			SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
			//主机名校对？ 允许所有的主机名进行校对
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			//设置访问协议(http+https)
			final SchemeRegistry supportedSchemes = new SchemeRegistry();
			supportedSchemes.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			supportedSchemes.register(new Scheme("https", sf, 443));
			
			final HttpParams httpParams = createHttpParams();
			//重定向？
			HttpClientParams.setRedirecting(httpParams, false);

			final ClientConnectionManager ccm = new ThreadSafeClientConnManager(
					httpParams, supportedSchemes);
			mHttpClient = new DefaultHttpClient(ccm, httpParams);
			//重用策略
			mHttpClient.setReuseStrategy(new NoConnectionReuseStrategy());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static HttpClient getHttpClient() {
		return mHttpClient;
	}

	private static final HttpParams createHttpParams() {
		final HttpParams params = new BasicHttpParams();

		/* 设置路由
		 * 这里route的概念可以理解为 运行环境机器 到 目标机器的一条线路。
		 * 举例来说，我们使用HttpClient的实现来分别请求 www.baidu.com 
		 * 的资源和 www.bing.com 的资源那么他就会产生两个route。
		 */
		params.setParameter(CoreProtocolPNames.HTTP_ELEMENT_CHARSET, "UTF-8");
		
		params.setParameter(CoreProtocolPNames.USER_AGENT,
				USER_AGENT);
		//设置获取连接的最长等待时间
		ConnManagerParams.setTimeout(params, SOCKET_TIMEOUT);
		//每个路由的最大连接数(目前是4000)
		ConnManagerParams.setMaxConnectionsPerRoute(params,
				new ConnPerRouteBean(DEFAULT_MAX_CONNECTIONS));
		
		ConnManagerParams.setMaxTotalConnections(params,
				DEFAULT_MAX_CONNECTIONS);

		HttpConnectionParams.setStaleCheckingEnabled(params, false);
		HttpConnectionParams.setConnectionTimeout(params, CONNECT_TIMEOUT);
		HttpConnectionParams.setSoTimeout(params, SOCKET_TIMEOUT);
		HttpConnectionParams.setSocketBufferSize(params, 8192);
		HttpConnectionParams.setTcpNoDelay(params, true);
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		return params;
	}
	
	private static JSONObject executeHttpRequestBasicJSON(final HttpRequestBase httpRequest, int timeout) throws Exception{
		mHttpClient.getConnectionManager().closeExpiredConnections();
		try {
			mTimeoutSchedule.schedule(new Runnable() {

				@Override
				public void run() {
					if (!httpRequest.isAborted()) {
						httpRequest.abort();
					}
				}
			}, timeout, TimeUnit.MILLISECONDS);

			HttpResponse response = mHttpClient.execute(httpRequest);
			int responseCode = response.getStatusLine().getStatusCode();
			switch (responseCode) {
			case 200:
				String content = EntityUtils.toString(response.getEntity());
				DebugLog.logd(TAG, "content :" + content);
				return new JSONObject(content);
			default:
				DebugLog.loge(TAG, "response :"
						+ response.getStatusLine().getStatusCode() + ", "
						+ EntityUtils.toString(response.getEntity()));
				//这个异常需要自己改写
				throw new GalaxyHttpException(response.getStatusLine().toString(),
						response.getEntity().toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (httpRequest != null && !httpRequest.isAborted()) {
				httpRequest.abort();
			}
		}
	
	}

	private static <T extends BaseResponse> T excuteHttpRequestBasic(
			final HttpRequestBase httpRequest, int timeout, Class<T> rCls)
			throws IOException, GalaxyHttpException {
		mHttpClient.getConnectionManager().closeExpiredConnections();
		try {
			mTimeoutSchedule.schedule(new Runnable() {

				@Override
				public void run() {
					if (!httpRequest.isAborted()) {
						httpRequest.abort();
					}
				}
			}, timeout, TimeUnit.MILLISECONDS);

			HttpResponse response = mHttpClient.execute(httpRequest);
			int responseCode = response.getStatusLine().getStatusCode();
			switch (responseCode) {
			//返回值 OK
			case 200:
				String content = EntityUtils.toString(response.getEntity());
				//content = content.substring(5);
				DebugLog.logd(TAG, "content :" + content);
				
				return new Gson().fromJson(content, rCls);
			default:
				DebugLog.loge(TAG, "response :"
						+ response.getStatusLine().getStatusCode() + ", "
						+ EntityUtils.toString(response.getEntity()));
				throw new GalaxyHttpException(response.getStatusLine().toString(),
						response.getEntity().toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (httpRequest != null && !httpRequest.isAborted()) {
				httpRequest.abort();
			}
		}
	}

	/**
	 * 执行httpget 请求
	 * 
	 * @param url
	 * @param nameValueList
	 * @param tCls
	 * @param timeout
	 *            超时时间，单位毫秒，如果<=0 为默认时间
	 * @return
	 * @throws IOException
	 * @throws GalaxyHttpException
	 */
	public static <T extends BaseResponse> T executeHttpGet(String url,
			List<NameValuePair> nameValueList, Class<T> tCls, int timeout)
			throws IOException, GalaxyHttpException {
		String query = url + "?"
				+ URLEncodedUtils.format(nameValueList, HTTP.UTF_8);
		DebugLog.logd(TAG, "httpGet : " + query);
		final HttpGet httpGet = new HttpGet(query);

		return excuteHttpRequestBasic(httpGet,
				timeout <= 0 ? TIMEOUT : timeout, tCls);
	}

    public <T extends BaseResponse> T executeHttpPost(String url, List<NameValuePair> nameValueList, Class<T> rCls,
            File file) throws IOException, GalaxyHttpException {
        DebugLog.logd(TAG, "httpPost : " + url + "?" + URLEncodedUtils.format(nameValueList, HTTP.ISO_8859_1));
        HttpPost httpPost = new HttpPost(url);
        try {
            MultipartEntity mpEntity = new MultipartEntity(){

				@Override
				public void writeTo(OutputStream outstream) throws IOException {
					super.writeTo(new ProgressOutputStream(outstream,getContentLength()));
				}
            	
            };
            if (nameValueList != null && nameValueList.size() > 0)
                for (NameValuePair pair : nameValueList) {
                    mpEntity.addPart(pair.getName(), new StringBody(pair.getValue(), Charset.forName(HTTP.UTF_8)));
                }
            if (file != null && file.exists())
                mpEntity.addPart("file", new FileBody(file));
            httpPost.setEntity(mpEntity);
            mHttpClient.getConnectionManager().closeExpiredConnections();
            HttpResponse response = mHttpClient.execute(httpPost);
            int responseCode = response.getStatusLine().getStatusCode();
            switch (responseCode) {
                case 200:
                    String content = EntityUtils.toString(response.getEntity());
                    DebugLog.logd(TAG, "content :" + content);
                    return new Gson().fromJson(content, rCls);
                default:
                    DebugLog.loge(TAG,"response :" + response.getStatusLine().getStatusCode() + ", "
                                    + EntityUtils.toString(response.getEntity()));
                    throw new GalaxyHttpException(response.getStatusLine().toString(), response.getEntity().toString());
            }
        } catch (IOException e) {
            httpPost.abort();
            throw e;
        }
    }

	/**
	 * 执行httppost 请求
	 * 
	 * @param url
	 * @param nameValueList
	 * @param rCls
	 * @param timeout
	 *            超时时间，单位毫秒，如果<=0 为默认时间
	 * @return
	 * @throws IOException
	 * @throws GalaxyHttpException
	 */
	public  static <T extends BaseResponse> T executeHttpPost(String url,
			List<NameValuePair> nameValueList, Class<T> rCls, int timeout)
			throws IOException, GalaxyHttpException {
		DebugLog.logd(
				TAG,
				"httpPost : "
						+ url
						+ "?"
						+ URLEncodedUtils
								.format(nameValueList, HTTP.ISO_8859_1));
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(new UrlEncodedFormEntity(nameValueList, HTTP.UTF_8));
		return excuteHttpRequestBasic(httpPost, timeout <= 0 ? TIMEOUT
				: timeout, rCls);
	}
	
	/**
	 * 执行httppost 请求
	 * 
	 * @param url
	 * @param nameValueList
	 * @param timeout  超时时间，单位毫秒，如果<=0 为默认时间
	 * @return JSONObject
	 * @throws IOException
	 * @throws GalaxyHttpException
	 */
	public static JSONObject executeHttpPost(String url, List<NameValuePair> nameValueList, int timeout)
			throws Exception {
		DebugLog.logd( TAG, "httpPost : " + url + "?"
						+ URLEncodedUtils.format(nameValueList, HTTP.ISO_8859_1));
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(new UrlEncodedFormEntity(nameValueList, HTTP.UTF_8));
		return executeHttpRequestBasicJSON(httpPost, timeout <= 0 ? TIMEOUT: timeout);
	}

	/**
	 * 文件上传
	 * 
	 * @param url
	 * @param nameValueList
	 * @param rCls
	 * @param file
	 * @param timeout
	 *            超时时间，单位毫秒，如果<=0 为默认时间
	 * @return
	 * @throws IOException
	 * @throws GalaxyHttpException
	 */
	public static <T extends BaseResponse> T executeHttpPost(String url,
			List<NameValuePair> nameValueList, Class<T> rCls, File file,
			int timeout) throws IOException, GalaxyHttpException {
		DebugLog.logd(
				TAG,
				"httpPost : "
						+ url
						+ "?"
						+ URLEncodedUtils
								.format(nameValueList, HTTP.ISO_8859_1));
		HttpPost httpPost = new HttpPost(url);
		MultipartEntity mpEntity = new MultipartEntity();
		if (nameValueList != null && nameValueList.size() > 0)
			for (NameValuePair pair : nameValueList) {
				mpEntity.addPart(pair.getName(), new StringBody(
						pair.getValue(), Charset.forName(HTTP.UTF_8)));
			}
		if (file != null && file.exists())
			mpEntity.addPart("file", new FileBody(file));
		httpPost.setEntity(mpEntity);

		return excuteHttpRequestBasic(httpPost, timeout <= 0 ? TIMEOUT
				: timeout, rCls);
	}

	/**
	 * 提交 byte[]
	 * 
	 * @param url
	 * @param nameValueList
	 * @param rCls
	 * @param file
	 * @param timeout
	 *            超时时间，单位毫秒，如果<=0 为默认时间
	 * @return
	 * @throws IOException
	 * @throws GalaxyHttpException
	 */
	public static <T extends BaseResponse> T executeHttpPostBytes(String url,
			List<NameValuePair> nameValueList, String paramName, byte[] data,
			Class<T> rCls, int timeout) throws IOException, GalaxyHttpException {
		DebugLog.logd(
				TAG,
				"httpPost : "
						+ url
						+ "?"
						+ URLEncodedUtils
								.format(nameValueList, HTTP.ISO_8859_1));
		HttpPost httpPost = new HttpPost(url);
		MultipartEntity mpEntity = new MultipartEntity();
		if (nameValueList != null && nameValueList.size() > 0)
			for (NameValuePair pair : nameValueList) {
				mpEntity.addPart(pair.getName(), new StringBody(
						pair.getValue(), Charset.forName(HTTP.UTF_8)));
			}
		if (!TextUtils.isEmpty(paramName) && data != null)
			mpEntity.addPart(paramName, new ByteArrayBody(data,
					"application/octet-stream", paramName));
		httpPost.setEntity(mpEntity);
		DebugLog.logd(httpPost.toString());
		return excuteHttpRequestBasic(httpPost, timeout <= 0 ? TIMEOUT
				: timeout, rCls);
	}

	public static void updateProxySettings(Context context) {
		HttpParams httpParams = mHttpClient.getParams();
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo nwInfo = connectivity.getActiveNetworkInfo();
		if (nwInfo == null) {
			return;
		}
		DebugLog.logd(TAG, nwInfo.toString());
		if (nwInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
			String proxyHost = Proxy.getHost(context);
			if (proxyHost == null) {
				proxyHost = Proxy.getDefaultHost();
			}
			int proxyPort = Proxy.getPort(context);
			if (proxyPort == -1) {
				proxyPort = Proxy.getDefaultPort();
			}
			if (proxyHost != null && proxyPort > -1) {
				HttpHost proxy = new HttpHost(proxyHost, proxyPort);
				httpParams.setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
			} else {
				httpParams.setParameter(ConnRoutePNames.DEFAULT_PROXY, null);
			}
		} else {
			httpParams.setParameter(ConnRoutePNames.DEFAULT_PROXY, null);
		}
	}

	public static boolean executeHttpGetCache(String url,
			List<NameValuePair> nameValueList, String cachePath)
			throws IOException, GalaxyHttpException {
		String query = nameValueList == null ? "" : URLEncodedUtils.format(
				nameValueList, HTTP.UTF_8);
		HttpGet httpGet = new HttpGet(url + "?" + query);
		try {
			mHttpClient.getConnectionManager().closeExpiredConnections();
			HttpResponse response = mHttpClient.execute(httpGet);
			int responseCode = response.getStatusLine().getStatusCode();
			switch (responseCode) {
			case 200:
				return Tool.saveToLocal(response.getEntity().getContent(),
						new File(cachePath));
			default:
				DebugLog.loge(TAG, "response :"
						+ response.getStatusLine().getStatusCode() + ", "
						+ EntityUtils.toString(response.getEntity()));
				throw new GalaxyHttpException(response.getStatusLine().toString(),
						response.getEntity().toString());
			}
		} catch (IOException e) {
			httpGet.abort();
			throw e;
		}

	}

	public static void enableGZIPEncoding() {
		mHttpClient.addRequestInterceptor(new HttpRequestInterceptor() {

			public void process(final HttpRequest request,
					final HttpContext context) {
				if (!request.containsHeader("Accept-Encoding")) {
					request.addHeader("Accept-Encoding", "gzip");
				}
			}
		});

		mHttpClient.addResponseInterceptor(new HttpResponseInterceptor() {

			public void process(final HttpResponse response,
					final HttpContext context) {
				// Inflate any responses compressed with gzip
				final HttpEntity entity = response.getEntity();
				final Header encoding = entity.getContentEncoding();
				if (encoding != null) {
					for (HeaderElement element : encoding.getElements()) {
						if (element.getName().equalsIgnoreCase("gzip")) {
							response.setEntity(new GZIPInflatingEntity(entity));
							break;
						}
					}
				}
			}
		});
	}

	public static class GZIPInflatingEntity extends HttpEntityWrapper {

		public GZIPInflatingEntity(final HttpEntity wrapped) {
			super(wrapped);
		}

		@Override
		public InputStream getContent() throws IOException {
			return new GZIPInputStream(wrappedEntity.getContent());
		}

		@Override
		public long getContentLength() {
			return -1;
		}
	}
	/*
	 * 难道是自己的加密？
	 */
    public static class MySSLSocketFactory extends SSLSocketFactory {
    	/*
    	 * 安全传输层协议（TLS）用于在两个通信应用程序之间提供保密性和数据完整性。
    	 * 该协议由两层组成： TLS 记录协议（TLS Record）和 TLS 握手协议（TLS Handshake）。
    	 * 较低的层为 TLS 记录协议，位于某个可靠的传输协议（例如 TCP）上面。
    	 */
        SSLContext sslContext = SSLContext.getInstance("TLS");

        public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException,
                KeyManagementException, KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            sslContext.init(null, new TrustManager[] { tm }, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
                throws IOException, UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }
    
    public class ProgressOutputStream extends FilterOutputStream{
    	
    	public ProgressOutputStream(OutputStream out, long total) {
			super(out);
			this.total = total;
		}

		private long total;
    	private int pos;


		@Override
		public synchronized void write(byte[] buffer, int offset, int length)
				throws IOException {
			super.write(buffer, offset, length);
			pos+=length;
//			DebugLog.logd("ProgressOutputStream", "pos:"+pos+",total:"+total);
		}

		@Override
		public synchronized void write(int oneByte) throws IOException {
			super.write(oneByte);
			pos++;
//			DebugLog.logd("ProgressOutputStream", "pos:"+pos+",total:"+total);
		}

		@Override
		public void write(byte[] buffer) throws IOException {
			super.write(buffer);
			pos+=buffer.length;
//			DebugLog.logd("ProgressOutputStream", "pos:"+pos+",total:"+total);
		}
		
		

		@Override
		public void flush() throws IOException {
			super.flush();
			DebugLog.logd("ProgressOutputStream", "flush,pos:"+pos+",total:"+total);
		}

		@Override
		public void close() throws IOException {
			super.close();
			DebugLog.logd("ProgressOutputStream", "close,pos:"+pos+",total:"+total);
		}
    	
    }
}
