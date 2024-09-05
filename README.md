# PhotoTube_Android

PhotoTube_Android is the Android-based client for the PhotoTube social media platform, designed to interact seamlessly with the PhotoTube_Server's API endpoints. This mobile application provides an intuitive user interface that allows users to engage with features such as video creation, commenting, user profile customization, and more, directly from their Android devices.

## Features

### Offline Features
- **MainActivity**: Displays the first 10 most viewed videos along with an additional 10 randomly selected videos. Videos appear in a shuffled order for fresh user experience each visit.
- **RegisterActivity**: New users can register by navigating to the 'Register' section, filling in their details, and submitting the form.
- **LoginActivity**: Existing users can log in by going to the 'Login' section and entering their credentials.
- **VideoActivity**: Selecting a video will show it in full view with other suggested videos listed alongside.
- **UserPageActivity**: Clicking on a user's image under a video opens their profile page showcasing all their videos and highlighting the most viewed video.

### Login Features
- **VideoActivity**:
  1. Users can post, delete, and edit comments on videos.
  2. Users can like or dislike videos.
  3. Video creators have the option to edit or delete their videos.
- **UserInfoActivity**: Users can edit their profile details. Access this feature via the Profile section in the navigation menu post-login.
- **AddVideoActivity**: Authenticated users can upload new videos under their profile.

## Getting Started
### Sync and Build the Project:

After opening the project, allow Android Studio to automatically sync the project with Gradle files. This may take a few moments.
Build the project by selecting Build -> Make Project to ensure all dependencies are properly downloaded.


To make full use of PhotoTube_Android, ensure that the PhotoTube_Server is operational, as the app relies on its APIs for backend data.

### Prerequisites
- PhotoTube_Server running locally
- Android Studio and SDK set up for Android development
- An Android device or emulator

### Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/romGreen/PhotoTube_Android.git
   cd PhotoTube_Android


### Run the application:

Connect an Android device or set up an emulator through Device Manager.
Run the app by pressing the 'Run' button in Android Studio.
