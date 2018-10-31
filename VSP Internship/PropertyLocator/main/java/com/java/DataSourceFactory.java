package com.java;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.glassfish.hk2.api.Factory;
import org.h2.jdbcx.JdbcDataSource;

public class DataSourceFactory implements Factory<DataSource>{
	@Override
	public DataSource provide() {
		JdbcDataSource dataSource = new JdbcDataSource();
		dataSource.setURL("jdbc:h2:file:~/properties_database");
		dataSource.setUser("");
		dataSource.setPassword("");
		createTable(dataSource);
		return dataSource;
	}
	
	@Override
	public void dispose(DataSource arg0) {
		
	}
	public void createTable(DataSource ds) {
		Connection con = null;
		Statement stmt = null;
		try {
			FileReader reader = new FileReader(this.getClass().getClassLoader().getResource("DataSource.sql").getPath());
			BufferedReader bufferReader = new BufferedReader(reader);
			String statement = null;
			StringBuilder builder = new StringBuilder();
			while ((statement = bufferReader.readLine()) != null) {
				builder.append(statement);
			}
			con = ds.getConnection();
			stmt = con.createStatement();
			stmt.execute(builder.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			
		} catch(IOException e) {
			
		} finally{
			
			try {
				if(con != null) con.close();
				if(stmt != null) stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
