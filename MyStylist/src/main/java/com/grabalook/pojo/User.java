package com.grabalook.pojo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7499324136562971476L;
	//int id;
	String name;
	String email;
	String profilePic;
	
	public String getProfilePic() {
		return profilePic;
	}
	public void setProfilePic(String profilePicture) {
		this.profilePic = profilePicture;
	}
//	public int getId() {
//		return id;
//	}
//	public void setId(int id) {
//		this.id = id;
//	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Override
	public String toString() {
		return "Customer [name=" + name + ", emailId=" + email + ", profilePicture=" + profilePic
				+ "]";
	}
	
	
	

}
