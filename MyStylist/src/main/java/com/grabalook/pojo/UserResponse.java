package com.grabalook.pojo;

import java.io.Serializable;

public class UserResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7568813201422195139L;
	int id;
	boolean isNewUser;
	String message;
	
	public UserResponse(int id, boolean isNewUser, String message) {
		super();
		this.id = id;
		this.isNewUser = isNewUser;
		this.message = message;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isNewUser() {
		return isNewUser;
	}
	public void setNewUser(boolean isNewUser) {
		this.isNewUser = isNewUser;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "UserResponse [id=" + id + ", isNewUser=" + isNewUser + ", message=" + message + "]";
	}
	
}
