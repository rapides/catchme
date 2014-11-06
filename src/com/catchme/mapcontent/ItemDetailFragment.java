package com.catchme.mapcontent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.catchme.R;
import com.catchme.contactlist.ItemListFragment;
import com.catchme.exampleObjects.ExampleContent;
import com.catchme.exampleObjects.ExampleContent.ExampleItem;

public class ItemDetailFragment extends Fragment {
	MapView mapView;
	GoogleMap map;
	View rootView;

	private ExampleItem mItem;

	public ItemDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*
		 * if (getArguments() != null) { if
		 * (getArguments().containsKey(ItemListFragment.ARG_ITEM_ID)) { mItem =
		 * ExampleContent.ITEM_MAP.get(getArguments().getLong(
		 * ItemListFragment.ARG_ITEM_ID)); } } else { mItem =
		 * ExampleContent.ITEM_MAP.get(ItemListFragment.lastChoosedContactId); }
		 */

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_item_detail, container,
				false);
		mapView = (MapView) rootView.findViewById(R.id.mapview);
		mapView.onCreate(savedInstanceState);
		map = mapView.getMap();
		map.getUiSettings().setMyLocationButtonEnabled(true);
		map.setMyLocationEnabled(true);

		MapsInitializer.initialize(this.getActivity());

		if (mItem != null) {
			updateView(mItem.getId());
		} else {
			updateView(-1);
		}

		return rootView;
	}

	@Override
	public void onResume() {
		mapView.onResume();
		super.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mapView.onLowMemory();
	}

	public void updateView(long id) {
		mItem = ExampleContent.ITEM_MAP
				.get(ItemListFragment.lastChoosedContactId);
		if (id < 0) {
			((TextView) rootView.findViewById(R.id.item_detail))
					.setText("TODO");
			new GeocodeTask().execute("Wroclaw");
		} else {
			mItem = ExampleContent.ITEM_MAP.get(id);
			((TextView) rootView.findViewById(R.id.item_detail)).setText(mItem
					.getName());
			new GeocodeTask().execute(mItem.getCity());
		}
	}

	public String GET(String url) {
		InputStream inputStream = null;

		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
			inputStream = httpResponse.getEntity().getContent();
			return convertInputStreamToString(inputStream);
		} catch (Exception e) {
		}

		return "";
	}

	public String convertInputStreamToString(InputStream in) throws IOException {
		InputStreamReader is = new InputStreamReader(in);
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(is);
		String read = br.readLine();

		while (read != null) {
			sb.append(read);
			read = br.readLine();
		}

		return sb.toString();
	}

	private class GeocodeTask extends AsyncTask<String, Void, Location> {

		@Override
		protected Location doInBackground(String... params) {
			String query = "http://maps.googleapis.com/maps/api/geocode/json?address="
					+ params[0] + "&sensor=true";

			String result = GET(query);
			JSONObject json;
			Location l = new Location("Google Maps");
			try {
				json = new JSONObject(result);
				JSONArray articles = json.getJSONArray("results");

				l.setLatitude(articles.getJSONObject(0)
						.getJSONObject("geometry").getJSONObject("location")
						.getDouble("lat"));
				l.setLongitude(articles.getJSONObject(0)
						.getJSONObject("geometry").getJSONObject("location")
						.getDouble("lng"));
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return l;
		}

		@Override
		protected void onPostExecute(Location address) {
			LatLng location = new LatLng(address.getLatitude(),
					address.getLongitude());
			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
					location, 10);
			map.clear();
			map.animateCamera(cameraUpdate);
			map.addMarker(new MarkerOptions().title(mItem.getName())
					.snippet("Last seen: somewhen").position(location));
			
		}
	}
}
