package com.catchme.exampleObjects;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.util.LongSparseArray;

public class ExampleContent {
	public static LongSparseArray<ExampleItem> ITEM_MAP = new LongSparseArray<ExampleItem>();

	public static void updateItems(ArrayList<ExampleItem> itemList) {
		if (itemList != null && itemList.size() > 0) {
			LongSparseArray<ExampleItem> tempItemWithMessages = ITEM_MAP
					.clone();
			ITEM_MAP.clear();
			for (ExampleItem item : itemList) {
				long id = item.getId();
				ExampleItem oldItem = tempItemWithMessages.get(id);
				if (tempItemWithMessages != null && oldItem!=null) {
					LongSparseArray<List<Message>> oldMessages = oldItem
							.getAllMessages();
					item.setMessages(oldMessages);
				}
				ITEM_MAP.put(item.getId(), item);
			}
		}
	}

	public static void clear() {
		ITEM_MAP.clear();
	}

	public static void updatePositions(int contactId,
			List<UserLocation> locationList) {
		ITEM_MAP.get(contactId).setLocations(locationList);
	}
}
