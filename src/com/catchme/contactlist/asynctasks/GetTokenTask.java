package com.catchme.contactlist.asynctasks;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.catchme.R;
import com.catchme.connections.ConnectionConst;
import com.catchme.connections.ServerConection;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class GetTokenTask extends AsyncTask<String, Void, String> {
	Context context;

	public GetTokenTask(Context context) {
		super();
		this.context = context;
	}

	@Override
	protected String doInBackground(String... params) {
		String result = null;
		if (ServerConection.isOnline(context)) {
			String login = params[0];
			String password = params[1];
			HashMap<String, HashMap<String, String>> data = new HashMap<String, HashMap<String, String>>();
			HashMap<String, String> dataContent = new HashMap<String, String>();
			dataContent.put(ConnectionConst.USER_EMAIL, login);
			dataContent.put(ConnectionConst.USER_PASSWORD, password);
			data.put(ConnectionConst.USER, dataContent);
			result = ServerConection.JsonPOST(ConnectionConst.URL_CONTACTS_ALL,
					data);
		}

		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		if (result == null) {
			Toast.makeText(context,
					context.getResources().getString(R.string.err_no_internet),
					Toast.LENGTH_SHORT).show();
		} else {
			JSONObject json;
			try {
				json = new JSONObject(result);
				System.out.println(json.get(ConnectionConst.TOKEN));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

}
