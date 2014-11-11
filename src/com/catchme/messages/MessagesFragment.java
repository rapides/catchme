package com.catchme.messages;

import org.json.JSONException;
import org.json.JSONObject;

import com.catchme.R;
import com.catchme.connections.ServerConection;
import com.catchme.connections.ServerRequests;
import com.catchme.exampleObjects.ExampleContent;
import com.catchme.exampleObjects.ExampleContent.ExampleItem;
import com.catchme.itemdetails.ItemDetailsFragment;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class MessagesFragment extends Fragment implements OnClickListener {

	public static int timesClicked = 0;
	private ExampleItem mItem;
	ListView listView;
	View rootView;

	public MessagesFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mItem = ExampleContent.ITEM_MAP.get(getArguments().getLong(
				ItemDetailsFragment.ARG_ITEM_ID));
		setListnerToRootView();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_message_list, container,
				false);
		loadData();
		ImageButton sendBtn = (ImageButton) rootView
				.findViewById(R.id.message_send);
		sendBtn.setOnClickListener(this);

		getActivity()
				.startService(
						new Intent(rootView.getContext(),
								MessagesRefreshService.class));

		return rootView;
	}

	private void loadData() {
		listView = (ListView) rootView.findViewById(R.id.messages_list);
		TextView t = (TextView) rootView.findViewById(R.id.simpleText);
		t.setText("" + mItem.getFullName());
		MessagesListAdapter adapter = new MessagesListAdapter(getActivity(),
				mItem);
		listView.setAdapter(adapter);
		listView.setSelection(mItem.getMessages().size() - 1);
		listView.setOnScrollListener(new MessagesScrollListener(listView, mItem));
	}

	boolean isOpened = false;

	public void setListnerToRootView() {
		final View activityRootView = getActivity().getWindow().getDecorView()
				.findViewById(android.R.id.content);
		activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {

						int heightDiff = activityRootView.getRootView()
								.getHeight() - activityRootView.getHeight();
						if (heightDiff > 200) { // TODO different resolutions
							listView.setSelection(mItem.getMessages().size() - 1);
							isOpened = true;
						} else if (isOpened == true) {
							isOpened = false;
						}
					}
				});
	}

	@Override
	public void onClick(View v) {
		int notifyID = 1;
		timesClicked++;
		NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
				v.getContext()).setSmallIcon(R.drawable.o2)
				.setContentTitle("My notification")
				.setContentText("Hello World!");
		NotificationManager mNotificationManager = (NotificationManager) v
				.getContext().getSystemService(Context.NOTIFICATION_SERVICE);

		if (timesClicked > 3) {
			mNotifyBuilder.setContentText(timesClicked + " text").setNumber(
					timesClicked);
		}
		Notification note = mNotifyBuilder.build();
		note.defaults |= Notification.DEFAULT_VIBRATE;
		note.defaults |= Notification.DEFAULT_SOUND;
		mNotificationManager.notify(notifyID, note);
		// (new
		// ConnectTask()).execute("http://192.168.43.19:3000/api/v1/contacts/all");
		(new ConnectTask()).execute("http://192.168.43.19:3000/api/v1/auth");
	}

	public void updateView(long id) {
		mItem = ExampleContent.ITEM_MAP.get(id);
		loadData();
	}

	private class ConnectTask extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... params) {
			JSONObject result = ServerConection.JsonPOST(params[0], ServerRequests
					.getTokenRequest("rapides+3@gmail.com", "appleseed"));
			return result;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			try {
				System.out
						.println("Popacz sam, jak gumisie skacz¹ tam i siam: "
								+ (result.getJSONObject("user")).get("email"));
			} catch (JSONException e) {
				// e.printStackTrace();
			}
		}
	}
}
