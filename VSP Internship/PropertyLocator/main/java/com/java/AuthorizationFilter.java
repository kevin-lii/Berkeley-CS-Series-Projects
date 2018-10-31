package com.java;

import java.util.List;
import java.util.ResourceBundle;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.internal.util.Base64;

public class AuthorizationFilter implements ContainerRequestFilter{
	
	@Inject
	private Message message;
	
	@Override
	public void filter(ContainerRequestContext requestContext) {
		try {
			ResourceBundle bundle = message.generateBundle("authorization");
			final MultivaluedMap <String, String> header = requestContext.getHeaders();
			final List<String> authenticationTrials = header.get("Authorization");
			if (authenticationTrials == null || authenticationTrials.isEmpty()) {
				requestContext.abortWith(Response.status(Response.Status.BAD_REQUEST).entity("BAD REQUEST: No Username and Password was entered").build());
				return;
			}
			String authenticationTrial = authenticationTrials.get(0);
			String test = authenticationTrial.replace("Basic", "").trim();
			if (test.equals(authenticationTrial)) {
				requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).entity("Please make sure that you are using a basic authentication.").build());
				return;
			}
			String userPass = new String(Base64.decode(test.getBytes()));
			String[] userPassCombo = userPass.split(":");
			if (!checkUser(userPassCombo, bundle)) {
				requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).entity("You entered the wrong username or password. Please try again.").build());
				return;
			}
		} catch (Exception e) {
			requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build());	
		}
	}
	public boolean checkUser(String[] authenticationTrial, ResourceBundle bundle) {
		if (authenticationTrial == null) {
			return false;
		} else {
			if (!bundle.keySet().contains(authenticationTrial[0]) || !bundle.getString(authenticationTrial[0]).equals(authenticationTrial[1])) {
				return false;
			}
			return true; 
		}
	
	}
}
