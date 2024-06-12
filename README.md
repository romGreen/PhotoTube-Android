# Introduction

We developed an Android application with various activities and adapters to manage videos, comments, and users efficiently. Below is a detailed explanation of the components and flow of our application.

## Project Structure

Our project is organized into different managers and adapters to ensure easy access and manipulation of videos, comments, and users.

### Adapters

- **VideoAdapter**: Handles video items in the video list.
- **CommentsAdapter**: Manages video comments in the comments section.

### List Managers

- **UserListManager**: Manages the list of users, enabling easy access to user details from anywhere in the app.
- **CommentsListManager**: Manages the list of comments for each video, allowing for easy editing and deletion.
- **VideoListManager**: Manages the video list, providing functionalities to save, edit, and delete videos.

### Additional Handlers

- **PhotoHandler**: Manages user images, ensuring proper handling and display of user photos.

## Activities

We created a separate activity for each screen to ensure a good separation of concerns between different parts of the application.

### MainActivity

- **Purpose**: Serves as the main screen displaying the video list.
- **Features**:
  - Displays a RecyclerView for videos.
  - Includes a search option with a filter function.
  - Provides a left and bottom menu for navigation.
  - Buttons change according to the user's login status.

### VideoActivity

- **Purpose**: Displays a selected video along with its details and comments.
- **Features**:
  - Video player with fullscreen option.
  - Displays video name, author, date, views, like and dislike buttons.
  - Edit and delete buttons for the video.
  - Comments section with the ability to add, edit, and delete comments.
  - Checks user login status to control access to like, dislike, edit, delete, and comments functionalities.
  - Button to exit fullscreen mode.

### AddVideoActivity

- **Purpose**: Allows logged-in users to add a new video to the video list.
- **Features**:
  - Provides a form to input video details.
  - Uses VideoAdapter to add the video to the list.
  - Checks in MainActivity if a user is logged in to enable the add video feature.

## Flow of the Application

- **Home Page (MainActivity)**: Displays all videos in the app with a navigation bar on the left and bottom. Users can search for videos using the filter function. The buttons and options available change depending on whether a user is logged in.
- **Video Page (VideoActivity)**: When a video is selected, this activity shows the video along with its details, comments, and interactive buttons. The access to various functionalities like liking, disliking, editing, and deleting is controlled based on the user's login status.
- **Add Video Page (AddVideoActivity)**: Logged-in users can navigate to this page to add new videos. The video is added to the list through the VideoAdapter.

## Conclusion

We structured our Android application using best practices to ensure clean separation of concerns and efficient management of videos, comments, and users. The project helped us learn a lot about Android development, managing state, and providing a seamless user experience. We are proud of our work and the learning experience it provided.
