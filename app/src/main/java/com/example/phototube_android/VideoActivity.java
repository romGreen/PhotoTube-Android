package com.example.phototube_android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.phototube_android.entities.UserManager;

public class VideoActivity extends AppCompatActivity {
    private VideoView videoView;
    private TextView videoNameTextView, authorTextView;
    private EditText commentEditText;
    private Button submitCommentButton;

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
        commentEditText = findViewById(R.id.commentEditText);
        submitCommentButton = findViewById(R.id.submitCommentButton);

        // Assume UserManager has a method isLoggedIn()
        if (UserManager.getInstance().isLoggedIn()) {
            commentEditText.setVisibility(View.VISIBLE);
            submitCommentButton.setVisibility(View.VISIBLE);

            submitCommentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    postComment(commentEditText.getText().toString());
                    commentEditText.setText("");  // Clear input after posting
                }
            });
        } else {
            commentEditText.setVisibility(View.GONE);
            submitCommentButton.setVisibility(View.GONE);
        }
    }

    private void postComment(String comment) {
        // Logic to post comment
        // Update the comments view or notify the user of successful posting
        Toast.makeText(this, "Comment posted", Toast.LENGTH_SHORT).show();
        // You might also want to add this comment to a view that displays all comments
    }

}
