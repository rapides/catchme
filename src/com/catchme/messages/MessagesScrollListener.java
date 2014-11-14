package com.catchme.messages;

import com.catchme.exampleObjects.ExampleContent.ExampleItem;
import com.catchme.exampleObjects.Message;
import com.catchme.messages.asynctask.GetMessagesTask;

import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class MessagesScrollListener implements OnScrollListener {

	private ListView listView;
	private ExampleItem mItem;
	private GetMessagesTask task;
	private SwipeRefreshLayout swipeLayout;
	public static int messagesCount = 0;

	public MessagesScrollListener(ListView listView, ExampleItem item, SwipeRefreshLayout swipeLayout) {
		this.listView = listView;
		this.mItem = item;
		this.swipeLayout = swipeLayout;
		task = new GetMessagesTask(listView, mItem, swipeLayout);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (firstVisibleItem == 0 && task.getStatus() != Status.RUNNING
				&& visibleItemCount > 0) {
			task = new GetMessagesTask(listView, mItem, swipeLayout);
			task.execute();
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

}
