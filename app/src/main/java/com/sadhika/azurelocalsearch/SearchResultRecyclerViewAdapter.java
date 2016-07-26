package com.sadhika.azurelocalsearch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sadhika.azurelocalsearch.LocationSearchResultFragment.OnFragmentInteractionListener;
import com.sadhika.azurelocalsearch.locationservice.LocationService;
import com.sadhika.azurelocalsearch.pojos.LocationSearchResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class SearchResultRecyclerViewAdapter extends RecyclerView.Adapter<SearchResultRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = SearchResultRecyclerViewAdapter.class.getName();
    private final OnFragmentInteractionListener mListener;

    private List<LocationSearchResult> mValues;

    public SearchResultRecyclerViewAdapter(List<LocationSearchResult> items, OnFragmentInteractionListener listener) {
        mListener = listener;
        if (null == items) {
            mValues = new ArrayList<>();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mLocation.setText(mValues.get(position).getName());
        holder.mAddress.setText(mValues.get(position).getAddress());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onLocationSelected(mValues.get(position));
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    public void add(List<LocationSearchResult> list) {
        mValues.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        System.out.println("current size is " + mValues.size());
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mLocation;
        public final TextView mAddress;
        public LocationSearchResult mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mLocation = (TextView) view.findViewById(R.id.textview_location);
            mAddress = (TextView) view.findViewById(R.id.textview_address);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mLocation.getText() + "'";
        }
    }

    static public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int mVerticalSpaceHeight;

        public VerticalSpaceItemDecoration(int mVerticalSpaceHeight) {
            this.mVerticalSpaceHeight = mVerticalSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            outRect.bottom = mVerticalSpaceHeight;
        }
    }


    public void sortValuesByName() {
        Collections.sort(mValues, new Comparator<LocationSearchResult>() {
            @Override
            public int compare(LocationSearchResult s1, LocationSearchResult s2) {

                return s1.getName().compareToIgnoreCase(s2.getName());
            }
        });

        notifyDataSetChanged();
    }

    public int sortValuesByDistance(final Context context) {
        final LocationManager loc = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        int retVal = LocationService.getInstance(context).getCurrentLocation(new LocationService.CurrentLocationResult() {
            @Override
            public void onCurrentLocationReceived(final Location location) {
                Collections.sort(mValues, new Comparator<LocationSearchResult>() {
                    @Override
                    public int compare(LocationSearchResult s1, LocationSearchResult s2) {
                        try {
                            double lat1 = s1.getLatitude();
                            double long1 = s1.getLongitude();
                            double lat2 = s2.getLatitude();
                            double long2 = s2.getLongitude();

                            Location location1 = new Location(LocationManager.GPS_PROVIDER);
                            Location location2 = new Location(LocationManager.GPS_PROVIDER);

                            location1.setLatitude(lat1);
                            location1.setLongitude(long1);

                            location2.setLatitude(lat2);
                            location2.setLongitude(long2);

                            float dist1 = location.distanceTo(location1);
                            float dist2 = location.distanceTo(location2);
                            if (dist1 == dist2) {
                                return 0;
                            }
                            return dist1 >= dist2 ? 1 : -1;

                        } catch (NumberFormatException e) {
                            return 1;
                        }
                    }
                });
                notifyDataSetChanged();
            }

            @Override
            public void onError(int code) {
            }
        });

        notifyDataSetChanged();
        return retVal;
    }

    public void sortValuesByArrivalTime() {
        Collections.sort(mValues, new Comparator<LocationSearchResult>() {

            public int compare(LocationSearchResult s1, LocationSearchResult s2) {
                if (s1.getArrivalTimeStamp() == s2.getArrivalTimeStamp()) {
                    return 0;
                }

               return  s1.getArrivalTimeStamp() > s2.getArrivalTimeStamp() ? 1 : -1;
            }
        });

        notifyDataSetChanged();
    }

}
