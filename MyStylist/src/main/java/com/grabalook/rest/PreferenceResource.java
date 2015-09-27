package com.grabalook.rest;

import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;


@Path("/preferences")
public class PreferenceResource {

	@Path("/edit")
	public Response editPreferences(@QueryParam("username") String userName,
			@QueryParam("password") String password){
		String output = "Hello : " + userName;
		 
		return Response.status(200).entity(output).build();
	}
	
	@Path("/view")
	public Response viewPreferences(@QueryParam("username") String userName,
			@QueryParam("password") String password){
		String output = "Hello : " + userName;
		 
		return Response.status(200).entity(output).build();
	}
	
	
}
