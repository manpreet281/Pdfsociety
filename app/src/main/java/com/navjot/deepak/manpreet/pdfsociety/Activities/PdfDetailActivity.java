package com.navjot.deepak.manpreet.pdfsociety.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.navjot.deepak.manpreet.pdfsociety.R;

public class PdfDetailActivity extends AppCompatActivity {

    Button btnDownload;
    TextView userName;
    TextView pdfName;
    TextView pdfDescription;
    TextView downloadNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_detail);

        init();
    }

    public void init(){
        btnDownload = (Button)findViewById(R.id.btnDownload);
        userName = (TextView) findViewById(R.id.userName);
        pdfName = (TextView) findViewById(R.id.pdfName);
        pdfDescription = (TextView) findViewById(R.id.pdfDescription);
        downloadNo = (TextView) findViewById(R.id.downloadsNo);
    }
}
