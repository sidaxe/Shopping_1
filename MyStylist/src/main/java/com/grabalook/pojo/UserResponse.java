package com.grabalook.pojo;

import java.io.Serializable;

public class UserResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7568813201422195139L;
	int id;
	boolean newUser;
	String message;
	
	public UserResponse(int id, boolean newUser, String message) {
		super();
		this.id = id;
		this.newUser = newUser;
		this.message = message;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean newUser() {
		return newUser;
	}
	public void setNewUser(boolean newUser) {
		this.newUser = newUser;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "UserResponse [id=" + id + ", newUser=" + newUser + ", message=" + message + "]";
	}
	
}
