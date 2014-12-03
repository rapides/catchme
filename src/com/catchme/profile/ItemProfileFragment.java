package com.catchme.profile;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.catchme.R;
import com.catchme.connections.ServerConnection;
import com.catchme.connections.ServerRequests;
import com.catchme.contactlist.ItemListActivity;
import com.catchme.exampleObjects.ExampleContent;
import com.catchme.exampleObjects.ExampleContent.ExampleItem;
import com.catchme.exampleObjects.ExampleContent.LoggedUser;
import com.catchme.exampleObjects.ExampleContent.ExampleItem.ContactStateType;
import com.catchme.itemdetails.ItemDetailsFragment;
import com.catchme.utils.FloatingActionButton;
import com.catchme.utils.RoundedImageView;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ItemProfileFragment extends Fragment implements
		ContactChangedState, ImageUploadingListener {
	private View rootView;
	private ExampleItem item;
	private boolean isLoggedUser;
	public static final int PICK_IMAGE = 0;

	public ItemProfileFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater
				.inflate(R.layout.fragment_profile, container, false);

		if (getArguments() == null) {
			isLoggedUser = true;
			Gson gson = new Gson();
			String json = getActivity().getSharedPreferences(
					ItemListActivity.PREFERENCES, Context.MODE_PRIVATE)
					.getString(ItemListActivity.USER, "");
			item = gson.fromJson(json, LoggedUser.class);
		} else {
			isLoggedUser = false;
			long itemId = getArguments().getLong(
					ItemDetailsFragment.ARG_ITEM_ID);
			item = ExampleContent.ITEM_MAP.get(itemId);
		}

		TextView txtName = (TextView) rootView.findViewById(R.id.profile_name);
		TextView txtSurname = (TextView) rootView
				.findViewById(R.id.profile_surname);
		TextView txtEmail = (TextView) rootView
				.findViewById(R.id.profile_email);
		RoundedImageView image = (RoundedImageView) rootView
				.findViewById(R.id.profile_image);
		FloatingActionButton fab = (FloatingActionButton) rootView
				.findViewById(R.id.profile_floating_action_button);
		RelativeLayout buttonsContainer = (RelativeLayout) rootView
				.findViewById(R.id.profile_state_buttons_container);

		txtName.setText(item.getName());
		txtSurname.setText(item.getSurname());
		txtEmail.setText(item.getEmail());
		ImageLoader.getInstance().displayImage(item.getLargeImage(), image);
		if (isLoggedUser) {
			fab.setVisibility(View.VISIBLE);
			fab.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setType("image/*");
					intent.setAction(Intent.ACTION_GET_CONTENT);
					startActivityForResult(
							Intent.createChooser(intent, "Select Picture"),
							PICK_IMAGE);
				}
			});
			buttonsContainer.setVisibility(View.GONE);
			setHasOptionsMenu(true);
		} else {
			fab.setVisibility(View.GONE);
			if (item.getState() == ContactStateType.ACCEPTED) {
				buttonsContainer.setVisibility(View.GONE);
			} else {
				buttonsContainer.setVisibility(View.VISIBLE);
				Button accept = (Button) rootView
						.findViewById(R.id.profile_accept);
				Button reject = (Button) rootView
						.findViewById(R.id.profile_reject);
				accept.setOnClickListener(new ChangeStateButonListener(
						getActivity(), item, this));
				reject.setOnClickListener(new ChangeStateButonListener(
						getActivity(), item, this));
				setHasOptionsMenu(true);
			}
		}
		return rootView;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home: {
			getActivity().dispatchKeyEvent(
					new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
			getActivity().onBackPressed();
			return true;
		}
		}
		return super.onOptionsItemSelected(item);

	}

	@Override
	public void contactChangedState(ContactStateType newState) {
		if (newState != null) {

			if (newState == ContactStateType.ACCEPTED) {
				Toast.makeText(getActivity(),
						"ItemProfileFragment: Contact Accepted",
						Toast.LENGTH_SHORT).show();
			} else if (newState == ContactStateType.REJECTED) {
				Toast.makeText(getActivity(),
						"ItemProfileFragment: Contact Rejected",
						Toast.LENGTH_SHORT).show();
			}
			getActivity().dispatchKeyEvent(
					new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
			getActivity().onBackPressed();
		} else {
			Toast.makeText(getActivity(),
					"ItemProfileFragment: Server problem, try again later.",
					Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PICK_IMAGE && data != null && data.getData() != null) {
			Uri _uri = data.getData();
			
			// User had pick an image.
			Cursor cursor = getActivity()
					.getContentResolver()
					.query(_uri,
							new String[] { android.provider.MediaStore.Images.ImageColumns.DATA },
							null, null, null);
			cursor.moveToFirst();

			// Link to the image
			final String imageFilePath = cursor.getString(0);
			cursor.close();
			new UpdateAvatarTask(this.getActivity(), this).execute(
					((LoggedUser) item).getToken(), imageFilePath);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPreUpdate() {
		Toast.makeText(getActivity(), "Started", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onProgressUpdate(int value) {
		// TODO no idea how it work
	}

	@Override
	public void onImageUploaded() {
		// TODO refreshImages in navigation drawer and main image
		Toast.makeText(getActivity(), "Done", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onImageUploadError(ArrayList<String> errors) {
		Toast.makeText(getActivity(), errors.toString(), Toast.LENGTH_SHORT).show();
	}
}
