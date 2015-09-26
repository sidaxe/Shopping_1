package com.grabalook.dao;

import java.util.List;

import com.grabalook.pojo.Customer;

public interface CustomerDao {
	public Customer getCustomer(String emailId);
	public Customer getCustomer(int id);
	public List<Customer> getAllCustomers();
	
	

}
