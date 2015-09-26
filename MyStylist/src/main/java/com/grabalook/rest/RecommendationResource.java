package com.grabalook.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/recommendations")
public class RecommendationResource {
	
	
	@GET
	@Path("/get/all")
	public Response getAllRequests(@QueryParam("username") String userName){
		String output = "Hello : " + userName;
		 
		return Response.status(200).entity(output).build();
	}
	
	@POST
	@Path("/order/{recommendationId}")
	@Consumes()
	public Response addNewRequest(@PathParam("recommendationId") String recommendationId){
		String output = "Hello Wnt this: " + recommendationId;
		 
		return Response.status(200).entity(output).build();
	}
	
}
