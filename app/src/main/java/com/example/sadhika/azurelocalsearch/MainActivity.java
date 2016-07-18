package com.example.sadhika.azurelocalsearch;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.sadhika.azurelocalsearch.locationservice.LocationService;
import com.example.sadhika.azurelocalsearch.pojos.LocationSearchResult;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements LocationSearchResultFragment.OnFragmentInteractionListener, LocationDetailFragment.OnFragmentInteractionListener {

    private final int MY_PERMISSIONS_REQUEST_LOCATION = 1;

    private LocationSearchResultFragment mLocationSearchResultFragment;
    private LocationDetailFragment mLocationDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        if (null == savedInstanceState) {
            mLocationSearchResultFragment = new LocationSearchResultFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.main_content, mLocationSearchResultFragment, LocationSearchResultFragment.TAG_LOCATION_SEARCH_RES_FRAG).commit();
        } else {
            mLocationSearchResultFragment =  (LocationSearchResultFragment)getSupportFragmentManager().findFragmentByTag(LocationSearchResultFragment.TAG_LOCATION_SEARCH_RES_FRAG);
            mLocationDetailFragment = (LocationDetailFragment) getSupportFragmentManager().findFragmentByTag(LocationDetailFragment.TAG_LOCATION_DETAIL_FRAG);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        boolean granted = true;
        for (int grantResult : grantResults) {
            granted = granted && grantResult == PackageManager.PERMISSION_GRANTED;
        }
        if (granted) {
            mLocationSearchResultFragment.sortLocationByDistance();
        }
    }

    @Override
    public void onListFragmentInteraction(LocationSearchResult item) {

    }

    @Override
    public void onLocationSelected(LocationSearchResult result) {

        mLocationDetailFragment = LocationDetailFragment.newInstance(result);
        getSupportFragmentManager().beginTransaction().add(R.id.main_content, mLocationDetailFragment).addToBackStack(null).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sort_name) {
            mLocationSearchResultFragment.sortLocationListByName();
            return true;
        } else if (id == R.id.action_sort_arrival_time) {
            mLocationSearchResultFragment.sortLocationByArrivalTime();
            return true;
        } else if (id == R.id.action_sort_distance) {
            ArrayList<String> permissions = new ArrayList<>();
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (permissions.isEmpty()) {
                if (mLocationSearchResultFragment.sortLocationByDistance() == LocationService.ERROR_GPS_NOT_ENABLED) {
                    startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
                } else {
                    mLocationSearchResultFragment.sortLocationByDistance();
                }
            } else {
                ActivityCompat.requestPermissions(this, permissions.toArray(new String[0]), MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }

        return super.onOptionsItemSelected(item);
    }

}
