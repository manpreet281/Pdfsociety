package com.navjot.deepak.manpreet.pdfsociety.Viewholders;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.navjot.deepak.manpreet.pdfsociety.Models.Pdf;
import com.navjot.deepak.manpreet.pdfsociety.R;

public class PdfViewHolder extends RecyclerView.ViewHolder {

    public TextView pdfname;
    public TextView username;
    public TextView no_of_downloads;

    public PdfViewHolder(View itemView) {
        super(itemView);

        pdfname = itemView.findViewById(R.id.pdfname);
        username = itemView.findViewById(R.id.username);
        no_of_downloads = itemView.findViewById(R.id.no_of_downloads);
    }

    public void bindToPdf(Pdf Pdf, View.OnClickListener starClickListener) {
        Log.d("Pdfsociety", "bindToPdf pdf: " + Pdf);
        pdfname.setText(Pdf.getPdfname());
        no_of_downloads.setText(""+Pdf.getNo_of_downloads()+" downloads");
        username.setText(Pdf.getUsername());
//        authorView.setText(Pdf.author);
//        numStarsView.setText(String.valueOf(Pdf.starCount));
//        bodyView.setText(Pdf.body);
//
//        starView.setOnClickListener(starClickListener);
    }

}
