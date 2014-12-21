package com.catchme.contactlist.listeners;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;

import com.catchme.contactlist.ItemListActivity;
import com.catchme.contactlist.asynctasks.GetContactsTask;
import com.catchme.contactlist.interfaces.OnGetContactCompletedListener;
import com.catchme.database.CatchmeDatabaseAdapter;
import com.catchme.database.model.ExampleItem.ContactStateType;

public class SwipeLayoutOnRefreshListener implements OnRefreshListener {
	private OnGetContactCompletedListener listener;
	private Context context;
	private CatchmeDatabaseAdapter dbAdapter;

	public SwipeLayoutOnRefreshListener(Context context,
			OnGetContactCompletedListener listener,
			CatchmeDatabaseAdapter dbAdapter) {
		super();
		this.context = context;
		this.listener = listener;
		this.dbAdapter = dbAdapter;
	}

	@Override
	public void onRefresh() {
		new GetContactsTask(context, listener, dbAdapter,
				ContactStateType.ACCEPTED).execute(ItemListActivity
				.getLoggedUser(context).getToken());
		new GetContactsTask(context, listener, dbAdapter,
				ContactStateType.RECEIVED).execute(ItemListActivity
				.getLoggedUser(context).getToken());
		new GetContactsTask(context, listener, dbAdapter,
				ContactStateType.SENT).execute(ItemListActivity
				.getLoggedUser(context).getToken());
		
	}
}
