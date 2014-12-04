package com.catchme.contactlist;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;

import com.catchme.R;
import com.catchme.contactlist.listeners.DialogAddContactClickListener;
import com.catchme.exampleObjects.*;

public class NoticeDialogFragment extends DialogFragment {

	private SwipeRefreshLayout swipeLayout;
	private LoggedUser user;

	public NoticeDialogFragment(SwipeRefreshLayout swipeLayout, LoggedUser user) {
		super();
		this.swipeLayout = swipeLayout;
		this.user = user;
	}

	@SuppressLint("InflateParams")
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View dialogView = inflater.inflate(R.layout.dialog_new_contact, null);
		builder.setView(dialogView)
				.setPositiveButton(
						R.string.new_contact_add_button,
						new DialogAddContactClickListener(dialogView,
								swipeLayout, user))
				.setNegativeButton(
						R.string.cancel,
						new DialogAddContactClickListener(dialogView,
								swipeLayout, user));
		return builder.create();
	}

}