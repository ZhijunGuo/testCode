package com.example.galaxy08.tool;

import android.util.Log;

/*
 * 打印Log信息
 */

public class DebugLog {
    public static int LOG_LEVEL = Log.VERBOSE;
    
    private static String TAG = "jingwei-card";
    
    public static void loge(String msg) {
        log(msg, Log.ERROR);
    }
    
    public static void logd(String msg) {
        log(msg, Log.DEBUG);
    }
    
    public static void logw(String msg) {
        log(msg, Log.WARN);
    }
    
    public static void logi(String msg) {
        log(msg, Log.INFO);
    }
    
    public static void logv(String msg) {
        log(msg, Log.VERBOSE);
    }
    
    public static void loge(String tag, String msg) {
        log(tag, msg, Log.ERROR);
    }
    
    public static void logd(String tag, String msg) {
        log(tag, msg, Log.DEBUG);
    }
    
    
    
    public static void logw(String tag, String msg) {
        log(tag, msg, Log.WARN);
    }
    
    public static void logi(String tag, String msg) {
        log(tag, msg, Log.INFO);
    }
    
    public static void logv(String tag, String msg) {
        log(tag, msg, Log.VERBOSE);
    }
    
    private static void log(String msg, int level) {
    	log(TAG, msg, level);
    }
    
    private static void log(String tag, String msg, int level) {
        if (level < LOG_LEVEL) {
        	return;
        }
        //if (DebugReceiver.sm_debug) {
        	StackTraceElement[] elements = (new Throwable()).getStackTrace();
            try {
                StackTraceElement ste = elements[3];
                Log.println(level, tag, msg + "----" + ste.getFileName().split("\\.")[0] + ":" + ste.getLineNumber() + ":" + ste.getMethodName() 
                        +  "()");
            } catch (Exception e) {
                Log.println(level, tag, msg);
            }
//        }
//        else {
//        	Log.println(level, tag, msg);
//        }
    }
    
    public static void logd(String tag, String msg, Throwable tr){
    	if(LOG_LEVEL > Log.DEBUG) return;
    	//if (DebugReceiver.sm_debug){
    		Log.d(tag, msg, tr);
    	//}
    }
}
