package com.catchme.messages.interfaces;

import android.support.v4.util.LongSparseArray;

public interface GetMessagesListener {

	void onPreGetMessages();

	void onGetMessagesCompleted(long itemId, long conversationId,
			int moreMessagesCount);

	void onGetMessagesError(LongSparseArray<String> errors);
}