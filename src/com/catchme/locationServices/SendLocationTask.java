package com.catchme.locationServices;

import org.json.JSONObject;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.widget.Toast;

import com.catchme.R;
import com.catchme.connections.ReadServerResponse;
import com.catchme.connections.ServerConnection;
import com.catchme.connections.ServerRequests;
import com.catchme.exampleObjects.ExampleContent;

public class SendLocationTask extends AsyncTask<Location, Void, JSONObject> {

	Context context;
	String token;

	public SendLocationTask(Context context, String token) {
		this.context = context;
		this.token = token;
	}

	@Override
	protected JSONObject doInBackground(Location... params) {
		Location l = params[0];
		JSONObject result = new JSONObject();
		if (ServerConnection.isOnline(context)) {
			result = ServerRequests.setUserLocationRequest(token,
					l.getLatitude(), l.getLongitude());
		} else {
			result = null;
		}
		return result;

	}

	@Override
	protected void onPostExecute(JSONObject result) {
		if (result == null) {
			Toast.makeText(
					context,
					"Sending location... \n"
							+ context.getResources().getString(
									R.string.err_no_internet),
					Toast.LENGTH_SHORT).show();
		} else {
			if (ReadServerResponse.isSuccess(result)) {
				Toast.makeText(context, "Location sent", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(
						context,
						"Location sending failed, server error\n"
								+ ReadServerResponse.getErrors(result),
						Toast.LENGTH_SHORT).show();
			}
		}
	}
}
