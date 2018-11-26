package com.java;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

@Path("/resources")
public class MessageService {

	private Message message;

	@Autowired
	private IPropertyService propertyService;
	
	@Context
	HttpServletResponse response;
	
	private static final Logger logger = LogManager.getLogger("com.java");
	
	public IPropertyService getPropertyService() {
		return propertyService;
	}
	public void setPropertyService(IPropertyService propertyService) {
		this.propertyService = propertyService;
	}
	
	@Path("/{source}/{key}")
	@GET
	@Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
	public Response getInfo(@PathParam("source") @ValidateSource String source, @PathParam("key") String key) {
		
		try {
			Map<String, String> myMap = new HashMap<String, String>();
			String value = null;
			PropertyEntity entity = propertyService.findBySourceAndKey(source, key);
			value = entity.getValue();
//			String value = message.generateInput(source, key);
			if (value == null) {
				throw new BadRequestException();
			}
			myMap.put("value", value);
			logger.info("Value: " + value + " successfully received!");
			return Response.status(Response.Status.OK).entity(myMap).build();
		} catch (Exception e) {
			logger.error("Error: " + e.getMessage());
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}
	@Path("/{source}/{key}/{value}")
	@POST
	@Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
	public Response postInfo(@PathParam("source") @ValidateSource String source, @PathParam("key") String key, @PathParam("value") String value) {
//		message.updateMap(source, key, value);
		PropertyEntity pE = new PropertyEntity();
		pE.setName(source);
		pE.setKey(key);
		pE.setValue(value);
		propertyService.save(pE);
		Map<String, String> myMap = new HashMap<String, String>();
		myMap.put("authtoken", response.getHeader("x-jwt"));
		logger.info("Sucessfully stored!");
		return Response.status(Status.OK).entity(myMap).build();
	}
}
