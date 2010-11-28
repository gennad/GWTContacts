package ru.duonox.extcontacts.client;

import java.util.ArrayList;
import java.util.List;



public class GridData {

	public static List<ContactsModel> getContacts(ArrayList<String[]> servAnswer) {

		return getContacts("", servAnswer);
	}

	public static List<ContactsModel> getContacts(String groop, ArrayList<String[]> servAnswer) {

		List<ContactsModel> dataLoader = new ArrayList<ContactsModel>();

		if (groop.equals("")) {
			for (int i = 0; i < servAnswer.size() - 1; i++){
				dataLoader.add(new ContactsModel(servAnswer.get(i)[0], Integer.parseInt(servAnswer.get(i)[1])));
			}
		} else {
			for (int i = 0; i < servAnswer.size() - 1; i++){
				dataLoader.add(new ContactsModel(servAnswer.get(i)[0]));
			}
		}
		return dataLoader;
	}
}