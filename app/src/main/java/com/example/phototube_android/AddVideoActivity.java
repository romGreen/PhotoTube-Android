package com.example.phototube_android;

import static com.example.phototube_android.MainActivity.videoAdapter;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phototube_android.adapter.VideoAdapter;
import com.example.phototube_android.entities.Video;

public class AddVideoActivity extends Activity {
    private static int counterId = 11;

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int VIDEO_PICK_CODE = 1001;

    private EditText editTextVideoName;
    private EditText editTextAuthor;
    private ImageButton buttonChooseImage;
    private ImageButton buttonChooseVideo;
    private Button buttonUploadVideo;
    private ImageView selectedThumbnail;
    private TextView selectedVideo;
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
        selectedThumbnail = findViewById(R.id.selectedThumbnail);
        selectedVideo = findViewById(R.id.selectedVideo);

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

            if (videoUri == null) {
                Toast.makeText(AddVideoActivity.this, "Please select a video", Toast.LENGTH_LONG).show();

            } else if (imageUri == null) {
                Toast.makeText(AddVideoActivity.this, "Please select an image", Toast.LENGTH_LONG).show();

            } else if (videoName.isEmpty()) {
                Toast.makeText(AddVideoActivity.this, "video name must not be empty", Toast.LENGTH_LONG).show();

            } else if (author.isEmpty()) {
                Toast.makeText(AddVideoActivity.this, "author name must not be empty", Toast.LENGTH_LONG).show();

            } else {
                uploadVideo(videoName, author, imageUri, videoUri);
            }
        });
    }

    private void uploadVideo(String videoName, String author, String imagePath, String videoPath) {
        Video newVideo = new Video(counterId, videoName, author, "0 views", "today", imagePath, videoPath);
        counterId++;
        videoAdapter.addVideoToList(newVideo);
        Toast.makeText(this, "Video uploaded successfully!", Toast.LENGTH_SHORT).show();
        finish(); // Close this activity
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedUri = data.getData();
            String filePath = getPathFromUri(selectedUri); // Convert URI to file path

            if (requestCode == IMAGE_PICK_CODE) {
                imageUri = filePath; // Store image path as string
                selectedThumbnail.setImageURI(selectedUri);
                selectedThumbnail.setVisibility(View.VISIBLE);
            } else if (requestCode == VIDEO_PICK_CODE) {
                videoUri = filePath; // Store video path as string
                selectedVideo.setText("Selected Video: " + filePath);
                selectedVideo.setVisibility(View.VISIBLE);
            }
        }
    }

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
