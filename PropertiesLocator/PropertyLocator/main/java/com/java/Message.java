package com.java;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Message {
	
	Map<String, String> storageMap = new HashMap<String, String>();

	private DataSource ds;
	
	private static final Logger logger = LogManager.getLogger("com.java");
	
	@Inject
	public Message(DataSource ds) {
		this.ds = ds;
	}
	public void buildMap(String fileName, String location) {
		storageMap.put(fileName, location);
	}
	
	public void alterMap(String fileName, String key, String value) {
		try {
			ResourceBundle bundle = generateBundle(fileName);
			Properties prop = new Properties();
			fileName += ".properties";
			InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("/com/java/" + fileName);
			prop.load(inputStream);
			File file = new File(this.getClass().getClassLoader().getResource("/com/java/" + fileName).getFile());
			String path = file.getAbsolutePath();
			if (bundle.containsKey(key)) {
				prop.remove(key);
			}
			prop.setProperty(key, value);
			prop.store(new FileWriter(path), null);
			logger.info("Stored " + value + " to " + key + " in " + fileName);
		} catch (Exception e) {
			logger.error("Exception caught: " + e.getMessage());
		}
	}
	
	public String genInput(String fileName, String key) throws Exception {
		try{	
			return generateBundle(fileName).getString(key);
		} catch (MissingResourceException e) {
			logger.error("Exception caught: " + e.getMessage());
			throw new Exception("Resource does not exist on the file");
		} 
	}
	public ResourceBundle generateBundle(String fileName) throws Exception {
		try {
		ResourceBundle bundle = PropertyResourceBundle.getBundle(storageMap.get(fileName));
		return bundle;
		} catch (NullPointerException e) {
			logger.error("Exception caught: " + e.getMessage());
			throw new Exception("Resource does not exist on the file system");
		}
	}
	public String generateInput(String filename, String key) {
		String value = null;
		Connection con = null;
		Statement stmnt = null;
		try {
			con = ds.getConnection();
			stmnt = con.createStatement();
			ResultSet rs = stmnt.executeQuery("SELECT source_name, source_key, source_value FROM properties_table WHERE source_name = '" + filename + "' AND source_key ='" + key + "'");
			while (rs.next()) {
				value = rs.getString("source_value");
			}
			rs.close();
			return value;
		} catch (Exception e) {
			e.printStackTrace();
		} finally { 
	         try { 
				if(stmnt != null) stmnt.close();
				if(con != null) con.close();
	         } catch(Exception e) {  
	        	 e.printStackTrace();
	         }
	    }
		return value; 
	}
	public void updateMap(String fileName, String key, String value){
		Connection con = null;
		Statement stmnt = null;
		try {
			con = ds.getConnection();
			stmnt = con.createStatement();
			stmnt.executeUpdate("UPDATE properties_table SET source_value = '" + value + "' WHERE source_name = '" + fileName + "' AND source_key ='" + key + "'");
		} catch (Exception e) {
			e.printStackTrace();
		} finally { 
	         try { 
				if(stmnt != null) stmnt.close();
				if(con != null) con.close();
	         } catch(Exception e) {  
	        	 e.printStackTrace();
	         }
	    }
	}
	public boolean doesExist(String source) {
		return storageMap.containsKey(source);
	}
}
