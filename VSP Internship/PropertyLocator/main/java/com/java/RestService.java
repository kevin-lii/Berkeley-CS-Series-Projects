package com.java;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

import javax.crypto.spec.SecretKeySpec;
import javax.inject.Singleton;
import javax.servlet.ServletContext;
import javax.sql.DataSource;
import javax.ws.rs.core.Context;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.hibernate.SessionFactory;

public class RestService extends ResourceConfig {

	private ServletContext servletContext;
	
	public RestService(@Context ServletContext context) {
		servletContext = context;
		packages("com.java");
		property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
		register(new AbstractBinder() {
			@Override
			protected void configure() {
				bindFactory(DataSourceFactory.class).to(DataSource.class).proxy(false).in(Singleton.class);
				bindFactory(MessageFactory.class).to(Message.class).proxy(false).in(Singleton.class);
				bindFactory(HibernateFactory.class).to(SessionFactory.class).proxy(false).in(Singleton.class);
				bind(PropertyServiceImpl.class).to(IPropertyService.class);
			}
		});
		genSecretKey(context);
		register(new DynamicBinding());
	}
	public void genSecretKey(ServletContext context) {
		File file = new File("C:\\Users\\kevili\\Documents\\KEYS.txt");
		if (!file.exists()) {
			try {
				file.createNewFile();
				Key createKey = MacProvider.generateKey(SignatureAlgorithm.HS256);
				FileOutputStream outputStream = new FileOutputStream(file);
				outputStream.write(createKey.getEncoded());
				outputStream.close();
			} catch (IOException e) {
				System.out.print(e.getMessage());
			}
		}
		findSecretKey();
    	Key key = (Key) context.getAttribute("SecretKey");
		Date now = new Date(System.currentTimeMillis());
		Date exp = new Date(System.currentTimeMillis() + 1500000);
		JwtBuilder builder = Jwts.builder().setSubject("12354").setIssuedAt(now).setExpiration(exp);
		builder.signWith(SignatureAlgorithm.HS256, key);
		System.out.println(builder.compact());
	}

	private void findSecretKey() {
		File file = new File("C:\\Users\\kevili\\Documents\\KEYS.txt");
		Key key = null;
		try {
			byte[] buffer = new byte[(int) file.length()];
			FileInputStream inputStream = new FileInputStream(file);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			int i = 0;
			while ((i = inputStream.read(buffer)) != -1) {
				outputStream.write(i);
			}
			byte[] encodedKey = outputStream.toByteArray();
			key = new SecretKeySpec(encodedKey, SignatureAlgorithm.HS256.getValue());
			servletContext.setAttribute("SecretKey", key);
		} catch (Exception e) {
			return;
		}	
	}

}
