package ru.duonox.extcontacts.client;

import java.util.ArrayList;


import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of Loader.
 */
public interface LoaderAsync {

	void requestServer(String requestType, String email, String pass, String input, AsyncCallback<ArrayList<String[]>> callback) throws IllegalArgumentException;
	
}
