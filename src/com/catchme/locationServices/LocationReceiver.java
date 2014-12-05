package com.catchme.locationServices;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.catchme.contactlist.ItemListActivity;
import com.catchme.exampleObjects.LoggedUser;
import com.commonsware.cwac.locpoll.LocationPoller;
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
				if (preferences.contains(ItemListActivity.USER)) {

					LoggedUser user = ItemListActivity.getLoggedUser(context);
					new SendLocationTask(context, user.getToken()).execute(loc);
				} else {
					Log.i("Location", "LOCATION TRACKING CANCELing");
					Toast.makeText(context, "LOCATION TRACKING CANCELing",
							Toast.LENGTH_SHORT).show();
					AlarmManager alarmManager = (AlarmManager) context
							.getSystemService(Context.ALARM_SERVICE);

					Intent updateServiceIntent = new Intent(context,
							LocationPoller.class);
					PendingIntent pendingUpdateIntent = PendingIntent
							.getService(context, 0, updateServiceIntent, 0);
					alarmManager.cancel(pendingUpdateIntent);
					Log.i("Location", "LOCATION TRACKING CANCELED");
					Toast.makeText(context, "LOCATION TRACKING CANCELED",
							Toast.LENGTH_SHORT).show();
				}

			}
			Log.d("Location", msg);
		} catch (Exception e) {
			Log.e("Location", e.getMessage());
		}
	}
}