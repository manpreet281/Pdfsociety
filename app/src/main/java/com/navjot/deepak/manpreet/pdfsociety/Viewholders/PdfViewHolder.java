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
    public TextView uploadDate;
    public TextView pdfCategory;

    public PdfViewHolder(View itemView) {
        super(itemView);
        pdfname = itemView.findViewById(R.id.pdfname);
        username = itemView.findViewById(R.id.username);
        pdfCategory = itemView.findViewById(R.id.pdf_category);
    }

    public void bindToPdf(Pdf Pdf) {
        pdfname.setText(Pdf.getPdfname());
        username.setText(Pdf.getUsername());
        pdfCategory.setText(Pdf.getCategories());
    }

}
