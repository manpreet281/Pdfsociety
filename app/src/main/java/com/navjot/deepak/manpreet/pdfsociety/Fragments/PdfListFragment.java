package com.navjot.deepak.manpreet.pdfsociety.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.navjot.deepak.manpreet.pdfsociety.Activities.PdfDetailActivity;
import com.navjot.deepak.manpreet.pdfsociety.Models.Pdf;
import com.navjot.deepak.manpreet.pdfsociety.R;
import com.navjot.deepak.manpreet.pdfsociety.Viewholders.PdfViewHolder;

public abstract class PdfListFragment extends Fragment {

    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<Pdf, PdfViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    public PdfListFragment() {}

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_all_pdfs, container, false);

        // [START create_database_reference]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END create_database_reference]

        mRecycler =  rootView.findViewById(R.id.messages_list);
        mRecycler.setHasFixedSize(true);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        // Set up FirebaseRecyclerAdapter with the Query
        Query PdfsQuery = getQuery(mDatabase);

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Pdf>()
                .setQuery(PdfsQuery, Pdf.class)
                .build();

        mAdapter = new FirebaseRecyclerAdapter<Pdf, PdfViewHolder>(options) {

            @Override
            public PdfViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

                Log.d(getString(R.string.tag), "PostListFragment PostViewHolder onCreateViewHolder");
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new PdfViewHolder(inflater.inflate(R.layout.item_pdf, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(PdfViewHolder viewHolder, int position, final Pdf model) {
                final DatabaseReference PdfRef = getRef(position);
                Log.d(getString(R.string.tag), "PostListFragment onBindViewHolder position: "+position);
                // Set click listener for the whole Pdf view
                final String PdfKey = PdfRef.getKey();
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(getString(R.string.tag), "PostListFragment onBindViewHolder viewHolder onClick");
                        // Launch PdfDetailActivity
                        Intent intent = new Intent(getActivity(), PdfDetailActivity.class);
                        intent.putExtra("pdfkey", PdfKey);
                        startActivity(intent);
                    }
                });

                // Determine if the current user has liked this Pdf and set UI accordingly
//                if (model.stars.containsKey(getUid())) {
//                    viewHolder.starView.setImageResource(R.drawable.ic_toggle_star_24);
//                } else {
//                    viewHolder.starView.setImageResource(R.drawable.ic_toggle_star_outline_24);
//                }

                // Bind Pdf to ViewHolder, setting OnClickListener for the star button
                viewHolder.bindToPdf(model, new View.OnClickListener() {
                    @Override
                    public void onClick(View starView) {
                        Log.d(getString(R.string.tag), "PostListFragment viewholder bindToPost onClick");
                        // Need to write to both places the Pdf is stored
                        DatabaseReference globalPdfRef = mDatabase.child(getString(R.string.DB_Pdfs)).child(PdfRef.getKey());
                        DatabaseReference userPdfRef = mDatabase.child("user-Pdfs").child(model.getUid()).child(PdfRef.getKey());

                        // Run two transactions
                        onStarClicked(globalPdfRef);
                        onStarClicked(userPdfRef);
                    }
                });
            }
        };
        mRecycler.setAdapter(mAdapter);
    }

    // [START Pdf_stars_transaction]
    private void onStarClicked(DatabaseReference PdfRef) {
        PdfRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Pdf p = mutableData.getValue(Pdf.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }

//                if (p.stars.containsKey(getUid())) {
//                    // Unstar the Pdf and remove self from stars
//                    p.starCount = p.starCount - 1;
//                    p.stars.remove(getUid());
//                } else {
//                    // Star the Pdf and add self to stars
//                    p.starCount = p.starCount + 1;
//                    p.stars.put(getUid(), true);
//                }

                // Set value and report transaction success
                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d(getString(R.string.tag), "PdfTransaction:onComplete:" + databaseError);
            }
        });
    }
    // [END Pdf_stars_transaction]


    @Override
    public void onStart() {
        super.onStart();
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }


    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public abstract Query getQuery(DatabaseReference databaseReference);

}
