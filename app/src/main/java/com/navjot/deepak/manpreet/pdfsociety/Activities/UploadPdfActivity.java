package com.navjot.deepak.manpreet.pdfsociety.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.navjot.deepak.manpreet.pdfsociety.R;
import com.navjot.deepak.manpreet.pdfsociety.Services.MyUploadService;

public class UploadPdfActivity extends Progressdialog {

    EditText description;
    Button uploadbtn;

    private static final int RC_TAKE_PDF = 101;
    private Uri mFileUri = null;
    private Uri mDownloadUrl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pdf);

        initViews();
    }

    public void initViews(){
        description = (EditText)findViewById(R.id.Description);
        uploadbtn   = (Button)  findViewById(R.id.UploadButton);
    }

    public void uploadbtnClicked(View v){
        // Pick a Pdf from storage
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("pdf/*");
        startActivityForResult(intent, RC_TAKE_PDF);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(getString(R.string.tag), "onActivityResult: Requestcode: " + requestCode + " :resultcode: " + resultCode + ": data : " + data);
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
                .setAction(MyUploadService.ACTION_UPLOAD));

        // Show loading spinner
        showStorageProgressDialog(getString(R.string.progress_uploading));
    }
}
