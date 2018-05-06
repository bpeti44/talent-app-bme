package com.bartonpeter.talentapp;

import android.app.Activity;
import android.content.Context;
import android.media.Rating;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by petib on 2018. 02. 21..
 */

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.ViewHolder> {

    private Activity mActivity;
    private DatabaseReference mDatabaseReference;
    private ArrayList<DataSnapshot> mSnapshotList;
    private String mUsername;
    private Uri mUri;
    private List<VideoUploadInfo> mVideoUploadInfoList;
    private static Context context;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public VideoView videoview;
        public RatingBar ratingbar;

         public ViewHolder(View v){
            super(v);
            username = v.findViewById(R.id.row_author_text_view);
            videoview = v.findViewById(R.id.row_video_view);
            ratingbar = v.findViewById(R.id.row_rating_bar);
        }
    }

    public PostListAdapter(Context context, List<VideoUploadInfo> videoList) {
        this.context = context;
        this.mVideoUploadInfoList = videoList;
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
    public void onBindViewHolder(final PostListAdapter.ViewHolder holder, int position) {

        VideoUploadInfo videoUploadInfo = mVideoUploadInfoList.get(position);

        holder.username.setText(videoUploadInfo.getUsername());

        try{
            Uri uri = Uri.parse(videoUploadInfo.getVideoURL());
            holder.videoview.setVideoURI(uri);
            holder.videoview.seekTo(100);
            holder.videoview.pause();

        }catch(Exception e){
            Log.d("TalentApp","Error: " + e.getMessage());
        }


        holder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.videoview.isPlaying()){
                    holder.videoview.pause();
                    Log.d("TalentApp","videoView onClick - pause");
                }else {
                    holder.videoview.start();
                    Log.d("TalentApp","videoView onClick - play");
                }
            }
        });



        /*holder.ratingbar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Log.d("TalentApp","ratingbar value = " + holder.ratingbar.getRating());
                }
                return true;
            }
        });*/

        holder.videoview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    if (holder.videoview.isPlaying()) {
                        holder.videoview.pause();
                        Log.d("TalentApp", "videoView onClick - pause");
                    } else {
                        holder.videoview.start();
                        Log.d("TalentApp", "videoView onClick - play");
                    }
                }
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return mVideoUploadInfoList.size();
    }

}
