package com.grabalook.pojo;

public class Customer {
	
	int id;
	String name;
	String emailId;
	String profilePicture;
	
	public String getProfilePicture() {
		return profilePicture;
	}
	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
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
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		emailId = emailId;
	}
	@Override
	public String toString() {
		return "Customer [id=" + id + ", name=" + name + ", emailId=" + emailId + ", profilePicture=" + profilePicture
				+ "]";
	}
	
	
	

}
