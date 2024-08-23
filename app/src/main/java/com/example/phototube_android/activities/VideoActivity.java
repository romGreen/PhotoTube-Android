
package com.example.phototube_android.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.phototube_android.API.CommentOffApi;
import com.example.phototube_android.R;
import com.example.phototube_android.activities.EditVideoActivity;
import com.example.phototube_android.activities.FullscreenActivity;
import com.example.phototube_android.entities.UserManager;
import com.example.phototube_android.classes.Comment;
import com.example.phototube_android.classes.User;
import com.example.phototube_android.classes.Video;
import com.example.phototube_android.requests.LikeActionRequest;
import com.example.phototube_android.ui.adapters.CommentsAdapter;
import com.example.phototube_android.viewmodels.CommentInViewModel;
import com.example.phototube_android.viewmodels.CommentOffViewModel;
import com.example.phototube_android.viewmodels.VideoInViewModel;
import com.google.android.material.navigation.NavigationView;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class VideoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int REQUEST_CODE_EDIT_VIDEO = 1;
    private VideoView videoView;
    private TextView videoNameTextView, authorTextView, viewsTextView, timeAgoTextView;
    private EditText commentEditText;
    private RecyclerView commentsRecyclerView;
    private CommentsAdapter commentsAdapter;
    private ImageButton likeButton, dislikeButton;
    private TextView likeCountTextView;
    private ImageButton submitCommentButton;
    private static final int REQUEST_FULLSCREEN = 1;  // Request code for starting FullscreenActivity
    private boolean wasPlaying;
    private ImageButton fullscreenButton,shareButton,editButton;
    private String videoId;
    private String videoUrl;
    private VideoInViewModel videoInViewModel;
    private CommentInViewModel commentInViewModel;
    private CommentOffViewModel commentOffViewModel;
    private LinearLayout loginSection, homeSection,addVideoSection, registerSection;
    private DrawerLayout drawerLayout;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        Initialize();
        clickers();
    }

    private void Initialize()
    {
        drawerLayout = findViewById(R.id.drawer_layout);
        videoView = findViewById(R.id.video_view);
        videoNameTextView = findViewById(R.id.videoNameTextView);
        authorTextView = findViewById(R.id.authorTextView);
        viewsTextView = findViewById(R.id.viewsTextView);
        timeAgoTextView = findViewById(R.id.timeAgoTextView);
        commentEditText  = findViewById(R.id.commentEditText);
        submitCommentButton = findViewById(R.id.sumbit_Comment_Button);
        addVideoSection = findViewById(R.id.add_video_section);
        registerSection = findViewById(R.id.register_section);
        loginSection = findViewById(R.id.login_section);
        homeSection = findViewById(R.id.home_section);
        intent = getIntent();
        videoId = intent.getStringExtra("videoId");


        videoNameTextView.setText(intent.getStringExtra("Title"));
        authorTextView.setText(intent.getStringExtra("createdBy"));
        viewsTextView.setText(intent.getStringExtra("videoViews"));
        timeAgoTextView.setText(intent.getStringExtra("videoDate"));
        videoUrl = intent.getStringExtra("VideoUrl");
        Log.d("aaaadd", "Initialize: " + videoUrl);
        Uri uri = Uri.parse("http://10.0.2.2:"+intent.getStringExtra("VideoUrl"));
        // Set up the VideoView to play the video
        videoView.setVideoURI(uri);
        videoView.start(); // Start playing automatically
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);


        videoView.setMediaController(mediaController);
        fullscreenButton = findViewById(R.id.fullscreenButton);
        shareButton = findViewById(R.id.shareButton);

        editButton = findViewById(R.id.edit_Video_Button);
        String userId =  intent.getStringExtra("userId");
        if(UserManager.getInstance().isLoggedIn())
        {
            if(Objects.equals(UserManager.getInstance().getUserId(), userId))
                editButton.setVisibility(View.VISIBLE);
            else  editButton.setVisibility(View.GONE);

        }
        else  editButton.setVisibility(View.GONE);


        // Handle likes
        likeButton = findViewById(R.id.likeButton);
        dislikeButton = findViewById(R.id.dislikeButton);
        likeCountTextView = findViewById(R.id.likeCountTextView);



        List<Video.Like> videoLikes = (List<Video.Like>) intent.getSerializableExtra("videoLikes");
        updateLikes(videoLikes);


        commentsRecyclerView = findViewById(R.id.commentsRecyclerView);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentOffViewModel = new ViewModelProvider(this).get(CommentOffViewModel.class);
        getComments();

        videoInViewModel = new ViewModelProvider(this).get(VideoInViewModel.class);
        commentInViewModel = new ViewModelProvider(this).get(CommentInViewModel.class);
        setClickListeners();
        setupNavigationView();

        if (UserManager.getInstance().isLoggedIn()) {
            updateUserInfo();
        }
    }

    private void getComments()
    {
        commentOffViewModel.getComments(videoId);
        commentOffViewModel.getCommentData().observe(this, commentResponse -> {
            if (commentResponse.isSuccess() && commentResponse.getData() != null) {
                commentsAdapter = new CommentsAdapter(this, commentResponse.getData(),commentInViewModel);
                commentsRecyclerView.setAdapter(commentsAdapter); // Handle comments
            } else {
                Toast.makeText(this, "Failed to load user data: " + commentResponse.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        showCommentsIfLogin();
    }

    private void updateLikes(List<Video.Like> videoLikes)
    {
        int likeCount = 0;
        boolean userLiked = false;
        boolean userDisliked = false;

        for (Video.Like like : videoLikes) {
            if ("like".equals(like.getAction())) {
                likeCount++;
                if (like.getUserId().equals(UserManager.getInstance().getUserId())) {
                    userLiked = true;
                }
            }
            else{
                if ("dislike".equals(like.getAction())) {
                    if (like.getUserId().equals(UserManager.getInstance().getUserId())) {
                        userDisliked = true;
                    }
                }
            }
        }
        likeCountTextView.setText(String.valueOf(likeCount));

        likeButton.setImageResource(userLiked ? R.drawable.thumb_up_filled : R.drawable.thumb_up_40px);
        dislikeButton.setImageResource(userDisliked ? R.drawable.thumb_down_filled : R.drawable.thumb_down_40px);
    }

    private void clickers(){
        // Fullscreen
        fullscreenButton.setOnClickListener(v -> {
            if (intent.getStringExtra("VideoUrl") != null) {
                Intent intent = new Intent(VideoActivity.this, FullscreenActivity.class);
                intent.putExtra("VideoUrl", "http://10.0.2.2:"+videoUrl);

                startActivityForResult(intent, REQUEST_FULLSCREEN); // Start FullscreenActivity with the request code
            } else {
                // Handle null or invalid data case
                Toast.makeText(VideoActivity.this, "Video data is not available.", Toast.LENGTH_SHORT).show();
            }
        });


        // share button
        shareButton.setOnClickListener(v -> {
            if (UserManager.getInstance().isLoggedIn()) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String shareBody = "Choose share option";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(shareIntent, "Share via"));
            }
            Toast.makeText(VideoActivity.this, "You have to be logged in to share", Toast.LENGTH_SHORT).show();
        });

        //edit button

        editButton.setOnClickListener(view -> {

            Intent intent = new Intent(VideoActivity.this, EditVideoActivity.class);
            intent.putExtra("videoId", videoId); // Assuming videoId is the ID of the current video
            intent.putExtra("Title", videoNameTextView.getText()); // Assuming videoId is the ID of the current video
            intent.putExtra("VideoUrl", "http://10.0.2.2:"+getIntent().getStringExtra("VideoUrl")); // Assuming videoId is the ID of the current video
            intent.putExtra("CreatedBy", authorTextView.getText());
            intent.putExtra("Views", viewsTextView.getText());
            intent.putExtra("Date", timeAgoTextView.getText());
            intent.putExtra("UserId", getIntent().getStringExtra("userId"));
            intent.putExtra("CreatorImg",  getIntent().getStringExtra("creatorImg"));
            intent.putExtra("VideoLikes", (Serializable) getIntent().getSerializableExtra("videoLikes"));
            startActivity(intent);

        });

        submitCommentButton.setOnClickListener(v -> {
            addComment();
            closeKeyboard();
        });

        likeButton.setOnClickListener(v -> handleLike());
        dislikeButton.setOnClickListener(v -> handleDislike());
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (videoView.isPlaying()) {
            videoView.pause();
            wasPlaying = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (wasPlaying) {
            videoView.start();
            wasPlaying = false;
        }
        setupNavigationView();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_EDIT_VIDEO && resultCode == RESULT_OK) {
//            updateVideoUI(); // This method resumes playback where it was paused
        }
        if (requestCode == REQUEST_FULLSCREEN && resultCode == RESULT_OK && data != null) {
            int position = data.getIntExtra("currentPosition", 0); // Retrieve the current position of the video
            if (videoView != null) {
                videoView.seekTo(position);
                videoView.start();
            }
        }
    }



    private void handleLike() {
        if (UserManager.getInstance().isLoggedIn()) {
            LikeActionRequest LAR = new LikeActionRequest("like");
            videoInViewModel.likeAction(videoId, LAR);
            videoInViewModel.getLikeActionLiveData().observe(this, likeResponse -> {
                if (likeResponse.isSuccess() && likeResponse.getData() != null) {

                    updateLikes(likeResponse.getData().getLikes());
                } else {
                    Toast.makeText(this, "Failed to load user data: " + likeResponse.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
        else {
            Toast.makeText((this), "You must login for like", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleDislike() {
        if (UserManager.getInstance().isLoggedIn()) {
            LikeActionRequest LAR = new LikeActionRequest("dislike");
            videoInViewModel.likeAction(videoId, LAR);
            videoInViewModel.getLikeActionLiveData().observe(this, likeResponse -> {
                if (likeResponse.isSuccess() && likeResponse.getData() != null) {

                    updateLikes(likeResponse.getData().getLikes());
                } else {
                    Toast.makeText(this, "Failed to load user data: " + likeResponse.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
        else {
            Toast.makeText((this), "You must login for dislike", Toast.LENGTH_SHORT).show();
        }
    }


    private void addComment()
    {
        String commentText = commentEditText.getText().toString();
        if (!commentText.isEmpty()) {
            commentInViewModel.addComment(videoId,commentText);
            commentInViewModel.getAddCommentData().observe(this, commentResponse -> {
                if (commentResponse.isSuccess() && commentResponse.getData() != null) {
                    getComments();
                    // Clear the comment input field
                    commentEditText.setText("");
                    new Handler().postDelayed(() -> {
                        Toast.makeText(this, "Comment added successfully", Toast.LENGTH_SHORT).show();
                    }, 200); // Delay of 200 milliseconds
                } else {
                    Toast.makeText(this, "Failed to load user data: " + commentResponse.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        }
        else{
            Toast.makeText((this), "You must write to comment", Toast.LENGTH_SHORT).show();
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


    @Override
    protected void onStart() {
        super.onStart();
        updateMenuItems(UserManager.getInstance().isLoggedIn());
    }

    private void setClickListeners() {
        registerSection.setOnClickListener(v -> startActivity(new Intent(VideoActivity.this, RegisterActivity.class)));
        loginSection.setOnClickListener(v -> {
            Class<?> targetActivity = UserManager.getInstance().isLoggedIn() ? UserInfoActivity.class : LoginActivity.class;
            startActivity(new Intent(VideoActivity.this, targetActivity));
        });
        addVideoSection.setOnClickListener(v -> startActivity(new Intent(VideoActivity.this, AddVideoActivity.class)));
        homeSection.setOnClickListener(v -> {
            // Navigate to the Main Activity
            Intent homeIntent = new Intent(VideoActivity.this, MainActivity.class);
            startActivity(homeIntent);
        });
    }
    private void setupNavigationView() {
        updateMenuItems(UserManager.getInstance().isLoggedIn());
    }
    private void updateMenuItems(boolean isLoggedIn) {
        addVideoSection.setVisibility(isLoggedIn ? View.VISIBLE : View.GONE);
        registerSection.setVisibility(isLoggedIn ? View.GONE : View.VISIBLE);
        if (isLoggedIn) {
            updateUserInfo();
        }
    }

    private void updateUserInfo() {
        User user = UserManager.getInstance().getUser();
        if (user != null) {
            ImageView userImage = findViewById(R.id.user_image_view);
            TextView userName = findViewById(R.id.user_name_view);
            userImage.setVisibility(View.VISIBLE);
            userName.setText(user.getDisplayname());
            Glide.with(this).load("http://10.0.2.2:" + user.getProfileImg()).into(userImage);
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle Home click
        } else if (id == R.id.nav_login) {
            startActivity(new Intent(this, LoginActivity.class));
        } else if (id == R.id.nav_register) {
            startActivity(new Intent(this, RegisterActivity.class));
        } else if (id == R.id.nav_logout) {
            logoutClear();
        } else if (id == R.id.nav_add_video) {
            startActivity(new Intent(this, AddVideoActivity.class));
        } else if (id == R.id.nav_dark_mode) {
            toggleNightMode();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void toggleNightMode() {
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        AppCompatDelegate.setDefaultNightMode(currentNightMode == Configuration.UI_MODE_NIGHT_YES ?
                AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES);
        recreate();
    }
    private void logoutClear() {
        UserManager.getInstance().logout();
        clearUserInfo();
        updateMenuItems(false);
        updateUIForLogout();
        Toast.makeText(VideoActivity.this, "User Logout", Toast.LENGTH_LONG).show();
        restartActivity();
    }

    private void clearUserInfo() {
        ImageView userImage = findViewById(R.id.user_image_view);
        TextView userName = findViewById(R.id.user_name_view);
        userImage.setVisibility(View.GONE);
        userName.setText("");
    }

    private void updateUIForLogout() {
        addVideoSection.setVisibility(View.GONE);
        registerSection.setVisibility(View.VISIBLE);
        findViewById(R.id.user_image_view).setVisibility(View.GONE);
        TextView usernameTextView = findViewById(R.id.usernameTextView);
        if (usernameTextView != null) {
            usernameTextView.setText("");
        }
    }

    private void restartActivity() {
        Intent intent = new Intent(this, VideoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}

