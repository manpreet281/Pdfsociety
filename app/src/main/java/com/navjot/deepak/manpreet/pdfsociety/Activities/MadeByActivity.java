package com.navjot.deepak.manpreet.pdfsociety.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.navjot.deepak.manpreet.pdfsociety.R;

public class MadeByActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }
        setContentView(R.layout.activity_made_by);

    }


    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }
}
