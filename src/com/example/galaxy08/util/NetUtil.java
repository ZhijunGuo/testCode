package com.example.galaxy08.util;

import com.example.galaxy08.app.PreferenceWrapper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;


public class NetUtil {
	public static boolean is2GNetwork(Context context){
    	boolean is2GNetwork = false;
    	ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    	if(manager!=null && manager.getActiveNetworkInfo()!=null){
    		NetworkInfo netinfo = manager.getActiveNetworkInfo();
    		if(netinfo.getType()==ConnectivityManager.TYPE_MOBILE
    				||netinfo.getType()==ConnectivityManager.TYPE_MOBILE_DUN
    				||netinfo.getType()==ConnectivityManager.TYPE_MOBILE_HIPRI
    				||netinfo.getType()==ConnectivityManager.TYPE_MOBILE_MMS
    				||netinfo.getType()==ConnectivityManager.TYPE_MOBILE_SUPL){
    			final int telephonyType = netinfo.getSubtype();
    			is2GNetwork = TelephonyManager.NETWORK_TYPE_GPRS==telephonyType
    					|| TelephonyManager.NETWORK_TYPE_EDGE == telephonyType
    					|| TelephonyManager.NETWORK_TYPE_CDMA == telephonyType
    					|| TelephonyManager.NETWORK_TYPE_1xRTT == telephonyType
    					|| TelephonyManager.NETWORK_TYPE_IDEN == telephonyType;
    		}
    	}
    	return is2GNetwork;
    }
	
	
	public static boolean isNetworkServiceAvailable(Context context) {
		// String login_mode =
		// PreferenceWrapper.get(PreferenceWrapper.LOGIN_MODE, "0");
		String login = PreferenceWrapper.get(PreferenceWrapper.LOGIN, "0");
		return hasInternet(context) && !"0".equals(login);// &&
															// !"5".equals(login_mode);
	}
	/** 判断网络连接是否存在 */
	public static boolean hasInternet(Context activity) {

		ConnectivityManager manager = (ConnectivityManager) activity

		.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo info = manager.getActiveNetworkInfo();

		if (info == null || !info.isConnected()) {
			return false;
		}
		if (info.isRoaming()) {
			return true;
		}
		return true;
	}
}
