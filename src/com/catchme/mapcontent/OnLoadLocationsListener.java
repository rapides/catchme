package com.catchme.mapcontent;

import java.util.ArrayList;

import android.location.Location;
import android.support.v4.util.LongSparseArray;

public interface OnLoadLocationsListener {
	public void loadLocationsSucceded(
			LongSparseArray<ArrayList<Location>> longSparseArray);

	public void loadLocationError(LongSparseArray<String> errors);
}
