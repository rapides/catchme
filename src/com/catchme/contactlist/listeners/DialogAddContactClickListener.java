package com.catchme.contactlist.listeners;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.catchme.R;
import com.catchme.contactlist.ItemListActivity;
import com.catchme.contactlist.asynctasks.AddContactTask;
import com.catchme.contactlist.interfaces.OnAddContactCompletedListener;

public class DialogAddContactClickListener implements OnClickListener {
	View dialogView;
	SwipeRefreshLayout swipeLayout;
	Context context;
	private OnAddContactCompletedListener listener;

	public DialogAddContactClickListener(Context context, View dialogView,
			OnAddContactCompletedListener listener) {
		this.dialogView = dialogView;
		this.context = context;
		this.listener = listener;
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if (which == DialogInterface.BUTTON_POSITIVE) {
			EditText txt = (EditText) dialogView
					.findViewById(R.id.new_contact_email);
			String email = txt.getText().toString();
			if (isEmailValid(email)) {
				new AddContactTask(context, listener).execute(ItemListActivity
						.getLoggedUser(context).getToken(), email);
			} else {
				Toast.makeText(context,
						context.getResources().getString(R.string.bad_email),
						Toast.LENGTH_SHORT).show();
			}
		} else if (which == DialogInterface.BUTTON_NEGATIVE) {
			dialog.cancel();
		}
	}

	private boolean isEmailValid(String email) {
		if (email == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
		}
	}

}
