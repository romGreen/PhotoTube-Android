package com.example.phototube_android;

import static com.example.phototube_android.MainActivity.videoAdapter;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.phototube_android.entities.Video;
import com.example.phototube_android.entities.VideoListManager;

public class EditVideoActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_PICK = 1;
    private static final int REQUEST_VIDEO_PICK = 2;
    private String selectedImagePath = "";
    private String selectedVideoPath = "";
    private EditText editVideoName;
    private EditText editVideoAuthor;
    private int videoId;
    private ImageView selectedThumbnail;
    private TextView selectedVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_video);

        editVideoName = findViewById(R.id.editVideoName);
        editVideoAuthor = findViewById(R.id.editVideoAuthor);
        selectedThumbnail = findViewById(R.id.selectedThumbnail);
        selectedVideo = findViewById(R.id.selectedVideo);
        Button saveButton = findViewById(R.id.saveButton);

        videoId = getIntent().getIntExtra("VIDEO_ID", -1);
        if (videoId != -1) {
            Video video = findVideoById(videoId);
            if (video != null) {
                editVideoName.setText(video.getVideoName());
                editVideoAuthor.setText(video.getAuthor());
                selectedImagePath = video.getImagePath();
                selectedVideoPath = video.getVideoPath();

                if (!selectedImagePath.isEmpty()) {
                    selectedThumbnail.setImageURI(Uri.parse(selectedImagePath));
                    selectedThumbnail.setVisibility(View.VISIBLE);
                }

                if (!selectedVideoPath.isEmpty()) {
                    selectedVideo.setText(selectedVideoPath);
                    selectedVideo.setVisibility(View.VISIBLE);
                }
            }
        }

        findViewById(R.id.selectImageButton).setOnClickListener(view -> selectImage());
        findViewById(R.id.selectVideoButton).setOnClickListener(view -> selectVideo());
        saveButton.setOnClickListener(v -> saveVideoDetails());
    }

    private Video findVideoById(int id) {
        for (Video video : VideoListManager.getInstance().getVideoList()) {
            if (video.getId() == id) {
                return video;
            }
        }
        return null;
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    private void selectVideo() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_VIDEO_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE_PICK:
                    selectedImagePath = getPathFromUri(data.getData());
                    selectedThumbnail.setImageURI(Uri.parse(selectedImagePath));
                    selectedThumbnail.setVisibility(View.VISIBLE);
                    break;
                case REQUEST_VIDEO_PICK:
                    selectedVideoPath = getPathFromUri(data.getData());
                    selectedVideo.setText(selectedVideoPath);
                    selectedVideo.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    private String getPathFromUri(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            return cursor.getString(column_index);
        }
        if (cursor != null) cursor.close();
        return null;
    }

    private void saveVideoDetails() {
        Video video = findVideoById(videoId);
        if (video != null) {
            video.setVideoName(editVideoName.getText().toString());
            video.setAuthor(editVideoAuthor.getText().toString());
            video.setImagePath(selectedImagePath);
            video.setVideoPath(selectedVideoPath);
        }
        Intent returnIntent = new Intent();
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}
