package com.catchme.exampleObjects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import android.annotation.SuppressLint;

import com.catchme.R;

public class ExampleContent {
	public static ArrayList<ExampleItem> ITEMS = new ArrayList<ExampleItem>();
	@SuppressLint("UseSparseArrays")
	public static Map<Long, ExampleItem> ITEM_MAP = new HashMap<Long, ExampleItem>();
	public static LoggedUser currentUser;
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
		addItem(new ExampleItem("Weronika", "Grodecka 2",
				"rapides+1@gmail.com", R.drawable.o5, ExampleItem.STATE_TYPE[2]));
		addItem(new ExampleItem("Evilish", "EviLeenda", "rapides+2@gmail.com",
				R.drawable.o1, ExampleItem.STATE_TYPE[0]));
		addItem(new ExampleItem("Jarek", "Maksymiuk", "rapides+3@gmail.com",
				R.drawable.o2, ExampleItem.STATE_TYPE[0]));
		addItem(new ExampleItem("Joachim", "Pflaume", "rapides+5@gmail.com",
				R.drawable.o3, ExampleItem.STATE_TYPE[0]));
		addItem(new ExampleItem("Mayak", "Balop", "rapides+6@gmail.com",
				R.drawable.o4, ExampleItem.STATE_TYPE[0]));
		addItem(new ExampleItem("Mayak", "STAN1", "rapides+7@gmail.com",
				R.drawable.o4, ExampleItem.STATE_TYPE[1]));
		addItem(new ExampleItem("Janusz", "STAN1", "rapides+7@gmail.com",
				-1, ExampleItem.STATE_TYPE[1]));
		addItem(new ExampleItem("Weronika", "STAN2", "rapides+8@gmail.com",
				R.drawable.o5, ExampleItem.STATE_TYPE[2]));
		currentUser = new LoggedUser("Januszy", "Cebula—ski", "cycki@cycuszki.pl", "appleseed", -1, ExampleItem.STATE_TYPE[0]);
	}

	private static void addItem(ExampleItem item) {
		ITEM_MAP.put(item.getId(), item);
		ITEMS.add(item);
	}

	/**
	 * A dummy item representing a piece of content.
	 */
	public static class ExampleItem {
		public static int[] STATE_TYPE = { 0, 1, 2 };// "0-Accepted", "1-Sent",
														// "2-Received" };

		private long id;// idcontactu
		private int state;

		private String name;
		private String surname;
		private String email;

		private int photoId;
		private String photoUrl;

		private LinkedList<Message> messages;

		public ExampleItem(String name, String surname, String email,
				int photoId, int state) {
			this.id = email.hashCode();
			this.name = name;
			this.photoId = photoId;
			this.email = email;
			this.surname = surname;
			this.photoUrl = null;
			this.messages = new LinkedList<Message>();
			this.state = state;
			addRandomMessages();
		}

		public ExampleItem(String name, String surname, String email,
				String photoUrl, int state) {
			this.id = email.hashCode();
			this.name = name;
			this.photoUrl = photoUrl;
			this.email = email;
			this.surname = surname;
			this.photoId = -1;
			this.state = state;
			this.messages = new LinkedList<Message>();
			addRandomMessages();
		}

		private void addRandomMessages() {
			for (int i = 0; i < 50; i++) {
				messages.add(new Message());
			}
			messages.add(new Message("Poszukiwanie gorπcej linii z Niebem "
					+ "w tym úwiecie pozorÛw stanowi wyprawÍ "
					+ "z gÛry skazanπ na niepowodzenie. "
					+ "Poszukiwanie nie zepsutej instytucji, "
					+ "osoby ucieleúniajπcej dobro i prawdÍ, "
					+ "ksiπøki, nauk, miejsca, sposobu øycia, "
					+ "ktÛry odpowie na wszelkie pytania "
					+ "jest úlepπ uliczkπ, prowadzπcπ na "
					+ "manowce istnienia. "));
			messages.add(new Message("ok"));
			messages.add(new Message("cycki"));
			if (Math.random() < 0.5) {
				messages.add(new Message(
						"Ostatnia dluga wiadomosc, na tyle dluga zeby sie nie miescila "
								+ ""
								+ "w widoku na glownej stornie. Jednak musi byc"
								+ " dluøsza bo to co napiaslem wczesniej nie "
								+ "wystarczylo i sie nie skracalo, a ja chce "
								+ "sprawdziÊ czy elipsize dziala."));
			} else {
				messages.add(new Message("Ostatnia krÛtka wiadomosc"));
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
			if(photoId==-1){
				return R.drawable.loader;
			}else{
				return photoId;
			}
		}

		public String getImageUrl() {
			return photoUrl;
		}
		public String getName(){
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

		public void addFirstMessage(Message message) {
			Collections.reverse(messages);
			messages.add(message);
			Collections.reverse(messages);
		}
	}

	public static class LoggedUser extends ExampleItem{
		String password;
		public LoggedUser(String name, String surname, String email, String password,
				int photoId, int state) {
			super(name, surname, email, photoId, state);
			this.password = password;
		}
		public String getPassword(){
			return password;
		}
		
	}
}
