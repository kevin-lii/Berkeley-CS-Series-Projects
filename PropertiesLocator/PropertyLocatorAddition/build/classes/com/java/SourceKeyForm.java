package com.java;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.struts.action.ActionForm;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SourceKeyForm extends ActionForm {
	public String source = null;
	
	public String key = null;
	
	private Map<String, String> storageMap = new HashMap<String, String>();
	
	public SourceKeyForm() {
		super();	
	}
	public String getSource() {
		return source;
	}
	public String getKey() {
		return key;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public void setKey(String key) {
		this.key = key;
	}
}
