package com.catchme.exampleObjects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;

import com.catchme.R;

public class ExampleContent {
	public static ArrayList<ExampleItem> ITEMS = new ArrayList<ExampleItem>();
	@SuppressLint("UseSparseArrays")
	public static Map<Long, ExampleItem> ITEM_MAP = new HashMap<Long, ExampleItem>();

	/*
	 * static { // Add 3 sample items.
	 * 
	 * addItem(new ExampleItem(6, "Weronika Grodecka 2",
	 * "http://i.imgur.com/fK76n1p.jpg", "Poznan")); addItem(new ExampleItem(1,
	 * "Evilish EviLeenda", "http://i.imgur.com/KfpkaMQ.jpg", "Lublin"));
	 * addItem(new ExampleItem(2, "Jarek Maksymiuk",
	 * "http://i.imgur.com/eOxT2wO.jpg", "Chlebczyn")); addItem(new
	 * ExampleItem(3, "Joachim Pflaume", "http://i.imgur.com/X1u2HP5.jpg",
	 * "Frankfurt"));
	 * 
	 * addItem(new ExampleItem(1, "Weronika", "Grodecka 2",
	 * "rapides+1@gmail.com", R.drawable.o5, ExampleItem.STATE_TYPE[2]));
	 * addItem(new ExampleItem(2, "Evilish", "EviLeenda", "rapides+2@gmail.com",
	 * R.drawable.o1, ExampleItem.STATE_TYPE[0])); addItem(new ExampleItem(3,
	 * "Jarek", "Maksymiuk", "rapides+3@gmail.com", R.drawable.o2,
	 * ExampleItem.STATE_TYPE[0])); addItem(new ExampleItem(4, "Joachim",
	 * "Pflaume", "rapides+5@gmail.com", R.drawable.o3,
	 * ExampleItem.STATE_TYPE[0])); addItem(new ExampleItem(5, "Mayak", "Balop",
	 * "rapides+6@gmail.com", R.drawable.o4, ExampleItem.STATE_TYPE[0]));
	 * addItem(new ExampleItem(6, "Mayak", "STAN1", "rapides+7@gmail.com",
	 * R.drawable.o4, ExampleItem.STATE_TYPE[1])); addItem(new ExampleItem(7,
	 * "Janusz", "STAN1", "rapides+7@gmail.com", -1,
	 * ExampleItem.STATE_TYPE[1])); addItem(new ExampleItem(8, "Weronika",
	 * "STAN2", "rapides+8@gmail.com", R.drawable.o5,
	 * ExampleItem.STATE_TYPE[2])); }
	 */

	private static void addItem(ExampleItem item) {
		ITEM_MAP.put(item.getId(), item);
		ITEMS.add(item);
	}

	public static void updateItems(ArrayList<ExampleItem> itemList) {
		if (itemList != null && itemList.size() > 0) {
			ITEMS = itemList;
			ITEM_MAP.clear();
			for (ExampleItem item : itemList) {
				ITEM_MAP.put(item.getId(), item);
			}
		}

	}

	/**
	 * A dummy item representing a piece of content.
	 */
	public static class ExampleItem {
		public static final int IMAGE_INVALID = -1;

		public static int[] STATE_TYPE = { 0, 1, 2 };// "0-Accepted", "1-Sent",
														// "2-Received" };

		private long id;// idcontactu
		private int state;

		private String name;
		private String surname;
		private String email;

		private int photoId;
		private String photoUrl;

		private Map<Long, List<Message>> messages;
		private List<Long> conversationIds;

		public ExampleItem(long id, String name, String surname, String email,
				int photoId, int state, List<Long> conv_ids) {
			this.id = id;
			this.name = name;
			this.photoId = photoId;
			this.email = email;
			this.surname = surname;
			this.photoUrl = null;
			this.messages = new HashMap<Long, List<Message>>();
			this.state = state;
			this.conversationIds = conv_ids;
			addRandomMessages();
		}

