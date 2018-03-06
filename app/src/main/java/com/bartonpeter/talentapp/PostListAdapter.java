package com.bartonpeter.talentapp;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petib on 2018. 02. 21..
 */

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.ViewHolder> {

    private Activity mActivity;
    private List<Content> mDataset;
    private DatabaseReference mDatabaseReference;
    private ArrayList<DataSnapshot> mSnapshotList;
    private String mUsername;
    private Uri mUri;



    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView author;
        public TextView body;
        public VideoView videoview;

        public ViewHolder(View v){
            super(v);
            author = v.findViewById(R.id.row_author_text_view);
            body = v.findViewById(R.id.row_post_text_view);
            videoview = v.findViewById(R.id.row_video_view);
        }
    }



    public PostListAdapter(List<Content> dataset, String username) {
        mDataset = dataset;
        mUsername = username;
    }


    @Override
    public PostListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View rowLayout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_row, parent, false);

        return new ViewHolder(rowLayout);
    }

    @Override
    public void onBindViewHolder(PostListAdapter.ViewHolder holder, int position) {

        Content content = mDataset.get(position);
        holder.author.setText(content.getAuthor());
        holder.body.setText(content.getPost());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }




        /*private ChildEventListener mListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            mSnapshotList.add(dataSnapshot);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };*/

}
