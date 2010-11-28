package ru.duonox.extcontacts.client;

import java.util.ArrayList;


import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("loader")
public interface Loader extends RemoteService {

	ArrayList<String[]> requestServer(String requestType, String email, String pass, String input) throws IllegalArgumentException;

}
