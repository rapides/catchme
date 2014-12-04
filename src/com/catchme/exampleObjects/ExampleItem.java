package com.catchme.exampleObjects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.location.Location;

import com.catchme.R;
import com.catchme.connections.ServerConst;

public class ExampleItem {
	public static final int IMAGE_INVALID = -1;
	public static final String AVATAR_SMALL = "avatar_small";
	public static final String AVATAR_BIG = "avatar_big";
	public static final String AVATAR_MEDIUM = "avatar_medium";
	public static final String AVATAR_URL = "avatar_url";

	public enum ContactStateType {
		INVITED(0), ACCEPTED(1), REJECTED(2), SENT(3), RECEIVED(4);
		int intValue;

		ContactStateType(int val) {
			intValue = val;
		}

		public int getIntegerValue() {
			return intValue;
		}

		public static ContactStateType getStateType(int integerValue) {
			for (int i = 0; i < values().length; i++) {
				if (values()[i].getIntegerValue() == integerValue) {
					return values()[i];
				}
			}
			return null;
		}

		public int getMenuPosition() {
			if (this == ACCEPTED) {
				return 1;
			} else if (this == SENT) {
				return 2;
			} else if (this == RECEIVED) {
				return 3;
			} else {
				return 0;
			}
		}
	}

	private long id;// idcontactu
	private ContactStateType state;

	private String name;
	private String surname;
	private String email;

	private Map<Long, List<Message>> messages;
	private List<UserLocation> position;
	private List<Long> conversationIds;
	public HashMap<String, String> avatars;

	public ExampleItem(long id, String name, String surname, String email,
			ContactStateType state, List<Long> conv_ids,
			HashMap<String, String> avatars) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.surname = surname;
		this.messages = new HashMap<Long, List<Message>>();
		this.state = state;
		this.conversationIds = conv_ids;
		this.avatars = avatars;
		// addRandomMessages();
	}

	public ExampleItem(ExampleItem item) {
		this.id = item.id;
		this.name = item.name;
		this.avatars = item.avatars;
		this.email = item.email;
		this.surname = item.surname;
		this.state = item.state;
		this.messages = item.messages;
	}

	@Override
	public String toString() {
		return "Id: " + id + ", " + name + " " + surname + ", " + email
				+ ", ConvId: " + conversationIds.get(0);
	}

	public List<Message> getMessages(long conversationId) {
		return messages.get(conversationId);
	}

	public long getNewestMessage(long conversationId) {
		return messages.get(conversationId)
				.get(messages.get(conversationIds).size() - 1).getMessageId();
	}

	public long getOldestMessage(long conversationId) {
		return messages.get(conversationId).get(0 - 1).getMessageId();
	}

	public String getName() {
		return name;
	}

	public String getFullName() {
		return name + " " + surname;
	}

	public String getSurname() {
		return surname;
	}

	public long getId() {
		return id;
	}

	public ContactStateType getState() {
		return state;
	}

	public String getEmail() {
		return email;
	}
	public Location getLastLocation(){
		return position.get(0).getLocation();
	}
	public List<UserLocation> getLocations(){
		return position;
	}
	public void setLocations(List<UserLocation> position){
		this.position = position;
	}

	public void addOlderMessage(long conversationId, Message message) {
		List<Message> temp = messages.get(conversationId);
		Collections.reverse(temp);
		temp.add(message);
		Collections.reverse(temp);
		messages.put(conversationId, temp);
	}

	public void addOlderMessages(long conversationId, List<Message> messageList) {
		List<Message> temp = messages.get(conversationId);
		if (temp != null) {
			Collections.reverse(temp);
		} else {
			temp = new ArrayList<Message>();
		}
		Collections.reverse(messageList);
		temp.addAll(messageList);
		Collections.reverse(temp);
		messages.put(conversationId, temp);
	}

	public void addNewerMessages(long conversationId,
			ArrayList<Message> newerMessages) {
		List<Message> temp = messages.get(conversationId);
		if (temp == null) {
			temp = new ArrayList<Message>();
		}
		temp.addAll(newerMessages);
		messages.put(conversationId, temp);
	}

	/**
	 * Returns first conversation id. Used in conversations between 2 people.
	 * 
	 * @return Conversation id;
	 */
	public Long getFirstConversationId() {
		return conversationIds.get(0);
	}

	public String getSmallImageUrl() {
		if (avatars.get(AVATAR_SMALL).length() > 4) {// no idea why if null it
														// return string "null"
			return ServerConst.SERVER_IP + avatars.get(AVATAR_SMALL);
		} else {
			return getDefaultImage();
		}
	}

	public String getMediumImage() {
		if (avatars != null && avatars.get(AVATAR_MEDIUM) != null
				&& avatars.get(AVATAR_MEDIUM).length() > 4) {
			return ServerConst.SERVER_IP + avatars.get(AVATAR_MEDIUM);
		} else {
			return getDefaultImage();
		}
	}

	public String getLargeImage() {
		if (avatars.get(AVATAR_BIG).length() > 4) {
			return ServerConst.SERVER_IP + avatars.get(AVATAR_BIG);
		} else {
			return getDefaultImage();
		}
	}

	private String getDefaultImage() {
		return "drawable://" + R.drawable.loader;
	}

	
}