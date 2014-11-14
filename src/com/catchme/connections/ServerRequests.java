package com.catchme.connections;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ServerRequests {

	public static JSONObject getAcceptedContactsRequest(String token) {
		return ServerConnection.GET(ServerConst.URL_CONTACTS_ALL,
				getHeader(token));
	}

	public static JSONObject getSentContactsRequest(String token) {
		return ServerConnection.GET(ServerConst.URL_CONTACTS_SENT,
				getHeader(token));
	}

	public static JSONObject getTokenRequest(String login, String password) {
		return ServerConnection.JsonPOST(ServerConst.URL_AUTH,
				ServerRequests.buildTokenRequest(login, password),
				getHeader(null));

	}

	public static JSONObject setUserLocationRequest(String token, double lat,
			double lng) {
		return ServerConnection.JsonPOST(ServerConst.URL_POSITION_CREATE,
				buildAddPositionRequest(lat, lng), getHeader(token));
	}// TODO wysylac dane do serwera w serwisie co 5minut

	public static JSONObject addContactRequest(String token, String email) {
		return ServerConnection.JsonPOST(ServerConst.URL_CONTACTS_CREATE,
				buildAddContactRequest(email), getHeader(token));
	}// dialog do dodawania kontaktów + asyncTask + odswiezenie widoku

	public static JSONObject updateUserRequest(String token, String name,
			String surname) {
		return ServerConnection.JsonPOST(ServerConst.URL_USER_UPDATE,
				buildUpdateUserRequest(name, surname), getHeader(token));
	}

	public static JSONObject addUserRequest(String name, String surname,
			String email, String password, String confirmationPassword) {
		return ServerConnection.JsonPOST(
				ServerConst.URL_USER_CREATE,
				buildRegistationRequest(name, surname, email, password,
						confirmationPassword), getHeader(null));
	}

	public static JSONObject sendMessageRequest(String token, String message) {
		return ServerConnection.JsonPOST(ServerConst.URL_MESSAGES,
				buildSendMessageRequest(message), getHeader(token));
	}

	private static JSONObject buildSendMessageRequest(String message) {
		// TODO
		return null;
	}

	private static JSONObject buildUpdateUserRequest(String name, String surname) {
		JSONObject o = new JSONObject();
		JSONObject user = new JSONObject();
		try {
			user.put(ServerConst.USER_NAME, name);
			user.put(ServerConst.USER_SURNAME, surname);
			o.put(ServerConst.USER, user);
		} catch (JSONException e) {
			Log.e("JSONParseError", e.getMessage());
		}

		return o;
	}

	private static JSONObject buildAddContactRequest(String email) {
		JSONObject contact = new JSONObject();
		try {
			contact.put(ServerConst.USER_EMAIL, email);
		} catch (JSONException e) {
			Log.e("JSONParseError", e.getMessage());
		}
		return contact;
	}

	private static JSONObject buildAddPositionRequest(double lat, double lng) {
		JSONObject o = new JSONObject();
		JSONObject pos = new JSONObject();
		try {
			pos.put(ServerConst.POSITION_LATITUDE, lat);
			pos.put(ServerConst.POSITION_LONGITUDE, lng);
			o.put(ServerConst.POSITION, pos);
		} catch (JSONException e) {
			Log.e("JSONParseError", e.getMessage());
		}
		return o;
	}

	private static JSONObject buildRegistationRequest(String name,
			String surname, String email, String password,
			String confirmationPassword) {
		JSONObject o = new JSONObject();
		JSONObject user = new JSONObject();
		try {
			user.put(ServerConst.USER_NAME, name);
			user.put(ServerConst.USER_SURNAME, surname);
			user.put(ServerConst.USER_EMAIL, email);
			user.put(ServerConst.USER_PASSWORD, password);
			user.put(ServerConst.USER_PASSWORD_CONFIRMATION, password);
			o.put(ServerConst.USER, user);
		} catch (JSONException e) {
			Log.e("JSONParseError", e.getMessage());
		}

		return o;
	}

	private static JSONObject buildTokenRequest(String email, String password) {
		JSONObject o = new JSONObject();
		JSONObject user = new JSONObject();
		try {
			user.put(ServerConst.USER_EMAIL, email);
			user.put(ServerConst.USER_PASSWORD, password);
			o.put(ServerConst.USER, user);
		} catch (JSONException e) {
			Log.e("JSONParseError", e.getMessage());
		}
		return o;
	}

	private static Map<String, String> getHeader(String token) {
		Map<String, String> header = new HashMap<String, String>();
		header.put("Accept", "application/json");
		header.put("Content-Type", "application/json");
		header.put("Encoding", "UTF-8");
		if (token != null) {
			header.put(ServerConst.TOKEN_GET, token);
		}
		return header;
	}

}
