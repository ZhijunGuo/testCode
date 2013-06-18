package com.example.galaxy08.entity;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.database.Cursor;
import android.util.Log;

import com.example.galaxy08.SysApplication;
import com.example.galaxy08.app.PreferenceWrapper;
import com.example.galaxy08.dao.GalaxyProviderInfo;
import com.example.galaxy08.dao.UserColumns;

/*
 * Users 进行User的相关操作  2013/04/16
 */
public class Users {
	/**
	 * 在数据库中读取 联系人信息
	 * @return
	 */
	public static ArrayList<User> getUserInfoFromDB(){
		ArrayList<User> userInfo = null;
		String userid = PreferenceWrapper.get(PreferenceWrapper.USER_ID, "");
		ContentResolver cr = SysApplication.application.getContentResolver();
		Cursor cursor = cr.query(GalaxyProviderInfo.USER_URI, 
				new String[]{
				UserColumns.USER_ID,
				UserColumns.USER_NAME,
				UserColumns.IMG_PATH,
				UserColumns.POSITION,
				UserColumns.DEPARTMENT,
				UserColumns.MOBILE,
				UserColumns.TELEPHONY,
				UserColumns.EMAIL,
				UserColumns.ADDRESS,
				UserColumns.GROUP_ID,
				UserColumns.LEVEL,
				UserColumns.TYPE
		}, UserColumns.OWNERID+"=?", 	
		new String[]{userid},null);
		
		if(cursor!=null){
			userInfo = new ArrayList<User>();
			while(cursor.moveToNext()){
				User u = new User();
				u.setUser_id(cursor.getString(cursor.getColumnIndex(UserColumns.USER_ID)));
				u.setUser_name(cursor.getString(cursor.getColumnIndex(UserColumns.USER_NAME)));
				u.setPosition(cursor.getString(cursor.getColumnIndex(UserColumns.POSITION)));
				u.setDepartment(cursor.getString(cursor.getColumnIndex(UserColumns.DEPARTMENT)));
				u.setAddress(cursor.getString(cursor.getColumnIndex(UserColumns.ADDRESS)));
				u.setEmail(cursor.getString(cursor.getColumnIndex(UserColumns.EMAIL)));
				u.setImg_path(cursor.getString(cursor.getColumnIndex(UserColumns.IMG_PATH)));
				u.setMobile(cursor.getString(cursor.getColumnIndex(UserColumns.MOBILE)));
				u.setTelephony(cursor.getString(cursor.getColumnIndex(UserColumns.TELEPHONY)));
				u.setType(cursor.getInt(cursor.getColumnIndex(UserColumns.TYPE)));
				u.setLevel(cursor.getInt(cursor.getColumnIndex(UserColumns.LEVEL)));
				//u.set
				if(!u.getUser_id().equals(userid)){
					userInfo.add(u);
				}
			}
			cursor.close();
		}
		
		return userInfo;
	}
	/**
	 * 根据 targetId 查询用户信息
	 */
	public static ArrayList<User> getUserInfoFromDB(String targetId){
		ArrayList<User> userInfo = null;
		
		ContentResolver cr = SysApplication.application.getContentResolver();
		Cursor cursor = cr.query(GalaxyProviderInfo.USER_URI, 
				new String[]{
				UserColumns.USER_ID,
				UserColumns.USER_NAME,
				UserColumns.IMG_PATH,
				UserColumns.POSITION,
				UserColumns.DEPARTMENT,
				UserColumns.MOBILE,
				UserColumns.TELEPHONY,
				UserColumns.EMAIL,
				UserColumns.ADDRESS,
				UserColumns.GROUP_ID,
				UserColumns.LEVEL,
				UserColumns.TYPE
		}, UserColumns.OWNERID+"=? and "+UserColumns.USER_ID+"=?", 	
		new String[]{PreferenceWrapper.get(PreferenceWrapper.USER_ID, ""),
				targetId},null);
		
		if(cursor!=null){
			userInfo = new ArrayList<User>();
			while(cursor.moveToNext()){
				User u = new User();
				u.setUser_id(cursor.getString(cursor.getColumnIndex(UserColumns.USER_ID)));
				u.setUser_name(cursor.getString(cursor.getColumnIndex(UserColumns.USER_NAME)));
				u.setPosition(cursor.getString(cursor.getColumnIndex(UserColumns.POSITION)));
				u.setDepartment(cursor.getString(cursor.getColumnIndex(UserColumns.DEPARTMENT)));
				u.setAddress(cursor.getString(cursor.getColumnIndex(UserColumns.ADDRESS)));
				u.setEmail(cursor.getString(cursor.getColumnIndex(UserColumns.EMAIL)));
				u.setImg_path(cursor.getString(cursor.getColumnIndex(UserColumns.IMG_PATH)));
				u.setMobile(cursor.getString(cursor.getColumnIndex(UserColumns.MOBILE)));
				u.setTelephony(cursor.getString(cursor.getColumnIndex(UserColumns.TELEPHONY)));
				u.setType(cursor.getInt(cursor.getColumnIndex(UserColumns.TYPE)));
				u.setLevel(cursor.getInt(cursor.getColumnIndex(UserColumns.LEVEL)));
				userInfo.add(u);
				Log.i("user", u.getUser_name()+":"+u.getDepartment());
			}
			cursor.close();
		}
		
		return userInfo;
	}
	
}
