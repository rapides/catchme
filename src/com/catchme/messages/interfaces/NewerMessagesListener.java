package com.catchme.messages.interfaces;

import java.util.LinkedList;

import android.support.v4.util.LongSparseArray;

import com.catchme.model.Message;

public interface NewerMessagesListener {
	public void onNewMessage(long userId, long conversationId, LinkedList<Message> newerMessages);
	public void onNewMessageError(LongSparseArray<String> errors);
}
