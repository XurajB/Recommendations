package com.suraj.examples.googleplayservices.api;

import com.suraj.examples.googleplayservices.model.ActiveListings;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

/**
 * Created by surajbhattarai on 7/25/15.
 */
public class Etsy {
    private static final String API_KEY = "daad7lgpkq6jwvskj4hqk8jx";

    private static RequestInterceptor getInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addEncodedQueryParam("api_key", API_KEY);
            }
        };
    }

    private static Api getApi() {
        return new RestAdapter.Builder()
                .setEndpoint("https://openapi.etsy.com/v2")
                .setRequestInterceptor(getInterceptor())
                .build()
                .create(Api.class);
    }

    public static void getActiveListings(Callback<ActiveListings> callback) {
        getApi().activeListings("Images,Shop", callback);
    }
}
