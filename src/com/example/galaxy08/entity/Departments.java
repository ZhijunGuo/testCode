package com.example.galaxy08.entity;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.database.Cursor;
import android.util.Log;

import com.example.galaxy08.SysApplication;
import com.example.galaxy08.app.PreferenceWrapper;
import com.example.galaxy08.dao.DepartmentColumns;
import com.example.galaxy08.dao.GalaxyProviderInfo;

public class Departments {
	
	/**
	 * 查询数 部门信息
	 */
	public static ArrayList<Department> getDepartMentInfoDB() {

		ArrayList<Department> ds = null;
		ContentResolver cr = SysApplication.application.getContentResolver();
		String userid = PreferenceWrapper.get(PreferenceWrapper.USER_ID, "");
		Cursor cursor = cr.query(GalaxyProviderInfo.DEPARTMENT_URI,
				new String[] { DepartmentColumns._ID,
						DepartmentColumns.DEPARTMENT_ID,
						DepartmentColumns.DEPARTMENT_NAME,
						DepartmentColumns.DEPARTMENT_LEVEL,
						DepartmentColumns.OWNER_ID, },
				DepartmentColumns.OWNER_ID+"=?", new String[] {userid}, null);
		if (cursor != null) {
			ds = new ArrayList<Department>();
			while (cursor.moveToNext()) {
				Department d = new Department();
				d.setDepartment_id(cursor.getInt(cursor
						.getColumnIndex(DepartmentColumns.DEPARTMENT_ID)));
				d.setDepartment_name(cursor.getString(cursor
						.getColumnIndex(DepartmentColumns.DEPARTMENT_NAME)));
				d.setLevel(cursor.getInt(cursor
						.getColumnIndex(DepartmentColumns.DEPARTMENT_LEVEL)));
				d.setOwner_id(cursor.getString(cursor
						.getColumnIndex(DepartmentColumns.OWNER_ID)));
				Log.i("de", d.getDepartment_name()+":"+d.getDepartment_id());
				ds.add(d);
			}
			cursor.close();
		}
		return ds;
	}
}
