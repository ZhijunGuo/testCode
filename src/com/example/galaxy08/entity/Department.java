package com.example.galaxy08.entity;

import java.io.Serializable;
import java.util.ArrayList;

import com.example.galaxy08.dao.DepartmentColumns;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

@SuppressWarnings("serial")
public class Department implements Serializable,Parcelable{

	//id 部门名称 部门员工
	private int department_id;
	private String department_name;
	private ArrayList<User> users;
	private int level;//部门等级
	private String owner_id;//信息所有者
	
	public String getOwner_id() {
		return owner_id;
	}
	public void setOwner_id(String owner_id) {
		this.owner_id = owner_id;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getDepartment_id() {
		return department_id;
	}
	public void setDepartment_id(int department_id) {
		this.department_id = department_id;
	}
	public String getDepartment_name() {
		return department_name;
	}
	public void setDepartment_name(String department_name) {
		this.department_name = department_name;
	}
	public ArrayList<User> getUsers() {
		return users;
	}
	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + department_id;
		result = prime * result
				+ ((department_name == null) ? 0 : department_name.hashCode());
		result = prime * result + ((users == null) ? 0 : users.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Department other = (Department) obj;
		if (department_id != other.department_id)
			return false;
		if (department_name == null) {
			if (other.department_name != null)
				return false;
		} else if (!department_name.equals(other.department_name))
			return false;
		if (users == null) {
			if (other.users != null)
				return false;
		} else if (!users.equals(other.users))
			return false;
		return true;
	}
	
	public static final Parcelable.Creator<Department> CREATOR = new Creator<Department>() {

		@Override
		public Department createFromParcel(Parcel p) {
			Department d = new Department();
			d.setDepartment_id(p.readInt());
			d.setDepartment_name(p.readString());
			d.setLevel(p.readInt());
			d.setOwner_id(p.readString());
			return null;
		}

		@Override
		public Department[] newArray(int arg0) {
			return null;
		}
		
	};
	
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(department_id);
		dest.writeString(department_name);
		dest.writeInt(level);
		dest.writeString(owner_id);
	}
	public ContentValues getContentValues() {
		ContentValues v = new ContentValues();
		v.put(DepartmentColumns.DEPARTMENT_ID, department_id);
		v.put(DepartmentColumns.DEPARTMENT_NAME, department_name);
		v.put(DepartmentColumns.DEPARTMENT_LEVEL, level);
		v.put(DepartmentColumns.OWNER_ID, owner_id);
		return v;
	}
	
	
}
