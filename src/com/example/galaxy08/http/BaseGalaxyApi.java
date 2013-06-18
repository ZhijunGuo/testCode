package com.example.galaxy08.http;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.text.TextUtils;

import com.example.galaxy08.SysApplication;
import com.example.galaxy08.app.PreferenceWrapper;
import com.example.galaxy08.http.exception.GalaxyHttpException;
import com.example.galaxy08.http.model.BaseResponse;
import com.example.galaxy08.tool.SystemInfo;

/**
 * api 接口基类 2013/04/15
 *
 */
public abstract class BaseGalaxyApi {

    public static final String TAG = "BaseGalaxyApi";

    // 用户信息集合
    private static List<BasicNameValuePair> mMobileInfo;
    
    public static <T extends BaseResponse>T executeRequest(String url, HashMap<String, String> params ,Class<T> cls) throws IOException, GalaxyHttpException{
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        
        Set<Entry<String, String>>  entrySet = params.entrySet();
        Iterator<Entry<String, String>> it = entrySet.iterator();
        while(it.hasNext()){
            Entry<String,String>   entry = it.next();
            String key = entry.getKey();
            String value = entry.getValue();
            list.add(new BasicNameValuePair(key, value));
        }
        return doPost(url, list, cls);
    };

    /**
     * 执行get请求，使用默认超时时间
     * @param url  请求接口地址
     * @param list 参数集合
     * @param rCls 放回数据解析类型
     * @return
     * @throws IOException
     * @throws GalaxyHttpException
     */
    protected static <T extends BaseResponse>T doGet(String url, List<NameValuePair> list,Class<T> rCls) throws IOException, GalaxyHttpException {
        list.add(new BasicNameValuePair(RequestParames.TOKEN, PreferenceWrapper.get(RequestParames.TOKEN, "")));
        list.addAll(createMobileInfo());
        return GalaxyHttpClient.executeHttpGet(url, list,rCls,0);
    }

    /** 
     * 执行get请求并另行指定超时时间
     * @param url  请求接口地址
     * @param list 参数集合
     * @param rCls 放回数据解析类型
     * @return
     * @throws IOException
     * @throws GalaxyHttpException
     */
    protected static <T extends BaseResponse>T doGetWithTimeout(String url, List<NameValuePair> list,Class<T> rCls,int timeout) throws IOException, GalaxyHttpException {
        list.add(new BasicNameValuePair(RequestParames.TOKEN, PreferenceWrapper.get(RequestParames.TOKEN, "")));
        list.addAll(createMobileInfo());
        return GalaxyHttpClient.executeHttpGet(url, list,rCls,timeout);
    }
    
    /**
     * 上传文件请求，使用默认超时时间
     * @param url  请求接口地址
     * @param list 参数集合
     * @param rCls 放回数据解析类型
     * @param file file
     * @return
     * @throws IOException
     * @throws GalaxyHttpException
     */
    protected static <T extends BaseResponse>T doPost(String url, List<NameValuePair> list,Class<T> rCls,File file) throws IOException, GalaxyHttpException {
        list.add(new BasicNameValuePair(RequestParames.TOKEN, PreferenceWrapper.get(RequestParames.TOKEN, "")));
        list.addAll(createMobileInfo());
        return GalaxyHttpClient.executeHttpPost(url, list,rCls,file,60000);
    }
    
    /**
     * post bytes，使用默认超时时间
     * @param url  请求接口地址
     * @param list 参数集合
     * @param rCls 放回数据解析类型
     * @param file file
     * @return
     * @throws IOException
     * @throws GalaxyHttpException
     */
    protected static <T extends BaseResponse>T doPostBytes(String url, List<NameValuePair> list,String paramName, byte[] data, Class<T> rCls,int timeout) throws IOException, GalaxyHttpException {
        list.add(new BasicNameValuePair(RequestParames.TOKEN, PreferenceWrapper.get(RequestParames.TOKEN, "")));
        list.addAll(createMobileInfo());
        return GalaxyHttpClient.executeHttpPostBytes(url, list, paramName, data, rCls,timeout);
    }
    /**
     * 提交post请求
     * @param url  请求接口地址
     * @param list 参数集合
     * @param rCls 放回数据解析类型
     * @return
     * @throws IOException
     * @throws GalaxyHttpException
     */
    protected static <T extends BaseResponse>T doPost(String url, List<NameValuePair> list,Class<T> rCls) throws IOException, GalaxyHttpException {
        list.add(new BasicNameValuePair(RequestParames.TOKEN, PreferenceWrapper.get(RequestParames.TOKEN, "")));
        list.addAll(createMobileInfo());
        return GalaxyHttpClient.executeHttpPost(url, list,rCls,0);
    }
    /**
     * 提交post请求
     * @param url  请求接口地址
     * @param list 参数集合
     * @param rCls 放回数据解析类型
     * @return
     * @throws IOException
     * @throws GalaxyHttpException
     */
    protected static <T extends BaseResponse>T doPostWithoutInfo(String url, List<NameValuePair> list,Class<T> rCls) throws IOException, GalaxyHttpException {
    	return GalaxyHttpClient.executeHttpPost(url, list,rCls,0);
    }
    
