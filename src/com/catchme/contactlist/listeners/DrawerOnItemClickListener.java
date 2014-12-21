package com.catchme.contactlist.listeners;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.catchme.R;
import com.catchme.contactlist.ItemListActivity;
import com.catchme.database.CatchmeDatabaseAdapter;
import com.catchme.loginregister.LoginFragment;
import com.catchme.messages.MessagesRefreshService;
import com.catchme.profile.ItemProfileFragment;

@SuppressWarnings("deprecation")
public class DrawerOnItemClickListener implements OnItemClickListener {
	private DrawerLayout drawerLayout;
	private ListView drawerList;
	private Context context;
	private ActionBarDrawerToggle drawerToggle;
	private CatchmeDatabaseAdapter dbAdapter;
	

	public DrawerOnItemClickListener(Context context,
			DrawerLayout drawerLayout, ActionBarDrawerToggle drawerToggle,
			ListView drawerList, CatchmeDatabaseAdapter dbAdapter) {
		super();
		this.context = context;
		this.drawerLayout = drawerLayout;
		this.drawerList = drawerList;
		this.drawerToggle = drawerToggle;
		this.dbAdapter = dbAdapter;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (position == 5) {
			ItemListActivity.removeLoggedUser(context);
			context.stopService(new Intent(context,
					MessagesRefreshService.class));
			dbAdapter.clear();
			LoginFragment loginFragment = new LoginFragment(dbAdapter);
			((FragmentActivity) context).getSupportFragmentManager()
					.beginTransaction().setCustomAnimations(android.R.anim.slide_in_left,
							android.R.anim.slide_out_right)
					.replace(R.id.main_fragment_container, loginFragment)
					.commit();
			((Activity) context).getActionBar()
					.setDisplayHomeAsUpEnabled(false);
			((Activity) context).getActionBar().setHomeButtonEnabled(false);
			
		}
		drawerLayout.closeDrawer(drawerList);
		if (position == 0) {
			ItemProfileFragment frag = new ItemProfileFragment(dbAdapter);
			FragmentTransaction transaction = ((FragmentActivity) context)
					.getSupportFragmentManager().beginTransaction();
			transaction.setCustomAnimations(android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
			transaction.replace(R.id.main_fragment_container, frag);
			transaction.addToBackStack(null);
			transaction.commit();
			drawerToggle.setDrawerIndicatorEnabled(false);
		}
	}
}
