package com.example.phototube_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.phototube_android.entities.User;
import com.example.phototube_android.entities.UserManager;

public class VideoActivity extends AppCompatActivity {
    private VideoView videoView;
    private TextView videoNameTextView, authorTextView;
    private EditText commentEditText;
    private Button submitCommentButton;
    private TextView commentsContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        // Initialize views
        videoView = findViewById(R.id.video_view);
        videoNameTextView = findViewById(R.id.videoNameTextView);
        authorTextView = findViewById(R.id.authorTextView);

        // Get data from intent
        String videoPath = getIntent().getStringExtra("videoResource");
        String videoName = getIntent().getStringExtra("videoName");
        String author = getIntent().getStringExtra("author");
        int videoId = getIntent().getIntExtra("videoId", -1);
        if (videoId == -1) {
            finish();  // Exit the activity if no valid video ID is passed
            return;
        }
        // Set video information
        videoNameTextView.setText(videoName);
        authorTextView.setText(author);

        // Set up the VideoView to play the video
        Uri videoUri = Uri.parse(videoPath);
        videoView.setVideoURI(videoUri);
        videoView.start(); // Start playing automatically

        // Media controller
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        //comment handle
        commentsContent = findViewById(R.id.commentsContent);
        commentEditText = findViewById(R.id.commentEditText);
        submitCommentButton = findViewById(R.id.submitCommentButton);



        // if user logged in , show comment button
        if (UserManager.getInstance().isLoggedIn()) {
            commentEditText.setVisibility(View.VISIBLE);
            submitCommentButton.setVisibility(View.VISIBLE);
        } else {
            commentEditText.setVisibility(View.GONE);
            submitCommentButton.setVisibility(View.GONE);
        }

            submitCommentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postComment(videoId);
                }
            });

        // Now use this video ID for loading and posting comments
        loadComments(videoId);
        submitCommentButton.setOnClickListener(v -> postComment(videoId));
    }

    private void postComment(int videoId) {
        String comment = commentEditText.getText().toString();
        if (!comment.isEmpty()) {
            User currentUser = UserManager.getInstance().getUser();
            String userName = currentUser != null ? currentUser.getFirstName() : "Anonymous";
            String newComment = userName + ": " + comment + "\n";

            SharedPreferences prefs = getSharedPreferences("VideoComments", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            // Generate a unique key for each video by appending videoId to a string base
            String key = "comments_" + videoId;
            String existingComments = prefs.getString(key, "");
            existingComments += newComment;
            editor.putString(key, existingComments);
            editor.apply();

            commentsContent.append(newComment);
            commentEditText.setText(""); // Clear the input field after posting
        }
    }


    private void loadComments(int videoId) {
        SharedPreferences prefs = getSharedPreferences("VideoComments", MODE_PRIVATE);
        // Generate a unique key for each video by appending videoId to a string base
        String key = "comments_" + videoId;
        String savedComments = prefs.getString(key, "");
        commentsContent.setText(savedComments);
    }

}
