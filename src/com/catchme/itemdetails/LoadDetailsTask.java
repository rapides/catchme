package com.catchme.itemdetails;

import android.os.AsyncTask;
import android.view.View;
import android.widget.RelativeLayout;

import com.catchme.utils.GifMovieView;

public class LoadDetailsTask extends AsyncTask<Void, Void, Void> {
	private GifMovieView loader;
	private RelativeLayout loaderContainer;

	public LoadDetailsTask(GifMovieView loader, RelativeLayout loaderContainer) {
		this.loader = loader;
		this.loaderContainer = loaderContainer;
	}

	@Override
	protected void onPreExecute() {
		loader.setVisibility(View.VISIBLE);
		loaderContainer.setVisibility(View.VISIBLE);
	}

	@Override
	protected Void doInBackground(Void... params) {
		//try {
			//Thread.sleep((long) (Math.random() * 2000));
		//} catch (InterruptedException e) {
			//e.printStackTrace();
		//}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		loader.setVisibility(View.GONE);
		loaderContainer.setVisibility(View.GONE);
	}
}
