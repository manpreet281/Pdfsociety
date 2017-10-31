package com.navjot.deepak.manpreet.pdfsociety.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.navjot.deepak.manpreet.pdfsociety.R;

public class MyPdfsFragment extends PdfListFragment {

    public MyPdfsFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // [START my_top_pdfs_query]
        // My top pdfs by number of stars
        String myUserId = getUid();
        Query myToppdfsQuery = databaseReference.child(getString(R.string.DB_user_pdfs)).child(getUid());
        // [END my_top_pdfs_query]

        return myToppdfsQuery;
    }


}
