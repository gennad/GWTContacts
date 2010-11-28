package ru.duonox.extcontacts.client;

import com.extjs.gxt.ui.client.data.BaseModel;

public class ContactsModel extends BaseModel {

	private static final long serialVersionUID = 1L;

	public ContactsModel() {
	}

	public ContactsModel(String name, Integer number) {
		set("name", name);
		set("number", number);
	}

	public ContactsModel(String name) {
		set("name", name);
	}


	public String getName() {
		return (String) get("name");
	}

	public Integer getNumber() {
		return (Integer) get("number");
	}
}
