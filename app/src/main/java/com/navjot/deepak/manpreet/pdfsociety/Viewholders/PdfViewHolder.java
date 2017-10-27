package com.navjot.deepak.manpreet.pdfsociety.Viewholders;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.navjot.deepak.manpreet.pdfsociety.Models.Pdf;
import com.navjot.deepak.manpreet.pdfsociety.Models.User;
import com.navjot.deepak.manpreet.pdfsociety.R;

public class PdfViewHolder extends RecyclerView.ViewHolder {

    public TextView pdfname;
    public TextView username;
    public TextView description;
    public TextView no_of_downloads;

    public static String userName;

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
        username.setText(getuser(Pdf.getUid()));
//        authorView.setText(Pdf.author);
//        numStarsView.setText(String.valueOf(Pdf.starCount));
//        bodyView.setText(Pdf.body);
//
//        starView.setOnClickListener(starClickListener);

    }

    public String getuser(String uid){

        FirebaseDatabase.getInstance().getReference()
                .child("Users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                User user = dataSnapshot.getValue(User.class);

                userName = user.getUsername();
                Log.d("Pdfsociety", "userName "+userName);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.d("Pdfsociety", "Failed to read value.", error.toException());
            }
        });
        return userName;
    }

}
