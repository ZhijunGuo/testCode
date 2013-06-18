package com.example.galaxy08.tool;

import java.io.File;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.os.Environment;

import com.example.galaxy08.finals.PictureStorage;

public class Common {
	/*
	 * 判断应用是否由前台转换到后台
	 */
    public static boolean isApplicationBroughtToBackground(final Context context) {
    	//获取系统的ActivityManager
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //获取正在运行的任务信息
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        //当前顶部的activity是不是本应用
        if (tasks!=null&&!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }
    public static File getPhotoFile(Context context) {
		File dir;
		String file_name = Tool.nowTime() + ".jpg";
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			dir = new File(PictureStorage.ARECORD_DIR);
			if (!dir.exists()) {
				dir.mkdirs();
			}
		} else {
			ToastUtil.showMessage(context, "没有SD卡");
			return null;
		}
		File file = new File(dir, file_name);
		DebugLog.logd("context= " + context.toString() + " fileName=" + file.getAbsolutePath());
		return file;
	}
    
    public static File getAudioFile(Context context) {
		File dir;
		String file_name = Tool.nowTime() + ".spx";
		//String file_name = Tool.nowTime();
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			dir = new File(PictureStorage.ARECORD_AUDIO_DIR);
			if (!dir.exists()) {
				dir.mkdirs();
			}
		} else {
			File internalCacheDir = context.getCacheDir();
			dir = new File(internalCacheDir.getAbsolutePath());
		}
		
		File file = new File(dir, file_name);
		DebugLog.logd("context= " + context.toString() + " fileName=" + file.getAbsolutePath());
		return file;
	}
    
    public static File getMP3File(Context context) {
    	File dir;
    	String file_name = Tool.nowTime() + ".mp3";
    	if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
    		dir = new File(PictureStorage.ARECORD_AUDIO_DIR);
    		if (!dir.exists()) {
    			dir.mkdirs();
    		}
    	} else {
    		File internalCacheDir = context.getCacheDir();
    		dir = new File(internalCacheDir.getAbsolutePath());
    	}
    	
    	File file = new File(dir, file_name);
    	DebugLog.logd("context= " + context.toString() + " fileName=" + file.getAbsolutePath());
    	return file;
    }
}
