package com.java;

import org.glassfish.hk2.api.Factory;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateFactory implements Factory<SessionFactory>{

    public SessionFactory provide() {
        try {
        	SessionFactory sf= new Configuration().configure().buildSessionFactory();
        	return sf;
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

	@Override
	public void dispose(SessionFactory arg0) {		
	}


}
