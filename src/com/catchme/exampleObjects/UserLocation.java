package com.catchme.exampleObjects;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import com.catchme.connections.ServerConst;

import android.location.Location;

public class UserLocation {
	private float accuracy;
	private double latitude;
	private double longitude;
	private long fixTime;

	public UserLocation(float accuracy, double latitude, double longitude,
			String fixTime) {
		super();
		this.accuracy = accuracy;
		this.latitude = latitude;
		this.longitude = longitude;
		this.fixTime = getTimeFromString(fixTime);
	}

	public Location getLocation() {
		Location l = new Location(ServerConst.SERVER_NAME);
		l.setTime(fixTime);
		l.setAccuracy(accuracy);
		l.setLatitude(latitude);
		l.setLongitude(longitude);
		return l;
	}

	public long getTimeFromString(String date) {
		try {
			return new SimpleDateFormat(ServerConst.DATE_FORMAT,
					Locale.getDefault()).parse(date).getTime();
		} catch (ParseException e) {
			try {//TODO remove, only for testing on computer, with newer java
				return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
						Locale.getDefault()).parse(date).getTime();
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}
		return -1;
	}

	public String toString() {
		return "[" + latitude + "," + longitude + "], " + accuracy + ", "
				+ fixTime;
	}

}
