package com.catchme.messages.asynctask;

import org.json.JSONObject;

import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListView;

import com.catchme.exampleObjects.ExampleContent.ExampleItem;
import com.catchme.exampleObjects.Message;
import com.catchme.messages.MessagesListAdapter;

public class GetMessagesTask extends AsyncTask<String, Void, JSONObject> {
	private MessagesListAdapter adapter;
	private ListView listView;
	private ExampleItem item;
	private SwipeRefreshLayout swipeLatout;
	private static int messagesCount = 0;

	public GetMessagesTask(ListView listView, ExampleItem item,
			SwipeRefreshLayout swipeLayout) {
		super();
		this.listView = listView;
		this.item = item;
		this.swipeLatout = swipeLayout;
		this.adapter = (MessagesListAdapter) listView.getAdapter();
	}

	@Override
	protected void onPreExecute() {
		swipeLatout.setRefreshing(true);
	}

	@Override
	protected JSONObject doInBackground(String... params) {
		try {
			Thread.sleep(2000);
			loadMoreMessages();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	protected void onPostExecute(JSONObject result) {
		adapter.notifyDataSetChanged();
		listView.setSelection(10);
		swipeLatout.setRefreshing(false);
	}

	private void loadMoreMessages() {
		for (int i = 0; i < 10; i++) {
			item.addFirstMessage(new Message("Nowa wiadomosc:  "
					+ messagesCount));
		}
		messagesCount++;
	}
}
