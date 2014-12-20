package com.catchme.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import android.location.Location;

import com.catchme.connections.ServerConst;

public class UserLocation implements Comparable<UserLocation>{
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

	public float getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(float accuracy) {
		this.accuracy = accuracy;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public long getFixTime() {
		return fixTime;
	}

	public void setFixTime(long fixTime) {
		this.fixTime = fixTime;
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

	

	@Override
	public int compareTo(UserLocation another) {
		Long time1 = this.fixTime;
		Long time2 = another.fixTime;
		return time2.compareTo(time1);
	}

}
