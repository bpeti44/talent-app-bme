package com.bartonpeter.talentapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class PostContentActivity extends AppCompatActivity {

    public static final String USER_KEY = "USER_KEY";
    private static final int PICK_VIDEO_REQUEST = 234;
    private static int videoId = 0;

    private EditText mContentText;
    private Button mPostButton;
    private String mUsername;
    private VideoView mVideoView;

    private Uri mUri;
    private DatabaseReference mDatabaseReference;
    private StorageReference videoRef;
    private StorageReference mStorageReference;
    private FirebaseAuth mAuth;

    public static String Storage_Path = "All_Video_Uploads/";
    public static String Database_Path = "All_Video_Uploads_Database/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_content);


        mDatabaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);
        mStorageReference = FirebaseStorage.getInstance().getReference();




        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String email = currentUser.getEmail();
        int index = email.indexOf('@');
        mUsername = email.substring(0, index);

        mVideoView = findViewById(R.id.videoView);
        Log.d("TalentApp","username: " + mUsername);

    }

    public void postContent(View v){

        String input = mContentText.getText().toString();

        if(!input.equals("")){
            Content content = new Content(input, mUsername);
            mDatabaseReference.child("posts").push().setValue(content);
            mContentText.setText("");
            Toast.makeText(this,"Your post is uploaded!",Toast.LENGTH_SHORT).show();
            Log.d("TalentApp","post: " + content.getPost() + "username: " + content.getAuthor());
        }
    }

    public void showFileChooser(View v){
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Video"), PICK_VIDEO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(resultCode == RESULT_OK){
            mUri  = data.getData();
            try{
                MediaController mediaController = new MediaController(this);
                mediaController.setAnchorView(mVideoView);
                mVideoView.setMediaController(mediaController);
                mVideoView.setVideoURI(mUri);
                mVideoView.start();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public void uploadVideoToFirebaseStorage(View v){
        if(mUri != null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference storageReference2nd = mStorageReference.child(Storage_Path + System.currentTimeMillis() + ".mp4");

            storageReference2nd.putFile(mUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Video Uploaded Successfully ", Toast.LENGTH_LONG).show();
                    VideoUploadInfo videoUploadInfo = new VideoUploadInfo(mUsername, taskSnapshot.getDownloadUrl().toString());
                    String VideoUploadID = mDatabaseReference.push().getKey();

                    mDatabaseReference.child(VideoUploadID).setValue(videoUploadInfo);

                    Intent intent = new Intent(PostContentActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred())
                            / taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage("Uploaded: " + (int) progress + "%");
                }
            });
        }else{
            Log.d("TalentApp","No file");
            showErrorDialoge("Please select a video");
        }
    }

    public void upload(View v){
        if(mUri != null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            videoRef = storageRef.child("videos/" + mUsername + "/" + videoId++ + ".mp4");
            videoRef.putFile(mUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Video uploaded",Toast.LENGTH_LONG);


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Video upload failed: " +
                            e.getMessage(), Toast.LENGTH_LONG);
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred())
                            / taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage("Uploaded: " + (int) progress + "%");
                }
            });
        }else{
            Log.d("TalentApp","No file");
            showErrorDialoge("Please select a video");
        }
    }

    private void showErrorDialoge(String message){
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok,null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


}
