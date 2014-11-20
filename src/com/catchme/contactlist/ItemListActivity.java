package com.catchme.contactlist;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.Toast;

import com.catchme.R;
import com.catchme.exampleObjects.ExampleContent;
import com.catchme.exampleObjects.ExampleContent.ExampleItem;
import com.catchme.itemdetails.ItemDetailsFragment;
import com.catchme.locationServices.LocationReceiver;
import com.catchme.mapcontent.ItemMapFragment;
import com.catchme.messages.MessagesFragment;
import com.commonsware.cwac.locpoll.LocationPoller;
import com.commonsware.cwac.locpoll.LocationPollerParameter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ItemListActivity extends FragmentActivity implements
		ItemListFragment.Callbacks {

	public static final String USER_TOKEN = "user_token";
	public final static String PREFERENCES = "com.catchme";
	public static final String USER_EMAIL = "user_email";
	public static final String USER_PASSWORD = "user_password";
	private static final int INTERVAL = 300000;//ms
	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_list);
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

		ItemListFragment firstFragment = new ItemListFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.main_fragment_container, firstFragment).commit();

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		
	    //LocationRecorder locationRecorder = new LocationRecorder(getApplicationContext());
	    //locationRecorder.startRecording();
		 
		AlarmManager alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
		Intent i = new Intent(this, LocationPoller.class);

		Bundle bundle = new Bundle();
		LocationPollerParameter parameter = new LocationPollerParameter(bundle);
		parameter.setIntentToBroadcastOnCompletion(new Intent(this,
				LocationReceiver.class));
		parameter.setProviders(new String[] { LocationManager.GPS_PROVIDER,
				LocationManager.NETWORK_PROVIDER });
		parameter.setTimeout(20000);
		i.putExtras(bundle);
		
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
		alarmMgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
				SystemClock.elapsedRealtime(), INTERVAL, pi);
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
			ExampleItem item = ExampleContent.ITEM_MAP.get(id);
			if (item.getState() == ExampleItem.STATE_TYPE[0]) {
				Bundle arguments = new Bundle();
				arguments.putLong(ItemDetailsFragment.ARG_ITEM_ID, id);
				ItemDetailsFragment frag = new ItemDetailsFragment();
				frag.setArguments(arguments);
				FragmentTransaction transaction = getSupportFragmentManager()
						.beginTransaction();
				transaction.setCustomAnimations(android.R.anim.fade_in,
						android.R.anim.fade_out);
				transaction.replace(R.id.main_fragment_container, frag);
				transaction.addToBackStack(null);
				transaction.commit();

				setTitle(ExampleContent.ITEM_MAP.get(id).getFullName());
			} else if (item.getState() == ExampleItem.STATE_TYPE[1]) {
				Toast.makeText(this, "state 1", 0).show();
			} else if (item.getState() == ExampleItem.STATE_TYPE[2]) {
				Toast.makeText(this, "state 2", 0).show();
			}

		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setTitle(getResources().getString(R.string.app_name));
			// getActionBar().setDisplayHomeAsUpEnabled(false);
		}
		return super.onKeyDown(keyCode, event);
	}

}
