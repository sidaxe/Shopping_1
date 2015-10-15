package com.grabalook.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import com.grabalook.dao.UserDao;
import com.grabalook.pojo.User;
import com.grabalook.pojo.UserResponse;






@Path("/users")
public class UserResource {
	
	
	/*
	/*	
		private UserDAO userDAO;
			public static Log getLog() {
			return log;
		}

		@Autowired
		private com.ixigo.next.planner.security.dao.UserDAO userSecurityDAO;

		@PostConstruct
		public void init() {
			namedEntityApi = (NamedEntityApi) SpringApplicationContext.getBean("namedEntityApi");
		}

		
		@Path("/merge/{id}")
		@PUT
		@Consumes(MediaType.APPLICATION_JSON)
		@Produces(MediaType.APPLICATION_JSON)
		@ApiOperation(value = "Update non-cms user profile", response = GenericResponse.class)
		@ApiResponses(value = { @ApiResponse(code = 200, message = "User updated"), @ApiResponse(code = 404, message = "User not found") })
		public Response updateUserProfile(@ApiParam(value = "user id") @PathParam("id") String id, com.ixigo.next.cms.entity.User user) {
			if (StringUtils.isNotEmpty(id)) {
				com.ixigo.next.cms.entity.User existingUser = getUserSecurityDAO().getById(id);
				existingUser.merge(user);
				getUserSecurityDAO().saveUser(existingUser);
				return Response.status(Status.OK).entity(new GenericResponse(null, "User updated", null)).build();
			} else {
				return Response.status(Status.NOT_FOUND).entity(new GenericResponse(null, null, new ResponseError(404, "User not found"))).build();
			}
		}

		@GET
		@Path("/{id}")
		@Produces(MediaType.APPLICATION_JSON)
		@ApiOperation(value = "Get non-cms user profile", response = GenericResponse.class)
		@ApiResponses(value = { @ApiResponse(code = 200, message = "User fetched"), @ApiResponse(code = 404, message = "No Matching Response for Request"),
				@ApiResponse(code = 500, message = "Some internal error occured while fetchnig the data") })
		public Response getUser(@ApiParam(value = "user id") @PathParam("id") final String id, @ApiParam(value = "keys") @QueryParam("keys") final String keys) {
			try {
				String user = getUserSecurityDAO().getUserById(id, keys);
				if (user == null) {
					return Response.status(Status.NOT_FOUND).entity(new GenericResponse(null, null, new ResponseError(404, "No Matching Response for Request")))
							.build();
				}
				return Response.ok().entity(new GenericResponse(null, user, null)).build();
			} catch (Exception ex) {
				ex.printStackTrace();
				return Response.status(Status.INTERNAL_SERVER_ERROR)
						.entity(new GenericResponse(null, null, new ResponseError(500, "Some internal error occured while fetchnig the data"))).build();
			}
		}

		@POST
		@Path("/smsotp")
		@Produces("application/json; charset=utf-8")
		@ApiOperation(value = "Send otp to user of cab app", response = GenericResponse.class)
		@ApiResponses(value = { @ApiResponse(code = 200, message = "otp sent|otp already exists|invalid phone number"),
				@ApiResponse(code = 400, message = "paramters passed incorrectly|Either the phone number is incorrect or we are unable to send the sms"),
				@ApiResponse(code = 404, message = "user not found"), @ApiResponse(code = 500, message = "Inetrnal Server error occured") })
		public Response sendOtpSms(@ApiParam(value = "user id") @FormParam("userId") String userId,
				@ApiParam(value = "Phone Number") @FormParam("phoneNumber") String phoneNumber, @ApiParam(value = "Prefix") @FormParam("prefix") String prefix,
				@ApiParam(value = "countryCode") @FormParam("countryCode") String countryCode, @ApiParam(value = "gcmId") @FormParam("gcmId") String gcmId) {
			DBObject obj = new BasicDBObject();
			System.out.println("in sms otp function; parameters : " + userId + " " + phoneNumber + " " + gcmId);
			try {
				if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(phoneNumber) || StringUtils.isEmpty(prefix) || StringUtils.isEmpty(countryCode)) {
					return Response.status(Status.BAD_REQUEST).entity(new GenericResponse(null, null, new ResponseError(400, "paramters passed incorrectly")))
							.build();
				} else {
					com.ixigo.next.cms.entity.User user = null;
					if ((user = getUserSecurityDAO().getById(userId)) != null) {
						boolean isValidNumber = PhoneNumberUtils.validate(phoneNumber, countryCode);
						String otpStatus = "invalid";
						if (isValidNumber) {
							otpStatus = getUserSecurityDAO().resendOtpSms(user, prefix, phoneNumber, gcmId, "cab");
						}
						if ("true".equals(otpStatus)) {
							return Response.status(Status.ACCEPTED).entity(new GenericResponse(null, "otp sent", null)).build();
						} else if ("invalidAttempt".equals(otpStatus)) {
							return Response.status(Status.ACCEPTED).entity(new GenericResponse(null, "otp already exists", null)).build();
						} else if ("invalid".equals(otpStatus)) {
							return Response.status(Status.ACCEPTED).entity(new GenericResponse(null, null, new ResponseError(400, "invalid phone number"))).build();
						}
						return Response
								.status(Status.BAD_REQUEST)
								.entity(new GenericResponse(null, null, new ResponseError(412,
										"Either the phone number is incorrect or we are unable to send the sms"))).build();

					} else {
						return Response.status(Status.NOT_FOUND).entity(new GenericResponse(null, null, new ResponseError(404, "user not found"))).build();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				return Response.status(Status.INTERNAL_SERVER_ERROR)
						.entity(new GenericResponse(null, null, new ResponseError(500, "Internal Server error occured"))).build();
			}
		}

		@POST
		@Path("/verifyotp")
		@Produces("application/json; charset=utf-8")
		@ApiOperation(value = "Verify if the otp is correct", response = GenericResponse.class)
		@ApiResponses(value = { @ApiResponse(code = 200, message = "Otp verified"), @ApiResponse(code = 400, message = "paramters passed incorrectly"),
				@ApiResponse(code = 404, message = "User not found"), @ApiResponse(code = 409, message = "otp expired or user / otp did not match"),
				@ApiResponse(code = 500, message = "Some internal error occured") })
		public Response verifyOtp(@ApiParam(value = "user id") @FormParam("userId") String userId, @ApiParam(value = "otp") @FormParam("otp") String otp) {
			try {
				if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(otp)) {
					return Response.status(Status.BAD_REQUEST).entity(new GenericResponse(null, null, new ResponseError(400, "paramters passed incorrectly")))
							.build();
				} else {
					com.ixigo.next.cms.entity.User user = null;
					if ((user = getUserSecurityDAO().getById(userId)) != null && user.getVerifiedNumber() != null) {
						if (getUserSecurityDAO().verifyPhoneNumber(user, otp, user.getVerifiedNumber())) {
							return Response.status(Status.OK).entity(new GenericResponse(null, "Otp verified", null)).build();
						} else {
							return Response.status(Status.CONFLICT)
									.entity(new GenericResponse(null, null, new ResponseError(409, "otp expired or user / otp did not match"))).build();
						}
					} else {
						return Response.status(Status.NOT_FOUND).entity(new GenericResponse(null, null, new ResponseError(404, "User not found"))).build();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				return Response.status(Status.INTERNAL_SERVER_ERROR).entity(new GenericResponse(null, null, new ResponseError(500, "Some internal error occured")))
						.build();
			}
		}

		
		@GET
		@Path("/ixigouser/logout/{key}")
		@Produces("application/json; charset=utf-8")
		@ApiOperation(value = "Logout API for cms user", response = GenericResponse.class)
		@ApiResponses(value = { @ApiResponse(code = 200, message = "User Logged out") })
		public Response logout(@ApiParam(value = "key") @PathParam("key") String key) {
			getUserDAO().deleteRedis(key);
			return Response.status(Status.OK).entity(new GenericResponse(null, "User Logged out", null)).build();
		}

		@PUT
		@Path("/ixigouser/password")
		@Produces("application/json; charset=utf-8")
		@ApiOperation(value = "Change password of cms user", response = GenericResponse.class)
		@ApiResponses(value = { @ApiResponse(code = 200, message = "Password Changed successfully"), @ApiResponse(code = 401, message = "Invalid Key"),
				@ApiResponse(code = 500, message = "Error in changing password") })
		public Response changeUserPassword(@ApiParam(value = "authkey") @CookieParam("authkey") String authKey,
				@ApiParam(value = "X-AUTH") @HeaderParam("X-AUTH") String headerAuth, @ApiParam(value = "New Password") @FormParam("newpassword") String newPassword) {
			try {
				User user = null;
				if (StringUtils.isNotEmpty(headerAuth)) {
					user = getUserDAO().getRedis(headerAuth);
				} else {
					user = getUserDAO().getRedis(authKey);
				}
				if (user == null) {
					return Response.status(Status.UNAUTHORIZED).entity(new GenericResponse(null, null, new ResponseError(401, "Invalid Key"))).build();
				}

				MessageDigest hash = MessageDigest.getInstance("MD5");
				String md5Password = (new HexBinaryAdapter()).marshal(hash.digest(newPassword.getBytes()));
				user.setPassword(md5Password.toLowerCase());
				getUserDAO().saveUser(user);
				getUserDAO().putRedis(user);
				return Response.status(Status.OK).entity(new GenericResponse(null, "Password Changed successfully", null)).build();

			} catch (Exception ex) {
				ex.printStackTrace();
				log.error(ex);
				return Response.status(Status.INTERNAL_SERVER_ERROR).entity(new GenericResponse(null, null, new ResponseError(500, "Error in changing password")))
						.build();
			}

		}

		@PUT
		@Path("/password/reset/mail")
		@Produces("application/json; charset=utf-8")
		@ApiOperation(value = "Reset password of cms user and send corresponding mail", response = GenericResponse.class)
		@ApiResponses(value = { @ApiResponse(code = 200, message = "Mail successfully sent"), @ApiResponse(code = 400, message = "Email is empty"),
				@ApiResponse(code = 404, message = "User with given email id not present"), @ApiResponse(code = 500, message = "Internal server error") })
		public Response sendPasswordRestMail(@ApiParam(value = "email") @QueryParam("email") String email) {
			try {
				if (StringUtils.isEmpty(email)) {
					return Response.status(Status.BAD_REQUEST).entity(new GenericResponse(null, null, new ResponseError(400, "Email is empty"))).build();
				}
				User user = getUserDAO().getUserByEmailOrMobile(email, null);
				if (user != null) {
					getUserDAO().sendResetPasswordMail(user);
					return Response.status(Status.OK).entity(new GenericResponse(null, "Mail successfully sent", null)).build();
				} else {
					return Response.status(Status.NOT_FOUND)
							.entity(new GenericResponse(null, null, new ResponseError(404, "User with given email id not present"))).build();
				}
			} catch (Exception e) {
				e.printStackTrace();
				return Response.status(Status.INTERNAL_SERVER_ERROR).entity(new GenericResponse(null, null, new ResponseError(500, "Internal server error")))
						.build();
			}
		}

		@PUT
		@Path("/password/reset")
		@Produces("application/json; charset=utf-8")
		@ApiOperation(value = "Reset password of cms user", response = GenericResponse.class)
		@ApiResponses(value = { @ApiResponse(code = 200, message = "Password Successfully reset"),
				@ApiResponse(code = 400, message = "email id, key, password required"), @ApiResponse(code = 500, message = "Some internal error occured") })
		public Response resetPassword(@ApiParam(value = "key") @FormParam("key") String key, @ApiParam(value = "password") @FormParam("password") String newPassword) {
			DBObject obj = new BasicDBObject();
			try {
				if (StringUtils.isEmpty(key) || StringUtils.isEmpty(newPassword)) {
					return Response.status(Status.BAD_REQUEST).entity(new GenericResponse(null, null, new ResponseError(400, "email id, key, password required")))
							.build();
				}
				if (getUserDAO().resetPassword(key, newPassword)) {
					return Response.status(Status.OK).entity(new GenericResponse(null, "Password Successfully reset", null)).build();
				} else {

					return Response.status(Status.INTERNAL_SERVER_ERROR)
							.entity(new GenericResponse(null, null, new ResponseError(500, "Some internal error occured"))).build();
				}
			} catch (Exception e) {
				e.printStackTrace();
				return Response.status(Status.INTERNAL_SERVER_ERROR).entity(new GenericResponse(null, null, new ResponseError(500, "Some internal error occured")))
						.build();
			}
		}

		@Path("/")
		@POST
		@Produces("application/json;charset=utf-8")
		@ApiOperation(value = "Update profile of cms user", response = GenericResponse.class)
		@ApiResponses(value = { @ApiResponse(code = 200, message = "Updated user profile"),
				@ApiResponse(code = 400, message = "Either email or mobile should be present"), @ApiResponse(code = 401, message = "Invalid Key") })
		public Response updateUserProfile(@ApiParam(value = "authkey") @CookieParam("authkey") String authKey,
				@ApiParam(value = "X-AUTH") @HeaderParam("X-AUTH") String headerAuth, @ApiParam(value = "mobile") @FormParam("mobile") String mobile,
				@ApiParam(value = "email") @FormParam("email") String email) {
			User existingUser = null;
			if (StringUtils.isNotEmpty(headerAuth)) {
				existingUser = getUserDAO().getRedis(headerAuth);
			} else {
				existingUser = getUserDAO().getRedis(authKey);
			}
			if (existingUser == null) {
				return Response.status(Status.UNAUTHORIZED).entity(new GenericResponse(null, null, new ResponseError(401, "Invalid Key"))).build();
			}
			if (StringUtils.isEmpty(mobile) && StringUtils.isEmpty(email)) {
				return Response.status(Status.BAD_REQUEST).entity(new GenericResponse(null, null, new ResponseError(400, "email and mobile both cannot be empty")))
						.build();
			}
			existingUser.setMobile(mobile);
			existingUser.setEmail(email);
			existingUser.setUsername(email);
			getUserDAO().saveUser(existingUser);
			getUserDAO().putRedis(existingUser);
			return Response.status(Status.OK).entity(new GenericResponse(null, existingUser, null)).build();
		}

		@POST
		@Path("/ixigouser/login")
		@Produces("application/json;charset=utf-8")
		@ApiOperation(value = "API for login of cms user", response = GenericResponse.class)
		@ApiResponses(value = { @ApiResponse(code = 200, message = "User profile"), @ApiResponse(code = 400, message = "Please pass username and password"),
				@ApiResponse(code = 401, message = "User is not authorized"), @ApiResponse(code = 404, message = "User does not exists"),
				@ApiResponse(code = 500, message = "Some internal server error occured") })
		public Response login(@ApiParam(value = "username") @FormParam("username") String userName,
				@ApiParam(value = "password") @FormParam("password") String password) {
			try {
				if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)) {
					return Response.status(Status.BAD_REQUEST).entity(new GenericResponse(null, null, new ResponseError(400, "Please pass username and password")))
							.build();
				}
				User user = getUserDAO().getByUsername(userName);
				if (user != null) {
					MessageDigest hash = MessageDigest.getInstance("MD5");
					String md5Password = (new HexBinaryAdapter()).marshal(hash.digest(password.getBytes()));
					if (md5Password.toLowerCase().equals(user.getPassword())) {
						String key = UniqueTrackingCodeUtils.generateUniqueUserId();
						user.setKey(key);
						getUserDAO().saveUser(user);
						getUserDAO().putRedis(user);
						return Response.status(Status.OK).entity(new GenericResponse(null, user, null)).cookie(new NewCookie(new Cookie("authkey", key))).build();
					}
					return Response.status(Status.UNAUTHORIZED).entity(new GenericResponse(null, null, new ResponseError(401, "User is not authorized"))).build();
				}
				return Response.status(Status.NOT_FOUND).entity(new GenericResponse(null, null, new ResponseError(404, "User does not exists"))).build();
			} catch (Exception ex) {
				ex.printStackTrace();
				log.error(ex);
				return Response.status(Status.INTERNAL_SERVER_ERROR)
						.entity(new GenericResponse(null, null, new ResponseError(500, "Some internal server error occured"))).build();
			}
		}

		@GET
		@Path("/ixigouser/{key}")
		@Produces("application/json")
		@ApiOperation(value = "API to maintain session of the user", response = GenericResponse.class)
		@ApiResponses(value = { @ApiResponse(code = 200, message = "User password"), @ApiResponse(code = 400, message = "Please pass a valid key"),
				@ApiResponse(code = 404, message = "Invalid Cookie"), @ApiResponse(code = 500, message = "Internal server error") })
		public Response login(@ApiParam(value = "key") @PathParam("key") String key) {
			try {
				if (StringUtils.isEmpty(key)) {
					return Response.status(Status.BAD_REQUEST).entity(new GenericResponse(null, null, new ResponseError(400, "Please pass a valid key"))).build();
				}
				User user = getUserDAO().getRedis(key);
				if (user != null) {
					return Response.status(Status.OK).entity(new GenericResponse(null, user, null)).cookie(new NewCookie(new Cookie("authkey", key))).build();
				}
				return Response.status(Status.NOT_FOUND).entity(new GenericResponse(null, null, new ResponseError(404, "Invalid Cookie"))).build();
			} catch (Exception ex) {
				log.error(ex);
				return Response.status(Status.INTERNAL_SERVER_ERROR).entity(new GenericResponse(null, null, new ResponseError(500, "Internal server error")))
						.build();
			}

		}

		@GET
		@Path("/email/exists")
		@Produces("application/json;charset=utf-8")
		@ApiOperation(value = "Fetch cms user from email, if exists", response = GenericResponse.class)
		@ApiResponses(value = { @ApiResponse(code = 200, message = "User profile"), @ApiResponse(code = 404, message = "User with email not found"),
				@ApiResponse(code = 500, message = "Internal server error") })
		public Response isEmailPresent(@ApiParam(value = "email", required = true) @QueryParam("email") String email) {
			try {
				User user = getUserDAO().getUserByEmailOrMobile(email, null);
				DBObject obj = new BasicDBObject();
				if (user != null) {
					if (user.getQcRejectedCount() >= 3) {

						obj.put("isUserBanned", true);

					}
					obj.put("user", user);
					return Response.ok().entity(new GenericResponse(null, obj, null)).build();
				} else {
					return Response.status(Status.NOT_FOUND).entity(new GenericResponse(null, null, new ResponseError(404, "User with email not found"))).build();
				}
			} catch (Exception e) {
				e.printStackTrace();
				return Response.status(Status.INTERNAL_SERVER_ERROR).entity(new GenericResponse(null, null, new ResponseError(500, "Internal server error")))
						.build();
			}
		}

		@GET
		@Path("/user/email/mobile")
		@Produces("application/json; charset=utf-8")
		@ApiOperation(value = "Reset password of cms user and send corresponding mail", response = GenericResponse.class)
		@ApiResponses(value = {
				@ApiResponse(code = 200, message = "User profile"),
				@ApiResponse(code = 400, message = "Email or mobile missing!"),
				@ApiResponse(code = 403, message = "You cannot create/claim a listing on ixigo and have been banned permanently. Please get in touch with our sales department."),
				@ApiResponse(code = 500, message = "Some internal error occured") })
		public Response getUserByEmailOrMobile(@ApiParam(value = "name", required = true) @QueryParam("name") String name,
				@ApiParam(value = "email", required = true) @QueryParam("email") String email,
				@ApiParam(value = "mobile", required = true) @QueryParam("mobile") String mobile,@ApiParam(value = "country Code", required = true) @QueryParam("countryCode") String countryCode) {
			try {
				if (StringUtils.isEmpty(email) || StringUtils.isEmpty(mobile) || StringUtils.isEmpty(countryCode)) {
					return Response.status(Status.BAD_REQUEST).entity(new GenericResponse(null, null, new ResponseError(400, "Email or mobile or country code is missing!"))).build();
				}
				if (!PhoneNumberUtils.validate(mobile, countryCode)) {
					return Response.status(422).entity(new GenericResponse(null, null, new ResponseError(422, "Mobile number is not in proper format."))).build();
				}
				if (StringUtils.isEmpty(name)) {
					return Response.status(Status.BAD_REQUEST).entity(new GenericResponse(null, null, new ResponseError(400, "Please enter your name"))).build();
				}
				User user = getUserDAO().getUserByEmailOrMobile(email, mobile);
				if (user == null) {
					user = new User(name, email, mobile);
					user.setGlobalRole(EXTERNAL_ROLE);
					getUserDAO().saveUser(user);
				} else if(StringUtils.isNotEmpty(user.getEmail()) && user.getEmail().equalsIgnoreCase(email)){
					user.setMobile(mobile);
					if(StringUtils.isEmpty(user.getName())){
						user.setName(name);
					}
					getUserDAO().saveUser(user);
				}
				if (user.getQcRejectedCount() >= 3) {
					DBObject obj = new BasicDBObject();
					obj.put("isUserBanned", true);
					return Response
							.ok(Status.FORBIDDEN)
							.entity(new GenericResponse(null, obj, new ResponseError(403,
									"You cannot create/claim a listing on ixigo and have been banned permanently. Please get in touch with our sales department.")))
							.build();
				}
				return Response.ok().entity(new GenericResponse(null, user, null)).build();
			} catch (Exception ex) {
				ex.printStackTrace();
				return Response.status(Status.INTERNAL_SERVER_ERROR).entity(new GenericResponse(null, null, new ResponseError(500, "Some internal error occured")))
						.build();
			}
		}

		@GET
		@Path("/properties/assigned")
		@Produces("application/json; charset=utf-8")
		@ApiOperation(value = "Fetch properties assigned to a DRE", response = GenericResponse.class)
		@ApiResponses(value = { @ApiResponse(code = 200, message = "List of Assigned Properties"),
				@ApiResponse(code = 400, message = "Either username is empty or productName is missing/incorrect"),
				@ApiResponse(code = 401, message = "Either user not found or user is unauthorized"),
				@ApiResponse(code = 500, message = "Some internal error occured") })
		public Response getAssignedHotelsOfADre(@ApiParam(value = "username") @QueryParam("username") String username,
				@ApiParam(value = "productName") @QueryParam("productName") String productName, @ApiParam(value = "keys") @QueryParam("keys") String keys,
				
				@ApiParam(value = "startDate") @QueryParam("startDate") String startDate,@ApiParam(value = "endDate") @QueryParam("endDate") String endDate,@ApiParam(value = "en") @QueryParam("en") String enabled
				
				) {
			try {

				if (StringUtils.isEmpty(username) || StringUtils.isEmpty(productName) || MarketPlaceProductEnum.getProductByName(productName) == null) {
					return Response.status(Status.BAD_REQUEST)
							.entity(new GenericResponse(null, null, new ResponseError(400, "Either username is empty or productName is missing/incorrect")))
							.build();
				}
				User user = getUserDAO().getByUsername(username);

				if (user == null || user.getGlobalRole() == null || !user.getGlobalRole().equalsIgnoreCase(RoleEnum.DRE.getRole())) {
					return Response.status(Status.UNAUTHORIZED)
							.entity(new GenericResponse(null, null, new ResponseError(401, "Either user not found or user is unauthorized"))).build();
				}
				MarketPlaceProductEnum mpe = MarketPlaceProductEnum.getProductByName(productName);
				PropertyService service = ServiceContainer.instance().getService(PropertyService.class, mpe.getProductName());
				Long sDate=null;
				Long eDate=null;
				
	            if(StringUtils.isNotEmpty(startDate)&&StringUtils.isNotEmpty(endDate))
	            {
	            	try{
	            	sDate=Long.parseLong(startDate);
	            	eDate=Long.parseLong(endDate);
	            	}
	            	catch(Exception e)
	            	{
	            		e.printStackTrace();
	            		return Response.status(Status.BAD_REQUEST)
	    						.entity(new GenericResponse(null, null, new ResponseError(400, "Dates are incorrect")))
	    						.build();
	            		
	            	}
	            }
				return Response.status(Status.OK)
						.entity(new GenericResponse(null, service.fetchAssignedProperties(Class.forName(mpe.getPojoClass()), keys, user.getId(),sDate,eDate,enabled), null)).build();

			} catch (Exception e) {
				e.printStackTrace();
				return Response.status(Status.INTERNAL_SERVER_ERROR).entity(new GenericResponse(null, null, new ResponseError(500, "Some internal error occured")))
						.build();

			}
		}

		@GET
		@Path("/dre/list")
		@Produces("application/json; charset=utf-8")
		@ApiOperation(value = "Fetch list of available DREs", response = GenericResponse.class)
		@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"), @ApiResponse(code = 401, message = "Invalid Key"),
				@ApiResponse(code = 500, message = "Internal Server Error") })
		public Response getListOfDREs(@ApiParam(value = "authkey") @CookieParam("authkey") String authKey,
				@ApiParam(value = "X-AUTH") @HeaderParam("X-AUTH") String headerAuth, @ApiParam(value = "keys") @QueryParam("keys") String keys) {
			try {

				User user = null;
				if (StringUtils.isNotEmpty(headerAuth)) {
					user = getUserDAO().getRedis(headerAuth);
				} else {
					user = getUserDAO().getRedis(authKey);
				}
				if (user == null) {
					return Response.status(Status.UNAUTHORIZED).entity(new GenericResponse(null, null, new ResponseError(401, "Invalid Key"))).build();
				}
				if (StringUtils.isEmpty(keys)) {
					keys = "n,u,em,_id";
				}
				List<User> dreList = getUserDAO().getAllUsers(Integer.MAX_VALUE, 0, null, null, null, RoleEnum.DRE.getRole(), keys);
				return Response.status(Status.OK).entity(new GenericResponse(null, dreList, null)).build();
			} catch (Exception e) {
				e.printStackTrace();
				return Response.status(Status.INTERNAL_SERVER_ERROR).entity(new GenericResponse(null, null, new ResponseError(500, "Internal Server Error")))
						.build();
			}

		}

		
		public UserDAO getUserDAO() {
			if (userDAO == null)
				userDAO = (UserDAO) SpringApplicationContext.getBean("userDAO");
			return userDAO;
		}

		public com.ixigo.next.planner.security.dao.UserDAO getUserSecurityDAO() {
			if (userSecurityDAO == null)
				userSecurityDAO = (com.ixigo.next.planner.security.dao.UserDAO) SpringApplicationContext.getBean("userSecurityDAO");
			return userSecurityDAO;
		}

		public NamedEntityApi getNamedEntityApi() {
			if (namedEntityApi == null) {
				namedEntityApi = (NamedEntityApi) SpringApplicationContext.getBean("namedEntityApi");
			}

			return namedEntityApi;
		}

		public void setNamedEntityApi(NamedEntityApi namedEntityApi) {
			this.namedEntityApi = namedEntityApi;
		}
*/
//	@POST
//	@Path("/signin")
//	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//	public Response signin(@FormParam("username") String userName,
//			@FormParam("password") String password){
//		String output = "Hello : " + userName + password;
//		System.out.println(output);
//		if(userName!=null && userName.equals("sidhant"))
//		return Response.status(200).entity(output).build();
//		else
//		return Response.status(200).entity("User not found").build();
//	}

