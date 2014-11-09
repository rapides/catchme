package com.catchme.contactlist;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.catchme.R;
import com.catchme.itemdetails.ItemDetailsFragment;
import com.catchme.mapcontent.ItemMapFragment;
import com.catchme.messages.MessagesFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class ItemListActivity extends FragmentActivity implements
		ItemListFragment.Callbacks {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;
	//ViewPagerAdapter pagerAdapter;
	//ViewPager viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_list);
		//final ActionBar actionBar = getActionBar();
		//actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// actionBar.setDisplayShowHomeEnabled(false);
		// actionBar.setDisplayShowTitleEnabled(false);
		if (findViewById(R.id.item_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			((ItemListFragment) getSupportFragmentManager().findFragmentById(
					R.id.item_list)).setActivateOnItemClick(true);
		}
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this).build();
		ImageLoader.getInstance().init(config);
		
		//pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
		//viewPager = (ViewPager) findViewById(R.id.main_pager);
		//viewPager.setAdapter(pagerAdapter);
		//actionBar.addTab(actionBar.newTab()
		//		.setText(R.string.tab_title_messages)
		//		.setTabListener(new ActionBarListener(viewPager)));
		//actionBar.addTab(actionBar.newTab()
		//		.setText(R.string.tab_title_contacts)
		//		.setTabListener(new ActionBarListener(viewPager)));
		//actionBar.addTab(actionBar.newTab().setText(R.string.tab_title_map)
		//		.setTabListener(new ActionBarListener(viewPager)));

		//viewPager.setOnPageChangeListener(new ViewPagerListener(actionBar));
		//viewPager.setOffscreenPageLimit(2);
		//viewPager.setCurrentItem(1);
	}

	/**
	 * Callback method from {@link ItemListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(long id) {
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putLong(ItemDetailsFragment.ARG_ITEM_ID, id);
			ItemMapFragment fragment = new ItemMapFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.item_detail_container, fragment).commit();

			Bundle arguments2 = new Bundle();
			arguments2.putLong(ItemDetailsFragment.ARG_ITEM_ID, id);
			MessagesFragment fragment2 = new MessagesFragment();
			fragment2.setArguments(arguments2);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.messages_list_container, fragment2).commit();

		} else {
			Bundle arguments = new Bundle();
			arguments.putLong(ItemDetailsFragment.ARG_ITEM_ID, id);
			/*Intent intent = new Intent(this, ItemDetailsActivity.class);
			intent.putExtras(arguments);
			startActivity(intent);*/
			ItemDetailsFragment frag = new ItemDetailsFragment();
			//MessagesFragment frag = new MessagesFragment();
			frag.setArguments(arguments);
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.replace(R.id.main_fragment_container, frag);
			transaction.addToBackStack(null);
			transaction.commit();
			/*viewPager.setCurrentItem(0);
			MessagesFragment msgFrag = (MessagesFragment) viewPager
					.getAdapter().instantiateItem(viewPager, 0);
			msgFrag.updateView(id);
			ItemMapFragment mapFrag = (ItemMapFragment) viewPager
					.getAdapter().instantiateItem(viewPager, 2);
			mapFrag.updateView(id);*/
		}
	}

	/*@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if (viewPager.getCurrentItem() == 0) {
				viewPager.setCurrentItem(1);
				return true;
			} else if (viewPager.getCurrentItem() == 2) {
				viewPager.setCurrentItem(1);
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}*/

	
}
