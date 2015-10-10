package com.grabalook.dao;

import java.util.List;

import com.grabalook.pojo.User;



public interface UserDao {
	public User getUser(String emailId);
	public User getUser(int id);
	public List<User> getAllUser();
	public boolean addUser(User user);
	
	

}