		public ExampleItem(long id, String name, String surname, String email,
				String photoUrl, int state, List<Long> conv_ids) {
			this.id = id;
			this.name = name;
			this.photoUrl = photoUrl;
			this.email = email;
			this.surname = surname;
			this.photoId = -1;
			this.state = state;
			this.messages = new HashMap<Long, List<Message>>();
			this.conversationIds = conv_ids;
			addRandomMessages();
		}

		public ExampleItem(ExampleItem item) {
			this.id = item.id;
			this.name = item.name;
			this.photoUrl = item.photoUrl;
			this.email = item.email;
			this.surname = item.surname;
			this.photoId = item.photoId;
			this.state = item.state;
			this.messages = item.messages;
		}

		private void addRandomMessages() {
			LinkedList<Message> messageList = new LinkedList<Message>();
			for (int i = 0; i < 20; i++) {
				messageList.add(new Message());
			}
			messageList.add(new Message("Poszukiwanie gor�cej linii z Niebem "
					+ "w tym �wiecie pozor�w stanowi wypraw� "
					+ "z g�ry skazan� na niepowodzenie. "
					+ "Poszukiwanie nie zepsutej instytucji, "
					+ "osoby uciele�niaj�cej dobro i prawd�, "
					+ "ksi��ki, nauk, miejsca, sposobu �ycia, "
					+ "kt�ry odpowie na wszelkie pytania "
					+ "jest �lep� uliczk�, prowadz�c� na "
					+ "manowce istnienia. "));
			messageList.add(new Message("ok"));
			messageList.add(new Message("cycki"));
			if (Math.random() < 0.5) {
				messageList
						.add(new Message(
								"Ostatnia dluga wiadomosc, na tyle dluga zeby sie nie miescila "
										+ ""
										+ "w widoku na glownej stornie. Jednak musi byc"
										+ " dlu�sza bo to co napiaslem wczesniej nie "
										+ "wystarczylo i sie nie skracalo, a ja chce "
										+ "sprawdzi� czy elipsize dziala."));
			} else {
				messageList.add(new Message("Ostatnia kr�tka wiadomosc"));
			}
			if (conversationIds != null) {
				messages.put(conversationIds.get(0), messageList);
			}

		}

		@Override
		public String toString() {
			return name + " " + surname + " " + email + " "
					+ conversationIds.get(0);
		}

		public List<Message> getMessages(long conversationId) {
			return messages.get(conversationId);
		}

		public long getNewestMessage(long conversationId) {
			return messages.get(conversationId)
					.get(messages.get(conversationIds).size() - 1)
					.getMessageId();
		}

		public long getOldestMessage(long conversationId) {
			return messages.get(conversationId).get(0 - 1).getMessageId();
		}

		public int getImageResource() {
			if (photoId == IMAGE_INVALID) {
				return R.drawable.loader;
			} else {
				return photoId;
			}
		}

		public String getImageUrl() {
			return photoUrl;
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

		public int getState() {
			return state;
		}

		public String getEmail() {
			return email;
		}

		public void addOlderMessage(long conversationId, Message message) {
			List<Message> temp = messages.get(conversationId);
			Collections.reverse(temp);
			temp.add(message);
			Collections.reverse(temp);
			messages.put(conversationId, temp);
		}

		public void addOlderMessages(long conversationId,
				List<Message> messageList) {
			List<Message> temp = messages.get(conversationId);
			Collections.reverse(temp);
			temp.addAll(messageList);
			Collections.reverse(temp);
			messages.put(conversationId, temp);
		}

		/**
		 * Returns first conversation id. Used in conversations between 2
		 * people.
		 * 
		 * @return Conversation id;
		 */
		public Long getFirstConversationId() {
			return conversationIds.get(0);
		}
	}

	public static class LoggedUser extends ExampleItem {
		private String token;

		public LoggedUser(long id, String name, String surname, String email,
				int photoId, String token) {
			super(id, name, surname, email, photoId, ExampleItem.STATE_TYPE[0],
					null);
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

	}

	public static void clear() {
		ITEM_MAP.clear();
		ITEMS.clear();
	}

}
