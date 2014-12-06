package com.catchme.messages.interfaces;

import android.support.v4.util.LongSparseArray;

public interface NewerMessagesListener {
	public void onNewMessage();
	public void onNewMessageError(LongSparseArray<String> errors);
}
