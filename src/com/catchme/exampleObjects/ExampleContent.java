package com.catchme.exampleObjects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;

import com.catchme.R;

public class ExampleContent {
	public static ArrayList<ExampleItem> ITEMS = new ArrayList<ExampleItem>();
	@SuppressLint("UseSparseArrays")
	public static Map<Long, ExampleItem> ITEM_MAP = new HashMap<Long, ExampleItem>();



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

		public enum ContactStateType {
			ACCEPTED(0), SENT(1), RECEIVED(2);
			int intValue;

			ContactStateType(int val) {
				intValue = val;
			}

			public int getIntegerValue() {
				return intValue;
			}

			public static ContactStateType getStateType(int integerValue) {
				for (int i = 0; i < values().length; i++) {
					if(values()[i].getIntegerValue() == integerValue){
						return values()[i];
					}
				}
				return null;
			}
		}

		private long id;// idcontactu
		private ContactStateType state;

		private String name;
		private String surname;
		private String email;

		private int photoId;
		private String photoUrl;

		private Map<Long, List<Message>> messages;
		private List<Long> conversationIds;

		public ExampleItem(long id, String name, String surname, String email,
				int photoId, ContactStateType state, List<Long> conv_ids) {
			this.id = id;
			this.name = name;
			this.photoId = photoId;
			this.email = email;
			this.surname = surname;
			this.photoUrl = null;
			this.messages = new HashMap<Long, List<Message>>();
			this.state = state;
			this.conversationIds = conv_ids;
			//addRandomMessages();
		}

		public ExampleItem(long id, String name, String surname, String email,
				String photoUrl, ContactStateType state, List<Long> conv_ids) {
			this.id = id;
			this.name = name;
			this.photoUrl = photoUrl;
			this.email = email;
			this.surname = surname;
			this.photoId = -1;
			this.state = state;
			this.messages = new HashMap<Long, List<Message>>();
			this.conversationIds = conv_ids;
			//addRandomMessages();
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

		/*private void addRandomMessages() {
			LinkedList<Message> messageList = new LinkedList<Message>();
			for (int i = 0; i < 20; i++) {
				//messageList.add(new Message());
			}
			messageList.add(new Message("Poszukiwanie gor¹cej linii z Niebem "
					+ "w tym œwiecie pozorów stanowi wyprawê "
					+ "z góry skazan¹ na niepowodzenie. "
					+ "Poszukiwanie nie zepsutej instytucji, "
					+ "osoby ucieleœniaj¹cej dobro i prawdê, "
					+ "ksi¹¿ki, nauk, miejsca, sposobu ¿ycia, "
					+ "który odpowie na wszelkie pytania "
					+ "jest œlep¹ uliczk¹, prowadz¹c¹ na "
					+ "manowce istnienia. "));
			messageList.add(new Message("ok"));
			messageList.add(new Message("cycki"));
			if (Math.random() < 0.5) {
				messageList
						.add(new Message(
								"Ostatnia dluga wiadomosc, na tyle dluga zeby sie nie miescila "
										+ ""
										+ "w widoku na glownej stornie. Jednak musi byc"
										+ " dlu¿sza bo to co napiaslem wczesniej nie "
										+ "wystarczylo i sie nie skracalo, a ja chce "
										+ "sprawdziæ czy elipsize dziala."));
			} else {
				messageList.add(new Message("Ostatnia krótka wiadomosc"));
			}
			if (conversationIds != null) {
				messages.put(conversationIds.get(0), messageList);
			}

		}*/

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

		public ContactStateType getState() {
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
			if(temp!=null){
				Collections.reverse(temp);
			}else{
				temp = new ArrayList<Message>();
			}
			Collections.reverse(messageList);
			temp.addAll(messageList);
			Collections.reverse(temp);
			messages.put(conversationId, temp);
		}
		public void addNewerMessages(long conversationId, ArrayList<Message> newerMessages) {
			List<Message> temp = messages.get(conversationId);
			if(temp==null){	
				temp = new ArrayList<Message>();
			}
			temp.addAll(newerMessages);
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
			super(id, name, surname, email, photoId, ContactStateType.ACCEPTED,
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
