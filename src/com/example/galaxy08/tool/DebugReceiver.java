package com.example.galaxy08.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Environment;
import android.util.Log;

//import com.jingwei.card.MainActivity;
//import com.jingwei.card.app.JwApplication;
//import com.jingwei.card.dao.DaoBase;
//import com.jingwei.card.entity.User;
//import com.jingwei.card.entity.Cards;
//import com.jingwei.card.entity.Groups;
//import com.jingwei.card.entity.JwMessages;
//import com.jingwei.card.finals.PictureStorage;

/*
 * DebugReceiver 广播接收类？ 2013/04/16
 */

public class DebugReceiver extends BroadcastReceiver {
	/*
	 * 这些地址应该进行更换
	 */
	private static final String TEST_BROADCAST_OPEN  = "com.jingwei.card.testopen";
	private static final String TEST_BROADCAST_CLOSE = "com.jingwei.card.testclose";
	private static final String TEST_BROADCAST_DELETE_MYCARD = "com.jingwei.card.delmycard";
	private static final String TEST_BROADCAST_DELETE_ALL = "com.jingwei.card.delall";
	private static final String TEST_BROADCAST_DEBUG = "com.jingwei.card.debugopen";
	private static final String TEST_BROADCAST_NO_DEBUG = "com.jingwei.card.debugclose";
	private static final String TEST_SHOW_ALL_DATA = "com.jingwei.card.showdata";
	private static final String TEST_COPY_MOBILE_DB = "com.jingwei.card.copydb";
	public static boolean sm_test = false;
	public static boolean sm_debug = true;
	public static boolean sm_db_test = true;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		DebugLog.loge("TestReceiver action=" + action);
		
