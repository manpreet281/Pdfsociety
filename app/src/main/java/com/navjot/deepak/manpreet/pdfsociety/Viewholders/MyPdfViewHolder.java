package com.navjot.deepak.manpreet.pdfsociety.Viewholders;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.navjot.deepak.manpreet.pdfsociety.Models.Pdf;
import com.navjot.deepak.manpreet.pdfsociety.R;

public class MyPdfViewHolder extends RecyclerView.ViewHolder{

    public TextView pdfname;
    public TextView username;
    public ImageView deleteImage;
    public TextView pdfCategory;

    public MyPdfViewHolder(View itemView) {
        super(itemView);

        pdfname = itemView.findViewById(R.id.pdfname);
        username = itemView.findViewById(R.id.username);
        deleteImage = itemView.findViewById(R.id.deleteImageView);
        pdfCategory = itemView.findViewById(R.id.pdf_category);
    }

    public void bindToPdf(Pdf Pdf, View.OnClickListener deletePdfClickListener) {
        Log.d("Pdfsociety", "bindToPdf pdf: " + Pdf);
        pdfname.setText(Pdf.getPdfname());
        username.setText(Pdf.getUsername());
        pdfCategory.setText(Pdf.getCategories());
        deleteImage.setOnClickListener(deletePdfClickListener);
    }
}
