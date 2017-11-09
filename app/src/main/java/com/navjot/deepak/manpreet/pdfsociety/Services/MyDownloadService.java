package com.navjot.deepak.manpreet.pdfsociety.Services;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StreamDownloadTask;
import com.google.firebase.storage.UploadTask;
import com.navjot.deepak.manpreet.pdfsociety.Activities.NavDrawer.HomeActivity;
import com.navjot.deepak.manpreet.pdfsociety.Activities.PdfDetailActivity;
import com.navjot.deepak.manpreet.pdfsociety.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;



import com.google.firebase.storage.StorageReference;

public class MyDownloadService extends MyBaseTaskService{

    public static final String ACTION_DOWNLOAD = "action_download";
    public static final String DOWNLOAD_COMPLETED = "download_completed";
    public static final String DOWNLOAD_ERROR = "download_error";
    public static final String EXTRA_DOWNLOAD_PATH = "extra_download_path";
    public static final String EXTRA_BYTES_DOWNLOADED = "extra_bytes_downloaded";

    private StorageReference mStorageRef;
    private static String TAG;

    @Override
    public void onCreate() {
        super.onCreate();
        TAG = getString(R.string.tag);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand:" + intent + ":" + startId);

        if (ACTION_DOWNLOAD.equals(intent.getAction())) {
            mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://pdfsociety-936a1.appspot.com")
                    .child(intent.getStringExtra("uid"))
                    .child(intent.getStringExtra("pdfkey"))
                    .child(intent.getStringExtra("pdfname"));
            String downloadFileName = mStorageRef.getName();
            downloadFromPath(downloadFileName);
        }

        return START_REDELIVER_INTENT;
    }

    private void downloadFromPath(final String downloadPath) {
        Log.d(TAG, "downloadFromPath:" + downloadPath);

        // Mark task started
        taskStarted();
        showProgressNotification(getString(R.string.progress_downloading), 0, 0);

        File rootPath = new File(Environment.getExternalStorageDirectory(), getString(R.string.app_name));
        if(!rootPath.exists()) {
            rootPath.mkdirs();
        }
        final File localFile = new File(rootPath,downloadPath);
        // Download and get total bytes
        mStorageRef.getFile(localFile)
                .addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        showDownloadProgressNotification("Downloading", taskSnapshot.getBytesTransferred(), taskSnapshot.getTotalByteCount());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        //Log.d(TAG, "download:SUCCESS");
                        Log.e("firebase ",";local tem file created  created " +localFile.toString());

                        showDownloadFinishedNotification(downloadPath, (int) taskSnapshot.getTotalByteCount());

                        // Mark task completed
                        taskCompleted();
                        Toast.makeText(MyDownloadService.this, "Download Finished", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.w(TAG, "download:FAILURE", exception);
                        showDownloadFinishedNotification(downloadPath, -1);
                        Toast.makeText(MyDownloadService.this, "Download Failed", Toast.LENGTH_SHORT).show();
                        // Mark task completed
                        taskCompleted();
                    }
                });
    }

    private void showDownloadFinishedNotification(String downloadPath, int bytesDownloaded) {
        // Hide the progress notification
        dismissProgressNotification();

        Intent intent = new Intent(MyDownloadService.this, HomeActivity.class);

        boolean success = bytesDownloaded != -1;
        String caption = success ? getString(R.string.download_success) : getString(R.string.download_failure);
        showFinishedNotification(caption, intent, success);
    }
}

