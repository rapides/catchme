package com.catchme.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Message {
	private long senderId;
	private long messageId;
	private String content;
	private Date time;
	@SuppressWarnings("unused")
	private ArrayList<Object> readFeeds;

	public Message(long messageId, String content, Date createdAt, long userId,
			ArrayList<Object> readFeeds) {
		this.messageId = messageId;
		this.content = content;
		this.time = createdAt;
		this.senderId = userId;
		this.readFeeds = readFeeds;
	}

	public long getSenderId() {
		return senderId;
	}

	public String getContent() {
		return content;
	}

	public String getTime() {
		return new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
				.format(time);
	}

	public long getMessageId() {
		return messageId;
	}

	public String toString() {
		return getTime() + ", " + content + ", id:" + messageId + ", user:"
				+ senderId;
	}
}
