package com.example.sadhika.azurelocalsearch;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sadhika.azurelocalsearch.pojos.LocationSearchResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;


public class LocationDetailFragment extends Fragment implements OnMapReadyCallback {

    private static final String ARG_RESULT = "arg.result";
    private final int MY_PERMISSIONS_REQUEST_LOCATION = 1;

    public static final String TAG_LOCATION_DETAIL_FRAG = "tag.LocationDetailFragment";

    private LocationSearchResult mLocationSearchResult;

    private OnFragmentInteractionListener mListener;
    private SupportMapFragment mMapFragment;

    public LocationDetailFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static LocationDetailFragment newInstance(LocationSearchResult result) {
        LocationDetailFragment fragment = new LocationDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_RESULT, result);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLocationSearchResult = getArguments().getParcelable(ARG_RESULT);
        }
        setRetainInstance(true);
        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(mLocationSearchResult.getName());
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
            mMapFragment.getMapAsync(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_location_detail, container, false);
        rootView.setClickable(true);

        mMapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.location_map);

//        mMapFragment.setRetainInstance(true);
        getActivity().invalidateOptionsMenu();
        updateContent(rootView);
        return rootView;
    }

    private void updateContent(View root) {
        TextView tvArrivalTime = (TextView) root.findViewById(R.id.textview_arrival_time);
        tvArrivalTime.setText(getString(R.string.tag_arrival_time, mLocationSearchResult.getRemainingTimeToArrive()));

        TextView tvAddress = (TextView) root.findViewById(R.id.textview_address);
        tvAddress.setText(getString(R.string.tag_address, mLocationSearchResult.getAddress()));

        TextView tvLocation = (TextView) root.findViewById(R.id.textview_location);
        tvLocation.setText(getString(R.string.tag_location, mLocationSearchResult.getName()));

        TextView tvLongitude = (TextView) root.findViewById(R.id.textview_longitude);
        tvLongitude.setText(getString(R.string.tag_longitude, mLocationSearchResult.getLongitude()));

        TextView tvLattitude = (TextView) root.findViewById(R.id.textview_lattitule);
        tvLattitude.setText(getString(R.string.tag_latitude, mLocationSearchResult.getLatitude()));

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng latlng = new LatLng(mLocationSearchResult.getLatitude(), mLocationSearchResult.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(latlng).title(mLocationSearchResult.getName()));

        CameraUpdate center= CameraUpdateFactory.newLatLng(latlng);
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);

        googleMap.moveCamera(center);
        googleMap.animateCamera(zoom);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        Log.v("adaas", "sumit disable");
        menu.setGroupVisible(R.id.group_options, false);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
