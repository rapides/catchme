package com.catchme.exampleObjects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.location.Location;
import android.support.v4.util.LongSparseArray;

import com.catchme.R;
import com.catchme.connections.ServerConst;

public class ExampleItem {
	public static final int IMAGE_INVALID = -1;
	public static final long AVATAR_SMALL = 0;
	public static final long AVATAR_MEDIUM = 1;
	public static final long AVATAR_BIG = 2;
	public static final long AVATAR_URL = 3;

	public enum UserSex {
		UNKNOWN, MAN, WOMAN
	}

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

	private LongSparseArray<List<Message>> messages;
	private List<UserLocation> position;
	private List<Long> conversationIds;
	protected LongSparseArray<String> avatars;
	private UserSex sex;
	private Date dateOfBirth;

	public ExampleItem(long id, String name, String surname, String email,
			ContactStateType state, List<Long> conv_ids,
			LongSparseArray<String> avatars, String sex, String dob) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.surname = surname;
		this.messages = new LongSparseArray<List<Message>>();
		this.state = state;
		this.conversationIds = conv_ids;
		this.avatars = avatars;
		this.sex = UserSex.UNKNOWN;// TODO importing sex
		this.dateOfBirth = new Date();// TODO importing date

	}

	public ExampleItem(ExampleItem item) {
		this.id = item.id;
		this.name = item.name;
		this.avatars = item.avatars;
		this.email = item.email;
		this.surname = item.surname;
		this.state = item.state;
		this.messages = item.messages;
		this.dateOfBirth = item.dateOfBirth;
		this.sex = item.sex;
	}

	@Override
	public String toString() {
		return "Id: " + id + ", " + name + " " + surname + ", " + email;
	}

	public List<Message> getMessages(long conversationId) {
		return messages.get(conversationId);
	}

	public long getNewestMessage(long conversationId) {
		if (messages.size() > 0 && messages.get(conversationId).size() > 0) {
			return messages.get(conversationId)
					.get(messages.get(conversationId).size() - 1)
					.getMessageId();
		} else {
			return -1;
		}
	}

	public long getOldestMessage(long conversationId) {
		if (messages != null && messages.size() > 0) {
			return messages.get(conversationId).get(0).getMessageId();
		} else {
			return Long.MAX_VALUE;
		}
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

	public Location getLastLocation() {
		if (position != null && position.size() > 0) {
			return position.get(0).getLocation();
		} else {
			return null;
		}
	}

	public List<UserLocation> getLocations() {
		return position;
	}

	public void setLocations(List<UserLocation> position) {
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

	public LongSparseArray<List<Message>> getAllMessages() {
		return messages;
	}

	public void setMessages(LongSparseArray<List<Message>> allMessages) {
		this.messages = allMessages;
	}

}