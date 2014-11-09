package com.catchme.contactlist;

import com.catchme.R;
import com.catchme.exampleObjects.ExampleContent;
import com.catchme.exampleObjects.ExampleContent.ExampleItem;
import com.catchme.mapcontent.ItemMapFragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

/**
 * A list fragment representing a list of Items. This fragment also supports
 * tablet devices by allowing list items to be given an 'activated' state upon
 * selection. This helps indicate which item is currently being viewed in a
 * {@link ItemMapFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class ItemListFragment extends Fragment implements OnClickListener,
		OnQueryTextListener {

	private static final String STATE_ACTIVATED_POSITION = "activated_position";
	private final int UNDERLINE_HEIGHT = 3;
	private final int UNDERLINE_HEIGHT_BIG = 8;
	public ListView listView;
	private Button btnAll;
	private ImageButton btnSent;
	private ImageButton btnReceived;
	private ImageButton btnAccepted;
	private View btnAllUnderline;
	private View btnSentUnderline;
	private View btnReceivedUnderline;
	private View btnAcceptedUnderline;
	private SearchView searchView;
	/**
	 * The fragment's current callback object, which is notified of list item
	 * clicks.
	 */
	private Callbacks mCallbacks = sDummyCallbacks;

	/**
	 * The current activated item position. Only used on tablets.
	 */
	private int mActivatedPosition = ListView.INVALID_POSITION;

	public interface Callbacks {

		public void onItemSelected(long id);

	}

	private static Callbacks sDummyCallbacks = new Callbacks() {
		@Override
		public void onItemSelected(long id) {
		}
	};

	public ItemListFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_item_list,
				container, false);
		listView = (ListView) rootView.findViewById(R.id.list_item);
		btnAll = (Button) rootView.findViewById(R.id.list_all_button);
		btnSent = (ImageButton) rootView.findViewById(R.id.list_sent_button);
		btnReceived = (ImageButton) rootView
				.findViewById(R.id.list_received_button);
		btnAccepted = (ImageButton) rootView
				.findViewById(R.id.list_accepted_button);
		btnAllUnderline = rootView.findViewById(R.id.list_all_underline);
		btnReceivedUnderline = rootView
				.findViewById(R.id.list_received_underline);
		btnAcceptedUnderline = rootView
				.findViewById(R.id.list_accepted_underline);
		btnSentUnderline = rootView.findViewById(R.id.list_sent_underline);
		btnAll.setOnClickListener(this);
		btnAccepted.setOnClickListener(this);
		btnSent.setOnClickListener(this);
		btnReceived.setOnClickListener(this);

		listView.setAdapter(new CustomListAdapter(getActivity(),
				ExampleContent.ITEMS));
		// new GetContactsTask().execute("tokenCyckitoken");
		// new GetTokenTask().execute("rapides+03@gmail.com","appleseed");
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {

				mCallbacks.onItemSelected(((ExampleItem) a
						.getItemAtPosition(position)).getId());

			}
		});
		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// Restore the previously serialized activated item position.
		if (savedInstanceState != null
				&& savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
			setActivatedPosition(savedInstanceState
					.getInt(STATE_ACTIVATED_POSITION));
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}

		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = sDummyCallbacks;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mActivatedPosition != AdapterView.INVALID_POSITION) {
			// Serialize and persist the activated item position.
			outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
		}

	}

	/**
	 * Turns on activate-on-click mode. When this mode is on, list items will be
	 * given the 'activated' state when touched.
	 */
	public void setActivateOnItemClick(boolean activateOnItemClick) {
		// When setting CHOICE_MODE_SINGLE, ListView will automatically
		// give items the 'activated' state when touched.
		getListView().setChoiceMode(
				activateOnItemClick ? AbsListView.CHOICE_MODE_SINGLE
						: AbsListView.CHOICE_MODE_NONE);
	}

	private void setActivatedPosition(int position) {
		if (position == AdapterView.INVALID_POSITION) {
			getListView().setItemChecked(mActivatedPosition, false);
		} else {
			getListView().setItemChecked(position, true);
		}

		mActivatedPosition = position;
	}

	private ListView getListView() {
		return listView;
	}

	@Override
	public void onClick(View v) {
		if (v == btnAll) {
			setUnderline(0);
			filterList(-1);
		} else if (v == btnAccepted) {
			setUnderline(1);
			filterList(ExampleItem.STATE_TYPE[0]);
		} else if (v == btnReceived) {
			setUnderline(2);
			filterList(ExampleItem.STATE_TYPE[1]);
		} else if (v == btnSent) {
			setUnderline(3);
			filterList(ExampleItem.STATE_TYPE[2]);
		}
	}

	private void setUnderline(int tabNumber) {
		LayoutParams paramsAll = (LayoutParams) btnAllUnderline
				.getLayoutParams();
		LayoutParams paramsSent = (LayoutParams) btnSentUnderline
				.getLayoutParams();
		LayoutParams paramsReceived = (LayoutParams) btnReceivedUnderline
				.getLayoutParams();
		LayoutParams paramsAccepted = (LayoutParams) btnAcceptedUnderline
				.getLayoutParams();

		if (tabNumber == 0) {
			paramsAll.height = UNDERLINE_HEIGHT_BIG;
			paramsAccepted.height = UNDERLINE_HEIGHT;
			paramsSent.height = UNDERLINE_HEIGHT;
			paramsReceived.height = UNDERLINE_HEIGHT;
		} else if (tabNumber == 1) {
			paramsAll.height = UNDERLINE_HEIGHT;
			paramsAccepted.height = UNDERLINE_HEIGHT_BIG;
			paramsSent.height = UNDERLINE_HEIGHT;
			paramsReceived.height = UNDERLINE_HEIGHT;
		} else if (tabNumber == 2) {
			paramsAll.height = UNDERLINE_HEIGHT;
			paramsAccepted.height = UNDERLINE_HEIGHT;
			paramsSent.height = UNDERLINE_HEIGHT;
			paramsReceived.height = UNDERLINE_HEIGHT_BIG;
		} else if (tabNumber == 3) {
			paramsAll.height = UNDERLINE_HEIGHT;
			paramsAccepted.height = UNDERLINE_HEIGHT;
			paramsSent.height = UNDERLINE_HEIGHT_BIG;
			paramsReceived.height = UNDERLINE_HEIGHT;
		}
		btnAllUnderline.setLayoutParams(paramsAll);
		btnReceivedUnderline.setLayoutParams(paramsReceived);
		btnSentUnderline.setLayoutParams(paramsSent);
	}

	private void filterList(int state) {
		if (state >= 0) {
			((CustomListAdapter) listView.getAdapter()).getFilter().filter(
					CustomListAdapter.SEARCHTYPES[0]
							+ CustomListAdapter.SEARCHCHAR + state);
		} else {
			((CustomListAdapter) listView.getAdapter()).getFilter()
					.filter(null);
		}
	}

	private void filterList(String newText) {
		((CustomListAdapter) listView.getAdapter()).getFilter().filter(
				CustomListAdapter.SEARCHTYPES[1] + CustomListAdapter.SEARCHCHAR
						+ newText);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.main, menu);
		MenuItem searchItem = menu.findItem(R.id.action_search);
		searchView = (SearchView) searchItem.getActionView();
		setupSearchView(searchItem);
		
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_sort:
			return true;
		case R.id.action_search:
			return true;
		case R.id.action_settings:
			return true;
		case R.id.action_overflow:
			openOverflowMenu();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	private void openOverflowMenu() {
		PopupMenu popup = new PopupMenu(getActivity(), getActivity().findViewById(R.id.action_overflow));
		MenuInflater inflater = popup.getMenuInflater();
		inflater.inflate(R.menu.menu_overflow, popup.getMenu());
		popup.show();

	}
	private void setupSearchView(MenuItem searchItem) {

		if (isAlwaysExpanded()) {
			searchView.setIconifiedByDefault(false);
		} else {
			searchItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM
					| MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
		}
		searchView.setOnQueryTextListener(this);
	}

	protected boolean isAlwaysExpanded() {
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		filterList(newText);
		setUnderline(0);
		return true;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		return false;
	}
	
	
}
