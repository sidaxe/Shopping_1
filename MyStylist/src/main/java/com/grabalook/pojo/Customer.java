package com.grabalook.pojo;

public class Customer {
	
	int id;
	String name;
	String EmailId;
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
	public String getEmailId() {
		return EmailId;
	}
	public void setEmailId(String emailId) {
		EmailId = emailId;
	}
	@Override
	public String toString() {
		return "Customer [id=" + id + ", name=" + name + ", EmailId=" + EmailId + "]";
	}
	
	
	

}
