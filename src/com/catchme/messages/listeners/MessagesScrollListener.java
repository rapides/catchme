package com.catchme.messages.listeners;

import android.os.AsyncTask.Status;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.catchme.exampleObjects.ExampleContent.ExampleItem;
import com.catchme.messages.asynctask.GetOlderMessagesTask;
import com.catchme.messages.asynctask.GetOlderMessagesTaskCompleted;

public class MessagesScrollListener implements OnScrollListener,
		GetOlderMessagesTaskCompleted {

	private ListView listView;
	private ExampleItem mItem;
	private GetOlderMessagesTask task;
	private SwipeRefreshLayout swipeLayout;
	public boolean isMoreMessages = true;

	public MessagesScrollListener(ListView listView, ExampleItem item,
			SwipeRefreshLayout swipeLayout) {
		this.listView = listView;
		this.mItem = item;
		this.swipeLayout = swipeLayout;
		task = new GetOlderMessagesTask(listView, mItem, swipeLayout,
				mItem.getFirstConversationId(), this);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (isMoreMessages && firstVisibleItem == 0) {
			if (task.getStatus() != Status.RUNNING && visibleItemCount > 0) {
				task = new GetOlderMessagesTask(listView, mItem, swipeLayout,
						mItem.getFirstConversationId(), this);
				task.execute(mItem.getMessages(mItem.getFirstConversationId())
						.get(0).getMessageId());
			}
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void setIsMoreMessagesAvailable(boolean isMoreMessages) {
		this.isMoreMessages = isMoreMessages;
	}

}
