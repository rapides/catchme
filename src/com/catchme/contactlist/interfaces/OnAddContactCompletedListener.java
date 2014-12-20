package com.catchme.contactlist.interfaces;

import android.support.v4.util.LongSparseArray;

public interface OnAddContactCompletedListener {
	public void onAddContactSucceded();
	public void onAddContactError(LongSparseArray<String> errors);
	public void onPreAddContact();
}
