package com.catchme.messages;

import java.util.ArrayList;
import java.util.List;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
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
import com.catchme.database.CatchmeDatabaseAdapter;
import com.catchme.itemdetails.ItemDetailsFragment;
import com.catchme.messages.asynctasks.GetMessagesInitTask;
import com.catchme.messages.asynctasks.GetNewerMessagesTask;
import com.catchme.messages.asynctasks.GetOlderMessagesTask;
import com.catchme.messages.interfaces.GetMessagesListener;
import com.catchme.messages.interfaces.NewerMessagesListener;
import com.catchme.messages.interfaces.OnMessageSent;
import com.catchme.messages.listeners.MessagesRefreshListener;
import com.catchme.messages.listeners.SendButtonOnClickListener;
import com.catchme.model.ExampleItem;
import com.catchme.model.LoggedUser;
import com.catchme.model.Message;

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
	private CatchmeDatabaseAdapter dbAdapter;

	public MessagesFragment(CatchmeDatabaseAdapter dbAdapter) {
		this.dbAdapter = dbAdapter;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		item = dbAdapter.getItem(getArguments().getLong(
				ItemDetailsFragment.ARG_ITEM_ID));
		user = ItemListActivity.getLoggedUser(getActivity());
		isGetingMessages = false;
		isMoreMessages = true;
		setListnerToRootView();
		MessagesBroadcastReceiver receiver = new MessagesBroadcastReceiver(this);
		IntentFilter filterMessage = new IntentFilter(
				MessagesRefreshService.BROADCAST_NEW_MESSAGE);
		IntentFilter filterError = new IntentFilter(
				MessagesRefreshService.BROADCAST_ERROR);
		LocalBroadcastManager
				.getInstance(getActivity().getApplicationContext())
				.registerReceiver(receiver, filterMessage);
		LocalBroadcastManager
				.getInstance(getActivity().getApplicationContext())
				.registerReceiver(receiver, filterError);

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
		List<Message> listMessages = dbAdapter.getMessages(item
				.getFirstConversationId());
		MessagesListAdapter adapter = new MessagesListAdapter(getActivity(),
				user, item, dbAdapter, item.getFirstConversationId());
		listView.setAdapter(adapter);
		if (listMessages != null && listMessages.size() > 0) {
			listView.setSelection(dbAdapter.getMessages(
					item.getFirstConversationId()).size() - 1);
		} else {
			new GetMessagesInitTask(getActivity(), item, dbAdapter, this)
					.execute(item.getFirstConversationId());
		}
		listView.setOnScrollListener(this);

		swipeLayout.setColorSchemeResources(R.color.swipelayout_bar,
				R.color.swipelayout_color1, R.color.swipelayout_color2,
				R.color.swipelayout_color3);

		swipeLayout.setOnRefreshListener(new MessagesRefreshListener(
				swipeLayout));

		sendBtn.setOnClickListener(new SendButtonOnClickListener(getActivity(),
				user, item, textBox, this));
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
							listView.setSelection(dbAdapter.getMessages(
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
		new GetNewerMessagesTask(getActivity(), item, dbAdapter, this).execute(
				item.getFirstConversationId(),
				listView.getItemIdAtPosition(listView.getCount() - 1));
		textBox.setText("");
		textBox.setEnabled(true);
	}

	@Override
	public void onNewMessage(long itemId, long conversationId,
			int newMessagesCount) {
		((MessagesListAdapter) listView.getAdapter()).notifyDataSetChanged();
		listView.setSelection(listView.getCount() - 1);
	}

	@Override
	public void onNewMessageError(LongSparseArray<String> errors) {
	}

	@Override
	public void onPreGetMessages() {
		swipeLayout.setRefreshing(true);
		isGetingMessages = true;
	}

	@Override
	public void onGetMessagesCompleted(long id, long conversationId,
			ArrayList<Message> messages) {
		((MessagesListAdapter) listView.getAdapter()).notifyDataSetChanged();
		listView.setSelection(listView.getFirstVisiblePosition()
				+ messages.size());
		isGetingMessages = false;
		isMoreMessages = messages.size() > 0;
		swipeLayout.setRefreshing(false);
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
			long conversationId = item.getFirstConversationId();
			new GetOlderMessagesTask(getActivity(), item, dbAdapter, this)
					.execute(conversationId,
							dbAdapter.getOldestMessageId(conversationId));
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

}
