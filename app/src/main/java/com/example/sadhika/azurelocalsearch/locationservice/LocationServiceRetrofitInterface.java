package com.example.sadhika.azurelocalsearch.locationservice;

import com.example.sadhika.azurelocalsearch.pojos.LocationSearchResult;

import retrofit2.http.GET;
import rx.Observable;


public interface LocationServiceRetrofitInterface {

    @GET("/")
    Observable<LocationSearchResult> getLocationsFromNetwork();

}
