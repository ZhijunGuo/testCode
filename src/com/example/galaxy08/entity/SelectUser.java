package com.example.galaxy08.entity;

import java.io.Serializable;

public class SelectUser implements Serializable{
	
	private static final long serialVersionUID = -7129789592726813048L;
	private User contact;
	private boolean isSelect;
	public User getContact() {
		return contact;
	}
	public void setContact(User contact) {
		this.contact = contact;
	}
	public boolean isSelect() {
		return isSelect;
	}
	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}

}
