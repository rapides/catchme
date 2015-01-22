package com.catchme.profile;

import com.catchme.R;
import com.catchme.contactlist.ItemListActivity;
import com.catchme.database.model.ExampleItem;
import com.catchme.loginregister.Register1Fragment.watcher;
import com.catchme.profile.asynctasks.OnUpdateProfileListener;
import com.catchme.profile.asynctasks.UpdateProfileTask;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.LongSparseArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class SettingsFragment extends Fragment implements OnClickListener,
		OnUpdateProfileListener {
	private View rootView;
	private ExampleItem user;
	private EditText email, password, confPass;
	private ProgressBar settings_change;
	private Drawable originalBackground;

	public SettingsFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_settings, container,
				false);
		user = ItemListActivity.getLoggedUser(getActivity());
		settings_change = (ProgressBar) rootView
				.findViewById(R.id.settings_change);
		settings_change.setVisibility(View.GONE);
		email = (EditText) rootView.findViewById(R.id.settings_email);
		password = (EditText) rootView.findViewById(R.id.settings_password);
		confPass = (EditText) rootView.findViewById(R.id.settings_confPass);
		email.setText(user.getEmail(), EditText.BufferType.EDITABLE);
		originalBackground = email.getBackground();
		Button saveBtn = (Button) rootView.findViewById(R.id.settings_save);
		saveBtn.setOnClickListener(this);
		setHasOptionsMenu(true);

		return rootView;
	}

	@Override
	public void onClick(View v) {
		if (validate()) {
			new UpdateProfileTask(getActivity(), this).execute(ItemListActivity
					.getLoggedUser(getActivity()).getToken(), email.getText()
					.toString(), password.getText().toString(), confPass
					.getText().toString());
		}

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
	public void onPreUpdate() {
		settings_change.setVisibility(View.VISIBLE);
	}

	@Override
	public void onUpdateProfileCompleted() {
		settings_change.setVisibility(View.GONE);

		Toast.makeText(getActivity(),
				"Success! Edited user: " + user.getFullName(),
				Toast.LENGTH_SHORT).show();
		SettingsFragment frag = new SettingsFragment();
		FragmentTransaction transaction = getActivity()
				.getSupportFragmentManager().beginTransaction();
		transaction.setCustomAnimations(android.R.anim.fade_in,
				android.R.anim.fade_out);
		transaction.replace(R.id.main_fragment_container, frag);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	@Override
	public void onUpdateProfileError(LongSparseArray<String> errors) {
		settings_change.setVisibility(View.GONE);
		String errorsSring = "";
		if (errors != null) {
			for (int i = 0; i < errors.size(); i++) {
				errorsSring += errors.get(errors.keyAt(i)) + "\n";
			}
		}
		Toast.makeText(getActivity(), errorsSring, Toast.LENGTH_SHORT).show();
	}

	public class watcher implements TextWatcher {
		private EditText et;

		private watcher(EditText editText) {
			this.et = editText;
		}

		public void afterTextChanged(Editable s) {
			switch (et.getId()) {
			case R.id.reg_email: {
				email.setBackgroundDrawable(originalBackground);
			}
				break;

			case R.id.reg_pass: {
				password.setBackgroundDrawable(originalBackground);

			}
				break;
			case R.id.reg_pass_conf: {
				confPass.setBackgroundDrawable(originalBackground);

			}
				break;
			}
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}
	};

	public boolean validate() {

		int errCnt = 0;
		String errorsString = "";
		if (email.getText() == null || email.getText().toString().length() < 1) {
			email.setError("E-mail is required!");
			email.setBackgroundResource(R.drawable.error_frame);
			email.addTextChangedListener(new watcher(email));
			errorsString += "E-mail is required! \n";

			errCnt += 1;
		}
		if (email.getText().toString().length() > 0
				&& !android.util.Patterns.EMAIL_ADDRESS.matcher(
						email.getText().toString()).matches()) {
			email.setError("E-mail is incorrect!");
			email.setBackgroundResource(R.drawable.error_frame);
			email.addTextChangedListener(new watcher(email));
			errorsString += "E-mail is incorrect! \n";

			errCnt += 1;
		}

		if (password.getText() == null
				|| password.getText().toString().length() < 1) {
			password.setError("Password is required!");
			errorsString += "Password is required! \n";
			password.setBackgroundResource(R.drawable.error_frame);
			password.addTextChangedListener(new watcher(password));

			errCnt += 1;
		}
		if (password.getText() != null
				&& password.getText().toString().length() < 6) {
			password.setError("Password must be atleast 6 characters long!");
			password.setBackgroundResource(R.drawable.error_frame);
			errorsString += "Password must be atleast 6 characters long! \n";
			password.addTextChangedListener(new watcher(password));
			errCnt += 1;
		}
		if (confPass.getText() == null
				|| confPass.getText().toString().length() < 1) {
			confPass.setError("Password Confirm is required!");
			errorsString += "Password Confirm is required! \n";
			confPass.setBackgroundResource(R.drawable.error_frame);
			confPass.addTextChangedListener(new watcher(confPass));

			errCnt += 1;
		}
		if (confPass.getText() != null
				&& password.getText() != null
				&& password.getText().toString().length() >= 6
				&& !confPass.getText().toString()
						.equals(password.getText().toString())) {
			confPass.setError("Confirm Password is wrong!");
			errorsString += "Passwords do not match! ";
			confPass.setBackgroundResource(R.drawable.error_frame);
			confPass.addTextChangedListener(new watcher(confPass));

			errCnt += 1;
		}
		if (errCnt > 0) {
			Toast.makeText(getActivity(), errorsString, Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		return true;
	}

}
