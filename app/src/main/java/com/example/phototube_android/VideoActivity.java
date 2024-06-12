package com.example.phototube_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
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
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class VideoActivity extends AppCompatActivity {
    private VideoView videoView;
    private TextView videoNameTextView, authorTextView, viewsTextView, timeAgoTextView;
    private EditText commentEditText;
    private RecyclerView commentsRecyclerView;
    private CommentsAdapter commentsAdapter;
    private Video video;
    private ImageButton likeButton, dislikeButton;
    private TextView likeCountTextView;
    private ImageButton submitCommentButton;
    private static final int REQUEST_FULLSCREEN = 1;  // Request code for starting FullscreenActivity



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
        viewsTextView = findViewById(R.id.viewsTextView);
        timeAgoTextView = findViewById(R.id.timeAgoTextView);
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
        viewsTextView.setText(getIntent().getStringExtra("views"));
        timeAgoTextView.setText(getIntent().getStringExtra("timeAgo"));

        // Set up the VideoView to play the video
        videoView.setVideoURI(Uri.parse(getIntent().getStringExtra("videoResource")));
        videoView.start(); // Start playing automatically
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        // Handle comments
        commentsRecyclerView = findViewById(R.id.commentsRecyclerView);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        String loggedInUsername = UserManager.getInstance().isLoggedIn() ?
                UserManager.getInstance().getUser().getUsername() : "";
        commentsAdapter = new CommentsAdapter(this, video.getComments(),loggedInUsername);
        commentsRecyclerView.setAdapter(commentsAdapter);

        // Handle likes
        likeButton = findViewById(R.id.likeButton);
        dislikeButton = findViewById(R.id.dislikeButton);
        likeCountTextView = findViewById(R.id.likeCountTextView);
        updateButtonStates(video);
        setButtonListeners(video);
        updateLikeCountDisplay(video);

        // Fullscreen
        ImageButton fullscreenButton = findViewById(R.id.fullscreenButton);
        fullscreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (video.getVideoPath() != null && video != null) {
                    Intent intent = new Intent(VideoActivity.this, FullscreenActivity.class);
                    intent.putExtra("videoPath", video.getVideoPath());

                    startActivityForResult(intent, REQUEST_FULLSCREEN); // Start FullscreenActivity with the request code
                } else {
                    // Handle null or invalid data case
                    Toast.makeText(VideoActivity.this, "Video data is not available.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_FULLSCREEN && resultCode == RESULT_OK && data != null) {
            int position = data.getIntExtra("currentPosition", 0); // Retrieve the current position of the video
            if (videoView != null) {
                videoView.seekTo(position);
                videoView.start(); // Resume video playback
            }
        }
    }



    private void setButtonListeners(Video currentVideo) {
        likeButton.setOnClickListener(v -> handleLike(currentVideo));
        dislikeButton.setOnClickListener(v -> handleDislike(currentVideo));
    }

    private void handleLike(Video currentVideo) {
        if (UserManager.getInstance().isLoggedIn()) {
            User currentUser = UserManager.getInstance().getUser();
            if (currentVideo.isLikedBy(currentUser.getUsername())) {
                currentVideo.removeLike(currentUser.getUsername());
            } else {
                if (currentVideo.isDislikedBy(currentUser.getUsername())) {
                    currentVideo.removeDislike(currentUser.getUsername());  // Ensure to remove dislike if previously disliked
                }
                currentVideo.addLike(currentUser.getUsername()); // add like to count
            }
            updateButtonStates(currentVideo);
            updateLikeCountDisplay(currentVideo);
        }
        else {
            Toast.makeText((this), "You must login for like", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleDislike(Video currentVideo) {
        if (UserManager.getInstance().isLoggedIn()) {
            User currentUser = UserManager.getInstance().getUser();
            if (currentVideo.isDislikedBy(currentUser.getUsername())) {
                currentVideo.removeDislike(currentUser.getUsername());
            } else {
                if (currentVideo.isLikedBy(currentUser.getUsername())) {
                    currentVideo.removeLike(currentUser.getUsername());  // Ensure to remove like if previously liked
                }
                currentVideo.addDislike(currentUser.getUsername()); // add dislike to count
            }
            updateButtonStates(currentVideo);
            updateLikeCountDisplay(currentVideo);
        } else {
            Toast.makeText((this), "You must login for dis like", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateButtonStates(Video currentVideo) {
        if (UserManager.getInstance().isLoggedIn()) {
            User currentUser = UserManager.getInstance().getUser();
            likeButton.setImageResource(currentVideo.isLikedBy(currentUser.getUsername()) ? R.drawable.thumb_up_filled : R.drawable.thumb_up_40px);
            dislikeButton.setImageResource(currentVideo.isDislikedBy(currentUser.getUsername()) ? R.drawable.thumb_down_filled : R.drawable.thumb_down_40px);
        }
    }

    private void updateLikeCountDisplay(Video currentVideo) {
        likeCountTextView.setText(currentVideo.getLikes() + " ");
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