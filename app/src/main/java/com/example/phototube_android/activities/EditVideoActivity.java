
package com.example.phototube_android.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.phototube_android.R;
import com.example.phototube_android.classes.Video;
import com.example.phototube_android.db.dao.VideoDao;
import com.example.phototube_android.entities.UserManager;
import com.example.phototube_android.requests.VideoUpdateRequest;
import com.example.phototube_android.viewmodels.UserViewModel;
import com.example.phototube_android.viewmodels.VideoInViewModel;

import java.io.File;
import java.io.Serializable;
import java.util.List;


public class EditVideoActivity extends AppCompatActivity {

    private static final int VIDEO_PICK_CODE = 1001;

    private EditText editVideoName;
    private ImageButton buttonChooseVideo;
    private ImageButton buttonDeleteVideo;

    private Button buttonEditVideo;
    private TextView selectedVideo;
    private String videoUri; // URI for the video file
    private String videoId;
    private VideoInViewModel videoInViewModel;
    private String[] arr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_video);
        initialize();
        clickerListen();
    }
    private void initialize() {
        editVideoName = findViewById(R.id.editTextVideoTitle);
        buttonChooseVideo = findViewById(R.id.buttonChooseVideoEdit);
        buttonEditVideo = findViewById(R.id.buttonEditVideo);
        buttonDeleteVideo = findViewById(R.id.buttonDeleteVideo);
        selectedVideo = findViewById(R.id.selectedVideoEdit);

        Intent intent = getIntent();
        // Retrieve the extras
        videoId = intent.getStringExtra("videoId");
        String title = intent.getStringExtra("Title");
        String videoUrl = intent.getStringExtra("VideoUrl");


        if (videoId != null) {
            editVideoName.setText(title);
            selectedVideo.setText(videoUrl);
            selectedVideo.setVisibility(View.VISIBLE);
            videoInViewModel =  new ViewModelProvider(this).get(VideoInViewModel.class);
        }

    }
    private void clickerListen() {
        buttonEditVideo.setOnClickListener(v -> {


            String title = editVideoName.getText().toString().trim();
            boolean file;
            if (title.isEmpty()) {
                Toast.makeText(this, "Video name must not be empty", Toast.LENGTH_LONG).show();
            } else{
                VideoUpdateRequest VUR;
                if (videoUri == null || videoUri.isEmpty()) {
                    file = false;
                    VUR = new VideoUpdateRequest(title, null);
                } else {
                    File videoFile = new File(videoUri);
                    if (!videoFile.exists()) {
                        Toast.makeText(this, "File path is not valid!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    file = true;
                   VUR = new VideoUpdateRequest(title, videoFile);
                }

                    videoInViewModel.updateVideo(UserManager.getInstance().getUserId(),file, videoId, VUR);
                    observeUpdateVideoResponse();

            }



        });

        buttonChooseVideo.setOnClickListener(v -> {
            Intent intentUp = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intentUp, VIDEO_PICK_CODE);
        });

        buttonDeleteVideo.setOnClickListener(v -> {
            if (videoId != null) {
                videoInViewModel.deleteVideo(UserManager.getInstance().getUserId(), videoId);
                observeDeleteVideoResponse();
            } else {
                Toast.makeText(this, "Video ID is not available", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri selectedUri = data.getData();
            if (selectedUri != null) {
                String filePath = getPathFromUri(selectedUri);
                if (requestCode == VIDEO_PICK_CODE) {
                    videoUri = filePath;
                    selectedVideo.setText("Selected Video: " + filePath);
                    selectedVideo.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private String getPathFromUri(Uri uri) {
        String[] projection = { MediaStore.Video.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        return null;
    }

    private void observeDeleteVideoResponse() {
        videoInViewModel.getVideoListData().observe(this, apiResponse -> {
            if (apiResponse.isSuccess()) {
                Toast.makeText(this, "Video deleted successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
            } else {
                Toast.makeText(this, "Failed to delete video: " + apiResponse.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void observeUpdateVideoResponse() {
        videoInViewModel.getVideoListData().observe(this, apiResponse -> {
            if (apiResponse.isSuccess()) {
                Toast.makeText(this, "Video updated successfully", Toast.LENGTH_SHORT).show();

                // Prepare an intent to navigate back to VideoActivity
                Intent intent2 = getIntent();
                Intent intent = new Intent(EditVideoActivity.this, VideoActivity.class);
                new Handler().postDelayed(() -> {
                // Force a refresh of the video data from the database
                new Thread(() -> {
                    Video currentVideo = videoInViewModel.getVideoByServerId(videoId);

                    runOnUiThread(() -> {
                        // Pass all necessary data to VideoActivity
                        intent.putExtra("videoId", videoId);
                        intent.putExtra("Title", editVideoName.getText().toString().trim());
                        intent.putExtra("createdBy", intent2.getStringExtra("CreatedBy"));
                        intent.putExtra("videoViews", intent2.getStringExtra("Views"));
                        intent.putExtra("videoDate", intent2.getStringExtra("Date"));
                        intent.putExtra("VideoUrl", currentVideo.getVideoUrl());
                        intent.putExtra("userId", intent2.getStringExtra("UserId"));
                        intent.putExtra("creatorImg", intent2.getStringExtra("CreatorImg"));
                        intent.putExtra("videoLikes", (Serializable) intent2.getSerializableExtra("VideoLikes"));

                        // Start VideoActivity
                        startActivity(intent);
                        finish(); // Finish EditVideoActivity to remove it from the back stack
                    });
                }).start();
            }, 1200);

            } else {
                Toast.makeText(this, "Failed to update video: " + apiResponse.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }



}