package com.catchme.database.model;

import android.support.v4.util.LongSparseArray;

public class LoggedUser extends ExampleItem {
	private String token;

	public LoggedUser(long id, String name, String surname, String email,
			String token, LongSparseArray<String> avatars, UserSex sex,
			String dob) {
		super(id, name, surname, email, ContactStateType.ACCEPTED, null,
				avatars, sex, dob);
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

	public void setAvatars(LongSparseArray<String> avatars) {
		this.avatars = avatars;
	}

	public boolean isComplete() {
		return getName() != null && getSurname() != null && getToken() != null;
	}

}
