package com.catchme.messages.asynctask;

import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.catchme.R;
import com.catchme.connections.ReadServerResponse;
import com.catchme.connections.ServerConnection;
import com.catchme.connections.ServerRequests;
import com.catchme.messages.listeners.OnMessageSent;

public class SendMessageTask extends AsyncTask<String, Void, JSONObject> {
	private Context context;
	long conversationId;
	OnMessageSent listener;

	public SendMessageTask(Context context, long conversationId,
			OnMessageSent listener) {
		this.context = context;
		this.conversationId = conversationId;
		this.listener = listener;
	}

	@Override
	protected JSONObject doInBackground(String... params) {
		String token = params[0];
		String messageContent = params[1];
		JSONObject result = new JSONObject();
		if (ServerConnection.isOnline(context)) {
			result = ServerRequests.sendMessageRequest(token, conversationId,
					messageContent);
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
			listener.onMessageSent(false);
		} else if (ReadServerResponse.isSuccess(result)) {
			Toast.makeText(context, "Message sent", Toast.LENGTH_SHORT).show();
			listener.onMessageSent(true);
		} else {
			Toast.makeText(context, "Message sending problem",
					Toast.LENGTH_SHORT).show();
			listener.onMessageSent(false);
		}
		// adapter.notifyDataSetChanged();
	}

}
