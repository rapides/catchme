package com.catchme.mapcontent;

import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.util.LongSparseArray;
import android.widget.Toast;

import com.catchme.R;
import com.catchme.connections.ReadServerResponse;
import com.catchme.connections.ServerConnection;
import com.catchme.connections.ServerRequests;
import com.catchme.database.CatchmeDatabaseAdapter;

public class LoadLocationsTask extends AsyncTask<String, Void, JSONObject> {
	private Context context;
	private OnLoadLocationsListener listener;
	private CatchmeDatabaseAdapter dbAdapter;

	private LongSparseArray<ArrayList<Location>> locations;

	public LoadLocationsTask(Context context, CatchmeDatabaseAdapter dbAdapter,
			OnLoadLocationsListener listener) {
		super();
		this.context = context;
		this.listener = listener;
		this.dbAdapter = dbAdapter;
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
			if (ReadServerResponse.isSuccess(result)) {
				locations = ReadServerResponse.getLocations(result);
				dbAdapter.updateLocations(locations);
			}
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
			listener.loadLocationsSucceded(locations);
		} else {
			listener.loadLocationError(ReadServerResponse.getErrors(result));
		}
	}

}
