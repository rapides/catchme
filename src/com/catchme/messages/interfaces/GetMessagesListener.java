package com.catchme.messages.interfaces;

import android.support.v4.util.LongSparseArray;

public interface GetMessagesListener {

	void onPreGetMessages();

	void onGetMessagesCompleted(int moreMessagesCount);

	void onGetMessagesError(LongSparseArray<String> errors);
}