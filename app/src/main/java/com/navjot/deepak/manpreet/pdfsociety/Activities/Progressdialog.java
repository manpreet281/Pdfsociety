package com.navjot.deepak.manpreet.pdfsociety.Activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import com.navjot.deepak.manpreet.pdfsociety.R;

public class Progressdialog extends AppCompatActivity {


    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }
}
