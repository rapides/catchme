package catchme.messages;

import catchme.exampleObjects.ExampleContent;
import cycki.catchme.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MessagesFragment extends Fragment implements OnClickListener {

	public static final String ARG_ITEM_ID = "message_id";
	public static int timesClicked = 0;
	private ExampleContent.ExampleItem mItem;
	ListView listView;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public MessagesFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			if (getArguments().containsKey(ARG_ITEM_ID)) {
				mItem = ExampleContent.ITEM_MAP.get(getArguments().getLong(
						ARG_ITEM_ID));
			}
			setListnerToRootView();
		} else {
			mItem = null;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_message_list,
				container, false);
		if (mItem != null) {
			listView = (ListView) rootView.findViewById(R.id.messages_list);
			TextView t = (TextView) rootView.findViewById(R.id.simpleText);
			t.setText("" + mItem);
			MessagesListAdapter adapter = new MessagesListAdapter(
					getActivity(), mItem);
			listView.setAdapter(adapter);
			listView.setSelection(mItem.getMessages().size() - 1);
		}
		Button sendBtn = (Button) rootView.findViewById(R.id.message_send);
		sendBtn.setOnClickListener(this);
		getActivity().startService(new Intent(rootView.getContext(), MessagesRefreshService.class));
		return rootView;
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
			mNotifyBuilder.setContentText(timesClicked + " text")
	        .setNumber(timesClicked);
		}
		Notification note = mNotifyBuilder.build();
		note.defaults |= Notification.DEFAULT_VIBRATE;
	    note.defaults |= Notification.DEFAULT_SOUND;
		mNotificationManager.notify(notifyID, note);
		
	}

}
