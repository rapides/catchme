package com.catchme.messages;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.LongSparseArray;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.catchme.R;
import com.catchme.contactlist.ItemListActivity;
import com.catchme.exampleObjects.ExampleContent;
import com.catchme.exampleObjects.ExampleItem;
import com.catchme.exampleObjects.LoggedUser;
import com.catchme.itemdetails.ItemDetailsFragment;
import com.catchme.messages.asynctasks.GetMessagesInitTask;
import com.catchme.messages.asynctasks.GetNewerMessagesTask;
import com.catchme.messages.asynctasks.GetOlderMessagesTask;
import com.catchme.messages.interfaces.GetMessagesListener;
import com.catchme.messages.interfaces.NewerMessagesListener;
import com.catchme.messages.interfaces.OnMessageSent;
import com.catchme.messages.listeners.MessagesRefreshListener;
import com.catchme.messages.listeners.SendButtonOnClickListener;

public class MessagesFragment extends Fragment implements OnMessageSent,
		NewerMessagesListener, GetMessagesListener, OnScrollListener {

	public static int timesClicked = 0;
	private ExampleItem item;
	private ListView listView;
	private View rootView;
	private EditText textBox;
	private LoggedUser user;
	private SwipeRefreshLayout swipeLayout;
	private boolean isGetingMessages;
	private boolean isMoreMessages;
	boolean isOpened = false;

	public MessagesFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		item = ExampleContent.ITEM_MAP.get(getArguments().getLong(
				ItemDetailsFragment.ARG_ITEM_ID));
		user = ItemListActivity.getLoggedUser(getActivity());
		isGetingMessages = false;
		isMoreMessages = true;
		setListnerToRootView();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_message_list, container,
				false);
		listView = (ListView) rootView.findViewById(R.id.messages_list);
		textBox = (EditText) rootView.findViewById(R.id.message_input);
		TextView t = (TextView) rootView.findViewById(R.id.simpleText);
		swipeLayout = (SwipeRefreshLayout) rootView
				.findViewById(R.id.message_swipe_container);
		ImageButton sendBtn = (ImageButton) rootView
				.findViewById(R.id.message_send);

		t.setText("" + item.getFullName());
		MessagesListAdapter adapter = new MessagesListAdapter(getActivity(),
				item, user);
		listView.setAdapter(adapter);
		if (item.getMessages(item.getFirstConversationId()) != null) {
			listView.setSelection(item.getMessages(
					item.getFirstConversationId()).size() - 1);
		}
		listView.setOnScrollListener(this);

		swipeLayout.setColorSchemeResources(R.color.swipelayout_bar,
				R.color.swipelayout_color1, R.color.swipelayout_color2,
				R.color.swipelayout_color3);

		swipeLayout.setOnRefreshListener(new MessagesRefreshListener(
				swipeLayout));

		sendBtn.setOnClickListener(new SendButtonOnClickListener(getActivity(),
				user, item, textBox, this));
		new GetMessagesInitTask(getActivity(), item, this).execute(
				user.getToken(), "" + item.getFirstConversationId());
		Intent i = new Intent(getActivity(), MessagesRefreshService.class);
		i.putExtra("item_id", item.getId());

		// getActivity().startService(i);

		return rootView;
	}

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
		new GetNewerMessagesTask(getActivity(), item, this).execute(listView
				.getItemIdAtPosition(listView.getCount() - 1));
		textBox.setText("");
		textBox.setEnabled(true);
	}

	@Override
	public void onNewMessage() {
		((MessagesListAdapter) listView.getAdapter()).notifyDataSetChanged();
		listView.setSelection(listView.getCount() - 1);
	}

	@Override
	public void onNewMessageError(LongSparseArray<String> errors) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onPreGetMessages() {
		swipeLayout.setRefreshing(true);
		isGetingMessages = true;
	}

	@Override
	public void onGetMessagesCompleted(int moreMessagesCount) {
		((MessagesListAdapter) listView.getAdapter()).notifyDataSetChanged();
		listView.setSelection(listView.getFirstVisiblePosition()
				+ moreMessagesCount);
		swipeLayout.setRefreshing(false);
		isGetingMessages = false;
		isMoreMessages = moreMessagesCount > 0;
	}

	@Override
	public void onGetMessagesError(LongSparseArray<String> errors) {
		swipeLayout.setRefreshing(false);
		Toast.makeText(getActivity(), "Message get OLDER server problem",
				Toast.LENGTH_SHORT).show();
		isGetingMessages = false;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (isMoreMessages && !isGetingMessages && firstVisibleItem == 0
				&& visibleItemCount > 0) {
			new GetOlderMessagesTask(getActivity(), item, this).execute(item
					.getMessages(item.getFirstConversationId()).get(0)
					.getMessageId());
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

}
