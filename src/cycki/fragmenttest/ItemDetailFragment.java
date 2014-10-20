package cycki.fragmenttest;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cycki.fragmenttest.dummy.DummyContent;

public class ItemDetailFragment extends Fragment {
	MapView mapView;
	GoogleMap map;
	public static final String ARG_ITEM_ID = "item_id";

	private DummyContent.DummyItem mItem;

	public ItemDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			mItem = DummyContent.ITEM_MAP.get(getArguments().getLong(
					ARG_ITEM_ID));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_item_detail,
				container, false);
		mapView = (MapView) rootView.findViewById(R.id.mapview);
		mapView.onCreate(savedInstanceState);
		map = mapView.getMap();
		map.getUiSettings().setMyLocationButtonEnabled(true);
		map.setMyLocationEnabled(true);

		MapsInitializer.initialize(this.getActivity());
		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
				new LatLng(43.1, -87.9), 10);
		map.animateCamera(cameraUpdate);

		if (mItem != null) {
			((TextView) rootView.findViewById(R.id.item_detail))
					.setText(mItem.name);
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
}
