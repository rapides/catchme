package com.catchme.connections;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.catchme.exampleObjects.ExampleContent.ExampleItem;
import com.catchme.exampleObjects.ExampleContent.ExampleItem.ContactStateType;
import com.catchme.exampleObjects.ExampleContent.LoggedUser;
import com.catchme.exampleObjects.Message;

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
		ContactStateType state = adjustState(o.getInt(ServerConst.USER_STATE));
		JSONObject user = o.getJSONObject(ServerConst.USER);
		long id = o.getLong(ServerConst.USER_ID);
		String name = user.getString(ServerConst.USER_NAME);
		String surname = user.getString(ServerConst.USER_SURNAME);
		String email = user.getString(ServerConst.USER_EMAIL);
		JSONArray jsonArray = o.getJSONArray(ServerConst.USER_CONVERSATIONS);
		ArrayList<Long> conv_ids = new ArrayList<Long>();
		for (int i = 0; i < jsonArray.length(); i++) {
			conv_ids.add(jsonArray.getLong(i));
		}
		ExampleItem contact = new ExampleItem(id, name, surname, email,
				ExampleItem.IMAGE_INVALID, state, conv_ids);
		return contact;
	}
	

	private static ContactStateType adjustState(int oldState) {
		ContactStateType state = null;
		switch (oldState) {
		case 0:
			state = ContactStateType.RECEIVED;
			break;
		case 1:
			state = ContactStateType.ACCEPTED;
			break;
		case 2:
			state = ContactStateType.SENT;
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

	public static ArrayList<Message> getMessagesList(JSONObject fullResponse) {
		ArrayList<Message> messageList = null;

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

	private static ArrayList<Message> getMessageList(JSONArray messageArray) throws JSONException {
		ArrayList<Message> messageList = new ArrayList<Message>();
		for(int i=0;i<messageArray.length();i++){
			messageList.add(getMessage(messageArray.getJSONObject(i)));
		}
		return messageList;
	}

	private static Message getMessage(JSONObject messageFull) throws JSONException {
		JSONObject message = messageFull.getJSONObject(ServerConst.MESSAGE);
		JSONObject user = messageFull.getJSONObject(ServerConst.USER);
		ArrayList<Object> read_feeds = null;
		Date createdAt = getDateFromString(message.getString(ServerConst.MESSAGE_CREATED_AT));
		long messageId = message.getLong(ServerConst.MESSAGE_ID);
		String content = message.getString(ServerConst.MESSAGE_CONTENT);
		long userId = user.getLong("id"); 
		Message m = new Message(messageId, content, createdAt, userId, read_feeds);
		return m;
	}

	private static Date getDateFromString(String string) {
		Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH).parse(string);
		} catch (ParseException e) {
			Log.e("ParseError", e.getMessage());
		}
		return date;
	}
}
