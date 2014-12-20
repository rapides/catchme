package com.catchme.mapcontent;

import java.util.ArrayList;

import com.catchme.model.UserLocation;

import android.support.v4.util.LongSparseArray;

public interface OnLoadLocationsListener {
	public void loadLocationsSucceded(
			LongSparseArray<ArrayList<UserLocation>> longSparseArray);

	public void loadLocationError(LongSparseArray<String> errors);
}
