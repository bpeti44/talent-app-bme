package com.bartonpeter.talentapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String USER_KEY = "USER_KEY";


    private List<Content> mContent;
    private String mUsername;
    private DatabaseReference mDatabaseReference;
    private PostListAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String email = currentUser.getEmail();
        int index = email.indexOf('@');
        mUsername = email.substring(0,index);

        Log.d("TalentApp","ez az igazi user: " + mUsername);



        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabaseReference = database.getReference("posts");

        mRecyclerView = findViewById(R.id.my_recycler_view);
        mContent = new ArrayList<>();

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        //mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new PostListAdapter(mContent, mUsername);
        mRecyclerView.setAdapter(mAdapter);





        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot != null || dataSnapshot.getValue() != null){
                    try{

                        Content content = dataSnapshot.getValue(Content.class);
                        mContent.add(content);
                        mRecyclerView.scrollToPosition(mContent.size()-1);
                        mAdapter.notifyItemInserted(mContent.size()-1);

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
        intent.putExtra(USER_KEY, mUsername);
        startActivity(intent);
    }

}
