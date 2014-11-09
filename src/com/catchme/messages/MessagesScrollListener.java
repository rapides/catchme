package com.catchme.messages;

import com.catchme.exampleObjects.ExampleContent.ExampleItem;
import com.catchme.exampleObjects.Message;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class MessagesScrollListener implements OnScrollListener {

	ListView listView;
	boolean isRefreshing = false;
	private ExampleItem mItem;
	public static int messagesCount = 0;

	public MessagesScrollListener(ListView listView, ExampleItem item) {
		this.listView = listView;
		this.mItem = item;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (firstVisibleItem == 0 && !isRefreshing && visibleItemCount > 0) {
			// TODO is vibleItemCount needed?
			isRefreshing = true;
			loadMoreMessages();
		}
	}

	private void loadMoreMessages() {
		for (int i = 0; i < 10; i++) {
			mItem.addFirstMessage(new Message("Nowa wiadomosc:  "
					+ messagesCount));
		}

		((MessagesListAdapter) listView.getAdapter()).notifyDataSetChanged();
		listView.setSelection(11);
		messagesCount++;
		isRefreshing = false;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

}
