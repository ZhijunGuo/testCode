package com.example.galaxy08.entity;

import java.io.Serializable;
/*
 * Resume 摘要
 *  2013/04/15
 */
public class Resume implements Serializable {
	private static final long serialVersionUID = 7673347534817649176L;
	private String resumeId = "";//简历id
	private String userId = ""; //用户id
	private String targetId = "";//??
	private String company = ""; //公司
	private String title = ""; //标题
	private String department = ""; //部门
	private int beginYear = -1; //开始时间(年份)
	private int beginMonth = -1; //开始时间(月份)
	private int endYear = -1; //结束时间？
	private int endMonth = -1;
	private String type = "-1";
	private String creatTime = "";
  

	public int getBeginMonth() {
		return beginMonth;
	}


	public void setBeginMonth(int beginMonth) {
		this.beginMonth = beginMonth;
	}


	public int getEndMonth() {
		return endMonth;
	}


	public void setEndMonth(int endMonth) {
		this.endMonth = endMonth;
	}


	public int getBeginYear() {
		return beginYear;
	}


	public void setBeginYear(int beginYear) {
		this.beginYear = beginYear;
	}


	public int getEndYear() {
		return endYear;
	}


	public void setEndYear(int endYear) {
		this.endYear = endYear;
	}


	public String getResumeId() {
		return resumeId;
	}


	public void setResumeId(String resumeId) {
		this.resumeId = resumeId;
	}


	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public String getTargetId() {
		return targetId;
	}


	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}


	public String getCompany() {
		return company;
	}


	public void setCompany(String company) {
		this.company = company;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getDepartment() {
		return department;
	}


	public void setDepartment(String department) {
		this.department = department;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getCreatTime() {
		return creatTime;
	}


	public void setCreatTime(String creatTime) {
		this.creatTime = creatTime;
	}


	@Override
	public String toString() {
		return "Resume [resumeId="+resumeId+",userId=" + userId + ", targetid=" + targetId + ", company="
				+ company + ", position=" + title + ", department="
				+ department + ", beginYear=" + beginYear + ", beginMouth="+beginMonth+", endYear="
				+ endYear + ", endMouth="+endMonth +", type="+type+", creatTime ="+creatTime+"]";
	}

}
