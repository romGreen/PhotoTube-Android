package com.example.phototube_android.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.phototube_android.R;
import com.example.phototube_android.entities.UserManager;
import com.example.phototube_android.viewmodels.VideoInViewModel;

import java.io.File;

public class AddVideoActivity extends AppCompatActivity {
    private static final int VIDEO_PICK_CODE = 1001;

    private EditText editTextVideoName;
    private ImageButton buttonChooseVideo;
    private Button buttonUploadVideo;
    private TextView selectedVideo;
    private String videoUri; // URI for the video file

    private VideoInViewModel videoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);

        editTextVideoName = findViewById(R.id.editTextVideoName);
        buttonChooseVideo = findViewById(R.id.buttonChooseVideo);
        buttonUploadVideo = findViewById(R.id.buttonUploadVideo);
        selectedVideo = findViewById(R.id.selectedVideo);

        // Initialize the ViewModel
        videoViewModel = new ViewModelProvider(this).get(VideoInViewModel.class);

        buttonChooseVideo.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, VIDEO_PICK_CODE);
        });

        buttonUploadVideo.setOnClickListener(v -> {
            String videoName = editTextVideoName.getText().toString().trim();
            if (videoUri == null) {
                Toast.makeText(this, "Please select a video", Toast.LENGTH_LONG).show();
            } else if (videoName.isEmpty()) {
                Toast.makeText(this, "Video name must not be empty", Toast.LENGTH_LONG).show();
            } else {
                File videoFile = new File(videoUri);
                videoViewModel.addVideo(UserManager.getInstance().getUserId(), videoName, videoFile);
                startActivity(new Intent(this, MainActivity.class));
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
}
