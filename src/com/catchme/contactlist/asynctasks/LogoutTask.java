package com.catchme.contactlist.asynctasks;

import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;

import com.catchme.R;
import com.catchme.connections.ReadServerResponse;
import com.catchme.connections.ServerConnection;
import com.catchme.connections.ServerRequests;
import com.catchme.contactlist.ItemListActivity;
import com.catchme.contactlist.interfaces.OnLogoutCompletedListener;

public class LogoutTask extends AsyncTask<String, Void, JSONObject> {
	private Context context;
	private OnLogoutCompletedListener listener;
	public LogoutTask(SwipeRefreshLayout swipeLayout, OnLogoutCompletedListener listener) {
		super();
		context = swipeLayout.getContext();
		this.listener = listener;
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
			listener.onLogoutError(null);
			Toast.makeText(context,
					context.getResources().getString(R.string.err_no_internet),
					Toast.LENGTH_SHORT).show();
		} else {
			if (ReadServerResponse.isSuccess(result)) {
				listener.onLogoutSucceded();
				Toast.makeText(context, "Success! Logged out",
						Toast.LENGTH_SHORT).show();
				ItemListActivity.removeLoggedUser(context);
			} else {
				listener.onLogoutError(ReadServerResponse.getErrors(result));
				Toast.makeText(context,
						"Fail! " + ReadServerResponse.getErrors(result),
						Toast.LENGTH_SHORT).show();

			}
		}
	}
}
