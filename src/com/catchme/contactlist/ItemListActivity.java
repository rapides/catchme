package com.catchme.contactlist;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.BitmapFactory.Options;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.catchme.R;
import com.catchme.database.CatchmeDatabaseAdapter;
import com.catchme.database.model.ExampleItem;
import com.catchme.database.model.LoggedUser;
import com.catchme.database.model.ExampleItem.ContactStateType;
import com.catchme.itemdetails.ItemDetailsFragment;
import com.catchme.locationServices.LocationReceiver;
import com.catchme.loginregister.LoginFragment;
import com.catchme.loginregister.RegisterFragment;
import com.catchme.messages.MessagesRefreshService;
import com.catchme.profile.ItemProfileFragment;
import com.commonsware.cwac.locpoll.LocationPoller;
import com.commonsware.cwac.locpoll.LocationPollerParameter;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class ItemListActivity extends FragmentActivity implements
		ItemListFragment.Callbacks {

	public final static String PREFERENCES = "com.catchme";
	public static final String USER = "user";
	public static final String MODEL_VERSION = "model_version";
	public static final int CURRENT_VERSION = 3;
	private static final int GPS_INTERVAL = 300000;// ms
	public static final int NOTIFICATION_ID = 17;

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	// private boolean mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_list);

		SharedPreferences preferences = getSharedPreferences(
				ItemListActivity.PREFERENCES, Context.MODE_PRIVATE);
		Options decodingOptions = new Options();
		DisplayImageOptions m_options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.loader)
				.decodingOptions(decodingOptions).cacheOnDisk(true)
				.cacheInMemory(true).imageScaleType(ImageScaleType.NONE)
				.build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this).defaultDisplayImageOptions(m_options).build();
		ImageLoader.getInstance().init(config);

		if (preferences.getInt(MODEL_VERSION, -1) != CURRENT_VERSION) {
			removeLoggedUser(getApplicationContext());
		}

		if (preferences.contains(USER)) {
			if (getLoggedUser(getApplicationContext()).getToken() != null) {
				if (findViewById(R.id.item_detail_container) != null) {
					// mTwoPane = true;

					((ItemListFragment) getSupportFragmentManager()
							.findFragmentById(R.id.item_list))
							.setActivateOnItemClick(true);
				}
				ItemListFragment firstFragment = new ItemListFragment();
				getSupportFragmentManager().beginTransaction()
						.replace(R.id.main_fragment_container, firstFragment)
						.commit();
				// LocationRecorder locationRecorder = new
				// LocationRecorder(getApplicationContext());
				// locationRecorder.startRecording();

				// set up position listening
				AlarmManager alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
				Intent i = new Intent(this, LocationPoller.class);
				Bundle bundle = new Bundle();
				LocationPollerParameter parameter = new LocationPollerParameter(
						bundle);
				parameter.setIntentToBroadcastOnCompletion(new Intent(this,
						LocationReceiver.class));
				parameter.setProviders(new String[] {
						LocationManager.GPS_PROVIDER,
						LocationManager.NETWORK_PROVIDER });
				parameter.setTimeout(20000);
				i.putExtras(bundle);
				PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
				alarmMgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
						SystemClock.elapsedRealtime(), GPS_INTERVAL, pi);
				getActionBar().setDisplayHomeAsUpEnabled(true);
				getActionBar().setHomeButtonEnabled(true);
			} else {
				RegisterFragment registerFragment = new RegisterFragment();
				getSupportFragmentManager().beginTransaction()
						.replace(R.id.main_fragment_container, registerFragment)
						.commit();
			}
		} else {
			getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
					.edit()
					.putInt(ItemListActivity.MODEL_VERSION,
							ItemListActivity.CURRENT_VERSION).commit();
			LoginFragment loginFragment = new LoginFragment();
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.main_fragment_container, loginFragment)
					.commit();
		}
	}

	/**
	 * Callback method from {@link ItemListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(long id) {
		// if (mTwoPane) {
		// In two-pane mode, show the detail view in this activity by
		// adding or replacing the detail fragment using a
		// fragment transaction.
		// TODO handling big screens

		// } else {
		ExampleItem item = CatchmeDatabaseAdapter.getInstance(
				getApplicationContext()).getItem(id);
		if (item.getState() == ContactStateType.ACCEPTED) {
			Bundle arguments = new Bundle();
			arguments.putLong(ItemDetailsFragment.ARG_ITEM_ID, id);
			ItemDetailsFragment frag = new ItemDetailsFragment();
			frag.setArguments(arguments);
			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			transaction.setCustomAnimations(android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
			transaction.replace(R.id.main_fragment_container, frag);
			transaction.addToBackStack(null);
			transaction.commit();

		} else if (item.getState() == ContactStateType.RECEIVED) {
			Bundle arguments = new Bundle();
			arguments.putLong(ItemDetailsFragment.ARG_ITEM_ID, id);
			ItemProfileFragment frag = new ItemProfileFragment();
			frag.setArguments(arguments);
			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			transaction.setCustomAnimations(android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
			transaction.replace(R.id.main_fragment_container, frag);
			transaction.addToBackStack(null);
			transaction.commit();
		} else if (item.getState() == ContactStateType.SENT) {
			Toast.makeText(getApplicationContext(),
					"This type of contact? Not yet", Toast.LENGTH_SHORT).show();
		}

		// }
	}

	public static LoggedUser getLoggedUser(Context context) {

		SharedPreferences sharedpreferences = context.getSharedPreferences(
				ItemListActivity.PREFERENCES, Context.MODE_PRIVATE);
		String json = sharedpreferences.getString(ItemListActivity.USER, "");
		return new Gson().fromJson(json, LoggedUser.class);
	}

	public static void setLoggedUser(Context context, LoggedUser user) {
		SharedPreferences preferences = context.getSharedPreferences(
				ItemListActivity.PREFERENCES, Context.MODE_PRIVATE);
		if (user.getToken() == null) {
			user.setToken(getLoggedUser(context).getToken());
		}
		Editor e = preferences.edit();
		Gson gsonUser = new Gson();
		String json = gsonUser.toJson(user);
		e.putString(ItemListActivity.USER, json);
		e.commit();
	}

	public static void removeLoggedUser(Context context) {
		// context.stopService(new Intent(context,
		// MessagesRefreshService.class));
		SharedPreferences preferences = context.getSharedPreferences(
				ItemListActivity.PREFERENCES, Context.MODE_PRIVATE);
		Editor e = preferences.edit();
		e.remove(ItemListActivity.USER);
		e.commit();
	}

	@Override
	public void onPause() {
		stopService(new Intent(this, MessagesRefreshService.class));
		Intent messageIntent = new Intent(this, MessagesRefreshService.class);
		messageIntent.putExtra(MessagesRefreshService.REFRESH_TIME,
				MessagesRefreshService.MESSAGES_INTERVAL_LONG);
		startService(messageIntent);
		super.onPause();
	}

	@Override
	public void onResume() {
		stopService(new Intent(this, MessagesRefreshService.class));
		Intent messageIntent = new Intent(this, MessagesRefreshService.class);
		messageIntent.putExtra(MessagesRefreshService.REFRESH_TIME,
				MessagesRefreshService.MESSAGES_INTERVAL_SHORT);
		startService(messageIntent);
		NotificationManager nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		nMgr.cancelAll();
		super.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
