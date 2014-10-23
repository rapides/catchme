package catchme.contactlist;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import catchme.mapcontent.ItemDetailActivity;
import catchme.mapcontent.ItemDetailFragment;
import catchme.messages.MessagesActivity;
import catchme.messages.MessagesFragment;
import cycki.catchme.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


public class ItemListActivity extends FragmentActivity implements
		ItemListFragment.Callbacks {

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

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((ItemListFragment) getSupportFragmentManager().findFragmentById(
					R.id.item_list)).setActivateOnItemClick(true);
		}
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this).build();
		ImageLoader.getInstance().init(config);
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
			arguments.putLong(ItemListFragment.ARG_ITEM_ID, id);
			ItemDetailFragment fragment = new ItemDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.item_detail_container, fragment).commit();

			Bundle arguments2 = new Bundle();
			arguments2.putLong(MessagesFragment.ARG_ITEM_ID, id);
			MessagesFragment fragment2 = new MessagesFragment();
			fragment2.setArguments(arguments2);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.messages_list_container, fragment2).commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, MessagesActivity.class);
			detailIntent.putExtra(ItemListFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
		}
	}

	@Override
	public void onItemSwipedLeft(long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemSwipedRight(long id) {
		// TODO Auto-generated method stub

	}

}
