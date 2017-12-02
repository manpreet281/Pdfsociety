package com.navjot.deepak.manpreet.pdfsociety.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.navjot.deepak.manpreet.pdfsociety.Adapters.PdfAdapter;
import com.navjot.deepak.manpreet.pdfsociety.Models.Category;
import com.navjot.deepak.manpreet.pdfsociety.Models.Pdf;
import com.navjot.deepak.manpreet.pdfsociety.R;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {

     RecyclerView mRecycler;
     LinearLayoutManager mManager;
     ProgressBar mProgressBar;
     TextView Nopdf;
     SearchView searchView;
    PdfAdapter mAdapter;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_all_pdfs);

        String categoryName = getIntent().getStringExtra("CategoryName");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(categoryName);
        }

        init();
        getListData(getApplicationContext(), categoryName);
    }

    private void init(){
        Nopdf = (TextView)findViewById(R.id.Nopdf);
        mProgressBar = (ProgressBar)findViewById(R.id.mProgressBar);
        mRecycler =  (RecyclerView)findViewById(R.id.messages_list);
        mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);
    }

    public void getListData(final Context context, String categoryName){
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiInfo != null && wifiInfo.isConnected()) || (mobileInfo != null && mobileInfo.isConnected())) {

            FirebaseDatabase.getInstance().getReference()
                    .child(getString(R.string.DB_Categories))
                    .child(categoryName)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ArrayList<Pdf> pdfList = new ArrayList<>();
                            for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                                Pdf pdf = postSnapShot.getValue(Pdf.class);
                                pdfList.add(pdf);
                            }

                            mAdapter = new PdfAdapter(mProgressBar, Nopdf, pdfList, context);
                            mRecycler.setAdapter(mAdapter);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d("Pdfsociety", "getData Error: " + databaseError.getMessage());
                        }
                    });
        }else {
            mProgressBar.setVisibility(View.GONE);
            Nopdf.setVisibility(View.VISIBLE);
            Nopdf.setText("Check your internet connection");
        }
    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        MenuItem item = menu.findItem(R.id.menusearch);
        searchView = (SearchView)item.getActionView();
        searchViewListener();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_logout:
                showProgressDialog();
                FirebaseAuth.getInstance().signOut();
                startActivity(
                        new Intent(CategoryActivity.this, SignIn.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                );

        }
        return super.onOptionsItemSelected(item);
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    protected void searchViewListener(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
              @Override
              public boolean onQueryTextSubmit(String query) {
                  return false;
              }

              @Override
              public boolean onQueryTextChange(String newText) {
                  mAdapter.getFilter().filter(newText);
                  return false;
              }
        }
        );
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
