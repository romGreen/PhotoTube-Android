package com.example.phototube_android;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.phototube_android.MainActivity;
import com.example.phototube_android.R;
import com.example.phototube_android.entities.Video;

public class AddVideoActivity extends Activity {
    private static int counterId = 11;

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int VIDEO_PICK_CODE = 1001;

    private EditText editTextVideoName;
    private EditText editTextAuthor;
    private Button buttonChooseImage;
    private Button buttonChooseVideo;
    private Button buttonUploadVideo;
    private String imageUri;
    private String videoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);

        editTextVideoName = findViewById(R.id.editTextVideoName);
        editTextAuthor = findViewById(R.id.editTextAuthor);
        buttonChooseImage = findViewById(R.id.buttonChooseImage);
        buttonChooseVideo = findViewById(R.id.buttonChooseVideo);
        buttonUploadVideo = findViewById(R.id.buttonUploadVideo);

        buttonChooseImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, IMAGE_PICK_CODE);
        });

        buttonChooseVideo.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("video/*");
            startActivityForResult(intent, VIDEO_PICK_CODE);
        });

        buttonUploadVideo.setOnClickListener(v -> {
            String videoName = editTextVideoName.getText().toString().trim();
            String author = editTextAuthor.getText().toString().trim();
            if (videoUri != null && imageUri != null) {
                uploadVideo(videoName, author, imageUri, videoUri);
            } else {
                Toast.makeText(AddVideoActivity.this, "Please select both an image and a video", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void uploadVideo(String videoName, String author, String imagePath, String videoPath) {
        Video newVideo = new Video(counterId, videoName, author, imagePath, videoPath);
        counterId++;
        MainActivity.addVideoToList(newVideo); // Ensure this method accepts Video objects
        Toast.makeText(this, "Video uploaded successfully!", Toast.LENGTH_SHORT).show();
        finish(); // Close this activity
    }


    // Inside AddVideoActivity when handling the result of image/video selection
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedUri = data.getData();
            String filePath = getPathFromUri(selectedUri); // Convert URI to file path

            if (requestCode == IMAGE_PICK_CODE) {
                imageUri = filePath; // Store image path as string
            } else if (requestCode == VIDEO_PICK_CODE) {
                videoUri = filePath; // Store video path as string
            }
        }
    }

    // Helper method to get path from URI
    private String getPathFromUri(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) return uri.toString();
        else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            String path = cursor.getString(idx);
            cursor.close();
            return path;
        }
    }
}
