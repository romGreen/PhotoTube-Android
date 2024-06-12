package com.example.phototube_android;

import static com.example.phototube_android.MainActivity.videoAdapter;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_video);

        editVideoName = findViewById(R.id.editVideoName);
        editVideoAuthor = findViewById(R.id.editVideoAuthor);
        Button saveButton = findViewById(R.id.saveButton);

       /* buttonChooseVideo.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("video/*");
            startActivityForResult(intent, VIDEO_PICK_CODE);
        });*/

        videoId = getIntent().getIntExtra("VIDEO_ID", -1);
        if (videoId != -1) {
            Video video = findVideoById(videoId);
            if (video != null) {
                editVideoName.setText(video.getVideoName());
                editVideoAuthor.setText(video.getAuthor());
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
                    break;
                case REQUEST_VIDEO_PICK:
                    selectedVideoPath = getPathFromUri(data.getData());
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
        //setResult(RESULT_OK);
        finish();
    }
}
