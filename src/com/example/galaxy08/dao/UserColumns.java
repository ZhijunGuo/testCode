package com.example.galaxy08.dao;
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
public class UserColumns {
	public static final String _ID="_id";
	public static final String USER_ID="user_id";
	public static final String USER_NAME="user_name";
	public static final String IMG_PATH="img_path";
	public static final String POSITION="position";
	public static final String DEPARTMENT="department";
	public static final String MOBILE="mobile";
	public static final String TELEPHONY="telephony";
	public static final String EMAIL="email";
	public static final String ADDRESS="address";
	public static final String GROUP_ID="group_id";
	public static final String LEVEL="level";
	public static final String TYPE="level";
	public static final String OWNERID="owner_id";
}
