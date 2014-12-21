package com.catchme.messages.interfaces;

import java.util.LinkedList;

import android.support.v4.util.LongSparseArray;

import com.catchme.model.Message;

public interface GetMessagesListener {

	void onPreGetMessages();

	void onGetMessagesError(LongSparseArray<String> errors);

	void onGetMessagesCompleted(long id, long conversationId,
			LinkedList<Message> messages);
}