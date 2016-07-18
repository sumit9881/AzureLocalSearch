package com.example.sadhika.azurelocalsearch;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.sadhika.azurelocalsearch.locationservice.LocationService;
import com.example.sadhika.azurelocalsearch.pojos.LocationSearchResult;

import java.util.ArrayList;
import java.util.List;

public class LocationSearchResultFragment extends Fragment {

    private static final String TAG = LocationSearchResultFragment.class.getName();
    public static final String TAG_LOCATION_SEARCH_RES_FRAG = "LocationSearchResultFragment";
    private final int MY_PERMISSIONS_REQUEST_LOCATION = 1;

    private OnFragmentInteractionListener mListener;
    private ContentLoadingProgressBar mProgressDialog;
    private RecyclerView mRecyclerView;
    private SearchResultRecyclerViewAdapter mAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LocationSearchResultFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static LocationSearchResultFragment newInstance(int columnCount) {
        LocationSearchResultFragment fragment = new LocationSearchResultFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        new SearchResultRecyclerViewAdapter(null, mListener);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.location));
        Log.v(TAG, "onCreate mAdapter is " + mAdapter);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_location_searchresult, container, false);

        mRecyclerView =  (RecyclerView)view.findViewById(R.id.list);
        mAdapter = new SearchResultRecyclerViewAdapter(null, mListener);

        mProgressDialog = (ContentLoadingProgressBar) view.findViewById(R.id.progress_bar);

        mRecyclerView.addItemDecoration(new SearchResultRecyclerViewAdapter.VerticalSpaceItemDecoration(15));
        LocationService.getInstance(getActivity()).getResultLocationsFromWeb(new LocationService.LocationListener() {

            @Override
            public void onError(Throwable e) {
                mProgressDialog.hide();
                mProgressDialog.setVisibility(View.GONE);
                new AlertDialog.Builder(getActivity()).setTitle(getString(R.string.network_error))
                        .setMessage(R.string.search_failed)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                getActivity().finish();
                            }
                        }).create().show();
            }

            @Override
            public void onNext(List<LocationSearchResult> result) {
                mAdapter.add(result);
                Log.v(TAG, "onNext mAdapter is " + mAdapter);
            }

            @Override
            public void onCompleted() {
                mProgressDialog.hide();


            }
        });

        mRecyclerView.setAdapter(mAdapter);

       return view;
    }


    public void sortLocationListByName() {
        mAdapter.sortValuesByName();
    }

    public int sortLocationByDistance() {
         return mAdapter.sortValuesByDistance(getActivity());
    }

    public void  sortLocationByArrivalTime() {
        mAdapter.sortValuesByArrivalTime();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        Log.v(TAG, "onDetach mAdapter is " + mAdapter);
        super.onDetach();
        Log.v(TAG, "onDetach mAdapter is " + mAdapter);
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(LocationSearchResult item);
        void onLocationSelected(LocationSearchResult location);
    }


}
