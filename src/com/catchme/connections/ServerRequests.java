package com.catchme.connections;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.catchme.database.model.ExampleItem.ContactStateType;
import com.catchme.database.model.ExampleItem.UserSex;

public class ServerRequests {
	public static JSONObject getLocations(String token,
			ArrayList<String> contactIds, int number) {
		return ServerConnection.JsonPOST(ServerConst.URL_POSITIONS_GET,
				buildGetPositionsRequest(contactIds, number), getHeader(token));
	}

	public static JSONObject uploadAvatar(String token, String filepath) {
		return ServerConnection.uploadImage(ServerConst.URL_USER_UPDATE_AVATAR,
				filepath, getImageUploadHeader(token));
	}

	public static JSONObject getMessagesInit(String token, long conversationId) {
		return ServerConnection.GET(
				ServerConst.URL_MESSAGES_GET_PART1 + conversationId
						+ ServerConst.URL_MESSAGES_GET_PART2_TYPE_INIT,
				getHeader(token));
	}

	public static JSONObject getMessagesNewer(String token,
			long conversationId, long newestMessageId) {
		return ServerConnection.GET(ServerConst.URL_MESSAGES_GET_PART1
				+ conversationId
				+ ServerConst.URL_MESSAGES_GET_PART2_TYPE_NEWER
				+ ServerConst.MESSAGE_LAST_ID + newestMessageId,
				getHeader(token));
	}

	public static JSONObject getMessagesOlder(String token,
			long conversationId, long oldestMessageId) {
		return ServerConnection.GET(ServerConst.URL_MESSAGES_GET_PART1
				+ conversationId
				+ ServerConst.URL_MESSAGES_GET_PART2_TYPE_OLDER
				+ ServerConst.MESSAGE_LAST_ID + oldestMessageId,
				getHeader(token));
	}

	public static JSONObject getAllContactsRequest(String token) {
		return ServerConnection.GET(ServerConst.URL_CONTACTS_ALL,
				getHeader(token));
	}

	public static JSONObject getAcceptedContactsRequest(String token) {
		return ServerConnection.GET(ServerConst.URL_CONTACTS_ACCEPTED,
				getHeader(token));
	}

	public static JSONObject getSentContactsRequest(String token) {
		return ServerConnection.GET(ServerConst.URL_CONTACTS_SENT,
				getHeader(token));
	}

	public static JSONObject getReceivedContactsRequest(String token) {
		return ServerConnection.GET(ServerConst.URL_CONTACTS_RECEIVED,
				getHeader(token));
	}

	public static JSONObject getTokenRequest(String login, String password) {
		return ServerConnection.JsonPOST(ServerConst.URL_AUTH,
				ServerRequests.buildTokenRequest(login, password),
				getHeader(null));

	}

	public static JSONObject setUserLogOutRequest(String token, long userId) {
		return ServerConnection.DELETE(ServerConst.URL_USER_LOGOUT + userId,
				getHeader(token));
	}

	public static JSONObject setUserLocationRequest(String token, double lat,
			double lng, float accuracy, long fixTime) {
		return ServerConnection.JsonPOST(ServerConst.URL_POSITION_CREATE,
				buildAddPositionRequest(lat, lng, accuracy, fixTime),
				getHeader(token));
	}

	public static JSONObject setContactStateRequest(String token,
			long contactId, ContactStateType state) {
		return ServerConnection.JsonPOST(
				ServerConst.URL_CONTACTS_UPDATE_STATE_PART1 + contactId
						+ ServerConst.URL_CONTACTS_UPDATE_STATE_PART2,
				buildSetContactStateRequest(state), getHeader(token));
	}

	public static JSONObject addContactRequest(String token, String email) {
		return ServerConnection.JsonPOST(ServerConst.URL_CONTACTS_CREATE,
				buildAddContactRequest(email), getHeader(token));
	}

	public static JSONObject updateUserRequest(String token, String name,
			String surname) {
		return ServerConnection.JsonPOST(ServerConst.URL_USER_UPDATE,
				buildUpdateUserRequest(name, surname), getHeader(token));
	}

	public static JSONObject addUserRequest(String email, String password,
			String confirmationPassword) {
		return ServerConnection.JsonPOST(ServerConst.URL_USER_CREATE,
				buildRegistationRequest(email, password, confirmationPassword),
				getHeader(null));
	}

	public static JSONObject addPersonalDataRequest(String token, String name,
			String surname, UserSex sex, String date) {
		return ServerConnection.JsonPOST(ServerConst.URL_USER_PERSONAL_URL,
				buildPersonalDataRequest(name, surname, sex, date),
				getHeader(token));

	}

	public static JSONObject sendMessageRequest(String token, long convId,
			String message) {
		return ServerConnection.JsonPOST(ServerConst.URL_MESSAGES_SEND,
				buildSendMessageRequest(convId, message), getHeader(token));
	}

