package com.navjot.deepak.manpreet.pdfsociety.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
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

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            saveTokeninDB(refreshedToken);
        }
    }

    public void saveTokeninDB(String token){
        FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.DB_Users))
                .child(FirebaseAuth.getInstance().getUid())
                .child("reg_token")
                .setValue(token);
    }
}
