package com.catchme.loginregister;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.LongSparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.catchme.R;
import com.catchme.contactlist.ItemListFragment;
import com.catchme.exampleObjects.LoggedUser;
import com.catchme.loginregister.asynctasks.LoginRegisterInterface;
import com.catchme.loginregister.asynctasks.RegisterTask;
import com.catchme.profile.UpdateAvatarTask;
import com.catchme.utils.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class Register2Fragment extends Fragment implements OnClickListener,
		LoginRegisterInterface {
	public static final String EMAIL = "useremail";
	public static final String PASS = "password";
	public static final String CONFPASS = "confirmpassword";
	public static final int PICK_IMAGE = 0;
	
	private ProgressBar register_loading;
	private EditText  name, surname;
	private RoundedImageView image;
	private String email, pass, conf_pass;

	public Register2Fragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// inflate layout from xml
		View rootView = inflater.inflate(R.layout.fragment_register2, container,
				false);
		name = (EditText) rootView.findViewById(R.id.reg_name);
		surname = (EditText) rootView.findViewById(R.id.reg_surname);
		image = (RoundedImageView) rootView.findViewById(R.id.user_avatar);

		Bundle arguments = getArguments();
		email = arguments.getString(Register1Fragment.EMAIL);
		pass = arguments.getString(PASS);
		conf_pass = arguments.getString(CONFPASS);
		

		register_loading = (ProgressBar) rootView
				.findViewById(R.id.register_spinner);
		register_loading.setVisibility(View.GONE);
		Button reg = (Button) rootView.findViewById(R.id.register);
		reg.setOnClickListener(this);
		return rootView;
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(
				Intent.createChooser(intent, "Select Picture"),
				PICK_IMAGE);
		/*
		 * new RegisterTask(getActivity(), this).execute(
		 * name.getText().toString(), surname.getText().toString(), email, pass,
		 * conf_pass);
		 */
	}

	@Override
	public void onPreExecute() {
		register_loading.setVisibility(View.VISIBLE);
	}

	@Override
	public void onCompleted(LoggedUser user) {
		register_loading.setVisibility(View.GONE);

		Toast.makeText(getActivity(),
				"Success! Registered user: " + user.getFullName(),
				Toast.LENGTH_SHORT).show();
		ItemListFragment mainFragment = new ItemListFragment();
		// replace old view
		getActivity().getSupportFragmentManager().beginTransaction()
				.replace(R.id.main_fragment_container, mainFragment).commit();
		// adjust actionBar
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		getActivity().getActionBar().setHomeButtonEnabled(true);
	}

	@Override
	public void onError(LongSparseArray<String> errors) {
		register_loading.setVisibility(View.GONE);
		String errorsSring = "";
		if (errors != null) {
			for (int i = 0; i < errors.size(); i++) {
				errorsSring += errors.get(errors.keyAt(i)) + "\n";
			}
		}
		Toast.makeText(getActivity(), errorsSring, Toast.LENGTH_SHORT).show();
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PICK_IMAGE && data != null && data.getData() != null) {
			Uri uri = data.getData();
			String imageFilePath = null;
			if (uri.getScheme().equals("content")) {// fromGallery

				// User had pick an image.
				Cursor cursor = getActivity()
						.getContentResolver()
						.query(uri,
								new String[] { android.provider.MediaStore.Images.ImageColumns.DATA },
								null, null, null);
				cursor.moveToFirst();

				// Link to the image
				imageFilePath = cursor.getString(0);
				cursor.close();
			} else {
				imageFilePath = uri.getSchemeSpecificPart();
			}
			Toast.makeText(getActivity(), "DEBUG: " + "file://"+imageFilePath,
					Toast.LENGTH_LONG).show();

			ImageLoader.getInstance().displayImage("file://"+imageFilePath, image);
			/*new UpdateAvatarTask(this.getActivity(), this).execute(
					((LoggedUser) item).getToken(), imageFilePath);*/
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
