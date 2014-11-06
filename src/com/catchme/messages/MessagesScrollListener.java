package com.catchme.messages;

import com.catchme.contactlist.ItemListFragment;
import com.catchme.exampleObjects.ExampleContent;
import com.catchme.exampleObjects.Message;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.Toast;

public class MessagesScrollListener implements OnScrollListener {

	ListView listView;
	boolean isRefreshing = false;
	public static int messagesCount = 0;

	public MessagesScrollListener(ListView listView) {
		this.listView = listView;
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
			ExampleContent.ITEM_MAP.get(ItemListFragment.lastChoosedContactId)
					.addFirstMessage(
							new Message("Nowa wiadomosc:  " + messagesCount));
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
