package com.catchme.messages.listeners;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.widget.Toast;

public class MessagesRefreshListener implements OnRefreshListener {
	SwipeRefreshLayout swipeLayout;

	public MessagesRefreshListener(SwipeRefreshLayout swipeLayout) {
		this.swipeLayout = swipeLayout;
	}

	@Override
	public void onRefresh() {
		swipeLayout.setRefreshing(false);
		Toast.makeText(swipeLayout.getContext(),
				"MessageRefreshListener: no more messages", Toast.LENGTH_SHORT)
				.show();
	}
}
