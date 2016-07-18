package com.example.sadhika.azurelocalsearch.locationservice;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.example.sadhika.azurelocalsearch.pojos.LocationSearchResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Sadhika on 6/25/16.
 */
public class LocationService {

    private static final String BASE_URL = "http://localsearch.azurewebsites.net/";
    private static final int MIN_LOCATION_REFRESH_TIME = 1000;
    private static final int MIN_DISTANCE_CHANGE_FOR_UPDATES = 100;

    public static final int ERROR_NONE = 0;
    public static final int ERROR_GPS_NOT_ENABLED = 1;
    public static final int ERROR_NO_PERMISSION = 2;

    private  static LocationService mLocationService;
    private LocationNetworkService mService;
    private LocationManager mLocationManager;
    private Context mContext;

    public static LocationService getInstance(Context context) {
        if (null == mLocationService) {
            synchronized (LocationService.class) {
                if (null == mLocationService) {
                    mLocationService = new LocationService(context);
                }
            }
        }

        return mLocationService;
    }

    private LocationService(Context context) {

        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(rxAdapter)
                .build();

        mService = retrofit.create(LocationNetworkService.class);
        mContext = context;
    }

    public void getResultLocationsFromWeb(final LocationListener listener) {

        Observable<List<LocationSearchResult>> observable = mService.getLocationsFromNetwork();
        observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<LocationSearchResult>>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("result is passed");
                        if (null != listener) listener.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("result is failed");
                        e.printStackTrace();
                        if (null != listener) listener.onError(e);
                    }

                    @Override
                    public void onNext(List<LocationSearchResult> user) {
                        System.out.println(" result is " + user.toString());
                        if (null != listener) listener.onNext(user);
                    }
                });
    }

    public int getCurrentLocation(final CurrentLocationResult listener) {

        mLocationManager = (LocationManager) mContext
                .getSystemService(Context.LOCATION_SERVICE);

        if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return ERROR_GPS_NOT_ENABLED;
        }
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return ERROR_NO_PERMISSION;
        }
        mLocationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                MIN_LOCATION_REFRESH_TIME,
                MIN_DISTANCE_CHANGE_FOR_UPDATES, new android.location.LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        listener.onCurrentLocationReceived(location);
                        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        mLocationManager.removeUpdates(this);
                    }

                    @Override
                    public void onStatusChanged(String s, int i, Bundle bundle) {
                    }

                    @Override
                    public void onProviderEnabled(String s) {
                    }

                    @Override
                    public void onProviderDisabled(String s) {
                    }
                });
        return  ERROR_NONE;
    }

    public interface CurrentLocationResult {
        public void onCurrentLocationReceived(Location location);
        public void onError(int code);
    }
    public interface LocationNetworkService {
        @GET("api/Locations")
        Observable<List<LocationSearchResult>> getLocationsFromNetwork();

    }

    public interface LocationListener{
        public void onError(Throwable e);
        public void onNext(List<LocationSearchResult> result);
        public void onCompleted();
    }
}
