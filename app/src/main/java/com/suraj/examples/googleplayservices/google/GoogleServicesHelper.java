package com.suraj.examples.googleplayservices.google;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

/**
 * Created by surajbhattarai on 7/25/15.
 */
public class GoogleServicesHelper implements GoogleApiClient.ConnectionCallbacks,
GoogleApiClient.OnConnectionFailedListener{

    private static final int REQUEST_CODE_AVAILABILITY = -101;
    private static final int REQUEST_CODE_RESOLUTION = -100;

    public interface GoogleServicesListner {
        public void onConnected();
        public void onDisconnected();
    }

    private Activity activity;
    private GoogleServicesListner listner;
    private GoogleApiClient apiClient;

    public GoogleServicesHelper(Activity activity, GoogleServicesListner listner) {
        this.activity = activity;
        this.listner = listner;

        this.apiClient = new GoogleApiClient.Builder(activity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API,
                        Plus.PlusOptions.builder()
                        .setServerClientId("893400650990-lolapqiinhkrap5j9b4m8hckhbj4a10q.apps.googleusercontent.com").build())
                .build();
    }

    public void connect() {
        if (isGooglePlayServicesAvailable()) {
            apiClient.connect();
        } else {
            listner.onDisconnected();
        }

    }

    public void disconnect() {
        if (isGooglePlayServicesAvailable()) {
            apiClient.disconnect();
        } else {
            listner.onDisconnected();
        }
    }

    private boolean isGooglePlayServicesAvailable() {
        int availability = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
        switch (availability) {
            case ConnectionResult.SUCCESS:
                return true;
            case ConnectionResult.SERVICE_INVALID:
            case ConnectionResult.SERVICE_DISABLED:
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                GooglePlayServicesUtil.getErrorDialog(availability, activity, REQUEST_CODE_AVAILABILITY).show();
                return false;
            default:
                return false;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        listner.onConnected();
    }

    @Override
    public void onConnectionSuspended(int i) {
        listner.onDisconnected();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(activity, REQUEST_CODE_RESOLUTION);
            } catch (IntentSender.SendIntentException e) {
                connect();
            }
        } else {
            listner.onDisconnected();
        }
    }

    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_AVAILABILITY || requestCode == REQUEST_CODE_RESOLUTION) {
            if (requestCode == Activity.RESULT_OK) {
                connect();
            } else {
                listner.onDisconnected();
            }
        }
    }

}
