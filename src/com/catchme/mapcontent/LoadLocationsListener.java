package com.catchme.mapcontent;

import android.support.v4.util.LongSparseArray;

public interface LoadLocationsListener {
	public void locationsUpdated();

	public void locationsError(LongSparseArray<String> errors);
}
