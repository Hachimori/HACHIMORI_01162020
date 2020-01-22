## How to build ##

### Debug variant ###

1. Android Studio > Build Variants (on lower left corner) > choose "debug" as Active Build Variant
2. Android Studio > Build > Build Bundle (s) / APK (s) > Build APK(s)


### Release variant ###

1. Android Studio > Build Variants (on lower left corner) > choose "release" as Active Build Variant
2. Android Studio > Build > Build Bundle (s) / APK (s) > Build APK(s)

* keystore file is located under ${rootProject}/app/, and signing configuration is specified in build.gradle.
  * Since they are temporarily created, there will be no security concerns for version-controlling them.


## Points to be improved  ##

* When the user tries to play the video, it freezes for the first 10 seconds.
  * Adjusting ExoPlayer's buffering configuration did not work.
  * It needs further ExoPlayer's API research for solving the issue.
* Configuration change handling. When the screen is rotated, or the app goes background while the video is played, the video keeps playing in the background.
  * Properly releasing/restoring the ExoPlayer's state in accordance with Activity lifecycle would solve the issue.