		if (action.equals(TEST_BROADCAST_DEBUG)) {
			sm_debug = true;
			DebugLog.LOG_LEVEL = Log.DEBUG;
			DebugLog.loge("sm_debug=" + sm_debug);
			DebugLog.loge("LOG_LEVEL=" + DebugLog.LOG_LEVEL);
		}
		else if (action.equals(TEST_BROADCAST_NO_DEBUG)) {
			sm_debug = false;
			DebugLog.LOG_LEVEL = Log.ERROR;
			DebugLog.loge("sm_debug=" + sm_debug);
			DebugLog.loge("LOG_LEVEL=" + DebugLog.LOG_LEVEL);
		}
		else if (action.equals(TEST_BROADCAST_OPEN)) {
			sm_test = true;
			sm_debug = true;
			DebugLog.LOG_LEVEL = Log.DEBUG;
			DebugLog.loge("sm_debug=" + sm_debug);
			DebugLog.loge("LOG_LEVEL=" + DebugLog.LOG_LEVEL);
			DebugLog.loge("sm_test=" + sm_test);
		}
		else if (action.equals(TEST_BROADCAST_CLOSE)) {
			sm_test = false;
			sm_debug = false;
			DebugLog.LOG_LEVEL = Log.ERROR;
			DebugLog.loge("sm_debug=" + sm_debug);
			DebugLog.loge("LOG_LEVEL=" + DebugLog.LOG_LEVEL);
			DebugLog.loge("sm_test=" + sm_test);
		}
		else if (action.equals(TEST_BROADCAST_DELETE_MYCARD)) {
			deleteMycard(context);
		}		
		else if (action.equals(TEST_BROADCAST_DELETE_ALL)) {
			deleteAll(context);
		}	
		else if (action.equals(TEST_SHOW_ALL_DATA)) {
			showAllData(context);
		}
		else if (action.equals(TEST_COPY_MOBILE_DB)) {
			copyMobileDB();
			DebugLog.loge("copyMobileDB complete");
		}
	}
	/*
	 * 这个方法应该剔除(删除我的名片)
	 */
	private void deleteMycard(Context context) {
//	    User card = JwApplication.getMyCard();
//	    card.clean();
//		Cards.deleteCard(context,card.cardID);
//		card.photoLocalPath="";
//		card.photoRemotePath="";
//		card.cardID="";
//		
//		MainActivity.DeleteMycardStatus();
//		MainActivity.UpdateGUI();
//		DebugLog.loge("my card deleted");
	}
	
	private void showAllData(Context context) {
//		Cursor cur = Cards.query(context, null, null, null, null);
//		if (cur != null) {
//			DebugLog.logd("cursor.getCount()="  + cur.getCount());
//			User card = new User();
//			while(cur.moveToNext()) {					
//				card.cardID = cur.getString(cur.getColumnIndex("cardid"));
//				card.imageID = cur.getString(cur.getColumnIndex("imageid"));
//				card.userID = cur.getString(cur.getColumnIndex("userid"));
//				card.name = cur.getString(cur.getColumnIndex("username"));
//				card.chname=cur.getString(cur.getColumnIndex("userchname"));
//				card.company = cur.getString(cur.getColumnIndex("company"));
//				card.dep = cur.getString(cur.getColumnIndex("dep"));
//				card.address = cur.getString(cur.getColumnIndex("address"));
//				card.mobile = cur.getString(cur.getColumnIndex("mobile"));
//				card.fax = cur.getString(cur.getColumnIndex("fax"));
//				card.phoneCompany = cur.getString(cur.getColumnIndex("telephone"));
//				card.phoneHome = cur.getString(cur.getColumnIndex("phonehome"));
//				card.position = cur.getString(cur.getColumnIndex("position"));
////				card.enPosition = cur.getString(cur.getColumnIndex("enposition"));
//				card.email = cur.getString(cur.getColumnIndex("email"));
//				card.webSite = cur.getString(cur.getColumnIndex("website"));
//				card.sinaBlog = cur.getString(cur.getColumnIndex("weibo"));
//				card.im = cur.getString(cur.getColumnIndex("im"));
//				card.industry = cur.getString(cur.getColumnIndex("industry"));
//				
//				card.imagePath = cur.getString(cur.getColumnIndex("imagepath"));
//				card.imageSmallPath = cur.getString(cur.getColumnIndex("imageSmallpath"));
//				card.photoLocalPath = cur.getString(cur.getColumnIndex("photoLocalPath"));
//				card.photoRemotePath = cur.getString(cur.getColumnIndex("photoRemotePath"));
//				card.dateLine = cur.getString(cur.getColumnIndex("dateline"));
//				card.lastupdate = cur.getString(cur.getColumnIndex("lastupdate"));
//				card.isupload = cur.getString(cur.getColumnIndex("isupload"));
//				card.issuccess = cur.getString(cur.getColumnIndex("issuccess"));
//				card.groupID = cur.getString(cur.getColumnIndex("groupid"));
//				card.groupName = cur.getString(cur.getColumnIndex("groupname"));
//				card.remark = cur.getString(cur.getColumnIndex("remark"));
//				card.cardType = cur.getString(cur.getColumnIndex("cardtype"));
//				card.sync = cur.getInt(cur.getColumnIndex("sync"));
//				DebugLog.logd(card.toString());
//			}
//			cur.close();
//		}
	}
	
	private void deleteAll(Context context) {
//	    User card = JwApplication.getMyCard();
//	    card.clean();
//		
//		Cards.deleteAllData(context);
////		Images.deleteAllData(context);
//		Groups.deleteAllData(context);
//		JwMessages.deleteAllData(context);
//		
//		
//		card.photoLocalPath="";
//		card.photoRemotePath="";
//		card.cardID="";
//		
//		MainActivity.DeleteMycardStatus();
//		MainActivity.UpdateGUI();
//		DebugLog.loge("all data deleted");
	}
	
	private void copyMobileDB() {
//		String dbSdcardName = null;
//		
//		try {
//			String dbSdcardDir = Environment.getExternalStorageDirectory()
//					+ PictureStorage.ARECORD_DB_DIR;
//			File sdcardDir = new File(dbSdcardDir);
//			if (sdcardDir.exists() || (!sdcardDir.exists() && sdcardDir.mkdirs())) {
//				dbSdcardName = dbSdcardDir + Tool.nowTime() +DaoBase.DATABASE_NAME;
//			}
//			
//			String dbMobileName = Environment.getDataDirectory().getAbsolutePath()
//					+ File.separator + "data"
//					+ File.separator + "com.jingwei.card"
//					+ File.separator + "databases" + File.separator + DaoBase.DATABASE_NAME;
//
//			DebugLog.loge("dbSdcardName=" + dbSdcardName);
//			DebugLog.loge("dbMobileName=" + dbMobileName);
//			
//			File dbfileSdcard = new File(dbSdcardName);
//			File dbfileMobile = new File(dbMobileName);
//			
//			if (dbfileMobile.exists()) {
//				FileOutputStream fout = new FileOutputStream(dbSdcardName);
//				/* 取得文件的FileInputStream */
//				FileInputStream fin = new FileInputStream(dbfileMobile);
//				/* 设置每次写入1024bytes */
//				int bufferSize = 1024;
//				byte[] buffer = new byte[bufferSize];
//
//				int length = -1;
//
//				/* 从文件读取数据至缓冲区 */
//				while ((length = fin.read(buffer)) != -1) {
//					DebugLog.loge("length=" + length);
//					/* 将资料写入DataOutputStream中 */
//					fout.write(buffer, 0, length);
//				}
//				fin.close();
//				fout.close();
//			}
//		}
//		catch(FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}		
	}
}
