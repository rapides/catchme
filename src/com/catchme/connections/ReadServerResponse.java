package com.catchme.connections;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;
import android.support.v4.util.LongSparseArray;
import android.util.Log;

import com.catchme.database.model.ExampleItem;
import com.catchme.database.model.LoggedUser;
import com.catchme.database.model.Message;
import com.catchme.database.model.ExampleItem.ContactStateType;

public class ReadServerResponse {
	public static LongSparseArray<String> getErrors(JSONObject fullResponse) {
		LongSparseArray<String> errors = new LongSparseArray<String>();
		;
		try {
			if (!isSuccess(fullResponse)) {
				System.out.println(fullResponse);
				JSONArray a = fullResponse
						.getJSONArray(ServerConst.ERROR_MESSAGES);
				for (int i = 0; i < a.length(); i++) {
					JSONObject error = a.getJSONObject(i);
					errors.put(error.getInt(ServerConst.ERROR_ID),
							error.getString(ServerConst.ERROR_CONTENT));
				}
			}
		} catch (JSONException e) {
			Log.e("JSONException", e.getMessage());
		}
		return errors;

	}

	public static LoggedUser getLoggedUser(JSONObject fullResponse) {
		LoggedUser logged = null;
		try {
			if (isSuccess(fullResponse)) {
				JSONObject user = fullResponse.getJSONObject(ServerConst.USER);
				JSONObject personalData = user
						.getJSONObject(ServerConst.USER_PERSONAL_DATA);
				long id = user.getLong(ServerConst.USER_ID);
				String name = personalData
						.getString(ServerConst.USER_FIRST_NAME);
				String surname = personalData
						.getString(ServerConst.USER_LAST_NAME);
				String dob = personalData
						.optString(ServerConst.USER_BIRTH_DATE);
				String sex = personalData.optString(ServerConst.USER_SEX);
				String email = user.getString(ServerConst.USER_EMAIL);
				LongSparseArray<String> avatars = getAvatarsFromArray(user
						.getJSONObject(ServerConst.USER_AVATAR));

				logged = new LoggedUser(id, name, surname, email,
						getToken(fullResponse), avatars, sex, dob);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return logged;
	}

	public static ArrayList<ExampleItem> getContactList(
			JSONObject fullResponse, ContactStateType state) {
		ArrayList<ExampleItem> contactList = null;
		try {
			if (isSuccess(fullResponse)) {
				contactList = getContactList(getContactsArray(fullResponse),
						state);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return contactList;
	}

	public static boolean isSuccess(JSONObject fullResponse) {
		boolean isSuccess = false;
		try {
			if (fullResponse != null) {
				isSuccess = fullResponse.getBoolean(ServerConst.SUCCESS);
			} else {
				isSuccess = false;
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return isSuccess;
	}

	private static ArrayList<ExampleItem> getContactList(
			JSONArray contactsArray, ContactStateType state)
			throws JSONException {
		ArrayList<ExampleItem> contactList = new ArrayList<ExampleItem>();
		for (int i = 0; i < contactsArray.length(); i++) {
			contactList.add(getContact(contactsArray.getJSONObject(i), state));
		}
		return contactList;
	}

	private static JSONArray getContactsArray(JSONObject response)
			throws JSONException {

		return response.getJSONArray(ServerConst.CONTACTS);

	}

	private static ExampleItem getContact(JSONObject o,
			ContactStateType stateGlobal) throws JSONException {
		ContactStateType state = adjustState(o.getInt(ServerConst.USER_STATE),
				stateGlobal);
		JSONObject user = o.getJSONObject(ServerConst.USER);
		JSONObject personalData = user
				.getJSONObject(ServerConst.USER_PERSONAL_DATA);
		long id = o.getLong(ServerConst.USER_ID);
		String name = personalData.getString(ServerConst.USER_FIRST_NAME);
		String surname = personalData.getString(ServerConst.USER_LAST_NAME);
		String email = user.getString(ServerConst.USER_EMAIL);
		JSONArray jsonArray = o.getJSONArray(ServerConst.USER_CONVERSATIONS);
		ArrayList<Long> conv_ids = new ArrayList<Long>();
		for (int i = 0; i < jsonArray.length(); i++) {
			conv_ids.add(jsonArray.getLong(i));
		}
		LongSparseArray<String> avatars = getAvatarsFromArray(user
				.getJSONObject(ServerConst.USER_AVATAR));
		String dob = personalData.optString(ServerConst.USER_BIRTH_DATE);
		String sex = personalData.getString(ServerConst.USER_SEX);
		ExampleItem contact = new ExampleItem(id, name, surname, email, state,
				conv_ids, avatars, sex, dob);
		return contact;
	}

	public static LongSparseArray<String> updateAvatars(JSONObject avatars) {

		LongSparseArray<String> result = new LongSparseArray<String>();
		try {
			JSONObject mainAvatarArray = avatars.getJSONObject(
					ServerConst.USER_AVATAR).getJSONObject(
					ServerConst.USER_AVATAR);
			JSONObject small = mainAvatarArray
					.getJSONObject(ServerConst.USER_AVATAR_SMALL);
			JSONObject medium = mainAvatarArray
					.getJSONObject(ServerConst.USER_AVATAR_MEDIUM);
			JSONObject big = mainAvatarArray
					.getJSONObject(ServerConst.USER_AVATAR_BIG);
			String url = mainAvatarArray.optString(ServerConst.USER_AVATAR_URL);
			String smallUrl = small.optString(ServerConst.USER_AVATAR_URL);
			String mediumUrl = medium.optString(ServerConst.USER_AVATAR_URL);
			String bigUrl = big.optString(ServerConst.USER_AVATAR_URL);
			result.put(ExampleItem.AVATAR_SMALL, smallUrl);
			result.put(ExampleItem.AVATAR_MEDIUM, mediumUrl);
			result.put(ExampleItem.AVATAR_BIG, bigUrl);
			result.put(ExampleItem.AVATAR_URL, url);
		} catch (JSONException e) {
			Log.e("JSONParseException", e.getMessage());
		}

		return result;
	}

	private static LongSparseArray<String> getAvatarsFromArray(
			JSONObject avatars) throws JSONException {

		LongSparseArray<String> result = new LongSparseArray<String>();
		JSONObject small = avatars.getJSONObject(ServerConst.USER_AVATAR_SMALL);
		JSONObject medium = avatars
				.getJSONObject(ServerConst.USER_AVATAR_MEDIUM);
		JSONObject big = avatars.getJSONObject(ServerConst.USER_AVATAR_BIG);
		String url = avatars.optString(ServerConst.USER_AVATAR_URL);
		String smallUrl = small.optString(ServerConst.USER_AVATAR_URL);
		String mediumUrl = medium.optString(ServerConst.USER_AVATAR_URL);
		String bigUrl = big.optString(ServerConst.USER_AVATAR_URL);
		result.put(ExampleItem.AVATAR_SMALL, ServerConst.SERVER_IP + smallUrl);
		result.put(ExampleItem.AVATAR_MEDIUM, ServerConst.SERVER_IP + mediumUrl);
		result.put(ExampleItem.AVATAR_BIG, ServerConst.SERVER_IP + bigUrl);
		result.put(ExampleItem.AVATAR_URL, ServerConst.SERVER_IP + url);

		return result;
	}

	private static ContactStateType adjustState(int oldState,
			ContactStateType stateGlobal) {
		ContactStateType state = null;
		switch (oldState) {
		case 0:
			state = stateGlobal;
			break;
		case 1:
			state = ContactStateType.ACCEPTED;
			break;
		case 2:
			state = ContactStateType.REJECTED;
			break;
		}
		return state;
	}

	private static String getToken(JSONObject fullResponse)
			throws JSONException {
		String response = null;
		if (isSuccess(fullResponse)) {
			response = fullResponse.getString(ServerConst.TOKEN_RESPONSE);
		}
		return response;
	}

	public static LinkedList<Message> getMessagesList(JSONObject fullResponse) {
		LinkedList<Message> messageList = null;

		try {
			if (isSuccess(fullResponse)) {
				messageList = getMessageList(getMessagesArray(fullResponse));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return messageList;

	}

	private static JSONArray getMessagesArray(JSONObject fullResponse)
			throws JSONException {
		return fullResponse.getJSONArray(ServerConst.MESSAGES);
	}

	private static LinkedList<Message> getMessageList(JSONArray messageArray)
			throws JSONException {
		LinkedList<Message> messageList = new LinkedList<Message>();
		for (int i = 0; i < messageArray.length(); i++) {
			messageList.add(getMessage(messageArray.getJSONObject(i)));
		}
		return messageList;
	}

	private static Message getMessage(JSONObject messageFull)
			throws JSONException {
		JSONObject message = messageFull.getJSONObject(ServerConst.MESSAGE);
		JSONObject user = messageFull.getJSONObject(ServerConst.USER);
		ArrayList<Object> read_feeds = null;
		Date createdAt = getDateFromString(message
				.getString(ServerConst.MESSAGE_CREATED_AT));
		long messageId = message.getLong(ServerConst.MESSAGE_ID);
		String content = message.getString(ServerConst.MESSAGE_CONTENT);
		long userId = user.getLong("id");
		Message m = new Message(messageId, content, createdAt, userId,
				read_feeds);
		return m;
	}

	private static Date getDateFromString(String string) {
		Date date = null;
		try {
			date = new SimpleDateFormat(ServerConst.DATE_FORMAT,
					Locale.getDefault()).parse(string);

		} catch (ParseException e) {
			Log.e("ParseError", e.getMessage());
		}
		return date;
	}

	public static LongSparseArray<ArrayList<Location>> getLocations(
			JSONObject fullResponse) {
		LongSparseArray<ArrayList<Location>> locationList = new LongSparseArray<ArrayList<Location>>();
		try {
			if (isSuccess(fullResponse)) {
				JSONArray positionsArray = fullResponse
						.getJSONArray(ServerConst.POSITION_RESPONSE_ARRAY_NAME);
				for (int i = 0; i < positionsArray.length(); i++) {
					JSONArray coordinates = positionsArray.getJSONObject(i)
							.getJSONArray(
									ServerConst.POSITION_RESPONSE_COORDINATES);
					long contacId = positionsArray.getJSONObject(i).optLong(
							ServerConst.USER_CONTACT_ID);
					ArrayList<Location> userLocations = new ArrayList<Location>();
					for (int j = 0; j < coordinates.length(); j++) {
						Location location = getLocationFromJSONObject(coordinates
								.getJSONObject(j));
						userLocations.add(location);
						// Collections.sort(userLocations);
					}
					locationList.put(contacId, userLocations);
				}
			}
		} catch (JSONException e) {
			Log.e("JSONParseException", e.getMessage());
			e.printStackTrace();
		}
		return locationList;
	}

	private static Location getLocationFromJSONObject(JSONObject o)
			throws JSONException {
		Location result = new Location(ServerConst.SERVER_NAME);
		result.setAccuracy((float) o.getDouble(ServerConst.POSITION_ACCURACY));
		result.setLatitude(o.getDouble(ServerConst.POSITION_LATITUDE));
		result.setLongitude(o.getDouble(ServerConst.POSITION_LONGITUDE));
		result.setTime(getTimeFromString(o
				.getString(ServerConst.POSITION_FIX_TIME)));
		return result;

	}

	private static long getTimeFromString(String date) {
		try {
			return new SimpleDateFormat(ServerConst.DATE_FORMAT,
					Locale.getDefault()).parse(date).getTime();
		} catch (ParseException e) {
			try {// TODO remove, only for testing on computer, with newer java
				return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
						Locale.getDefault()).parse(date).getTime();
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}
		return -1;
	}
}
