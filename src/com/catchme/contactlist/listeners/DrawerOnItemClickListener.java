package com.catchme.contactlist.listeners;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.catchme.R;
import com.catchme.contactlist.CustomListAdapter;
import com.catchme.contactlist.ItemListActivity;
import com.catchme.contactlist.ItemListFragment;
import com.catchme.contactlist.asynctasks.GetContactsTask;
import com.catchme.exampleObjects.ExampleContent;
import com.catchme.exampleObjects.ExampleContent.ExampleItem.ContactStateType;
import com.catchme.exampleObjects.ExampleContent.LoggedUser;
import com.catchme.loginregister.LoginFragment;
import com.catchme.profile.ItemProfileFragment;

public class DrawerOnItemClickListener implements OnItemClickListener {
	private DrawerLayout drawerLayout;
	private ListView drawerList;
	private ListView listView;
	private SwipeRefreshLayout swipeLayout;
	private Context context;
	private LoggedUser user;
	private ActionBarDrawerToggle drawerToggle;

	public DrawerOnItemClickListener(Context context,
			DrawerLayout drawerLayout, ActionBarDrawerToggle drawerToggle,
			ListView drawerList, ListView listView,
			SwipeRefreshLayout swipeLayout, LoggedUser user) {
		super();
		this.context = context;
		this.drawerLayout = drawerLayout;
		this.drawerList = drawerList;
		this.listView = listView;
		this.swipeLayout = swipeLayout;
		this.user = user;
		this.drawerToggle = drawerToggle;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (position == 1) {
			SharedPreferences preferences = context.getSharedPreferences(
					ItemListActivity.PREFERENCES, Context.MODE_PRIVATE);
			int val = preferences.getInt(ItemListFragment.SELECTED_FILTER, -1);
			new GetContactsTask(swipeLayout,
					(CustomListAdapter) listView.getAdapter(),
					ContactStateType.getStateType(val))
					.execute(user.getToken());

		} else if (position == 5) {
			SharedPreferences preferences = context.getSharedPreferences(
					ItemListActivity.PREFERENCES, Context.MODE_PRIVATE);
			Editor e = preferences.edit();
			e.remove(ItemListActivity.USER);
			e.commit();
			ExampleContent.clear();
			LoginFragment loginFragment = new LoginFragment();
			((FragmentActivity) context).getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.main_fragment_container, loginFragment)
					.commit();
			((Activity) context).getActionBar()
					.setDisplayHomeAsUpEnabled(false);
			((Activity) context).getActionBar().setHomeButtonEnabled(false);
		}
		drawerLayout.closeDrawer(drawerList);
		if (position == 0) {
			ItemProfileFragment frag = new ItemProfileFragment();
			FragmentTransaction transaction = ((FragmentActivity) context)
					.getSupportFragmentManager().beginTransaction();
			transaction.setCustomAnimations(android.R.anim.fade_in,
					android.R.anim.fade_out);
			transaction.replace(R.id.main_fragment_container, frag);
			transaction.addToBackStack(null);
			transaction.commit();
			drawerToggle.setDrawerIndicatorEnabled(false);
		}
	}

}
