package com.catchme.profile.asynctasks;

import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.catchme.R;
import com.catchme.connections.ReadServerResponse;
import com.catchme.connections.ServerRequests;
import com.catchme.database.model.ExampleItem.ContactStateType;
import com.catchme.profile.ContactChangedState;

public class ChangeContactStateTask extends AsyncTask<String, Void, JSONObject> {
	private Context context;
	private ContactChangedState listener;
	ContactStateType newState;
	public ChangeContactStateTask(Context context, ContactChangedState listener) {
		super();
		this.context = context;
		this.listener = listener;
	}

	@Override
	protected JSONObject doInBackground(String... params) {
		String token = params[0];
		long contactId = Long.parseLong(params[1]);
		newState = ContactStateType.getStateType(Integer
				.parseInt(params[2]));
		return ServerRequests.setContactStateRequest(token, contactId, newState);

	}

	protected void onPostExecute(JSONObject result) {
		if (result == null) {
			Toast.makeText(context,
					context.getResources().getString(R.string.err_no_internet),
					Toast.LENGTH_SHORT).show();
		} else if (ReadServerResponse.isSuccess(result)) {
			listener.contactChangedState(newState);
		} else {
			listener.contactChangedState(null);
		}
	}
}
