package com.catchme.locationServices;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class LocationRecorder {
	private static final String TAG = "GPS OPTIONS";
	Timer gpsTimer;
	Context context;
	private Location lastLocation;
	private long lastprovidertimestamp;

	public LocationRecorder(Context context) {
		super();
		this.context = context;
		gpsTimer = new Timer();
	}

	public void startRecording() {
		gpsTimer.cancel();
		gpsTimer = new Timer();
		long checkInterval = getGPSCheckMilliSecsFromPrefs();
		long minDistance = getMinDistanceFromPrefs();
		// receive updates
		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		for (String s : locationManager.getAllProviders()) {
			locationManager.requestLocationUpdates(s, checkInterval,
					minDistance, new LocationListener() {

						@Override
						public void onStatusChanged(String provider,
								int status, Bundle extras) {
						}

						@Override
						public void onProviderEnabled(String provider) {
						}

						@Override
						public void onProviderDisabled(String provider) {
						}

						@Override
						public void onLocationChanged(Location location) {
							// if this is a gps location, we can use it
							if (location.getProvider().equals(
									LocationManager.GPS_PROVIDER)) {
								doLocationUpdate(location, true);
							}
						}
					});
			Toast.makeText(context, "GPS Service STARTED", Toast.LENGTH_SHORT)
					.show();
		}
		// start the gps receiver thread
		gpsTimer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				Location location = getBestLocation();
				doLocationUpdate(location, false);
			}
		}, 0, checkInterval);
	}

	private long getMinDistanceFromPrefs() {
		return 20;
	}

	private long getGPSCheckMilliSecsFromPrefs() {
		return 1000 * 10*5;
	}

	public void doLocationUpdate(Location l, boolean force) {
		long minDistance = getMinDistanceFromPrefs();
		Log.d(TAG, "update received:" + l);
		if (l == null) {
			Log.d(TAG, "Empty location");
			if (force)
				Toast.makeText(context, "Current location not available",
						Toast.LENGTH_SHORT).show();
			return;
		}
		if (lastLocation != null) {
			float distance = l.distanceTo(lastLocation);
			Log.d(TAG, "Distance to last: " + distance);
			if (l.distanceTo(lastLocation) < minDistance && !force) {
				Log.d(TAG, "Position didn't change");
				return;
			}
			if (l.getAccuracy() >= lastLocation.getAccuracy()
					&& l.distanceTo(lastLocation) < l.getAccuracy() && !force) {
				Log.d(TAG, "Accuracy got worse and we are still "
						+ "within the accuracy range.. Not updating");
				return;
			}
			if (l.getTime() <= lastprovidertimestamp && !force) {
				Log.d(TAG, "Timestamp not never than last");
				return;
			}
		}
		if (lastLocation != null) {
			Log.d(TAG, "Ops ops" + lastLocation.getLatitude() + " "
					+ lastLocation.getLongitude());
		}
	}

	private Location getBestLocation() {
		Location gpslocation = getLocationByProvider(LocationManager.GPS_PROVIDER);
		Location networkLocation = getLocationByProvider(LocationManager.NETWORK_PROVIDER);
		// if we have only one location available, the choice is easy
		if (gpslocation == null) {
			Log.d(TAG, "No GPS Location available.");
			return networkLocation;
		}
		if (networkLocation == null) {
			Log.d(TAG, "No Network Location available");
			return gpslocation;
		}
		// a locationupdate is considered 'old' if its older than the configured
		// update interval. this means, we didn't get a
		// update from this provider since the last check
		long old = System.currentTimeMillis() - getGPSCheckMilliSecsFromPrefs();
		boolean gpsIsOld = (gpslocation.getTime() < old);
		boolean networkIsOld = (networkLocation.getTime() < old);
		// gps is current and available, gps is better than network
		if (!gpsIsOld) {
			Log.d(TAG, "Returning current GPS Location");
			return gpslocation;
		}
		// gps is old, we can't trust it. use network location
		if (!networkIsOld) {
			Log.d(TAG, "GPS is old, Network is current, returning network");
			return networkLocation;
		}
		// both are old return the newer of those two
		if (gpslocation.getTime() > networkLocation.getTime()) {
			Log.d(TAG, "Both are old, returning gps(newer)");
			return gpslocation;
		} else {
			Log.d(TAG, "Both are old, returning network(newer)");
			return networkLocation;
		}
	}

	/**
	 * get the last known location from a specific provider (network/gps)
	 */
	private Location getLocationByProvider(String provider) {
		Location location = null;
		if (!isProviderSupported(provider)) {
			return null;
		}
		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		try {
			if (locationManager.isProviderEnabled(provider)) {
				location = locationManager.getLastKnownLocation(provider);
			}
		} catch (IllegalArgumentException e) {
			Log.d(TAG, "Cannot acces Provider " + provider);
		}
		return location;
	}

	private boolean isProviderSupported(String provider) {
		return ((LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE)).getAllProviders().contains(provider);
	}
}
