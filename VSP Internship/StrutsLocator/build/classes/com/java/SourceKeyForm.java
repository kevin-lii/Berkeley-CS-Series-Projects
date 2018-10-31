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
	
	public String value = null;
	
	private Map<String, String> storageMap = new HashMap<String, String>();
	
	public SourceKeyForm() {
		super();	
	}
	public void buildMap() {
		InputStream infoFile = this.getClass().getClassLoader().getResourceAsStream("prop.xml");
		DocumentBuilder docBuild;
		try {
			docBuild = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = docBuild.parse(infoFile);
			NodeList bundle = doc.getElementsByTagName("bundle");
			for (int i = 0; i < bundle.getLength(); i++) {
				Node node = bundle.item(i);
				Element el = (Element) node;
				storageMap.put(el.getAttributes().getNamedItem("file").getNodeValue() , el.getAttributes().getNamedItem("location").getNodeValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public ResourceBundle loadSource() {
		ResourceBundle bundle = PropertyResourceBundle.getBundle(storageMap.get(source));
		return bundle;
	}
	public String getSource() {
		return this.source;
	}
	public String getKey() {
		return key;
	}
	public String getValue() {
		return value;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public void setValue(ResourceBundle bundle) {
		this.value = bundle.getString(key);
	}
}
