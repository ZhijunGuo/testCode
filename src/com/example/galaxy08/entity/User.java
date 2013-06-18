package com.example.galaxy08.entity;

import java.io.Serializable;

import org.json.JSONObject;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;
/*
 * User 用户信息实体类 2013/04/15
 */
public class User implements Parcelable,Serializable{

	private static final long serialVersionUID = -2691912868069943811L;
	/*
	   * user(用户信息表)
	   * _id   数据编号
	   * user_id  用户编号
	   * user_name  用户名称
	   * img_path  用户头像路径
	   * position  用户的职位
	   * department  用户所在的部门
	   * mobile  用户的手机号
	   * telephony  用户的座机号   
	   * email  用户的电子邮件
	   * address  用户的地址
	   * group_id  用户所属的群组id
	   * level  用户的等级(建议使用等级标准区分用户)
	   * type                 用户的可见属性, 可以和level结合使用
	   */
		private int _id;//   数据编号
		private String user_id;//  用户编号
		private String user_name;//  用户名称
		private int gender;//性别
		private String img_path;//  用户头像路径
		
		private String position;//  用户的职位
		private String department;//  用户所在的部门
		private String company;//用户所在的公司
		
		private String mobile;//  用户的手机号
		private String telephony;//  用户的座机号   
		private String email;//  用户的电子邮件
		private String address;//  用户的地址
		
		private int group_id;//  用户所属的群组id
		private int level;//  用户的等级(建议使用等级标准区分用户)
		private int type;//   用户的可见属性, 可以和level结合使用
		private String owner_id;//该数据的所有者
		private int followcount;//该用户 关注的人 数量
		private int fanscount;//该用户 粉丝的 数量
		
		public String getCompany() {
			return company;
		}
		public void setCompany(String company) {
			this.company = company;
		}
		public int getGender() {
			return gender;
		}
		public void setGender(int gender) {
			this.gender = gender;
		}
		public String getOwner_id() {
			return owner_id;
		}
		public void setOwner_id(String owner_id) {
			this.owner_id = owner_id;
		}
		public int getFollowcount() {
			return followcount;
		}
		public void setFollowcount(int followcount) {
			this.followcount = followcount;
		}
		public int getFanscount() {
			return fanscount;
		}
		public void setFanscount(int fanscount) {
			this.fanscount = fanscount;
		}
		public int get_id() {
			return _id;
		}
		public void set_id(int _id) {
			this._id = _id;
		}
		public String getUser_id() {
			return user_id;
		}
		public void setUser_id(String user_id) {
			this.user_id = user_id;
		}
		public String getUser_name() {
			return user_name;
		}
		public void setUser_name(String user_name) {
			this.user_name = user_name;
		}
		public String getImg_path() {
			return img_path;
		}
		public void setImg_path(String img_path) {
			this.img_path = img_path;
		}
		public String getPosition() {
			return position;
		}
		public void setPosition(String position) {
			this.position = position;
		}
		public String getDepartment() {
			return department;
		}
		public void setDepartment(String department) {
			this.department = department;
		}
		public String getMobile() {
			return mobile;
		}
		public void setMobile(String mobile) {
			this.mobile = mobile;
		}
		public String getTelephony() {
			return telephony;
		}
		public void setTelephony(String telephony) {
			this.telephony = telephony;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public int getGroup_id() {
			return group_id;
		}
		public void setGroup_id(int group_id) {
			this.group_id = group_id;
		}
		public int getLevel() {
			return level;
		}
		public void setLevel(int level) {
			this.level = level;
		}
		public int getType() {
			return type;
		}
		public void setType(int type) {
			this.type = type;
		}
		
		public void parser(JSONObject obj){
			setUser_id(obj.optString("targetid"));
			setUser_name(obj.optString("name"));
			setPosition(obj.optString("title"));
			//部门
			setDepartment(obj.optString("dep"));
			setMobile(obj.optString("mobile"));
			setEmail(obj.optString("email"));
			setFollowcount(Integer.valueOf(obj.optString("followcount")));
			setFanscount(Integer.valueOf(obj.optString("fanscount")));
			setLevel(Integer.valueOf(obj.optString("level")));
		}
		
		public ContentValues getContentValues(){
			ContentValues cv = new ContentValues();
			//cv.put("_id",_id);
			cv.put("user_id",user_id);
			cv.put("user_name",user_name);
			cv.put("gender",gender);
			cv.put("img_path",img_path);
			
			cv.put("position",position);
			cv.put("department",department);
			cv.put("company",company);
			
			cv.put("address",address);
			cv.put("mobile",mobile);
			cv.put("telephony",telephony);
			cv.put("email",email);
			
			cv.put("group_id",group_id);
			cv.put("level",level);
			cv.put("type",type);
			cv.put("owner_id",owner_id);
			
			cv.put("followcount",followcount);
			cv.put("fanscount",fanscount);
			return cv;
		}
		
		@Override
		public int describeContents() {
			return 0;
		}
		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeInt(_id);
			dest.writeString(user_id);
			dest.writeString(user_name);
			dest.writeInt(gender);
			dest.writeString(img_path);
			
			dest.writeString(position);
			dest.writeString(department);
			dest.writeString(company);
			
			dest.writeString(address);
			dest.writeString(mobile);
			dest.writeString(telephony);
			dest.writeString(email);
			
			dest.writeInt(group_id);
			dest.writeInt(level);
			dest.writeInt(type);
			dest.writeString(owner_id);
			
			dest.writeInt(followcount);
			dest.writeInt(fanscount);
		}
		
		public static final Parcelable.Creator<User> CREATOR = new Creator<User>() {

			@SuppressWarnings("unchecked")
			@Override
			public User createFromParcel(Parcel parcel) {
				User u = new User();
				u.set_id(parcel.readInt());
				u.setUser_id(parcel.readString());
				u.setUser_name(parcel.readString());
				u.setGender(parcel.readInt());
				u.setImg_path(parcel.readString());
				
				u.setPosition(parcel.readString());
				u.setDepartment(parcel.readString());
				u.setCompany(parcel.readString());
				
				u.setAddress(parcel.readString());
				u.setMobile(parcel.readString());
				u.setTelephony(parcel.readString());
				u.setEmail(parcel.readString());
				
				u.setGroup_id(parcel.readInt());
				u.setType(parcel.readInt());
				u.setLevel(parcel.readInt());
				
				u.setOwner_id(parcel.readString());
				u.setFollowcount(parcel.readInt());
				u.setFanscount(parcel.readInt());
				
				return u;
			}

			@Override
			public User[] newArray(int size) {
				return new User[size];
			}

		};
		
}
