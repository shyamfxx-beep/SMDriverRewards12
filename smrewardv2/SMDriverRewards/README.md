# SM Driver Rewards - Client Agent

## Build Instructions:
1. Open this folder in Android Studio or push to GitHub.
2. The GitHub Actions workflow (`.github/workflows/build.yml`) has been updated to use `actions/upload-artifact@v4` to resolve the deprecation error.
3. Update the `serverIp` inside `C2Service.kt` with your C2 server's IP address.
4. Build the release APK using Gradle or GitHub Actions.