	private static JSONObject buildSetContactStateRequest(ContactStateType state) {
		JSONObject data = new JSONObject();
		try {
			data.put(ServerConst.USER_STATE, "" + state.getIntegerValue());
		} catch (JSONException e) {
			Log.e("JSONParseError", e.getMessage());
		}
		return data;
	}

	private static JSONObject buildSendMessageRequest(long conversationId,
			String message) {
		JSONObject o = new JSONObject();
		JSONObject mes = new JSONObject();
		try {
			mes.put(ServerConst.MESSAGE_CONVERSATION_ID, conversationId);
			mes.put(ServerConst.MESSAGE_CONTENT, message);
			o.put(ServerConst.MESSAGE, mes);
		} catch (JSONException e) {
			Log.e("JSONParseError", e.getMessage());
		}
		return o;
	}

	// TODO check
	private static JSONObject buildUpdateUserRequest(String name, String surname) {
		JSONObject o = new JSONObject();
		JSONObject user = new JSONObject();
		try {
			user.put(ServerConst.USER_FIRST_NAME, name);
			user.put(ServerConst.USER_LAST_NAME, surname);
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

	private static JSONObject buildAddPositionRequest(double lat, double lng,
			float accuracy, long fixTime) {
		JSONObject o = new JSONObject();
		JSONObject pos = new JSONObject();
		try {
			pos.put(ServerConst.POSITION_LATITUDE, lat);
			pos.put(ServerConst.POSITION_LONGITUDE, lng);
			pos.put(ServerConst.POSITION_ACCURACY, accuracy);
			pos.put(ServerConst.POSITION_FIX_TIME, getFormatedTime(fixTime));
			o.put(ServerConst.POSITION, pos);
		} catch (JSONException e) {
			Log.e("JSONParseError", e.getMessage());
		}
		return o;
	}

	private static String getFormatedTime(long fixTime) {
		SimpleDateFormat date = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss ZZZZ", Locale.getDefault());
		return date.format(new Date(fixTime));
	}

	private static JSONObject buildRegistationRequest(String email,
			String password, String confirmationPassword) {
		JSONObject o = new JSONObject();
		JSONObject user = new JSONObject();
		try {
			user.put(ServerConst.USER_EMAIL, email);
			user.put(ServerConst.USER_PASSWORD, password);
			user.put(ServerConst.USER_PASSWORD_CONFIRMATION,
					confirmationPassword);
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

	private static Map<String, String> getImageUploadHeader(String token) {
		Map<String, String> header = new HashMap<String, String>();
		header.put("Accept", "application/json");
		// header.put("Content-type", "text/plain");
		// header.put("Content-type", "application/json");
		// header.put("Connection", "Keep-alive");
		header.put("Content-type", "application/json");//
		// header.put("Encoding", "UTF-8");

		// header.put("Accept-Encoding", "gzip, deflate");
		// header.put("Accept-Language", "pl,en-us;q=0.7,en;q=0.3");

		if (token != null) {
			header.put(ServerConst.TOKEN_GET, token);
		}
		return header;
	}

	private static JSONObject buildGetPositionsRequest(
			ArrayList<String> contactIds, int number) {
		JSONObject request = new JSONObject();
		JSONObject detailParams = new JSONObject();
		try {
			detailParams.put(ServerConst.POSITION_CONTACTS, new JSONArray(
					contactIds));
			detailParams.put(ServerConst.POSITION_NUMBER, "" + number);
			request.put(ServerConst.POSITION_KEY, detailParams);
		} catch (JSONException e) {
			Log.e("JSONParseError", e.getMessage());
		}
		return request;
	}

	private static JSONObject buildPersonalDataRequest(String name,
			String surname, UserSex sex, String date) {
		JSONObject request = new JSONObject();
		JSONObject personalData = new JSONObject();
		try {
			personalData.put(ServerConst.USER_FIRST_NAME, name);
			personalData.put(ServerConst.USER_LAST_NAME, surname);
			personalData.put(ServerConst.USER_SEX, sex.getIntegerValue());
			personalData.put(ServerConst.USER_BIRTH_DATE, date);
			request.put(ServerConst.USER_PERSONAL_DATA, personalData);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return request;
	}

	public static JSONObject updateUserEmail(String token, String email,
			String password, String passwordConfirmation) {
		return ServerConnection.JsonPOST(ServerConst.URL_USER_UPDATE_EMAIL,
				buildEmailUpdate(email, password, passwordConfirmation),
				getHeader(token));
	}

	private static JSONObject buildEmailUpdate(String email, String password,
			String confirmationPassword) {
		JSONObject request = new JSONObject();
		JSONObject user = new JSONObject();
		try {
			user.put(ServerConst.USER_EMAIL, email);
			if (password != null) {
				user.put(ServerConst.USER_PASSWORD, password);
				user.put(ServerConst.USER_PASSWORD_CONFIRMATION,
						confirmationPassword);
			}

			request.put(ServerConst.USER, user);
		} catch (JSONException e) {
			Log.e("JSONParseError", e.getMessage());
		}
		return request;
	}
}
