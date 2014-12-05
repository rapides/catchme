package com.catchme.mapcontent;

import java.util.HashMap;

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
import android.widget.Toast;

import com.catchme.R;
import com.catchme.connections.ServerConnection;
import com.catchme.contactlist.ItemListActivity;
import com.catchme.exampleObjects.ExampleContent;
import com.catchme.exampleObjects.ExampleItem;
import com.catchme.exampleObjects.LoggedUser;
import com.catchme.itemdetails.ItemDetailsFragment;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ItemMapFragment extends Fragment implements LoadLocationsListener {
	MapView mapView;
	GoogleMap map;
	View rootView;

	private long itemId;
	private ExampleItem mItem;
	private LoggedUser user;

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
		user = ItemListActivity.getLoggedUser(getActivity());
		map.getUiSettings().setMyLocationButtonEnabled(true);
		map.setMyLocationEnabled(true);
		MapsInitializer.initialize(getActivity());

		((TextView) rootView.findViewById(R.id.item_detail)).setText(mItem
				.getFullName());
		ItemListActivity.getLoggedUser(getActivity());
		new LoadLocationsTask(getActivity(), this).execute(user.getToken(),
				"" + 10, "" + mItem.getId());
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

	private class GeocodeTask extends AsyncTask<String, Void, Location> {

		@Override
		protected Location doInBackground(String... params) {
			String query = "http://maps.googleapis.com/maps/api/geocode/json?address="
					+ params[0] + "&sensor=true";

			JSONObject json = ServerConnection.GET(query, null);
			;
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

	@Override
	public void locationsUpdated() {
		Location lastLocation = mItem.getLastLocation();
		if (lastLocation != null) {
			LatLng location = new LatLng(lastLocation.getLatitude(),
					lastLocation.getLongitude());
			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
					location, 10);
			map.clear();
			map.animateCamera(cameraUpdate);
			map.addMarker(new MarkerOptions()
					.title(mItem.getFullName())
					.snippet(
							"Last seen "
									+ getTimeAgo(System.currentTimeMillis()
											- lastLocation.getTime()))
					.position(location));
		}

	}

	private String getTimeAgo(long l) {
		int seconds = (int) (l / 1000);
		int minutes = seconds / 60;
		int hours = minutes / 60;
		int days = hours / 24;
		if (seconds < 60) {
			return seconds + " seconds ago";
		} else if (minutes < 60) {
			return minutes + " minutes ago";
		} else if (hours < 24) {
			return hours + " hours ago";
		} else {
			return days + " days ago";
		}
	}

	@Override
	public void locationsError(HashMap<Integer, String> errors) {
		Toast.makeText(getActivity(), errors.toString(), Toast.LENGTH_SHORT)
				.show();
	}
}
