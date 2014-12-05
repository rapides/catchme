package com.catchme.mapcontent;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.catchme.R;
import com.catchme.connections.ReadServerResponse;
import com.catchme.connections.ServerConnection;
import com.catchme.connections.ServerRequests;
import com.catchme.contactlist.ItemListActivity;
import com.catchme.exampleObjects.ExampleContent;
import com.catchme.exampleObjects.LoggedUser;
import com.catchme.exampleObjects.UserLocation;

public class LoadLocationsTask extends AsyncTask<String, Void, JSONObject> {
	private Context context;
	private LoadLocationsListener listener;

	public LoadLocationsTask(Context context, LoadLocationsListener listener) {
		super();
		this.context = context;
		this.listener = listener;
	}

	@Override
	protected JSONObject doInBackground(String... params) {
		String token = params[0];
		int numberPos = Integer.parseInt(params[1]);
		ArrayList<String> contactIds = new ArrayList<String>();
		for (int i = 2; i < params.length; i++) {
			contactIds.add(params[i]);
		}
		JSONObject result = null;
		if (ServerConnection.isOnline(context)) {
			result = ServerRequests.getLocations(token, contactIds, numberPos);
		}
		return result;
	}

	@Override
	protected void onPostExecute(JSONObject result) {
		if (result == null) {
			Toast.makeText(context,
					context.getResources().getString(R.string.err_no_internet),
					Toast.LENGTH_SHORT).show();
		} else if (ReadServerResponse.isSuccess(result)) {
			updateLocations(ReadServerResponse.getLocations(result));
			listener.locationsUpdated();
		} else {
			listener.locationsError(ReadServerResponse.getErrors(result));
		}
	}

	private void updateLocations(
			HashMap<Long, ArrayList<UserLocation>> locations) {
		for (long key : locations.keySet()) {
			if (key != 0) {
				ExampleContent.ITEM_MAP.get(key).setLocations(
						locations.get(key));
			} else {
				LoggedUser user = ItemListActivity.getLoggedUser(context);
				user.setLocations(locations.get(key));
				ItemListActivity.setLoggedUser(context, user);
			}
		}

	}

	
}
