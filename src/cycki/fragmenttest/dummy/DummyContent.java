package cycki.fragmenttest.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cycki.fragmenttest.R;


/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static ArrayList<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<Long , DummyItem> ITEM_MAP = new HashMap<Long, DummyItem>();

    static {
        // Add 3 sample items.
        addItem(new DummyItem(1, "Evilish EviLeenda", R.drawable.o1, "Lublin"));
        addItem(new DummyItem(2, "Jarek Maksymiuk", R.drawable.o2, "Chlebczyn"));
        addItem(new DummyItem(3, "Joachim Pflaume", R.drawable.o3, "Frankfurt"));
        addItem(new DummyItem(4, "Mayak Balop", R.drawable.o4, "Berlin"));
        addItem(new DummyItem(5, "Weronika Grodecka", R.drawable.o5, "Poznan"));
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public long id;
        public String name;
        public int photoId;
        public String cityName;
        

        public DummyItem(long id, String name, int photoId, String cityName) {
            this.id = id;
            this.name = name;
            this.photoId = photoId;
            this.cityName = cityName;
        }

        @Override
        public String toString() {
            return name;
        }


		public int getImageResource() {
			return photoId;
		}

		public String getName() {
			return name;
		}
		public long getId(){
			return id;
		}
		public String getCity(){
			return cityName;
		}
    }
}
