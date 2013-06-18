package com.example.galaxy08.task;

import java.io.Serializable;

import com.example.galaxy08.entity.User;

public class Task implements Serializable{

	private static final long serialVersionUID = 3828421061888910898L;
	private int id;
	private String name;
	private String content;
	private String startTime;
	private String endTime;
	private String commitTime;// or finishTime?
	private TASK_LEVEL level;//级别
	private User leader;
	private TASK_STATUS status;//状态
	
	/**
	 * 任务的状态：还未开启，正在执行，任务完成，任务取消，任务提交
	 */
	public static enum TASK_STATUS {
		STATUS_OFF, STATUS_ON, STATUS_FINISH,STATUS_CANCEL,
		STATUS_COMMIT
	}
	/**
	 * 任务的级别：普通，重要，紧急
	 */
	public static enum TASK_LEVEL {
		LEVEL_COMMON, LEVEL_IMPROTANT, LEVEL_INSTANCY
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public TASK_LEVEL getLevel() {
		return level;
	}
	public void setLevel(TASK_LEVEL level) {
		this.level = level;
	}
	public User getLeader() {
		return leader;
	}
	public void setLeader(User leader) {
		this.leader = leader;
	}
	public TASK_STATUS getStatus() {
		return status;
	}
	public void setStatus(TASK_STATUS status) {
		this.status = status;
	}
	
	public String getCommitTime() {
		return commitTime;
	}
	public void setCommitTime(String commitTime) {
		this.commitTime = commitTime;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
		result = prime * result + id;
		result = prime * result + ((level == null) ? 0 : level.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((startTime == null) ? 0 : startTime.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
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
		Task other = (Task) obj;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (endTime == null) {
			if (other.endTime != null)
				return false;
		} else if (!endTime.equals(other.endTime))
			return false;
		if (id != other.id)
			return false;
		if (level != other.level)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (startTime == null) {
			if (other.startTime != null)
				return false;
		} else if (!startTime.equals(other.startTime))
			return false;
		if (status != other.status)
			return false;
		return true;
	}
	
	
}
