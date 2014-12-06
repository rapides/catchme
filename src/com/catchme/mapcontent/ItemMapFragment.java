package com.catchme.mapcontent;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.catchme.R;
import com.catchme.R.color;
import com.catchme.contactlist.ItemListActivity;
import com.catchme.exampleObjects.ExampleContent;
import com.catchme.exampleObjects.ExampleItem;
import com.catchme.exampleObjects.LoggedUser;
import com.catchme.exampleObjects.UserLocation;
import com.catchme.itemdetails.ItemDetailsFragment;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import android.support.v4.util.LongSparseArray;

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

	@Override
	public void locationsUpdated() {
		Location lastLocation = mItem.getLastLocation();
		if (lastLocation != null) {
			LatLng location = new LatLng(lastLocation.getLatitude(),
					lastLocation.getLongitude());
			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
					location, 16);

			map.clear();
			map.addPolyline(getPolilyne());
			map.addCircle(getCircle());
			map.animateCamera(cameraUpdate);
			map.addMarker(
					new MarkerOptions()
							.title(mItem.getFullName())
							.snippet(
									"Last seen "
											+ getTimeAgo(System
													.currentTimeMillis()
													- lastLocation.getTime()))
							.position(location)).showInfoWindow();
		}

	}

	private CircleOptions getCircle() {
		CircleOptions circleOptions = new CircleOptions();
		return circleOptions
				.center(new LatLng(mItem.getLastLocation().getLatitude(), mItem
						.getLastLocation().getLongitude()))
				.radius(mItem.getLastLocation().getAccuracy())
				.strokeColor(getResources().getColor(color.map_circle_stroke))
				.strokeWidth(
						getResources().getInteger(
								R.integer.map_circle_stroke_width))
				.fillColor(getResources().getColor(color.map_circle_fill));

	}

	private PolylineOptions getPolilyne() {
		PolylineOptions rectOptions = new PolylineOptions();
		for (UserLocation l : mItem.getLocations()) {
			LatLng locTemp = new LatLng(l.getLatitude(), l.getLongitude());
			rectOptions.add(locTemp);
			map.addMarker(new MarkerOptions()
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.ic_action_place))
					.position(locTemp)
					.flat(false)
					.title("History")
					.snippet(
							"Seen here "
									+ getTimeAgo(System.currentTimeMillis()
											- l.getFixTime())));

		}
		return rectOptions.color(getResources().getColor(color.map_line))
				.width(getResources().getInteger(R.integer.map_line_width))
				.geodesic(true);
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
	public void locationsError(LongSparseArray<String> errors) {
		Toast.makeText(getActivity(), errors.toString(), Toast.LENGTH_SHORT)
				.show();
	}

}
