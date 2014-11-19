package com.catchme.profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.catchme.R;
import com.catchme.exampleObjects.ExampleContent;
import com.catchme.exampleObjects.ExampleContent.ExampleItem;
import com.catchme.itemdetails.ItemDetailsFragment;
import com.catchme.utils.RoundedImageView;

public class ItemProfileFragment extends Fragment {
	private View rootView;
	private ExampleItem item;

	public ItemProfileFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater
				.inflate(R.layout.fragment_profile, container, false);

		long itemId = getArguments().getLong(ItemDetailsFragment.ARG_ITEM_ID);
		item = ExampleContent.ITEM_MAP.get(itemId);
		TextView txtName = (TextView) rootView.findViewById(R.id.profile_name);
		TextView txtSurname = (TextView) rootView
				.findViewById(R.id.profile_surname);
		TextView txtEmail = (TextView) rootView
				.findViewById(R.id.profile_email);
		RoundedImageView image = (RoundedImageView) rootView
				.findViewById(R.id.profile_image);

		txtName.setText(item.getName());
		txtSurname.setText(item.getSurname());
		txtEmail.setText(item.getEmail());
		image.setImageResource(item.getImageResource());

		return rootView;
	}
}
