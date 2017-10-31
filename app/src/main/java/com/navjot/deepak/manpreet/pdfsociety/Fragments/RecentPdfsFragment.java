package com.navjot.deepak.manpreet.pdfsociety.Fragments;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.navjot.deepak.manpreet.pdfsociety.R;

public class RecentPdfsFragment extends PdfListFragment {

    public RecentPdfsFragment() {}

    public Query getQuery(DatabaseReference databaseReference){
        // [START recent_posts_query]
        // Last 100 posts, these are automatically the 100 most recent
        // due to sorting by push() keys
        Query recentPdfsQuery = databaseReference.child(getString(R.string.DB_Pdfs)).limitToFirst(100);
        // [END recent_posts_query]

        return recentPdfsQuery;
    }

}
