package com.catchme.locationServices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.catchme.contactlist.ItemListActivity;
import com.commonsware.cwac.locpoll.LocationPollerResult;

public class LocationReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		try {
			Bundle b = intent.getExtras();
			LocationPollerResult locationResult = new LocationPollerResult(b);
			Location loc = locationResult.getLocation();
			String msg;
			if (loc == null) {
				loc = locationResult.getLastKnownLocation();
				if (loc == null) {
					msg = locationResult.getError();
				} else {
					msg = "TIMEOUT, lastKnown=" + loc.toString();
				}
			} else {
				msg = loc.toString();
				Log.i("Location", String.valueOf(loc.getLatitude()));
				Log.i("Location", String.valueOf(loc.getLongitude()));
				Log.i("Location", String.valueOf(loc.getAccuracy()));

				SharedPreferences preferences = context.getSharedPreferences(
						ItemListActivity.PREFERENCES, Context.MODE_PRIVATE);
				String token = preferences.getString(
						ItemListActivity.USER_TOKEN, "ERROR");
				System.out.println("Cycki: " + token);
				new SendLocationTask(context, token).execute(loc);
			}
			Log.d("Location", msg);
		} catch (Exception e) {
			Log.e("Location", e.getMessage());
		}
	}
}