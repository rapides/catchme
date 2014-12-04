package com.catchme.mapcontent;

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

import com.catchme.R;
import com.catchme.connections.ServerConnection;
import com.catchme.exampleObjects.ExampleContent;
import com.catchme.exampleObjects.ExampleItem;
import com.catchme.itemdetails.ItemDetailsFragment;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ItemMapFragment extends Fragment {
	MapView mapView;
	GoogleMap map;
	View rootView;

	private long itemId;
	private ExampleItem mItem;

	public ItemMapFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		itemId = getArguments().getLong(ItemDetailsFragment.ARG_ITEM_ID);
		mItem = ExampleContent.ITEM_MAP.get(itemId);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_item_map, container,
				false);
		mapView = (MapView) rootView.findViewById(R.id.mapview);
		mapView.onCreate(savedInstanceState);
		map = mapView.getMap();
		//map.getUiSettings().setMyLocationButtonEnabled(true);
		//map.setMyLocationEnabled(true);

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
		if (id < 0) {
			((TextView) rootView.findViewById(R.id.item_detail))
					.setText("TODO");
			new GeocodeTask().execute("Wroclaw");
		} else {
			((TextView) rootView.findViewById(R.id.item_detail)).setText(mItem
					.getFullName());
			// new GeocodeTask().execute(mItem.getCity());
		}
	}

	

	private class GeocodeTask extends AsyncTask<String, Void, Location> {

		@Override
		protected Location doInBackground(String... params) {
			String query = "http://maps.googleapis.com/maps/api/geocode/json?address="
					+ params[0] + "&sensor=true";

			 
			JSONObject json = ServerConnection.GET(query, null);;
			Location l = new Location("Google Maps");
			try {
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
			map.addMarker(new MarkerOptions().title(mItem.getFullName())
					.snippet("Last seen: somewhen").position(location));

		}
	}
}
