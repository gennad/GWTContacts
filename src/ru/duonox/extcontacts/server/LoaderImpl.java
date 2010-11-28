package ru.duonox.extcontacts.server;

import java.util.ArrayList;
import ru.duonox.extcontacts.client.Loader;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class LoaderImpl extends RemoteServiceServlet implements Loader {

	ArrayList<String[]> result;
	LoginedUser loginedUser = null;

	public ArrayList<String[]> requestServer(String requestType, String email, String pass, String input) throws IllegalArgumentException {

		if (loginedUser == null) {
			this.loginedUser = new LoginedUser(email);
		}

		this.result = new ArrayList<String[]>();
		if(requestType.equals("groups")) {
			try {
				boolean authentificationResult = this.loginedUser.loginUser(pass);
				if (authentificationResult == true) {
					Group[] groupList = this.loginedUser.getAllGroups();
					this.result = GroupToArrayList.Convert(groupList);
					this.result.add(new String[]{"All","0"});
					this.result.add(new String[]{requestType});
				} else {
					this.result = null;
				}
			}
			catch(Exception e) {
			}
		} else if(requestType.equals("contacts")) {
				try {
					boolean authentificationResult = this.loginedUser.loginUser(pass);
					if (authentificationResult == true) {
						Contact[] contactList = this.loginedUser.getAllContacts();
						this.result = GroupToArrayList.Convert(contactList);
						this.result.add(new String[]{requestType});
					} else {
						this.result = null;
					}
				}
				catch(Exception e) {
				}
			} else {
			this.result = null;
		}

		return this.result;
	}
}