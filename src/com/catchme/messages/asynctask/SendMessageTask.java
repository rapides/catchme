package com.catchme.messages.asynctask;

import org.json.JSONObject;

import com.catchme.R;
import com.catchme.connections.ReadServerResponse;
import com.catchme.connections.ServerConnection;
import com.catchme.messages.MessagesListAdapter;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class SendMessageTask extends AsyncTask<String, Void, JSONObject> {
	private MessagesListAdapter adapter;
	private Context context;

	public SendMessageTask(Context context, MessagesListAdapter adapter) {
		this.adapter = adapter;
		this.context = context;

	}

	@Override
	protected JSONObject doInBackground(String... params) {
		String token = params[0];
		String message = params[1];
		JSONObject result = new JSONObject();
		if (ServerConnection.isOnline(context)) {
			// result = ServerRequests.sendMessageRequest(token, message);
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
			setMessageSent(false);
		} else if (ReadServerResponse.isSuccess(result)) {
			setMessageSent(true);
			Toast.makeText(context, "sent", Toast.LENGTH_SHORT).show();
		} else {
			setMessageSent(false);
			Toast.makeText(context, "sending problem", Toast.LENGTH_SHORT)
					.show();
		}
		adapter.notifyDataSetChanged();
	}

	private void setMessageSent(boolean b) {
		// TODO some kind of confirmation that message was sent.
	}

}
