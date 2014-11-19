package com.catchme.contactlist.listeners;

import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.catchme.contactlist.CustomListAdapter;
import com.catchme.contactlist.DrawerMenuAdapter;
import com.catchme.contactlist.asynctasks.GetContactsTask;
import com.catchme.contactlist.asynctasks.LoginTask;
import com.catchme.exampleObjects.ExampleContent;

public class DrawerOnItemClickListener implements OnItemClickListener {
	private DrawerLayout drawerLayout;
	private ListView drawerList;
	private ListView listView;
	private SwipeRefreshLayout swipeLayout;

	public DrawerOnItemClickListener(DrawerLayout drawerLayout,
			ListView drawerList, ListView listView,
			SwipeRefreshLayout swipeLayout) {
		super();
		this.drawerLayout = drawerLayout;
		this.drawerList = drawerList;
		this.listView = listView;
		this.swipeLayout = swipeLayout;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (position == 1) {
			new GetContactsTask(swipeLayout, (CustomListAdapter) listView.getAdapter())
					.execute(ExampleContent.currentUser.getToken());

		} else if (position == 4) {
			new LoginTask((DrawerMenuAdapter) drawerList.getAdapter(),
					swipeLayout).execute("mailczeslawa@cycki.pl", "appleseed");
		}
		if (position != 0) {
			drawerLayout.closeDrawer(drawerList);
		}
	}

}
