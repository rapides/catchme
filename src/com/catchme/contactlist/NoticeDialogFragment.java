package com.catchme.contactlist;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.catchme.R;
import com.catchme.contactlist.interfaces.OnAddContactCompletedListener;
import com.catchme.contactlist.listeners.DialogAddContactClickListener;

public class NoticeDialogFragment extends DialogFragment {
	private Context context;
	private OnAddContactCompletedListener listener;

	public NoticeDialogFragment(Context context,
			OnAddContactCompletedListener listener) {
		super();
		this.context = context;
		this.listener = listener;
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
						new DialogAddContactClickListener(context, dialogView,
								listener))
				.setNegativeButton(
						R.string.cancel,
						new DialogAddContactClickListener(context, dialogView,
								listener));
		return builder.create();
	}

}