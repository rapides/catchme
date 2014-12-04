package com.catchme.exampleObjects;

import java.util.HashMap;

import com.catchme.exampleObjects.ExampleItem.ContactStateType;

public class LoggedUser extends ExampleItem {
	private String token;

	public LoggedUser(long id, String name, String surname, String email,
			String token, HashMap<String, String> avatars) {
		super(id, name, surname, email, ContactStateType.ACCEPTED, null,
				avatars);
		this.token = token;
	}

	public LoggedUser(ExampleItem item, String token) {
		super(item);
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setAvatars(HashMap<String, String> avatars) {
		this.avatars = avatars;
	}

}

