package com.catchme.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;

import com.catchme.R;
import com.catchme.contactlist.ItemListActivity;
import com.catchme.exampleObjects.ExampleItem;
import com.catchme.exampleObjects.ExampleItem.ContactStateType;
import com.catchme.exampleObjects.LoggedUser;
import com.google.gson.Gson;

public class ChangeStateButonListener implements OnClickListener {

	private Context context;
	private ExampleItem item;
	private ContactChangedState listener;

	public ChangeStateButonListener(Context context, ExampleItem item, ContactChangedState listener) {
		super();
		this.context = context;
		this.item = item;
		this.listener = listener;
	}

	@Override
	public void onClick(View v) {
		SharedPreferences preferences = context.getSharedPreferences(
				ItemListActivity.PREFERENCES, Context.MODE_PRIVATE);
		Gson gson = new Gson();
		String json = preferences.getString(ItemListActivity.USER, "");
		LoggedUser user = gson.fromJson(json, LoggedUser.class);
		String token = user.getToken();
		if (v == v.getRootView().findViewById(R.id.profile_accept)) {
			new ChangeContactStateTask(context, listener).execute(token,
					"" + item.getId(), "" + ContactStateType.ACCEPTED.getIntegerValue());
		} else if (v == v.getRootView().findViewById(R.id.profile_reject)) {
			new ChangeContactStateTask(context, listener).execute(token,
					"" + item.getId(), "" + ContactStateType.REJECTED.getIntegerValue());

		}
	}
}
