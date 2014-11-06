package com.catchme.contactlist;

import com.catchme.R;
import com.catchme.exampleObjects.ExampleContent;
import com.catchme.mapcontent.ItemDetailFragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

/**
 * A list fragment representing a list of Items. This fragment also supports
 * tablet devices by allowing list items to be given an 'activated' state upon
 * selection. This helps indicate which item is currently being viewed in a
 * {@link ItemDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class ItemListFragment extends Fragment implements OnClickListener {

	private static final String STATE_ACTIVATED_POSITION = "activated_position";
	public static final String ARG_ITEM_ID = "item_id";
	private final int UNDERLINE_HEIGHT = 3;
	private final int UNDERLINE_HEIGHT_BIG = 8;
	public static long lastChoosedContactId;
	public ListView listView;
	ImageButton btnAll;
	ImageButton btnSent;
	ImageButton btnReceived;
	View btnAllUnderline;
	View btnSentUnderline;
	View btnReceivedUnderline;

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
		lastChoosedContactId = 0;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_item_list,
				container, false);
		listView = (ListView) rootView.findViewById(R.id.list_item);
		btnAll = (ImageButton) rootView.findViewById(R.id.list_all_button);
		btnSent = (ImageButton) rootView.findViewById(R.id.list_sent_button);
		btnReceived = (ImageButton) rootView
				.findViewById(R.id.list_received_button);
		btnAllUnderline = rootView.findViewById(R.id.list_all_underline);
		btnReceivedUnderline = rootView
				.findViewById(R.id.list_received_underline);
		btnSentUnderline = rootView.findViewById(R.id.list_sent_underline);
		btnAll.setOnClickListener(this);
		btnSent.setOnClickListener(this);
		btnReceived.setOnClickListener(this);
		listView.setAdapter(new CustomListAdapter(getActivity(),
				ExampleContent.ITEMS));

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {

				lastChoosedContactId = ExampleContent.ITEMS.get(position)
						.getId();
				mCallbacks.onItemSelected(ExampleContent.ITEMS.get(position)
						.getId());

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
		} else if (v == btnReceived) {
			setUnderline(1);
		} else if (v == btnSent) {
			setUnderline(2);
		}
	}

	private void setUnderline(int tabNumber) {
		LayoutParams paramsAll = (LayoutParams) btnAllUnderline
				.getLayoutParams();
		LayoutParams paramsSent = (LayoutParams) btnSentUnderline
				.getLayoutParams();
		LayoutParams paramsReceived = (LayoutParams) btnReceivedUnderline
				.getLayoutParams();

		if (tabNumber == 0) {
			paramsAll.height = UNDERLINE_HEIGHT_BIG;
			paramsSent.height = UNDERLINE_HEIGHT;
			paramsReceived.height = UNDERLINE_HEIGHT;
		} else if (tabNumber == 1) {
			paramsAll.height = UNDERLINE_HEIGHT;
			paramsSent.height = UNDERLINE_HEIGHT;
			paramsReceived.height = UNDERLINE_HEIGHT_BIG;
		} else if (tabNumber == 2) {
			paramsAll.height = UNDERLINE_HEIGHT;
			paramsSent.height = UNDERLINE_HEIGHT_BIG;
			paramsReceived.height = UNDERLINE_HEIGHT;
		}
		btnAllUnderline.setLayoutParams(paramsAll);
		btnReceivedUnderline.setLayoutParams(paramsReceived);
		btnSentUnderline.setLayoutParams(paramsSent);
	}

}
