package com.catchme.contactlist.listeners;

import com.catchme.R;
import com.catchme.contactlist.asynctasks.AddContactTask;
import com.catchme.exampleObjects.ExampleContent;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class DialogAddContactClickListener implements OnClickListener {
	View dialogView;
	SwipeRefreshLayout swipeLayout;
	Context context;

	public DialogAddContactClickListener(View dialogView,
			SwipeRefreshLayout swipeLayout) {
		this.dialogView = dialogView;
		this.swipeLayout = swipeLayout;
		this.context = swipeLayout.getContext();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if (which == DialogInterface.BUTTON_POSITIVE) {
			EditText txt = (EditText) dialogView
					.findViewById(R.id.new_contact_email);
			String email = txt.getText().toString();
			if (isEmailValid(email)) {
				new AddContactTask(swipeLayout).execute(
						ExampleContent.currentUser.getToken(), email);
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
