package com.java;

import java.io.InputStream;

import javax.inject.Inject;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.glassfish.hk2.api.Factory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MessageFactory implements Factory<Message>{
	@Inject 
	private DataSource ds;
	
	@Override
	public Message provide() {
		Message message = new Message(ds);
		InputStream infoFile = this.getClass().getClassLoader().getResourceAsStream("prop.xml");
		DocumentBuilder docBuild;
		try {
			docBuild = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = docBuild.parse(infoFile);
			NodeList bundle = doc.getElementsByTagName("bundle");
			for (int i = 0; i < bundle.getLength(); i++) {
				Node node = bundle.item(i);
				Element el = (Element) node;
				message.buildMap(el.getAttributes().getNamedItem("file").getNodeValue() , el.getAttributes().getNamedItem("location").getNodeValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return message;
	}
	@Override
	public void dispose(Message arg0) {
		
	}
}
