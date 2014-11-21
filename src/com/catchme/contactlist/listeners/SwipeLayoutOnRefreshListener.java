package com.catchme.contactlist.listeners;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.widget.ListView;

import com.catchme.contactlist.CustomListAdapter;
import com.catchme.contactlist.asynctasks.GetContactsTask;
import com.catchme.exampleObjects.ExampleContent;
import com.catchme.exampleObjects.ExampleContent.LoggedUser;

public class SwipeLayoutOnRefreshListener implements OnRefreshListener {
	SwipeRefreshLayout swipeLayout;
	ListView listView;
	private LoggedUser user;

	public SwipeLayoutOnRefreshListener(SwipeRefreshLayout swipeLayout,
			ListView listView, LoggedUser user) {
		super();
		this.swipeLayout = swipeLayout;
		this.listView = listView;
		this.user = user;
	}

	@Override
	public void onRefresh() {
		new GetContactsTask(swipeLayout, (CustomListAdapter) listView.getAdapter())
				.execute(user.getToken());
	}

}
