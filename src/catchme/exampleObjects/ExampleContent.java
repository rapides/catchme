package catchme.exampleObjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cycki.catchme.R;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class ExampleContent {

	/**
	 * An array of sample (dummy) items.
	 */
	public static ArrayList<ExampleItem> ITEMS = new ArrayList<ExampleItem>();

	/**
	 * A map of sample (dummy) items, by ID.
	 */
	public static Map<Long, ExampleItem> ITEM_MAP = new HashMap<Long, ExampleItem>();

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
				"Poznan"));
		addItem(new ExampleItem(1, "Evilish EviLeenda", R.drawable.o1, "Lublin"));
		addItem(new ExampleItem(2, "Jarek Maksymiuk", R.drawable.o2,
				"Chlebczyn"));
		addItem(new ExampleItem(3, "Joachim Pflaume", R.drawable.o3,
				"Frankfurt"));
		addItem(new ExampleItem(4, "Mayak Balop", R.drawable.o4, "Berlin"));
		addItem(new ExampleItem(5, "Weronika Grodecka", R.drawable.o5, "Poznan"));
	}

	private static void addItem(ExampleItem item) {
		ITEMS.add(item);
		ITEM_MAP.put(item.id, item);
	}

	/**
	 * A dummy item representing a piece of content.
	 */
	public static class ExampleItem {
		private long id;
		private String name;
		private int photoId;
		private String cityName;
		private ArrayList<Message> messages;
		private String photoUrl;

		public ExampleItem(long id, String name, int photoId, String cityName) {
			this.id = id;
			this.name = name;
			this.photoId = photoId;
			this.photoUrl = null;
			this.cityName = cityName;
			this.messages = new ArrayList<Message>();
			addRandomMessages();
		}

		public ExampleItem(long id, String name, String photoUrl,
				String cityName) {
			this.id = id;
			this.name = name;
			this.photoUrl = photoUrl;
			this.photoId = -1;
			this.cityName = cityName;
			this.messages = new ArrayList<Message>();
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

		public ArrayList<Message> getMessages() {
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
	}
}
