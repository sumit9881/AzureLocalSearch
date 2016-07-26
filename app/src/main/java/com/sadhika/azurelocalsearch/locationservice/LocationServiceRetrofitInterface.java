package com.sadhika.azurelocalsearch.locationservice;

import com.sadhika.azurelocalsearch.pojos.LocationSearchResult;

import retrofit2.http.GET;
import rx.Observable;


public interface LocationServiceRetrofitInterface {

    @GET("/")
    Observable<LocationSearchResult> getLocationsFromNetwork();

}
