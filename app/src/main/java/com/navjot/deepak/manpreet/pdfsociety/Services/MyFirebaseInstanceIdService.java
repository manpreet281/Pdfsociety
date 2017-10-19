package com.navjot.deepak.manpreet.pdfsociety.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.navjot.deepak.manpreet.pdfsociety.R;

import static android.content.ContentValues.TAG;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    public static String TAG;

    public MyFirebaseInstanceIdService() {

    }

    public void onTokenRefresh() {
        TAG = getString(R.string.tag);
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
    }
}
