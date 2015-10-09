package com.grabalook.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.grabalook.dao.CustomerDao;
import com.grabalook.pojo.Customer;


@Path("/users")
public class UserResource {
	/*
	@GET
	@Path("/signin")
	public Response signin(@QueryParam("username") String userName,
			@QueryParam("password") String password){
		String output = "Hello : " + userName + password;
		 
		return Response.status(200).entity(output).build();
	}
	*/
	@POST
	@Path("/signin")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response signin(Customer customer){
		System.out.println(customer.getEmailId());
		return Response.status(200).build();	
		
	}
	
	@POST
	@Path("/signout")
	public Response signout(@QueryParam("username") String userName,
			@QueryParam("password") String password){
		String output = "Hello : " + userName;
		 
		return Response.status(200).entity(output).build();
	}
	
	@POST
	@Path("/signup")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response signup(Customer customer){
		System.out.println(customer.getEmailId());
		return Response.status(200).build();
	}
	
	
	
	
}