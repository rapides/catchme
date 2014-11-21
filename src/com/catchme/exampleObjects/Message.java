package com.catchme.exampleObjects;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;

public class Message {
	private int senderId;
	private long messageId;
	private String content;
	private long time;

	public Message() {
		senderId = (int) (Math.random() * 2);
		content = "" + 10 * Math.random() + "" + "" + 10 * Math.random() + ""
				+ 10 * Math.random() + "" + 10 * Math.random() + "" + 10
				* Math.random();
		time = System.currentTimeMillis();
		messageId = time;
	}

	public Message(String string) {
		senderId = (int) (Math.random() * 2);
		time = System.currentTimeMillis();
		content = string;
		messageId = time;
	}

	public int getSenderId() {
		return senderId;
	}

	public String getContent() {
		return content;
	}

	public String getTime() {
		return getDate(time, "dd/MM/yyyy HH:mm");
	}

	@SuppressLint("SimpleDateFormat")
	public static String getDate(long milliSeconds, String dateFormat) {
		// Create a DateFormatter object for displaying date in specified
		// format.
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

		// Create a calendar object that will convert the date and time value in
		// milliseconds to date.
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliSeconds);
		return formatter.format(calendar.getTime());
	}
	
	public long getMessageId() {
		return messageId;
	}

	public String toString(){
		return content;
	}
}
