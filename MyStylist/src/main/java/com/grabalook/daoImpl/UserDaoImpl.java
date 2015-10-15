package com.grabalook.daoImpl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.grabalook.dao.UserDao;
import com.grabalook.pojo.User;

@Component
public class UserDaoImpl  implements UserDao {

	@Override
	public User getUser(String emailId) {
		User user=new User(1,"Sidhant","sidhant@gmail.com","url");
		return user;
	}

	@Override
	public User getUser(int id) {
		User user=new User(1,"Sidhant","sidhant@gmail.com","url");
		return user;
	}

	@Override
	public List<User> getAllUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addUser(User user) {
		// TODO Auto-generated method stub
		return false;
	}

}
