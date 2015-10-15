package com.practice;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.grabalook.pojo.User;

@Path("/test")
public class TestSendDto {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response  getAllUser(){
		User user1=new User();
		user1.setEmail("sidhant@gmail.com");
		user1.setId(1);
		user1.setName("Sidhant");
		User user2=new User();
		user2.setEmail("sparsh@gmail.com");
		user2.setId(2);
		user2.setName("Sparsh");
		List<User> userList=new ArrayList<User>();
		userList.add(user1);
		userList.add(user2);
		return Response.status(200).entity(userList).build();
		
		
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> adduser(List<User> userlist){
		System.out.println("j");
		return userlist;
		
	}
	
	
	
	
	

}
