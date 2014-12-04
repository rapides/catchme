package com.catchme.exampleObjects;

import java.util.Date;

import android.location.Location;

public class UserLocation {
	private long accuracy;
	private double latitude;
	private double longitude;
	private long fixTime;
	public UserLocation(long accuracy, double latitude, double longitude,
			String fixTime) {
		super();
		this.accuracy = accuracy;
		this.latitude = latitude;
		this.longitude = longitude;
		this.fixTime = getTimeFromString(fixTime);
	}
	public Location getLocation(){
		Location l = new Location("C@tchme");
		l.setTime(fixTime);
		l.setAccuracy(accuracy);
		l.setLatitude(latitude);
		l.setLongitude(longitude);
		return l;
	}
	public static long getTimeFromString(String date) {
		return 0;
	}
	
	
}
