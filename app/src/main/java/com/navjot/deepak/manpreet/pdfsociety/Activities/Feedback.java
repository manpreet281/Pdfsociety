package com.navjot.deepak.manpreet.pdfsociety.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.navjot.deepak.manpreet.pdfsociety.R;

public class Feedback extends AppCompatActivity {

    RatingBar RatingBar;
    EditText txtComment;
    Button buttonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        RatingBar = (RatingBar) findViewById(R.id.RatingBar);
        txtComment = (EditText) findViewById(R.id.editTextComment);
        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Submit();
            }
        });

    }

    private void Submit() {


            if(RatingBar.getRating() == 0.0){
                Toast.makeText(getApplicationContext(),"Rating bar cannot be empty",Toast.LENGTH_SHORT).show();
        }
            if(TextUtils.isEmpty(txtComment.getText().toString().trim())){
                Toast.makeText(getApplicationContext(),"Comment section cannot be empty",Toast.LENGTH_SHORT).show();}
            else {
                Toast.makeText(getApplicationContext(),"Submit Successful",Toast.LENGTH_SHORT).show();

            }

        }
    }








