package com.navjot.deepak.manpreet.pdfsociety.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.navjot.deepak.manpreet.pdfsociety.Models.Pdf;
import com.navjot.deepak.manpreet.pdfsociety.R;

public class PdfDetailActivity extends AppCompatActivity {

    Button btnDownload;
    TextView userName;
    TextView pdfName;
    TextView pdfDescription;
    TextView downloadNo;

    private DatabaseReference mPdfReference;
    private ValueEventListener mPdfListener;
    private static String pdfid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_detail);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        pdfid = getIntent().getStringExtra("pdfkey");
        init();
        pdfListener();
    }

    public void init(){
        btnDownload = (Button)findViewById(R.id.btnDownload);
        userName = (TextView) findViewById(R.id.userName);
        pdfName = (TextView) findViewById(R.id.pdfName);
        pdfDescription = (TextView) findViewById(R.id.pdfDescription);
        downloadNo = (TextView) findViewById(R.id.downloadsNo);
        mPdfReference = FirebaseDatabase.getInstance().getReference().child(getString(R.string.DB_Pdfs)).child(pdfid);
    }

    public void pdfListener(){
        mPdfListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Pdf pdf = dataSnapshot.getValue(Pdf.class);
                Log.d(getString(R.string.tag), "onDataChange: "+pdf);
                pdfName.setText(pdf.getPdfname());
                pdfDescription.setText(pdf.getDescription());
                downloadNo.setText(pdf.getNo_of_downloads()+" downloads");
                userName.setText(pdf.getUsername());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(getString(R.string.tag), "loadPdf:onCancelled", databaseError.toException());
                Toast.makeText(PdfDetailActivity.this, "Failed to load pdf.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        mPdfReference.addValueEventListener(mPdfListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mPdfListener != null) {
            Log.d(getString(R.string.tag), "pdfListener stopped");
            mPdfReference.removeEventListener(mPdfListener);
        }
    }

    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }
}