    /**
     * 提交post请求
     * @param url  请求接口地址
     * @param list 参数集合
     * @param rCls 放回数据解析类型
     * @return
     * @throws IOException
     * @throws GalaxyHttpException
     */
    protected static <T extends BaseResponse>T doPost(String url, List<NameValuePair> list,Class<T> rCls, int timeout) throws IOException, GalaxyHttpException {
        list.add(new BasicNameValuePair(RequestParames.TOKEN, PreferenceWrapper.get(RequestParames.TOKEN, "")));
        list.addAll(createMobileInfo());
        return GalaxyHttpClient.executeHttpPost(url, list,rCls,timeout);
    }
    
    protected static JSONObject doPost(String url, List<NameValuePair> list, int timeout) throws Exception{
    	list.add(new BasicNameValuePair(RequestParames.TOKEN, PreferenceWrapper.get(RequestParames.TOKEN, "")));
        list.addAll(createMobileInfo());
        return GalaxyHttpClient.executeHttpPost(url, list,timeout);
    }
    
    

    /**需要收集的用户信息
	  
	  * 1 model : 手机型号
	  * 2 uniqid : 设备的唯一标识，如果无则传“-1”
	  * 3 os : 手机操作系统型号及版本
	  * 4 screen : 屏幕尺寸
	  * 5 mac : MAC地址
	  * 6 from : 渠道编号
	 */
    public static List<BasicNameValuePair> createMobileInfo() {
        if (mMobileInfo == null || mMobileInfo.isEmpty()) {
            SystemInfo info = SysApplication.getSystemInfo();
            String model = info.getModel();
            //String appid = info.getAppid();
            String mac = info.getMac();
            String os = info.getOs();
            String uniqid = info.getUniqid();
            String screen = info.getScreen();
            int from  = info.getFrom();
            //String version = info.getVersion();
            //String versionCode = info.getVersionCode();

            mMobileInfo = new ArrayList<BasicNameValuePair>();
            mMobileInfo.add(new BasicNameValuePair(SystemInfo.PARAM_MODEL, model)); // 手机型号
//            mMobileInfo.add(new BasicNameValuePair(SystemInfo.PARAM_APPID, appid)); //
            mMobileInfo.add(new BasicNameValuePair(SystemInfo.PARAM_UNIQID, uniqid)); // 设备唯一标识
            mMobileInfo.add(new BasicNameValuePair(SystemInfo.PARAM_OS, os)); // 手机系统型号及版本。从系统中读出
            mMobileInfo.add(new BasicNameValuePair(SystemInfo.PARAM_SCREEN, screen));// 屏幕尺寸。
            mMobileInfo.add(new BasicNameValuePair(SystemInfo.PARAM_FROM, String.valueOf(from)));// 渠道ID。固定写入。
//            mMobileInfo.add(new BasicNameValuePair(SystemInfo.PARAM_VERSION, version));// 版本名称。//
//            mMobileInfo.add(new BasicNameValuePair(SystemInfo.PARAM_VERSIONCODE, versionCode));// 版本号。//
            mMobileInfo.add(new BasicNameValuePair(SystemInfo.PARAM_MAC, mac)); // 手机MAC地址，从系统中读出。（无法读出时，填空）
        }
        
        //由于当前mac地址为空，每次试图重新读取mac
        BasicNameValuePair  macValue = mMobileInfo.get(mMobileInfo.size() - 1);
        if(TextUtils.isEmpty(macValue.getValue()) && macValue.getName().equals(SystemInfo.PARAM_MAC)){
            String mac = SysApplication.getSystemInfo().getMac();
            if(!TextUtils.isEmpty(mac)){
                mMobileInfo.set(mMobileInfo.size() - 1, new BasicNameValuePair(SystemInfo.PARAM_MAC, mac) );
            }
        }
        return mMobileInfo;
    }
}
