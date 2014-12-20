package com.catchme.contactlist.interfaces;

import java.util.ArrayList;

import com.catchme.model.ExampleItem;

import android.support.v4.util.LongSparseArray;

public interface OnGetContactCompletedListener {

	public void onPreGetContacts();

	public void onGetContactsError(LongSparseArray<String> errors);

	public void onGetContactsSucceded(ArrayList<ExampleItem> contactList);
	
}
