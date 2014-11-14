package com.catchme.contactlist.asynctasks;

import org.json.JSONObject;

import com.catchme.R;
import com.catchme.connections.ReadServerResponse;
import com.catchme.connections.ServerConnection;
import com.catchme.connections.ServerRequests;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;

public class AddContactTask extends AsyncTask<String, Void, JSONObject> {
	private SwipeRefreshLayout swipeLayout;
	private Context context;

	public AddContactTask(SwipeRefreshLayout swipeLayout) {
		super();
		this.swipeLayout = swipeLayout;
		this.context = swipeLayout.getContext();
	}

	@Override
	protected void onPreExecute() {
		swipeLayout.setRefreshing(true);
	}

	@Override
	protected JSONObject doInBackground(String... params) {
		String token = params[0];
		String email = params[1];
		JSONObject result = null;
		if (ServerConnection.isOnline(context)) {
			result = ServerRequests.addContactRequest(token, email);
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
			Toast.makeText(context, "Add contact succeded: ", Toast.LENGTH_SHORT)
					.show();
			// TODO importing
		} else {
			Toast.makeText(context, "Add contact failed, server error", Toast.LENGTH_SHORT)
					.show();
		}

		// TODO handling error
		swipeLayout.setRefreshing(false);
	}

}
