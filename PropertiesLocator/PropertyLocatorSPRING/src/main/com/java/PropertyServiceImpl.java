package com.java;

import org.springframework.beans.factory.annotation.Autowired;

public class PropertyServiceImpl implements IPropertyService{

	//private SessionFactory sessionFactory;
	
	@Autowired
	private PropertyRepository propertyRepository;
	
	public void setPropertyRepository(PropertyRepository propertyRepository) {
		this.propertyRepository = propertyRepository;
	}

	/*public void setSessionFactory(SessionFactory sessionFactory) {
		//this.sessionFactory = sessionFactory;
	}*/

	@Override
	public void save(PropertyEntity object) {
		propertyRepository.deleteBySourceAndKey(object.getName(), object.getKey());
		propertyRepository.saveAndFlush(object);
//		Session session = sessionFactory.openSession();
//		session.beginTransaction();
//		session.save(object);
//		session.getTransaction().commit();		
	}

	@Override
	public PropertyEntity findBySourceAndKey(String source, String key) {
		return propertyRepository.findBySourceAndKey(source, key);
//		PropertyEntity entity = null;
//		Session session = null;
//		try{
//			session = sessionFactory.openSession();
//			Query q = session.createQuery("from PropertyEntity as p WHERE p.sourceName = '" + source + "' AND p.sourceKey = '" + key + "'");
//			entity = (PropertyEntity) q.uniqueResult();
//		} catch(Exception e){
//			e.printStackTrace();
//		} finally{
//			if(session != null){session.close();}
//		}
//		return entity;
	}

}
