package com.catchme.messages;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.catchme.R;
import com.catchme.contactlist.ItemListActivity;
import com.catchme.exampleObjects.ExampleContent;
import com.catchme.exampleObjects.ExampleItem;
import com.catchme.exampleObjects.LoggedUser;
import com.catchme.itemdetails.ItemDetailsFragment;
import com.catchme.messages.asynctask.GetMessagesInitTask;
import com.catchme.messages.asynctask.GetNewerMessagesTask;
import com.catchme.messages.listeners.MessagesRefreshListener;
import com.catchme.messages.listeners.MessagesScrollListener;
import com.catchme.messages.listeners.OnMessageSent;
import com.catchme.messages.listeners.SendButtonOnClickListener;
import com.google.gson.Gson;

public class MessagesFragment extends Fragment implements OnMessageSent {

	public static int timesClicked = 0;
	private ExampleItem item;
	ListView listView;
	View rootView;
	EditText textBox;
	LoggedUser user;
	private SwipeRefreshLayout swipeLayout;

	public MessagesFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		item = ExampleContent.ITEM_MAP.get(getArguments().getLong(
				ItemDetailsFragment.ARG_ITEM_ID));
		user = ItemListActivity.getLoggedUser(getActivity());
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
		sendBtn.setOnClickListener(new SendButtonOnClickListener(getActivity(),
				user, item, textBox, this));
		new GetMessagesInitTask(listView, swipeLayout, item).execute(
				user.getToken(), "" + item.getFirstConversationId());
		/*
		 * getActivity() .startService( new Intent(rootView.getContext(),
		 * MessagesRefreshService.class));
		 */

		return rootView;
	}

	private void loadData() {
		listView = (ListView) rootView.findViewById(R.id.messages_list);
		textBox = (EditText) rootView.findViewById(R.id.message_input);
		TextView t = (TextView) rootView.findViewById(R.id.simpleText);
		swipeLayout = (SwipeRefreshLayout) rootView
				.findViewById(R.id.message_swipe_container);
		t.setText("" + item.getFullName());
		MessagesListAdapter adapter = new MessagesListAdapter(getActivity(),
				item, user);
		listView.setAdapter(adapter);
		if (item.getMessages(item.getFirstConversationId()) != null) {
			listView.setSelection(item.getMessages(
					item.getFirstConversationId()).size() - 1);
		}
		listView.setOnScrollListener(new MessagesScrollListener(listView, item,
				swipeLayout));

		swipeLayout.setColorSchemeResources(R.color.swipelayout_bar,
				R.color.swipelayout_color1, R.color.swipelayout_color2,
				R.color.swipelayout_color3);

		swipeLayout.setOnRefreshListener(new MessagesRefreshListener(
				swipeLayout));
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
							listView.setSelection(item.getMessages(
									item.getFirstConversationId()).size() - 1);
							isOpened = true;
						} else if (isOpened == true) {
							isOpened = false;
						}
					}
				});
	}

	@Override
	public void onMessageSent(boolean b) {
		new GetNewerMessagesTask(listView, item).execute(listView
				.getItemIdAtPosition(listView.getCount() - 1));
		textBox.setText("");
		textBox.setEnabled(true);
	}
}
