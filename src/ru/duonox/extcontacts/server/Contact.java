package ru.duonox.extcontacts.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;



public class Contact implements IntContact{
	protected String fullName;
	protected String givenName;
	protected String familyName;
	protected String address;
	protected String phone;
	protected String birthday;
	protected String orgName;
	protected String orgValue;
	protected String website;
	protected String content;
	protected String link;
	
	
	
		
	
	public static String getAllContacts(LoginedUser user) throws Exception {
	
		URL url = new URL("http://www.google.com/m8/feeds/contacts/"+user.transformEmailToUrl()+"/full");
		
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
	
	
	
	

	
	
	
	
	public static String getOneContact(LoginedUser user, String contactUrl) throws IOException, ParserConfigurationException, SAXException {
		URL url = new URL(contactUrl);
		
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
	
	
	public static Contact parseOneContactFromXml(String xml) throws ParserConfigurationException, SAXException, IOException {
		  DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		  DocumentBuilder db = dbf.newDocumentBuilder();
		  
		  StringReader reader = new StringReader(xml);
		  InputSource inputSource = new InputSource(reader);
		 
		  Document doc = db.parse(inputSource);
		  doc.getDocumentElement().normalize();
		  
		  
		  NodeList nList = doc.getElementsByTagName("entry");
		  		  		  		  		  		  			  			  			 
		  Contact contact = new Contact();
		  Contact c = new Contact();
			  			  			  			  
		  Node firstNode = nList.item(0);			  			 
		  Element elem = (Element) firstNode;
		  NodeList childNodes = elem.getChildNodes();
			  
		  NodeList fullNames = elem.getElementsByTagName("gd:fullName");
		  NodeList givenNames = elem.getElementsByTagName("gd:givenName");
		  NodeList familyNames = elem.getElementsByTagName("gd:familyName");
		  NodeList titles = elem.getElementsByTagName("title");
		  NodeList emails = elem.getElementsByTagName("gd:email");
		  
		  NodeList birthdaysList = elem.getElementsByTagName("gContact:birthday");
		  NodeList organizationsList = elem.getElementsByTagName("gd:organization");
		  NodeList phoneNumbersList =  elem.getElementsByTagName("gd:phoneNumber");
		  NodeList addresses = elem.getElementsByTagName("formattedAddress");
		  NodeList sitesList = elem.getElementsByTagName("gContact:website");
		  NodeList contentList = elem.getElementsByTagName("content");
		  NodeList linkList = elem.getElementsByTagName("link");
		  
		  String fullNameValue = "";
		  String givenNameValue = "";
		  String familyNameValue = "";
		  String addressValue = "";
		  String phoneValue = "";
		  String birthdayValue = "";
		  String orgNameValue = "";
		  String orgValueValue = "";
		  String websiteValue = "";
		  String contentValue = "";
		  String linkValue = "";			  			  			  
		  
		  if (linkList.getLength() != 0) {				  				  
			  for (int a = 0; a < linkList.getLength(); a++) {
				Element el = (Element)linkList.item(a);
				String attr = el.getAttribute("rel");					
				attr = attr.trim();										
				if ( attr.equals("self")) {
					linkValue = el.getAttribute("href");
					//linkValue = URLEncoder.encode(linkValue);
				}
			  }				  				  
		  }				  
			  
		  if (fullNames.getLength() != 0) {
			  fullNameValue = fullNames.item(0).getTextContent();
		  }			  			  
		  
		  if (givenNames.getLength() != 0) {
			  givenNameValue = givenNames.item(0).getTextContent();
		  }	
		  
		  if (familyNames.getLength() != 0) {
			  familyNameValue = familyNames.item(0).getTextContent();
		  }	
		  
		  if (birthdaysList.getLength() != 0) {
			  Element element = (Element) birthdaysList.item(0);
			  birthdayValue = element.getAttribute("when");
		  }
		  
		  if (organizationsList.getLength() != 0) {
			  NodeList childNodesList = organizationsList.item(0).getChildNodes();
			  
			  for (int q = 0; q < childNodesList.getLength(); q++)
			  {
				  String name = childNodesList.item(q).getNodeName();
				  
				  if (name == "gd:orgName") {
					   orgNameValue = childNodesList.item(q).getTextContent();
				  }
				  else if (name == "gd:orgTitle") {
					  orgValueValue = childNodesList.item(q).getTextContent();
				  }
			  }				  				  
		  }
		  
		
		  
		  if (phoneNumbersList.getLength() != 0) {
			  Element element = (Element) phoneNumbersList.item(0);
			  phoneValue = element.getTextContent();
		  }
		  
		  if (addresses.getLength() != 0) {
			  Element element = (Element) addresses.item(0);
			  addressValue = element.getTextContent();
		  }
		  
		  if (sitesList.getLength() != 0) {
			  Element element = (Element) sitesList.item(0);
			  websiteValue = element.getTextContent();
		  }
		  
		  if (phoneNumbersList.getLength() != 0) {
			  Element element = (Element) phoneNumbersList.item(0);
			  phoneValue = element.getTextContent();
		  }
		  
		  if (contentList.getLength() != 0) {
			  Element element = (Element) contentList.item(0);
			  contentValue = element.getTextContent();
		  }
		  
		  c.setAddress(addressValue);
		  c.setBirthday(birthdayValue);
		  c.setContent(contentValue);
		  c.setFamilyName(familyNameValue);
		  c.setFullName(fullNameValue);
		  c.setGivenName(givenNameValue);
		  c.setOrgName(orgNameValue);
		  c.setPhone(phoneValue);
		  c.setWebSite(websiteValue);	
		  c.setLink(linkValue);
	
 	  		  		  		 
		  reader.close();
		
		
		  return c;
	}
	
	
	public static Contact[] parseXmlResponse(String xml) throws Exception
	{
		 
		  DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		  DocumentBuilder db = dbf.newDocumentBuilder();
		  
		  StringReader reader = new StringReader(xml);
		  InputSource inputSource = new InputSource(reader);
		 
		  Document doc = db.parse(inputSource);
		  doc.getDocumentElement().normalize();
		  
		  
		  NodeList nList = doc.getElementsByTagName("entry");
		  
		  int numberOfElements = nList.getLength();		  
		  Contact[] contactList = new Contact[numberOfElements];		  
		  
		  for (int i = 0; i < numberOfElements; i++) {
			  			  			 
			  contactList[i] = new Contact();
			  Contact c = contactList[i];
			  			  			  			  
			  Node firstNode = nList.item(i);			  			 
			  Element elem = (Element) firstNode;
			  NodeList childNodes = elem.getChildNodes();
			  
			  NodeList fullNames = elem.getElementsByTagName("gd:fullName");
			  NodeList givenNames = elem.getElementsByTagName("gd:givenName");
			  NodeList familyNames = elem.getElementsByTagName("gd:familyName");
			  NodeList titles = elem.getElementsByTagName("title");
			  NodeList emails = elem.getElementsByTagName("gd:email");
			  
			  NodeList birthdaysList = elem.getElementsByTagName("gContact:birthday");
			  NodeList organizationsList = elem.getElementsByTagName("gd:organization");
			  NodeList phoneNumbersList =  elem.getElementsByTagName("gd:phoneNumber");
			  NodeList addresses = elem.getElementsByTagName("formattedAddress");
			  NodeList sitesList = elem.getElementsByTagName("gContact:website");
			  NodeList contentList = elem.getElementsByTagName("content");
			  NodeList linkList = elem.getElementsByTagName("link");
			  
			  String fullNameValue = "";
			  String givenNameValue = "";
			  String familyNameValue = "";
			  String addressValue = "";
			  String phoneValue = "";
			  String birthdayValue = "";
			  String orgNameValue = "";
			  String orgValueValue = "";
			  String websiteValue = "";
			  String contentValue = "";
			  String linkValue = "";
			  
			  
			  
			  
			  if (linkList.getLength() != 0) {				  				  
				  for (int a = 0; a < linkList.getLength(); a++) {
					Element el = (Element)linkList.item(a);
					String attr = el.getAttribute("rel");					
					attr = attr.trim();										
					if ( attr.equals("self")) {
						linkValue = el.getAttribute("href");
						//linkValue = URLEncoder.encode(linkValue);
					}
				  }				  				  
			  }				  
			  
			  if (fullNames.getLength() != 0) {
				  fullNameValue = fullNames.item(0).getTextContent();
			  }			  			  
			  
			  if (givenNames.getLength() != 0) {
				  givenNameValue = givenNames.item(0).getTextContent();
			  }	
			  
			  if (familyNames.getLength() != 0) {
				  familyNameValue = familyNames.item(0).getTextContent();
			  }	
			  
			  if (birthdaysList.getLength() != 0) {
				  Element element = (Element) birthdaysList.item(0);
				  birthdayValue = element.getAttribute("when");
			  }
			  
			  if (organizationsList.getLength() != 0) {
				  NodeList childNodesList = organizationsList.item(0).getChildNodes();
				  
				  for (int q = 0; q < childNodesList.getLength(); q++)
				  {
					  String name = childNodesList.item(q).getNodeName();
					  
					  if (name == "gd:orgName") {
						   orgNameValue = childNodesList.item(q).getTextContent();
					  }
					  else if (name == "gd:orgTitle") {
						  orgValueValue = childNodesList.item(q).getTextContent();
					  }
				  }				  				  
			  }
			  
			
			  
			  if (phoneNumbersList.getLength() != 0) {
				  Element element = (Element) phoneNumbersList.item(0);
				  phoneValue = element.getTextContent();
			  }
			  
			  if (addresses.getLength() != 0) {
				  Element element = (Element) addresses.item(0);
				  addressValue = element.getTextContent();
			  }
			  
			  if (sitesList.getLength() != 0) {
				  Element element = (Element) sitesList.item(0);
				  websiteValue = element.getTextContent();
			  }
			  
			  if (phoneNumbersList.getLength() != 0) {
				  Element element = (Element) phoneNumbersList.item(0);
				  phoneValue = element.getTextContent();
			  }
			  
			  if (contentList.getLength() != 0) {
				  Element element = (Element) contentList.item(0);
				  contentValue = element.getTextContent();
			  }
			  
			  c.setAddress(addressValue);
			  c.setBirthday(birthdayValue);
			  c.setContent(contentValue);
			  c.setFamilyName(familyNameValue);
			  c.setFullName(fullNameValue);
			  c.setGivenName(givenNameValue);
			  c.setOrgName(orgNameValue);
			  c.setPhone(phoneValue);
			  c.setWebSite(websiteValue);	
			  c.setLink(linkValue);
		  }
		  
		  
		  System.out.println("Root element " + doc.getDocumentElement().getNodeName());		  
		  reader.close();
		  
		  return contactList;
	}			
	
	
	public void setFullName(String val) {
		fullName = val;
	}
	
	public void setGivenName(String val) {
		givenName = val;
	}
	
	public void setFamilyName(String val) {
		familyName = val;
	}
	
	public void setAddress(String val) {
		address = val;
	}
	
	public void setPhone(String val) {
		phone = val;
	}
	
	public void setBirthday(String val) {
		birthday = val;
	}
	
	public void setOrgName(String val) {
		orgName = val;
	}
	
	public void setOrgValue(String val) {
		orgValue = val;
	}
	
	public void setWebSite(String val) {
		website = val;
	}
	
	public void setContent(String val) {
		content = val;
	}		
	
	
	public String getFullName() {
		return fullName;
	}
	
	public String getGivenName() {
		return givenName;
	}
	
	public String getFamilyName() {
		return familyName;
	}
	
	public String getAddress() {
		return address;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public String getBirthday() {
		return birthday;
	}
	
	public String getOrgName() {
		return orgName;
	}
	
	public String getOrgValue() {
		return orgValue;
	}
	
	public String getWebSite() {
		return website;
	}
	
	public String getContent() {
		return content;
	}
	
	
	public void parseOneContact() {
		
	}
	
	public void parseAllContacts() {
		
	}
	
	public void getOneContact() {
		
	}
	
	public void getAllContacts() {
		
	}
	
	public void setLink(String link) {
		this.link = link;
	}
	
	
		



	public String getLink() {
		// TODO Auto-generated method stub
		return link;
	}
	
	
	public String encodeFromContactToXML(LoginedUser user, Contact c) throws IOException, ParserConfigurationException, SAXException {
		
		//get a contact from google
		String xml = getOneContact(user, c.getLink());
		
		System.out.println(xml);
		
		System.setProperty("javax.xml.transform.TransformerFactory", "com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl");

		//now replace everything with correct values
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		  
		StringReader reader = new StringReader(xml);
		InputSource inputSource = new InputSource(reader);
		 
		Document doc = db.parse(inputSource);
		doc.getDocumentElement().normalize();
		
		Element element = doc.getDocumentElement();
		Node parentNode;		
		
		
		Node givenName = element.getElementsByTagName("gd:givenName").item(0);
		if (givenName != null) {
			parentNode = givenName.getParentNode();		
			givenName = parentNode.removeChild(givenName);								
			givenName.setTextContent(c.getGivenName());
			parentNode.appendChild(givenName);						
		}
		
		
		Node fullName = element.getElementsByTagName("gd:fullName").item(0);
		if (fullName != null) {
			parentNode = fullName.getParentNode();
			fullName = parentNode.removeChild(fullName);
			fullName.setTextContent(c.getFullName());
			parentNode.appendChild(fullName);
		}
		
		
		Node familyName = element.getElementsByTagName("gd:familyName").item(0);
		if (familyName != null) {
			parentNode = familyName.getParentNode();
			familyName = parentNode.removeChild(familyName);
			familyName.setTextContent(c.getFamilyName());
			parentNode.appendChild(familyName);	
		}
		
		
		Node email = element.getElementsByTagName("gd:email").item(0);
		if (email != null) {
			parentNode = email.getParentNode();
			email = parentNode.removeChild(email);
			email.setTextContent("ggg@gmail.com");
			parentNode.appendChild(email);
		}
		
		
		Node title = element.getElementsByTagName("gd:title").item(0);
		if (title != null) {
			parentNode = title.getParentNode();
			title = parentNode.removeChild(title);
			title.setTextContent("ggg@gmail.com");
			parentNode.appendChild(title);
		}
		
				
		Node birthday = element.getElementsByTagName("gd:birthday").item(0);
		if (birthday != null) {
			parentNode = birthday.getParentNode();
			birthday = parentNode.removeChild(birthday);
			birthday.setTextContent("ggg@gmail.com");
			parentNode.appendChild(birthday);
		}
		
		/*
		Node organization = element.getElementsByTagName("gd:organization").item(0);
		if (organization != null) {
			parentNode = organization.getParentNode();
			organization = parentNode.removeChild(organization);
			organization.setTextContent("ggg@gmail.com");
			parentNode.appendChild(organization);
		}
		*/
										
		
		String pause = "pause";
		
		// here we generate xml
		try
		{
			DOMSource domSource = new DOMSource(doc);
		    StringWriter writer = new StringWriter();
		    StreamResult result = new StreamResult(writer);
		    TransformerFactory tf = TransformerFactory.newInstance();
		    Transformer transformer = tf.newTransformer();
		    transformer.transform(domSource, result);
		    String sss = "";
		    sss = writer.toString();
		    
		    System.out.println(sss);
		    
		    return sss;
		    
		    /* maybe will be useful in future
		    String qString = "";		    
		    Document newDocument = db.newDocument();		
			Node tempNode = newDocument.importNode(element, true); 			
			newDocument.appendChild(tempNode);
			*/
		}
		catch(TransformerException ex)
		{
		    ex.printStackTrace();		    
		    return null;
		}
	}
}
