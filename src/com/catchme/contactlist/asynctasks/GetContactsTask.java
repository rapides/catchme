package com.catchme.contactlist.asynctasks;

import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

import com.catchme.connections.ReadServerResponse;
import com.catchme.connections.ServerConnection;
import com.catchme.connections.ServerRequests;
import com.catchme.contactlist.interfaces.OnGetContactCompletedListener;
import com.catchme.database.CatchmeDatabaseAdapter;
import com.catchme.database.model.ExampleItem;
import com.catchme.database.model.ExampleItem.ContactStateType;

public class GetContactsTask extends AsyncTask<String, Void, JSONObject> {

	private Context context;
	private ContactStateType state;
	private OnGetContactCompletedListener listener;
	private CatchmeDatabaseAdapter dbAdapter;

	public GetContactsTask(Context context,
			OnGetContactCompletedListener listener,
			CatchmeDatabaseAdapter dbAdapter, ContactStateType state) {
		super();
		this.state = state;
		this.context = context;
		this.listener = listener;
		this.dbAdapter = dbAdapter;
	}

	@Override
	protected void onPreExecute() {
		listener.onPreGetContacts();
	}

	@Override
	protected JSONObject doInBackground(String... params) {
		String token = params[0];
		JSONObject result = null;
		if (ServerConnection.isOnline(context)) {
			if (state == ContactStateType.ACCEPTED) {
				result = ServerRequests.getAcceptedContactsRequest(token);
			} else if (state == ContactStateType.SENT) {
				result = ServerRequests.getSentContactsRequest(token);
			} else if (state == ContactStateType.RECEIVED) {
				result = ServerRequests.getReceivedContactsRequest(token);
			}
			if (ReadServerResponse.isSuccess(result)) {
				ArrayList<ExampleItem> contactList = ReadServerResponse
						.getContactList(result, state);
				if(dbAdapter.isOpened()){
					dbAdapter.updateItems(contactList);
				}
			}
		}
		return result;
	}

	@Override
	protected void onPostExecute(JSONObject result) {
		if (result == null) {
			listener.onGetContactsError(null);
		} else {
			if (ReadServerResponse.isSuccess(result)) {
				listener.onGetContactsSucceded(ReadServerResponse
						.getContactList(result, state));
			} else {
				listener.onGetContactsError(ReadServerResponse
						.getErrors(result));
			}
		}
	}

}