package com.catchme.contactlist.asynctasks;

import org.json.JSONException;
import org.json.JSONObject;

import com.catchme.R;
import com.catchme.connections.ConnectionConst;
import com.catchme.connections.ServerConection;
import com.catchme.connections.ServerRequests;
import com.catchme.contactlist.CustomListAdapter;
import com.catchme.contactlist.DrawerMenuAdapter;
import com.catchme.exampleObjects.ExampleContent;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class LoginTask extends AsyncTask<String, Void, JSONObject> {
	Context context;
	ListView drawerList;
	ListView listView;

	public LoginTask(Context context, ListView drawerList, ListView listView) {
		super();
		this.context = context;
		this.drawerList = drawerList;
		this.listView = listView;
	}

	@Override
	protected JSONObject doInBackground(String... params) {
		JSONObject result = null;
		if (ServerConection.isOnline(context)) {
			String login = params[0];
			String password = params[1];

			/*result = ServerConection.JsonPOST(ConnectionConst.URL_CONTACTS_ALL,
					ServerRequests.getTokenRequest(login, password));*/
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
			try {
				System.out.println(result.get(ConnectionConst.TOKEN));
			} catch (JSONException e) {
				Log.e("JSONParseError", e.getMessage());
			}
		}
		listView.setAdapter(new CustomListAdapter((Activity) context,
				ExampleContent.ITEMS));
		drawerList.setAdapter(new DrawerMenuAdapter((Activity) context));
	}
	
}
