package com.java;

import java.lang.reflect.Parameter;

import org.hibernate.Query;

public interface SessionServiceInter<T> {
	
	public void save(T object);
	
}
