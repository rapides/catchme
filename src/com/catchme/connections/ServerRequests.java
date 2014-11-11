package com.catchme.connections;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ServerRequests {
	public static JSONObject getRegistationRequest(String name, String surname,
			String email, String password) {
		JSONObject o = new JSONObject();
		JSONObject user = new JSONObject();
		try {
			user.put(ConnectionConst.USER_NAME, name);
			user.put(ConnectionConst.USER_SURNAME, surname);
			user.put(ConnectionConst.USER_EMAIL, email);
			user.put(ConnectionConst.USER_PASSWORD, password);
			o.put(ConnectionConst.USER, user);
		} catch (JSONException e) {
			Log.e("JSONParseError", e.getMessage());
		}

		return o;
	}

	public static JSONObject getTokenRequest(String email, String password) {
		JSONObject o = new JSONObject();
		JSONObject user = new JSONObject();
		try {
			user.put(ConnectionConst.USER_EMAIL, email);
			user.put(ConnectionConst.USER_PASSWORD, password);
			o.put(ConnectionConst.USER, user);
		} catch (JSONException e) {
			Log.e("JSONParseError", e.getMessage());
		}
		return o;
	}

	public static JSONObject getAddContatcRequest(String email) {
		JSONObject o = new JSONObject();
		try {
			o.put(ConnectionConst.USER_EMAIL, email);
		} catch (JSONException e) {
			Log.e("JSONParseError", e.getMessage());
		}
		return o;
	}

	public static JSONObject getAllContactsRequest() {
		JSONObject o = new JSONObject();
		// TODO Auto-generated method stub
		return o;
	}
}