	//@Autowired
	//UserDao userDao;
	
	
	@POST
	@Path("/signup")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response signup(User user){
		user.setId(10);
	    System.out.println(user);
	   // userDao.addUser(user);
		//return Response.status(200).entity(user).build();	
	   
	    if(user.getEmail().equals("sidhant"))
	    {	 UserResponse userResponse =new UserResponse(user.getId(),true,"User Added to Db");
	    	return Response.status(200).entity(userResponse).build();
	    }
	    else
	    {
	    	UserResponse userResponse =new UserResponse(user.getId(),false,"User couldnt be added");
	    	return Response.status(404).entity("user not found").build();
	    } 
		
	}
	
	@GET
	@Path("/getuser/{userid}")
	@Produces(MediaType.APPLICATION_JSON)
	public User getUser(@PathParam("userid") int userId ){
		//User user=userDao.getUser(1);
		//User user=userDao.getUser(userId);
		User user=new User();
		return user;
	}

//	@POST
//	@Path("/signup")
//	//@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	public Response signup(InputStream incomingData){
//    	StringBuilder builder = new StringBuilder();
//     	try {
//  		BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
//			String line = null;
//			while ((line = in.readLine()) != null) {
//				builder.append(line);
//			}
//		} catch (Exception e) {
//			
//		}
//     	System.out.println(builder.toString());
//     	String requestString=builder.toString();
//     	
//       return Response.status(200).entity(requestString).build();
//	}

}
