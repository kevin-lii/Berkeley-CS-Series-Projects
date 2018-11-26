package com.java;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.collections.map.HashedMap;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class PropertyServiceImpl implements IPropertyService{

	@Inject
	private SessionFactory factory;
	
	@Override
	public void save(PropertyEntity object) {
		Session session = factory.openSession();
		session.beginTransaction();
		session.save(object);
		session.getTransaction().commit();		
	}

	@Override
	public PropertyEntity findBySourceAndKey(String source, String key) {
		PropertyEntity entity = null;
		Session session = null;
		try{
			session = factory.openSession();
			Query q = session.createQuery("from PropertyEntity as p WHERE p.sourceName = '" + source + "' AND p.sourceKey = '" + key + "'");
			entity = (PropertyEntity) q.uniqueResult();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(session != null){session.close();}
		}
		return entity;
	}

}
