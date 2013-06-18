package com.example.galaxy08.dao;
/*
 * group(群组表)
 * group_id  群组编号
 * group_name  群组名称
 * img_path  群组头像
 * headman_id  群组领导id
 * time_stamp  消息的时间戳
 * level  群组等级(建议使用等级区分群组,用户等级高于群组等级才可以加入)
 * 查询群组内的用户：查询用户信息表中group_id==目标group_id的用户
 */
public class GroupColumns {
	public static final String _ID="group_id";
	public static final String GROUP_NAME="group_name";
	public static final String IMG_PATH="img_path";
	public static final String HEADMAN_ID="headman_id";
	public static final String TIME_STAMP="time_stamp";
	public static final String LEVEL="level";
}
