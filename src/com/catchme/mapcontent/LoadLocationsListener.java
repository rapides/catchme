package com.catchme.mapcontent;

import java.util.HashMap;

public interface LoadLocationsListener {
	public void locationsUpdated();
	public void locationsError(HashMap<Integer, String> hashMap);
}
