package com.java;

import javax.ws.rs.POST;

import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;

public class DynamicBinding implements DynamicFeature {
	
	@Override
	public void configure(ResourceInfo resourceInfo, FeatureContext context) {
		if (resourceInfo.getResourceMethod().isAnnotationPresent(POST.class)) {
			context.register(JsonTokenFilter.class);
		}
	}

}
