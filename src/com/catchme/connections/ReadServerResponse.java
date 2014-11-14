package com.catchme.connections;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.catchme.exampleObjects.ExampleContent.ExampleItem;
import com.catchme.exampleObjects.ExampleContent.LoggedUser;

public class ReadServerResponse {
	public static ArrayList<String> getErrors(JSONObject fullResponse) {
		ArrayList<String> errors = null;
		try {
			if (!isSuccess(fullResponse)) {
				errors = new ArrayList<String>();
				JSONArray a = fullResponse
						.getJSONArray(ServerConst.ERROR_MESSAGES);
				for (int i = 0; i < a.length(); i++) {
					errors.add(a.getString(i));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return errors;

	}

	public static LoggedUser getLoggedUser(JSONObject fullResponse) {
		LoggedUser logged = null;
		try {
			if (isSuccess(fullResponse)) {
				JSONObject user = fullResponse.getJSONObject(ServerConst.USER);

				long id = user.getLong(ServerConst.USER_ID);
				String name = user.getString(ServerConst.USER_NAME);
				String surname = user.getString(ServerConst.USER_SURNAME);
				String email = user.getString(ServerConst.USER_EMAIL);
				logged = new LoggedUser(id, name, surname, email,
						ExampleItem.IMAGE_INVALID, getToken(fullResponse));

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return logged;
	}

	public static ArrayList<ExampleItem> getContactList(JSONObject fullResponse) {
		ArrayList<ExampleItem> contactList = null;
		try {
			if (isSuccess(fullResponse)) {
				contactList = getContactList(getContactsArray(fullResponse));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return contactList;
	}

	public static boolean isSuccess(JSONObject fullResponse) {
		boolean isSuccess = false;
		try {
			if(fullResponse!=null){
				isSuccess = fullResponse.getBoolean(ServerConst.SUCCESS);
			}else{
				isSuccess = false;
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return isSuccess;
	}

	private static ArrayList<ExampleItem> getContactList(JSONArray contactsArray)
			throws JSONException {
		ArrayList<ExampleItem> contactList = new ArrayList<ExampleItem>();
		for (int i = 0; i < contactsArray.length(); i++) {

			contactList.add(getContact(contactsArray.getJSONObject(i)));

		}
		return contactList;
	}

	private static JSONArray getContactsArray(JSONObject response)
			throws JSONException {

		return response.getJSONArray(ServerConst.CONTACTS);

	}

	private static ExampleItem getContact(JSONObject o) throws JSONException {
		int state = o.getInt(ServerConst.USER_STATE);
		JSONObject user = o.getJSONObject(ServerConst.USER);

		long id = user.getLong(ServerConst.USER_ID);
		String name = user.getString(ServerConst.USER_NAME);
		String surname = user.getString(ServerConst.USER_SURNAME);
		String email = user.getString(ServerConst.USER_EMAIL);
		ExampleItem contact = new ExampleItem(id, name, surname, email,
				ExampleItem.IMAGE_INVALID, state);
		return contact;
	}

	private static String getToken(JSONObject fullResponse)
			throws JSONException {
		String response = null;
		if (isSuccess(fullResponse)) {
			response = fullResponse.getString(ServerConst.TOKEN_RESPONSE);
		}
		return response;
	}
}
