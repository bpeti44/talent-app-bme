package com.bartonpeter.talentapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String USER_KEY = "USER_KEY";


    private List<VideoUploadInfo> mVideoList;
    private String mUsername;
    private DatabaseReference mDatabaseReference;
    private PostListAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NestedScrollView nested = findViewById(R.id.nested_scroll_view);
        nested.setFocusable(false);

        RatingBar ratingBar = findViewById(R.id.row_rating_bar);


        //Getting the username
        mUsername = getCurrentUsername();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabaseReference = database.getReference(PostContentActivity.Database_Path);
        mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setNestedScrollingEnabled(false);
        mVideoList = new ArrayList<>();

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        //mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new PostListAdapter(getApplicationContext(), mVideoList);
        mRecyclerView.setAdapter(mAdapter);
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot != null || dataSnapshot.getValue() != null){
                    try{

                        VideoUploadInfo videoUploadInfo = dataSnapshot.getValue(VideoUploadInfo.class);
                        if (videoUploadInfo.getSeason().equals("Summer")) {
                            mVideoList.add(0,videoUploadInfo);
                            mAdapter.notifyItemInserted(0);
                        }


                    }catch(Exception ex){
                        Log.e("TalentApp", ex.getMessage());
                    }
                }
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
        });
    }

    public void postContentActivity(View v){
        Intent intent = new Intent(this,PostContentActivity.class);
        startActivity(intent);
    }

    public String getCurrentUsername(){
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String email = currentUser.getEmail();
        int index = email.indexOf('@');
        String username = email.substring(0, index);
        return username;
    }

}
