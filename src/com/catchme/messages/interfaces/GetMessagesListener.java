package com.catchme.messages.interfaces;

import java.util.ArrayList;

import com.catchme.model.Message;

import android.support.v4.util.LongSparseArray;

public interface GetMessagesListener {

	void onPreGetMessages();

	void onGetMessagesError(LongSparseArray<String> errors);

	void onGetMessagesCompleted(long id, long conversationId,
			ArrayList<Message> messages);
}