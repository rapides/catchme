package com.catchme.exampleObjects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.catchme.R;

public class ExampleContent {
	public static ArrayList<ExampleItem> ITEMS = new ArrayList<ExampleItem>();
	public static ArrayList<ExampleItem> ACTIVE = new ArrayList<ExampleItem>();
	public static Map<Long, ExampleItem> ITEM_MAP = new HashMap<Long, ExampleItem>();
	public static ArrayList<ExampleItem> RECEIVED = new ArrayList<ExampleItem>();
	public static ArrayList<ExampleItem> SENT = new ArrayList<ExampleItem>();

	static {
		// Add 3 sample items.
		/*
		 * addItem(new ExampleItem(6, "Weronika Grodecka 2",
		 * "http://i.imgur.com/fK76n1p.jpg", "Poznan")); addItem(new
		 * ExampleItem(1, "Evilish EviLeenda", "http://i.imgur.com/KfpkaMQ.jpg",
		 * "Lublin")); addItem(new ExampleItem(2, "Jarek Maksymiuk",
		 * "http://i.imgur.com/eOxT2wO.jpg", "Chlebczyn")); addItem(new
		 * ExampleItem(3, "Joachim Pflaume", "http://i.imgur.com/X1u2HP5.jpg",
		 * "Frankfurt"));
		 */
		addItem(new ExampleItem(0, "Weronika Grodecka 2", R.drawable.o5,
				"Poznan", ExampleItem.STATE_TYPE[0]));
		addItem(new ExampleItem(1, "Evilish EviLeenda", R.drawable.o1, "Lublin", ExampleItem.STATE_TYPE[0]));
		addItem(new ExampleItem(2, "Jarek Maksymiuk", R.drawable.o2,
				"Chlebczyn", ExampleItem.STATE_TYPE[0]));
		addItem(new ExampleItem(3, "Joachim Pflaume", R.drawable.o3,
				"Frankfurt", ExampleItem.STATE_TYPE[0]));
		addItem(new ExampleItem(4, "Mayak Balop", R.drawable.o4, "Berlin", ExampleItem.STATE_TYPE[0]));
		addItem(new ExampleItem(5, "Mayak Balop 2", R.drawable.o4, "Berlin", ExampleItem.STATE_TYPE[1]));
		addItem(new ExampleItem(6, "Weronika Grodecka", R.drawable.o5, "Poznan", ExampleItem.STATE_TYPE[2]));
	}

	private static void addItem(ExampleItem item) {
		ITEM_MAP.put(item.id, item);
		ITEMS.add(item);
		if(item.getState()==ExampleItem.STATE_TYPE[0]){
			ACTIVE.add(item);
		}else if(item.getState()==ExampleItem.STATE_TYPE[1]){
			SENT.add(item);
		}else if(item.getState()==ExampleItem.STATE_TYPE[2]){
			RECEIVED.add(item);
		}
	}

	/**
	 * A dummy item representing a piece of content.
	 */
	public static class ExampleItem {
		private long id;
		private String name;
		private int photoId;
		private String cityName;
		private LinkedList<Message> messages;
		private String photoUrl;
		private String state;
		public static String[] STATE_TYPE = {"Accepted", "Sent", "Received"};
		private String email;//TODO finish email\

		public ExampleItem(long id, String name, int photoId, String cityName, String state) {
			this.id = id;
			this.name = name;
			this.photoId = photoId;
			this.photoUrl = null;
			this.cityName = cityName;
			this.messages = new LinkedList<Message>();
			this.state = state;
			addRandomMessages();
		}

		public ExampleItem(long id, String name, String photoUrl,
				String cityName, String state) {
			this.id = id;
			this.name = name;
			this.photoUrl = photoUrl;
			this.photoId = -1;
			this.cityName = cityName;
			this.state = state;
			this.messages = new LinkedList<Message>();
			addRandomMessages();
		}

		private void addRandomMessages() {
			for (int i = 0; i < 50; i++) {
				messages.add(new Message());
			}
			messages.add(new Message("Poszukiwanie gor¹cej linii z Niebem "
					+ "w tym œwiecie pozorów stanowi wyprawê "
					+ "z góry skazan¹ na niepowodzenie. "
					+ "Poszukiwanie nie zepsutej instytucji, "
					+ "osoby ucieleœniaj¹cej dobro i prawdê, "
					+ "ksi¹¿ki, nauk, miejsca, sposobu ¿ycia, "
					+ "który odpowie na wszelkie pytania "
					+ "jest œlep¹ uliczk¹, prowadz¹c¹ na "
					+ "manowce istnienia. "));
			messages.add(new Message("ok"));
			messages.add(new Message("cycki"));
			if (Math.random() < 0.5) {
				messages.add(new Message(
						"Ostatnia dluga wiadomosc, na tyle dluga zeby sie nie miescila "
								+ ""
								+ "w widoku na glownej stornie. Jednak musi byc"
								+ " dlu¿sza bo to co napiaslem wczesniej nie "
								+ "wystarczylo i sie nie skracalo, a ja chce "
								+ "sprawdziæ czy elipsize dziala."));
			} else {
				messages.add(new Message("Ostatnia krótka wiadomosc"));
			}

		}

		@Override
		public String toString() {
			return name;
		}

		public LinkedList<Message> getMessages() {
			return messages;
		}

		public int getImageResource() {
			return photoId;
		}

		public String getImageUrl() {
			return photoUrl;
		}

		public String getName() {
			return name;
		}

		public long getId() {
			return id;
		}

		public String getCity() {
			return cityName;
		}

		public String getState() {
			return state;
		}

		public void addFirstMessage(Message message) {
			Collections.reverse(messages);
			messages.add(message);
			Collections.reverse(messages);
		}
	}
}
