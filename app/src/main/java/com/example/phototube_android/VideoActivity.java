package com.example.phototube_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phototube_android.adapter.CommentsAdapter;
import com.example.phototube_android.entities.Comment;
import com.example.phototube_android.entities.CommentListManager;
import com.example.phototube_android.entities.User;
import com.example.phototube_android.entities.UserListManager;
import com.example.phototube_android.entities.UserManager;
import com.example.phototube_android.entities.Video;
import com.example.phototube_android.entities.VideoListManager;

import java.util.ArrayList;
import java.util.List;

public class VideoActivity extends AppCompatActivity {
    private VideoView videoView;
    private TextView videoNameTextView, authorTextView;
    private EditText commentEditText;
    private RecyclerView commentsRecyclerView;
    private CommentsAdapter commentsAdapter;
    public List<Comment> commentsList;
    private Video video;
    private ImageButton submitCommentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        Initialize();
        showCommentsIfLogin();
        submitCommentButton.setOnClickListener(v -> {
          addComment();
        });


    }

    private void Initialize()
    {
        videoView = findViewById(R.id.video_view);
        videoNameTextView = findViewById(R.id.videoNameTextView);
        authorTextView = findViewById(R.id.authorTextView);
        commentEditText  = findViewById(R.id.commentEditText);
        submitCommentButton = findViewById(R.id.sumbit_Comment_Button);
        int videoId = getIntent().getIntExtra("videoId", -1);
        if (videoId == -1) {
            finish();  // Exit the activity if no valid video ID is passed
            return;
        }

        for (Video videoC :  VideoListManager.getInstance(). getVideoList()) {
            if (videoC.getId() == videoId) {
                video = videoC;
                break;
            }
        }
        videoNameTextView.setText(getIntent().getStringExtra("videoName"));
        authorTextView.setText(getIntent().getStringExtra("author"));
        // Set up the VideoView to play the video
        videoView.setVideoURI(Uri.parse(getIntent().getStringExtra("videoResource")));
        videoView.start(); // Start playing automatically
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        commentsRecyclerView = findViewById(R.id.commentsRecyclerView);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        String loggedInUsername = UserManager.getInstance().isLoggedIn() ?
                UserManager.getInstance().getUser().getUsername() : "";
        commentsAdapter = new CommentsAdapter(this, video.getComments(),loggedInUsername);
        commentsRecyclerView.setAdapter(commentsAdapter);
    }

    private void addComment()
    {
        String commentText = commentEditText.getText().toString();
        if (!commentText.isEmpty()) {
            User currentUser = UserManager.getInstance().getUser();
            Comment newComment = new Comment(currentUser.getUsername(), commentText);
            commentsAdapter.addComment(newComment);
            commentEditText.setText("");  // Clear the input field
        }
    }

    private void showCommentsIfLogin()
    {
        // if user logged in , show comment button
        if (UserManager.getInstance().isLoggedIn()) {
            commentEditText.setVisibility(View.VISIBLE);
            submitCommentButton.setVisibility(View.VISIBLE);
        } else {
            commentEditText.setVisibility(View.GONE);
            submitCommentButton.setVisibility(View.GONE);
        }
    }

}