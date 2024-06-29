
package com.example.phototube_android.activities;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.phototube_android.R;
import com.example.phototube_android.classes.Video;
import com.example.phototube_android.entities.UserManager;
import com.example.phototube_android.requests.VideoUpdateRequest;
import com.example.phototube_android.viewmodels.UserViewModel;
import com.example.phototube_android.viewmodels.VideoInViewModel;

import java.io.File;


public class EditVideoActivity extends AppCompatActivity {

    private static final int VIDEO_PICK_CODE = 1001;

    private EditText editVideoName;
    private ImageButton buttonChooseVideo;
    private Button buttonEditVideo;
    private TextView selectedVideo;
    private String videoUri; // URI for the video file
    private String videoId;
    private VideoInViewModel videoInViewModel;

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
          if (title.isEmpty()) {
                Toast.makeText(this, "Video name must not be empty", Toast.LENGTH_LONG).show();
            } else {
                File videoFile = new File(videoUri);
                VideoUpdateRequest VUR = new VideoUpdateRequest(title,videoFile);
                videoInViewModel.updateVideo(UserManager.getInstance().getUserId(), videoId,VUR);
                startActivity(new Intent(this, MainActivity.class));
            }
        });

        buttonChooseVideo.setOnClickListener(v -> {
            Intent intentUp = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intentUp, VIDEO_PICK_CODE);
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

