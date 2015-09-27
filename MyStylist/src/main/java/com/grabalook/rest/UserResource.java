package com.grabalook.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;


@Path("/users")
public class UserResource {
	
	@GET
	@Path("/signin")
	public Response signin(@QueryParam("username") String userName,
			@QueryParam("password") String password){
		String output = "Hello : " + userName;
		 
		return Response.status(200).entity(output).build();
	}
	
	
	
	
	
}