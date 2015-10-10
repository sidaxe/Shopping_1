package com.grabalook.dao;

import java.util.List;

import com.grabalook.pojo.User;



public interface CustomerDao {
	public User getCustomer(String emailId);
	public User getCustomer(int id);
	public List<User> getAllCustomers();
	
	

}
