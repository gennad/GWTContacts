package ru.duonox.extcontacts.server;

import java.util.ArrayList;

public class GroupToArrayList {

	public static ArrayList<String[]> Convert(Group[] groupList) {

		ArrayList<String[]> dataLoader = new ArrayList<String[]>();

		for (int i = 0; i < groupList.length - 1; i++){
			dataLoader.add(new String[] {groupList[i].getName(), Integer.toString(groupList[i].getNumber()), groupList[i].getLink()});
		}

		return dataLoader;
	}

	public static ArrayList<String[]> Convert(Contact[] contactList) {

		ArrayList<String[]> dataLoader = new ArrayList<String[]>();

		for (int i = 0; i < contactList.length - 1; i++){
			dataLoader.add(new String[] {(contactList[i].getFullName() == "") ? contactList[i].getGivenName() : contactList[i].getFullName()});
		}

		return dataLoader;
	}

}