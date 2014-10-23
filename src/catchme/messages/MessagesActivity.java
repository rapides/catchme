package catchme.messages;

import catchme.contactlist.ItemListActivity;
import catchme.contactlist.ItemListFragment;
import catchme.exampleObjects.ExampleContent;
import cycki.catchme.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

public class MessagesActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_list);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		if (savedInstanceState == null) {
			// Create the detail fragment and add it to the activity
			// using a fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putLong(MessagesFragment.ARG_ITEM_ID, getIntent()
					.getLongExtra(ItemListFragment.ARG_ITEM_ID, -1));
			MessagesFragment fragment = new MessagesFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.messages_list_container, fragment).commit();

			setTitle(""
					+ ExampleContent.ITEM_MAP.get(getIntent().getLongExtra(
							ItemListFragment.ARG_ITEM_ID, -1)));
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpTo(this,
					new Intent(this, ItemListActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
