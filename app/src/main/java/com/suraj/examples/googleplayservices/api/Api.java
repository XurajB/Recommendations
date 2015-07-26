package com.suraj.examples.googleplayservices.api;

import com.suraj.examples.googleplayservices.model.ActiveListings;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by surajbhattarai on 7/25/15.
 */
public interface Api {
    @GET("/listings/active")
    void activeListings(@Query("includes") String includes,
                        Callback<ActiveListings> callback);
}
