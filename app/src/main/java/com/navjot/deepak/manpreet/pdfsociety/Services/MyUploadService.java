package com.navjot.deepak.manpreet.pdfsociety.Services;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.navjot.deepak.manpreet.pdfsociety.Activities.NavDrawer.HomeActivity;
import com.navjot.deepak.manpreet.pdfsociety.Models.Pdf;
import com.navjot.deepak.manpreet.pdfsociety.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyUploadService extends MyBaseTaskService {

    public static String TAG;

    public static final String EXTRA_FILE_URI = "extra_file_uri";
    public static final String ACTION_UPLOAD = "action_upload";
    public static final String UPLOAD_COMPLETED = "upload_completed";
    public static final String UPLOAD_ERROR = "upload_error";
    public static final String EXTRA_DOWNLOAD_URL = "extra_download_url";

    public static String description;
    public static String uid;
    public static String username;
    private static double pdfsize;
    private static String uploaddate;


    public StorageReference mStorageRef;
    public DatabaseReference dbref;

    @Override
    public void onCreate() {
        super.onCreate();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        dbref = FirebaseDatabase.getInstance().getReference();
        TAG = getString(R.string.tag);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand:intent: " + intent + ":startId: " + startId);
        if (ACTION_UPLOAD.equals(intent.getAction())) {
            description = intent.getStringExtra("description");
            uid = intent.getStringExtra("uid");
            username = intent.getStringExtra("username");

            Uri fileUri = intent.getParcelableExtra(EXTRA_FILE_URI);
            uploadFromUri(fileUri);
        }

        return START_REDELIVER_INTENT;
    }

    public String getFileName(String uri){
        int index;
        while(uri.contains(":")){
            index = uri.indexOf(":");
            uri = uri.substring(index+1);
        }
        while (uri.contains("/")){
            index = uri.indexOf("/");
            uri = uri.substring(index+1);
        }
        return uri;
    }

    public void uploadFromUri(final Uri fileUri) {
        Log.d(TAG, "uploadFromUri");

        taskStarted();
        showProgressNotification(getString(R.string.progress_uploading), 0, 0);

        // Get a reference to store file at photos/<FILENAME>.jpg
        final StorageReference pdfref = mStorageRef.child(getString(R.string.storage_pdfs))
                .child(getFileName(fileUri.getLastPathSegment()));

        // Upload file to Firebase Storage
        pdfref.putFile(fileUri).
                addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        showProgressNotification(getString(R.string.progress_uploading),
                                taskSnapshot.getBytesTransferred(),
                                taskSnapshot.getTotalByteCount());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Upload succeeded
                        Log.d(TAG, "OnSuccessListener");

                        //  Get the public download URL
                        Uri downloadUri = taskSnapshot.getMetadata().getDownloadUrl();
                        Log.d(TAG, "downloadUri: " + downloadUri);

                        // Get the file size
                        pdfsize = taskSnapshot.getMetadata().getSizeBytes();
                        pdfsize = pdfsize/1048576;
                        Log.d(TAG, "onSuccess: fileSize"+pdfsize);


                        uploaddate = FindCurrentDate();




                        Toast.makeText(MyUploadService.this, getString(R.string.upload_success), Toast.LENGTH_LONG).show();

                        uploadOnDB(downloadUri.toString(), pdfref.getName());

                        broadcastUploadFinished(downloadUri, fileUri);
                        showUploadFinishedNotification(downloadUri, fileUri);
                        taskCompleted();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Upload failed
                        Log.w(TAG, "uploadFromUri:onFailure", exception.getCause());

                        // [START_EXCLUDE]
                        broadcastUploadFinished(null, fileUri);
                        showUploadFinishedNotification(null, fileUri);
                        taskCompleted();
                        // [END_EXCLUDE]
                    }
                });
    }


    private boolean broadcastUploadFinished(Uri downloadUrl, Uri fileUri) {
        boolean success = downloadUrl != null;

        String action = success ? UPLOAD_COMPLETED : UPLOAD_ERROR;

        Intent broadcast = new Intent(action)
                .putExtra(EXTRA_DOWNLOAD_URL, downloadUrl)
                .putExtra(EXTRA_FILE_URI, fileUri);
        return LocalBroadcastManager.getInstance(getApplicationContext())
                .sendBroadcast(broadcast);
    }

    private void showUploadFinishedNotification(Uri downloadUrl, Uri fileUri) {
        // Hide the progress notification
        dismissProgressNotification();

        // Make Intent to MainActivity
        Intent intent = new Intent(this, HomeActivity.class)
                .putExtra(EXTRA_DOWNLOAD_URL, downloadUrl)
                .putExtra(EXTRA_FILE_URI, fileUri)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        boolean success = downloadUrl != null;
        String caption = success ? getString(R.string.upload_success) : getString(R.string.upload_failure);
        showFinishedNotification(caption, intent, success);
    }

    public static IntentFilter getIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UPLOAD_COMPLETED);
        filter.addAction(UPLOAD_ERROR);

        return filter;
    }

    public void uploadOnDB(String downloadUrl, String pdfname) {
        Pdf pdf = new Pdf(
                pdfname,
                description,
                uid,
                0,
                downloadUrl,
                username,
                pdfsize,
                uploaddate
        );
        Log.d(TAG, "" + pdf);
        String pdfkey = dbref.child(getString(R.string.DB_Pdfs)).push().getKey();
        dbref.child(getString(R.string.DB_Pdfs)).child(pdfkey).setValue(pdf);
        dbref.child(getString(R.string.DB_user_pdfs)).child(uid).child(pdfkey).setValue(pdf);
    }

    String FindCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String currentdate = dateFormat.format(new Date());
        Log.i("test", currentdate);

        return currentdate;}

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
