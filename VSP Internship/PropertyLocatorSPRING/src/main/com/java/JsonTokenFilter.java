package com.java;

import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultJwsHeader;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class JsonTokenFilter implements ContainerRequestFilter{

	@Context
	ServletContext context;
	
	@Context 
	HttpServletResponse response;
	
	private static final Logger logger = LogManager.getLogger("com.java");

	@Override
	public void filter(ContainerRequestContext requestContext) {
        String authString = requestContext.getHeaderString("Authorization");
        if(authString == null){
        	requestContext.abortWith(Response.status(Response.Status.BAD_GATEWAY).entity("Authorization Token is null").build());
        }
        else if(authString.startsWith("Bearer")){
		    try {
		    	Key key = (Key) context.getAttribute("SecretKey");
		        validate(authString.replace("Bearer", "").trim(), key);
				Date now = new Date(System.currentTimeMillis());
				Date exp = new Date(System.currentTimeMillis() + 1500000);
				JwtBuilder builder = Jwts.builder().setSubject("12354").setIssuedAt(now).setExpiration(exp);
				builder.signWith(SignatureAlgorithm.HS256, key);
		        response.setHeader("x-jwt", builder.compact());
		        logger.info("New token created: " + builder.compact());
		    } catch (Exception e) {
		    	logger.error("Exception caught: " + e.getMessage());
		        requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage() + " UNAUTHORIZED REQUEST: The token you entered was incorrect").build());
		        return;
		    }
        } else {
        	logger.error("Exception caught: Invalid Token");
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("Invalid Token").build());
        }
	}
		
	public boolean validate(String authString, Key key) throws Exception{
		try {
			Jws jws = Jwts.parser().setSigningKey(key).parseClaimsJws(authString);
			Header header = jws.getHeader();
			if (!(header instanceof DefaultJwsHeader)) {
				logger.error("Exception caught: Token has been changed");
				throw new ForbiddenException("The token has been changed");
			}
			return true;
		} catch (Exception e) {
			throw new ForbiddenException(e.getCause());
		}
	}
	
}


