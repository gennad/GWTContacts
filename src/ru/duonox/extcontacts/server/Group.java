package ru.duonox.extcontacts.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;




public class Group {
	
	private String name = "";
	private String link = "";
	private Integer number = 0;
	
	public static String getAllGroups(LoginedUser user) throws Exception {
						
		URL url = new URL("http://www.google.com/m8/feeds/groups/"+user.transformEmailToUrl()+"/full");
		
		URLConnection urlConn = url.openConnection();
	
		urlConn.setDoInput (true);
		// Let the RTS know that we want to do output.
		urlConn.setDoOutput (true);
		// No caching, we want the real thing.
		urlConn.setUseCaches (false);
		
		// Specify the content type.		
		urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		urlConn.setRequestProperty("Authorization", "GoogleLogin auth="+user.getAuthKey());
		urlConn.setRequestProperty("GData-Version", "3.0");				
		
		BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
		StringBuffer sb = new StringBuffer();
		String line;				
		
		
		while ((line = br.readLine()) != null)
		{
			sb.append(line);
		}
		br.close();
		
		//System.out.println(sb.toString());					
		return sb.toString();						
	}	
	
	
	public static Group[] parseAllContactsFromXml(String xml) throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		  
		StringReader reader = new StringReader(xml);
		InputSource inputSource = new InputSource(reader);
		 
		Document doc = db.parse(inputSource);
		doc.getDocumentElement().normalize();
		  		  
		NodeList nList = doc.getElementsByTagName("entry");
		  
		int numberOfElements = nList.getLength();		  
		Group[] groupList = new Group[numberOfElements];
		
		String nodeName = "";
		String nodeValue = "";
		  
		for (int i = 0; i < numberOfElements; i++) {
			groupList[i] = new Group();			
			Node curNode = nList.item(i);
			
			Node childNode = curNode.getFirstChild();
			
			if (childNode == null) continue;
						
			do {															
				groupList[i] = mapField(childNode, groupList[i]);
			} while ((childNode = childNode.getNextSibling()) != null);						
		}
		
		return groupList;
			
	}
	
	
	private static Group mapField(Node child, Group group) {
		
			
		String nodeName = "";
		String nodeValue = "";
		String childNodeName = "";
		String childNodeValue = "";
		
		nodeName = child.getNodeName();
		nodeValue = child.getNodeValue();
		
		if (nodeName.equals("id")) {
			
		}
		else if (nodeName.equals("link")) {
			//then we must retrieve additional properties
			
			Element el = (Element)child;
			String href = el.getAttribute("href");
			group.setLink(href);									
			
		}
		else if(nodeName.equals("gContact:systemGroup")) {
			Element el = (Element)child;
			String id = el.getAttribute("id");
			group.setName(id);
		}
		
		
		return group;
	}
	
	public String getName() {
		return name;
	}		
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getLink() {
		return link;
	}
	
	public void setLink(String link) {
		this.link = link;		
	}
	
	public Integer getNumber() {
		return number;
	}
	
	public void setNumber(Integer number) {
		this.number = number;		
	}
	
	
	
}
