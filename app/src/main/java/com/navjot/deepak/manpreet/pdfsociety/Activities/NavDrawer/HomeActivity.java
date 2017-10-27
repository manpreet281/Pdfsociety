package com.navjot.deepak.manpreet.pdfsociety.Activities.NavDrawer;


import android.content.Intent;


import android.app.ProgressDialog;
import android.content.pm.ApplicationInfo;
import android.net.Uri;

import android.os.Bundle;

import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.navjot.deepak.manpreet.pdfsociety.Activities.UploadPdfActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.navjot.deepak.manpreet.pdfsociety.Activities.FeedbackActivity;
import com.navjot.deepak.manpreet.pdfsociety.Activities.SignIn;

import com.navjot.deepak.manpreet.pdfsociety.Fragments.PdfListFragment;
import com.navjot.deepak.manpreet.pdfsociety.R;

import java.io.File;

public class HomeActivity extends AppCompatActivity

        implements NavigationView.OnNavigationItemSelectedListener

{
    public ProgressDialog mProgressDialog;


    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
    }

    public void init(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, UploadPdfActivity.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

//    @Override
//    public Query getQuery(DatabaseReference databaseReference) {
//        // [START recent_posts_query]
//        // Last 100 posts, these are automatically the 100 most recent
//        // due to sorting by push() keys
//        Query recentPostsQuery = databaseReference.child(getString(R.string.DB_Pdfs)).limitToFirst(100);
//        // [END recent_posts_query]
//
//        return recentPostsQuery;
//    }

    @Override
    public void onBackPressed() {


            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
    }

    @Override
    protected void onDestroy() {

        finish();
        System.exit(0);
        super.onDestroy();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        // Inflate menu resource file.


        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_logout) {


                    showProgressDialog();
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(HomeActivity.this, SignIn.class));
                    return true;
                }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {
            item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    ApplicationInfo applicationInfo = getApplicationContext().getApplicationInfo();
                    String apkPath = applicationInfo.sourceDir;
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("application/vnd.android.package-archieve");
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(apkPath)));
                    startActivity(Intent.createChooser(intent, "Share App Using"));

                    return false;
                }
            });

        } else if (id == R.id.nav_feedback) {

            Intent intent = new Intent(HomeActivity.this, FeedbackActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }



}



