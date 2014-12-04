package com.catchme.exampleObjects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import com.catchme.R;
import com.catchme.connections.ServerConst;

public class ExampleContent {
	@SuppressLint("UseSparseArrays")
	public static Map<Long, ExampleItem> ITEM_MAP = new HashMap<Long, ExampleItem>();

	public static void updateItems(ArrayList<ExampleItem> itemList) {
		if (itemList != null && itemList.size() > 0) {
			ITEM_MAP.clear();
			for (ExampleItem item : itemList) {
				ITEM_MAP.put(item.getId(), item);
			}
		}
	}

	public static void clear() {
		ITEM_MAP.clear();
	}
	
	public static void updatePositions(int contactId, List<UserLocation> locationList){
		ITEM_MAP.get(contactId).setLocations(locationList);
	}
}
