package com.catchme.contactlist;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.LongSparseArray;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

import com.catchme.R;
import com.catchme.contactlist.asynctasks.GetContactsTask;
import com.catchme.contactlist.interfaces.OnAddContactCompletedListener;
import com.catchme.contactlist.interfaces.OnGetContactCompletedListener;
import com.catchme.contactlist.listeners.DrawerOnItemClickListener;
import com.catchme.contactlist.listeners.FloatingActionButtonListener;
import com.catchme.contactlist.listeners.SwipeLayoutOnRefreshListener;
import com.catchme.database.model.ExampleItem;
import com.catchme.database.model.ExampleItem.ContactStateType;
import com.catchme.database.model.LoggedUser;
import com.catchme.itemdetails.ItemDetailsFragment;
import com.catchme.messages.MessagesRefreshService;
import com.catchme.utils.FloatingActionButton;

@SuppressWarnings("deprecation")
public class ItemListFragment extends Fragment implements OnQueryTextListener,
		OnMenuItemClickListener, OnAddContactCompletedListener,
		OnGetContactCompletedListener {

	private static final String STATE_ACTIVATED_POSITION = "activated_position";
	public static final String SELECTED_FILTER = "filter";
	public ListView listView;
	private SearchView searchView;
	private SwipeRefreshLayout swipeLayout;
	private DrawerLayout drawerLayout;
	private ListView drawerList;
	private ActionBarDrawerToggle drawerToggle;
	private FloatingActionButton fab;
	private LoggedUser user;
	private int defaulFilter = ContactStateType.ACCEPTED.getIntegerValue();
	/**
	 * The fragment's current callback object, which is notified of list item
	 * clicks.
	 */
	private Callbacks mCallbacks = sDummyCallbacks;

	/**
	 * The current activated item position. Only used on tablets.
	 */
	private int mActivatedPosition = ListView.INVALID_POSITION;
	private PopupMenu popup;

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

		Intent messageIntent = new Intent(getActivity(),
				MessagesRefreshService.class);
		messageIntent.putExtra(MessagesRefreshService.REFRESH_TIME,
				MessagesRefreshService.MESSAGES_INTERVAL_SHORT);
		getActivity().startService(messageIntent);

		user = ItemListActivity.getLoggedUser(getActivity());
		listView = (ListView) rootView.findViewById(R.id.list_item);

		swipeLayout = (SwipeRefreshLayout) rootView
				.findViewById(R.id.swipe_container);
		drawerLayout = (DrawerLayout) rootView.findViewById(R.id.drawer_layout);
		drawerList = (ListView) rootView.findViewById(R.id.left_drawer);
		fab = (FloatingActionButton) rootView
				.findViewById(R.id.list_floating_action_button);

		listView.setAdapter(new CustomListAdapter(getActivity()));

		swipeLayout.setOnRefreshListener(new SwipeLayoutOnRefreshListener(
				getActivity().getApplicationContext(), this));
		swipeLayout.setColorSchemeResources(R.color.swipelayout_bar,
				R.color.swipelayout_color1, R.color.swipelayout_color2,
				R.color.swipelayout_color3);
		drawerList.setAdapter(new DrawerMenuAdapter(getActivity(), user));
		((DrawerMenuAdapter) drawerList.getAdapter()).notifyDataSetChanged();

		drawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				((DrawerMenuAdapter) drawerList.getAdapter())
						.notifyDataSetChanged();
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);

			}

		};

		drawerLayout.setDrawerListener(drawerToggle);
		drawerToggle.setDrawerIndicatorEnabled(true);
		drawerList.setOnItemClickListener(new DrawerOnItemClickListener(
				getActivity(), drawerLayout, drawerToggle, drawerList));
		filterList(defaulFilter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				drawerToggle.setDrawerIndicatorEnabled(false);
				mCallbacks.onItemSelected(listView
						.getItemIdAtPosition(position));
			}
		});
		fab.setOnClickListener(new FloatingActionButtonListener(getActivity(),
				this));

		getActivity().getActionBar().setTitle(
				getResources().getString(R.string.app_name));
		new GetContactsTask(this.getActivity(), this,
				ContactStateType.ACCEPTED).execute(user.getToken());
		new GetContactsTask(this.getActivity(), this,
				ContactStateType.SENT).execute(user.getToken());
		new GetContactsTask(this.getActivity(), this,
				ContactStateType.RECEIVED).execute(user.getToken());
		return rootView;
	}

	public void setListItemChecked(int position) {
		boolean isChecked = listView.getCheckedItemPositions().get(
				listView.getCheckedItemPositions().keyAt(position));
		listView.setItemChecked(position, !isChecked);
		// View itemView = getViewByPosition(position);

		String out = "";
		for (int i = 0; i < listView.getCheckedItemPositions().size(); i++) {
			out += ""
					+ listView.getCheckedItemPositions().keyAt(i)
					+ ":"
					+ listView.getCheckedItemPositions().get(
							listView.getCheckedItemPositions().keyAt(i)) + ", ";
		}
		System.out.println(out);
	}

	public View getViewByPosition(int pos) {
		final int firstListItemPosition = listView.getFirstVisiblePosition();
		final int lastListItemPosition = firstListItemPosition
				+ listView.getChildCount() - 1;

		if (pos < firstListItemPosition || pos > lastListItemPosition) {
			return listView.getAdapter().getView(pos, null, listView);
		} else {
			final int childIndex = pos - firstListItemPosition;
			return listView.getChildAt(childIndex);
		}
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
	public void onResume() {
		super.onResume();
		drawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}
		mCallbacks = (Callbacks) activity;
		super.onAttach(activity);
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

	private void filterList(int state) {
		if (listView != null && listView.getAdapter() != null) {
			((CustomListAdapter) listView.getAdapter()).getFilter().filter(
					"" + CustomListAdapter.SEARCHTYPES[1] + state);
		}
	}

	private void filterList(String newText) {
		if (listView != null && listView.getAdapter() != null) {
			((CustomListAdapter) listView.getAdapter()).getFilter().filter(
					CustomListAdapter.SEARCHTYPES[0] + newText);
		}

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		Bundle data = getActivity().getIntent().getExtras();
		if (data != null && data.containsKey(MessagesRefreshService.ITEM_ID)) {
			long itemId = data.getLong(MessagesRefreshService.ITEM_ID);
			Bundle arguments = new Bundle();
			arguments.putLong(ItemDetailsFragment.ARG_ITEM_ID, itemId);
			ItemDetailsFragment frag = new ItemDetailsFragment();
			frag.setArguments(arguments);
			FragmentTransaction transaction = getActivity()
					.getSupportFragmentManager().beginTransaction();
			transaction.setCustomAnimations(android.R.anim.fade_in,
					android.R.anim.fade_out);
			transaction.replace(R.id.main_fragment_container, frag);
			transaction.addToBackStack(null);
			transaction.commit();
		}
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

		switch (item.getItemId()) {
		case R.id.action_filter:
			openSortMenu();
			return true;
		case android.R.id.home: {
			if (drawerLayout.isDrawerVisible(drawerList)) {
				drawerLayout.closeDrawer(drawerList);
			} else if (this.isVisible()) {
				drawerLayout.openDrawer(drawerList);
			}
			return true;
		}
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	private void openSortMenu() {
		popup = new PopupMenu(getActivity(), getActivity().findViewById(
				R.id.action_filter));
		popup.getMenuInflater().inflate(R.menu.menu_sort, popup.getMenu());
		popup.setOnMenuItemClickListener(this);
		popup.getMenu().getItem(defaulFilter).setChecked(true);
		popup.show();
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_group_all:
			filterList(-1);
			defaulFilter = 0;
			return true;
		case R.id.menu_group_accepted:
			filterList(ContactStateType.ACCEPTED.getIntegerValue());
			defaulFilter = 1;
			return true;
		case R.id.menu_group_sent:
			filterList(ContactStateType.SENT.getIntegerValue());
			defaulFilter = 2;
			return true;
		case R.id.menu_group_received:
			filterList(ContactStateType.RECEIVED.getIntegerValue());
			defaulFilter = 3;
			return true;
		}
		return false;
	}

	/*
	 * private void openOverflowMenu() { PopupMenu popup = new
	 * PopupMenu(getActivity(), getActivity()
	 * .findViewById(R.id.action_overflow)); MenuInflater inflater =
	 * popup.getMenuInflater(); inflater.inflate(R.menu.menu_overflow,
	 * popup.getMenu()); popup.setOnMenuItemClickListener(this); popup.show(); }
	 */

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
		return true;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		return false;
	}

	@Override
	public void onAddContactSucceded() {
		Toast.makeText(getActivity(), "Add contact succeded: ",
				Toast.LENGTH_SHORT).show();
		swipeLayout.setRefreshing(false);
	}

	@Override
	public void onAddContactError(LongSparseArray<String> errors) {
		swipeLayout.setRefreshing(false);
		if (errors == null) {
			Toast.makeText(getActivity(),
					getResources().getString(R.string.err_no_internet),
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(getActivity(),
					"Add contact failed, server error\n" + errors,
					Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void onPreAddContact() {
		swipeLayout.setRefreshing(true);
	}

	@Override
	public void onPreGetContacts() {
		swipeLayout.setRefreshing(true);
	}

	@Override
	public void onGetContactsError(LongSparseArray<String> errors) {
		if (errors == null) {
			Toast.makeText(getActivity().getApplicationContext(),
					getResources().getString(R.string.err_no_internet),
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(getActivity().getApplicationContext(),
					"Refresh Failed, server error\n" + errors,
					Toast.LENGTH_SHORT).show();
		}
		swipeLayout.setRefreshing(false);
	}

	@Override
	public void onGetContactsSucceded(final ArrayList<ExampleItem> contactList) {
		((CustomListAdapter) listView.getAdapter()).notifyDataSetChanged();
		swipeLayout.setRefreshing(false);
	}
}
