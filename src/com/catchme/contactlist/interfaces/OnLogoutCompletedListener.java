package com.catchme.contactlist.interfaces;

import android.support.v4.util.LongSparseArray;

public interface OnLogoutCompletedListener {
	public void onLogoutSucceded();
	public void onLogoutError(LongSparseArray<String> errors);
}
