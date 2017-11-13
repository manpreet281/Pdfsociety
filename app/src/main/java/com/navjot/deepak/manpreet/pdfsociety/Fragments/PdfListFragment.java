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
import android.widget.ProgressBar;
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

    protected DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<Pdf, PdfViewHolder> mAdapter;
    protected RecyclerView mRecycler;
    protected LinearLayoutManager mManager;
    protected ProgressBar mProgressBar;
    public PdfListFragment() {}

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_all_pdfs, container, false);
        Log.d(getString(R.string.tag), "onCreateView: container: "+container);
        mProgressBar = rootView.findViewById(R.id.mProgressBar);

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
                mProgressBar.setVisibility(View.GONE);
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
                Log.d(getString(R.string.tag), "PdfKey: "+PdfKey);
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(getString(R.string.tag), "PostListFragment onBindViewHolder viewHolder onClick");
                        // Launch PdfDetailActivity
                        Log.d(getString(R.string.tag), "PdfKey: "+PdfKey);
                        Intent intent = new Intent(getActivity(), PdfDetailActivity.class);
                        intent.putExtra("pdfkey", PdfKey);
                        startActivity(intent);
                    }
                });
                viewHolder.bindToPdf(model);
            }
        };
        mRecycler.setAdapter(mAdapter);
    }

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
