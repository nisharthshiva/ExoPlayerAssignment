
# Exoplayer Assignment

### Overview

The **MainActivity** class is responsible for managing the video playback and fullscreen functionality using ExoPlayer in an Android application.

### Features
* Video playback using ExoPlayer.
* Toggle between fullscreen and normal screen modes.
* Save and restore playback state during configuration changes (e.g., screen rotation).
* Release the ExoPlayer instance when the activity is stopped, paused, or destroyed to conserve resources.
### Usage
**Layout:** The layout for the activity is defined in the activity_main.xml file, which includes a PlayerView for displaying the video content and a fullscreen button.

**Fullscreen Toggle:** Tapping the fullscreen button toggles between fullscreen and normal screen modes. In fullscreen mode, the video player occupies the entire screen, hiding the system bars (status bar and navigation bar). In normal screen mode, the video player returns to its original size and displays the system bars.

**ExoPlayer Setup:** The preparePlayer() method initializes the ExoPlayer instance, sets up the media source (HLS stream), and prepares the player for playback.

**Playback State Handling:** The playback position and playWhenReady state are saved and restored during configuration changes (e.g., screen rotation) to provide a seamless playback experience.

**Release Resources:** The ExoPlayer instance is released when the activity is stopped, paused, or destroyed to release system resources and ensure proper cleanup.

### Configuration Changes
The activity handles configuration changes, such as screen rotation, by saving and restoring the playback state. The fullscreen mode is maintained across configuration changes, ensuring a consistent user experience.

### Compatibility
The MainActivity class is compatible with Android devices running API level 21 (Android 5.0) and above.

### External Dependencies
The class requires the ExoPlayer library for video playback. Make sure to include the necessary dependencies in your project's build.gradle file.

### groovy
```groovy
implementation 'com.google.android.exoplayer:exoplayer-core:<version>'
implementation 'com.google.android.exoplayer:exoplayer-ui:<version>'
```
### Example Usage
To use the **MainActivity** class in your Android project:

Define the layout for the activity (**activity_main.xml**) with a **PlayerView** for video playback and a fullscreen button.

Implement the **MainActivity** class by extending the Activity class and follow the provided code snippet to set up ExoPlayer, handle fullscreen toggling, and manage playback state.

Ensure proper integration of the ExoPlayer library by adding the required dependencies to your project's build.gradle file.

