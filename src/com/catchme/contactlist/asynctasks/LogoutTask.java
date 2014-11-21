package com.catchme.contactlist.asynctasks;

import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;

import com.catchme.R;
import com.catchme.connections.ReadServerResponse;
import com.catchme.connections.ServerConnection;
import com.catchme.connections.ServerRequests;
import com.catchme.contactlist.ItemListActivity;
import com.catchme.exampleObjects.ExampleContent;

public class LogoutTask extends AsyncTask<String, Void, JSONObject> {
	private Context context;

	public LogoutTask(SwipeRefreshLayout swipeLayout) {
		super();
		context = swipeLayout.getContext();
	}

	@Override
	protected JSONObject doInBackground(String... params) {
		String token = params[0];
		long id = Long.parseLong(params[1]);
		JSONObject result = new JSONObject();
		if (ServerConnection.isOnline(context)) {
			result = ServerRequests.setUserLogOutRequest(token, id);
		} else {
			result = null;
		}

		return result;
	}

	@Override
	protected void onPostExecute(JSONObject result) {
		if (result == null) {
			Toast.makeText(context,
					context.getResources().getString(R.string.err_no_internet),
					Toast.LENGTH_SHORT).show();
		} else {
			if (ReadServerResponse.isSuccess(result)) {
				Toast.makeText(context, "Success! Logged out",
						Toast.LENGTH_SHORT).show();
				SharedPreferences preferences = context.getSharedPreferences(
						ItemListActivity.PREFERENCES, Context.MODE_PRIVATE);
				Editor e = preferences.edit();
				e.remove(ItemListActivity.USER);
				e.commit();
			} else {
				Toast.makeText(context,
						"Fail! " + ReadServerResponse.getErrors(result),
						Toast.LENGTH_SHORT).show();

			}
		}
	}
}
