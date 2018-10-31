package com.java;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="properties_table")
public class PropertyEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID", unique=true)
	private int id;
	
	@Column(name="source_name", length=128)
	private String sourceName;
	
	@Column(name="source_key", length=128)
	private String sourceKey;
	
	@Column(name="source_value")
	private String sourceValue;

	public int getID() {
		return id;
	}
	public String getName() {
		return sourceName;
	}
	public String getKey() {
		return sourceKey;
	}
	public String getValue() {
		return sourceValue;
	}
	public void setID(int id) {
		this.id = id;
	}
	public void setName(String source_name) {
		this.sourceName = source_name;
	}
	public void setKey(String key) {
		sourceKey = key;
	}
	public void setValue(String value) {
		sourceValue = value;
	}
}
