package ru.duonox.extcontacts.server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;


public class LoginedUser {
	private String authKey = "";
	private String email;
	private String parsedEmail = "";
	private Contact[] contacts;
	private Group[] groups;
		
	public static Contact[] contactList;	
	
	public String getEmail() {
		return this.email;
	}
	
	public  LoginedUser(String email) {
		this.email = email;
		this.parsedEmail = transformEmailToUrl();
	}
	
	public Contact[] getAllContacts() throws Exception {
		if (authKey != "") {
			
			if (contacts == null) {
				String allContactsXML = Contact.getAllContacts(this);
				contacts = Contact.parseXmlResponse(allContactsXML);
			}						
		}
		return contacts;
	}
	
	
	public Group[] getAllGroups() throws Exception {
		if (authKey != "") {
			
			if (groups == null) {
				String allGroupsXML = Group.getAllGroups(this);
				groups = Group.parseAllContactsFromXml(allGroupsXML);
			}						
		}
		return groups;
	}
	
	/**
	 * Returns one contact from the Contact[] array
	 * @param String
	 * @return Contact
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 */
	public Contact getContact(String url, LoginedUser user) throws IOException, ParserConfigurationException, SAXException {	
		if (url.indexOf("@") != -1) {
			
			url = url.replaceAll("[@]", "%40");
		}
		
		String tempString = Contact.getOneContact(user, url);
		Contact tempContact = Contact.parseOneContactFromXml(tempString);
		
		return (tempContact == null) ? null : tempContact;
		
		/*
		Contact tempContact;		
		for (int i = 0; i < contacts.length; i++) {
			String link = contacts[i].getLink().trim(); 
			if (link.equals(url)) {
				tempContact = contacts[i];
				
				t
				
				
				
				return tempContact;
			}
		}
		*/				 
						
	}
			
	
	public String getAuthKey() {
		return authKey;
	}
	
	public void setAuthKey(String key) {
		this.authKey = key;
	}
	
	/**
	 * Transform email @ symbol to %40
	 * @return 
	 */
	public String transformEmailToUrl() {		//
		
		String fullEmail = "";
		
		try {
			int sep = email.indexOf("@");			
			if (sep < 0) throw new Exception("Invalid email");
			
			String email1 = email.substring(0, sep);  
			String email2 = email.substring(sep+1);
			fullEmail = email1+"%40"+email2;						
		}
		catch(Exception e) {}
		
		return fullEmail;
	}
	
	
	public boolean loginUser(String password) throws Exception {
		
		URL url = new URL("https://www.google.com/accounts/ClientLogin");
		URLConnection urlConn = url.openConnection();
					
		urlConn.setDoInput (true);
		// Let the RTS know that we want to do output.
		urlConn.setDoOutput (true);
		// No caching, we want the real thing.
		urlConn.setUseCaches (false);
		// Specify the content type.
		urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		// Send POST output.
		DataOutputStream printout = new DataOutputStream (urlConn.getOutputStream ());
		String content =
			"accountType=" + URLEncoder.encode("HOSTED_OR_GOOGLE") +
			"&Email=" + URLEncoder.encode(this.email) +
			"&Passwd=" + URLEncoder.encode(password) +
			"&service=" + URLEncoder.encode("cp") +
			"&source=" + URLEncoder.encode("LemonComputing-ExtContacts-2.0");
		
		printout.writeBytes (content);
		printout.flush ();
		printout.close ();
		// Get response data.
		DataInputStream input = new DataInputStream (urlConn.getInputStream ());
		String str;
		String fullResponse = "";
	
		while (null != ((str = input.readLine())))
		{
			fullResponse += str;			
		}
		input.close ();
		
		
		if (fullResponse.indexOf("Error") != -1) {
			//not valid mail/password
			return false;
		}
		
				
		int position = fullResponse.indexOf("Auth");
		authKey = fullResponse.substring(position+5);
		
		System.out.println(fullResponse);
		
		if (authKey != null && authKey.trim() != "") {
			// call function for all contacts				
			return true;
		}
		else {
			return false;
		}
	}
	
	public void updateInGoogle(String xml, String contactUrl) throws IOException {
				
		URL url = new URL(contactUrl);		
		
		HttpURLConnection urlConn = (HttpURLConnection)url.openConnection();

		urlConn.setInstanceFollowRedirects(false);
		urlConn.setRequestMethod("PUT");
		urlConn.setDoInput (true);
		// Let the RTS know that we want to do output.
		urlConn.setDoOutput (true);
		// No caching, we want the real thing.
		urlConn.setUseCaches (false);
		
		
		
		// Specify the content type.		
		urlConn.setRequestProperty("Content-Type", "application/atom+xml");
		urlConn.setRequestProperty("Authorization", "GoogleLogin auth="+getAuthKey());
		urlConn.setRequestProperty("GData-Version", "3.0");
		urlConn.setRequestProperty("If-Match", "*");
				
		OutputStreamWriter out = new OutputStreamWriter(urlConn.getOutputStream());
		out.write(xml);
		out.close();
		
		
		InputStreamReader in = new InputStreamReader(urlConn.getInputStream());
		BufferedReader br = new BufferedReader(in);
		StringBuffer sb = new StringBuffer();
		String line;
		
		while ((line = br.readLine()) != null)
		{
			sb.append(line);
		}
		br.close();
		
		
		System.out.println("Answer from server: " + sb.toString()); 			
	}
	
	
}
