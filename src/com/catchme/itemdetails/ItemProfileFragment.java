package com.catchme.itemdetails;

import com.catchme.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ItemProfileFragment extends Fragment {
	private View rootView;
	private GifMovieView loader;

	public ItemProfileFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater
				.inflate(R.layout.fragment_profile, container, false);
		
		return rootView;
	}
}
