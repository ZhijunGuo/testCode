package com.example.galaxy08.dao;
/*
 * task(任务表)
 * task_id  任务编号
 * user_id  任务所属用户
 * task_name  任务名称
 * level  任务的级别(0普通 1重要 2紧急)
 * start_time 开始时间
 * end_time  截止时间
 * assigner_id 任务的汇报对象id(分配者)
 * actor_id  任务的执行者id
 * remark  任务备注
 * status  任务状态(0未开始  1进行中 2完成 3上报)
 *
 * <1> 查询需要自己执行的任务：user_id==actor_id
 * <2> 查询自己分配的任务： user_id==assigner_id
 *
 */
public class TaskColumns {
	public static final String _ID="task_id";
	public static final String USER_ID="user_id";
	public static final String TASK_NAME="task_name";
	public static final String LEVEL="level";
	public static final String START_TIME="start_time";
	public static final String END_TIME="end_time";
	public static final String ASSIGNER_ID="assigner_id";
	public static final String ACTOR_ID="actor_id";
	public static final String REMARK="remark";
	public static final String STATUS="status";
}
