package com.java;

public interface IPropertyService extends SessionServiceInter<PropertyEntity>{

	public PropertyEntity findBySourceAndKey(String source, String key);

}
