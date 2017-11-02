package com.navjot.deepak.manpreet.pdfsociety.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.EventLogTags;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.navjot.deepak.manpreet.pdfsociety.Models.User;
import com.navjot.deepak.manpreet.pdfsociety.R;
import com.navjot.deepak.manpreet.pdfsociety.Services.MyUploadService;

import java.util.Locale;

public class UploadPdfActivity extends Progressdialog {

    EditText description;
    Button uploadbtn;
    private BroadcastReceiver mBroadcastReceiver;

    private static final int RC_TAKE_PDF = 101;
    private Uri mFileUri = null;
    private Uri mDownloadUrl = null;
    private static final String KEY_FILE_URI = "key_file_uri";
    private static final String KEY_DOWNLOAD_URL = "key_download_url";
    private static String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setContentView(R.layout.activity_upload_pdf);
        }

        initViews();
        getusername();
        restoreInstanceState(savedInstanceState);
        broadcastReceive();
    }

    public void onStart() {
        super.onStart();

        // Register receiver for uploads and downloads
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.registerReceiver(mBroadcastReceiver, MyUploadService.getIntentFilter());
    }

    @Override
    public void onSaveInstanceState(Bundle out) {
        super.onSaveInstanceState(out);
        out.putParcelable(KEY_FILE_URI, mFileUri);
        out.putParcelable(KEY_DOWNLOAD_URL, mDownloadUrl);
        Log.d(getString(R.string.tag), "onSaveInstanceState");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(getString(R.string.tag), "onStop");
        // Unregister download receiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
    }

    public void initViews(){
        description = (EditText)findViewById(R.id.Description);
        uploadbtn   = (Button)  findViewById(R.id.UploadButton);
    }

    public void uploadbtnClicked(View v){
        if(validateForm()){
            // Pick a Pdf from storage
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/pdf");
            startActivityForResult(intent, RC_TAKE_PDF);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(getString(R.string.tag), "onActivityResult data : " + data);
        if (requestCode == RC_TAKE_PDF) {
            if (resultCode == RESULT_OK) {
                mFileUri = data.getData();

                if (mFileUri != null) {
                    uploadFromUri(mFileUri);
                } else {
                    Log.w(getString(R.string.tag), "File URI is null");
                }
            } else {
                Toast.makeText(this, "Taking Pdf failed.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadFromUri(Uri fileUri) {
        Log.d(getString(R.string.tag), "uploadFromUri:src(fileuri.tostring):" + fileUri.toString());

        // Save the File URI
        mFileUri = fileUri;

        // Clear the last download, if any
        mDownloadUrl = null;
        // Start MyUploadService to upload the file, so that the file is uploaded
        // even if this Activity is killed or put in the background
        startService(new Intent(this, MyUploadService.class)
                .putExtra(MyUploadService.EXTRA_FILE_URI, fileUri)
                .putExtra("description", description.getText().toString().trim())
                .putExtra("uid", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .putExtra("username",username)
                .setAction(MyUploadService.ACTION_UPLOAD));

        Toast.makeText(this, getString(R.string.progress_uploading), Toast.LENGTH_LONG).show();
    }

    public void getusername(){
        FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.DB_Users))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        username = user.getUsername();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(getString(R.string.tag), "onCancelled: getusername");
                    }
                });
    }

    public void restoreInstanceState(Bundle savedInstanceState){
        if (savedInstanceState != null) {
            mFileUri = savedInstanceState.getParcelable(KEY_FILE_URI);
            mDownloadUrl = savedInstanceState.getParcelable(KEY_DOWNLOAD_URL);
        }
        onNewIntent(getIntent());
    }

    public void broadcastReceive(){
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(getString(R.string.tag), "onReceive:" + intent);
                hideProgressDialog();

                switch (intent.getAction()) {
                    case MyUploadService.UPLOAD_COMPLETED:
                    case MyUploadService.UPLOAD_ERROR:
                        onUploadResultIntent(intent);
                        break;
                }
            }
        };
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(getString(R.string.tag), "onNewIntent(upload_activity_intent)");
        // Check if this Activity was launched by clicking on an upload notification
        if (intent.hasExtra(MyUploadService.EXTRA_DOWNLOAD_URL)) {
            Log.d(getString(R.string.tag), "onNewIntent has extra_download_url");
            onUploadResultIntent(intent);
        }

    }

    private void onUploadResultIntent(Intent intent) {
        Log.d(getString(R.string.tag), "onUploadResultIntent");
        // Got a new intent from MyUploadService with a success or failure
        mDownloadUrl = intent.getParcelableExtra(MyUploadService.EXTRA_DOWNLOAD_URL);
        mFileUri = intent.getParcelableExtra(MyUploadService.EXTRA_FILE_URI);
    }

    private boolean validateForm(){
        boolean valid = true;
        if(TextUtils.isEmpty(description.getText().toString().trim())){
            description.setError("Required");
            valid = false;
        }else{
            description.setError(null);
        }
        return valid;
    }
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }
}
