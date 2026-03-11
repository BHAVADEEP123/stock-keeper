# Stock Keeper - Android Studio Setup & Testing Guide

## Overview
Stock Keeper is an Android application built with Kotlin, featuring biometric authentication, local database support via Room, and cloud synchronization capabilities.

## System Requirements

### Minimum Requirements
- **Android Studio**: Latest version (2024.1 or higher recommended)
- **Java JDK**: Version 20 or higher
- **Gradle**: 8.7 or higher (automatically handled by wrapper)
- **Android SDK**: API 34 (Android 14)
- **Minimum Android Device/Emulator**: API 24 (Android 7.0)

### Recommended Setup
- **Java JDK**: 21 LTS (Long Term Support)
- **Gradle**: 8.7 or higher
- **Android SDK**: API 34+
- **Emulator Device**: Pixel 5 or Pixel 6 Pro (API 34)

## Setup Instructions

### 1. **Fix Gradle JVM Version Incompatibility** ✅ (Already Done)
The project has been updated to use **Gradle 8.7**, which is compatible with Java 21.

**What was changed:**
- Updated `gradle/wrapper/gradle-wrapper.properties`
- Changed from Gradle 8.4 to Gradle 8.7
- Gradle 8.7 supports Java versions 1.8 through 21

### 2. **Configure Java/JDK in Android Studio**

#### On Windows:
1. Open Android Studio
2. Go to **File → Settings → Build, Execution, Deployment → Gradle**
3. Under "Gradle JDK", select:
   - **Option A**: "jbr-21" or similar (Built-in JetBrains JDK)
   - **Option B**: Your installed Java 21 JDK path
4. Click **OK** to save

#### Alternative: Project Structure Dialog
1. **File → Project Structure**
2. Go to **SDK Location** tab
3. Set **Gradle JDK** to a compatible version (20 or 21)
4. Click **OK**

### 3. **Sync Gradle**
1. Click **File → Sync Now** (or Ctrl+Shift+A, type "Sync")
2. Wait for Gradle to download and sync dependencies (~2-5 minutes on first run)
3. Verify no errors appear in the Build panel

## Running the Application

### Method 1: Using Android Studio (Recommended)

#### Create Virtual Device (Emulator)
1. **Tools → Device Manager → Create Device**
2. Select device profile (e.g., Pixel 5)
3. Select system image:
   - API Level: **34** (Android 14) - Recommended
   - Minimum: API 24 (Android 7.0)
4. Click **Next** → **Finish**
5. Click the play icon to start the emulator

#### Run the App
1. Open `app/src/main/java/com/stockkeeper/app` folder
2. Or simply press **Shift + F10** (Run)
3. Select the emulator from the device list
4. Click **OK**

The app will build and launch on the emulator (~30-60 seconds for first build).

### Method 2: Using Physical Android Device

#### Prerequisites
- **Android Device** with API 24+ installed
- **USB Cable**
- **Developer Mode** enabled on device
- **USB Debugging** enabled

#### Steps
1. Connect device via USB
2. Press **Shift + F10** in Android Studio
3. Select your connected device
4. Click **OK**

## Application Features

### Authentication
- **Biometric Login** (Fingerprint/Face Recognition)
- Supports Android 9+ biometric API
- Secure credential storage using AndroidX Security

### Data Management
- **Local Storage**: SQLite via Room Database
- **Cloud Sync**: Retrofit for API integration
- **Background Tasks**: WorkManager for scheduled operations

### UI Components
- **View Binding**: Type-safe view references
- **Navigation**: Fragment-based navigation
- **Material Design 3**: Modern UI components

## Build Variants

### Debug Build
```bash
./gradlew assembleDebug
```
Output: `app/build/outputs/apk/debug/app-debug.apk`

### Release Build
```bash
./gradlew assembleRelease
```
Note: Requires signing configuration

## Troubleshooting

### Issue: "Gradle JVM version incompatible"
**Solution**: 
- Ensure Gradle 8.7+ is configured (✅ Already done)
- Set JDK to version 20 or 21 in Android Studio settings

### Issue: "Gradle sync failed"
**Solutions**:
1. Click **File → Invalidate Caches** → **Invalidate and Restart**
2. Click **File → Sync Now**
3. Check internet connection (first sync requires downloads)

### Issue: "Failed to install apk"
**Solutions**:
1. Ensure emulator/device has sufficient storage
2. Uninstall app: `adb uninstall com.stockkeeper.app`
3. Rebuild and reinstall

### Issue: "Compilation error in Kotlin files"
**Solutions**:
1. File → Invalidate Caches → Invalidate and Restart
2. Rebuild Project (Ctrl+Shift+Shift+B)

## Project Structure

```
stock-keeper/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/stockkeeper/app/  (Source code)
│   │   │   ├── res/                        (Resources)
│   │   │   └── AndroidManifest.xml
│   │   ├── test/                           (Unit tests)
│   │   └── androidTest/                    (Instrumented tests)
│   ├── build.gradle                        (App configuration)
│   └── proguard-rules.pro                  (Code obfuscation rules)
├── gradle/
│   └── wrapper/                            (Gradle wrapper files)
├── build.gradle                            (Project-level config)
├── settings.gradle                         (Module settings)
└── gradle.properties                       (Gradle properties)
```

## Gradle Configuration Details

### Current Build Settings
- **compileSdk**: 34
- **targetSdk**: 34
- **minSdk**: 24
- **Java Version**: 11
- **Kotlin Version**: 1.9.21

### Key Dependencies
- **Android Framework**: AndroidX Core, AppCompat, Material Design 3
- **Database**: Room 2.6.1
- **Networking**: Retrofit 2.10.0
- **Async**: Coroutines 1.7.3
- **Lifecycle**: AndroidX Lifecycle 2.7.0

## Testing

### Unit Tests
```bash
./gradlew test
```

### Instrumented Tests (on device/emulator)
```bash
./gradlew connectedAndroidTest
```

### Manual Testing on Emulator
1. Start emulator
2. Run app via Android Studio
3. Test biometric features:
   - Use emulator extended controls to simulate fingerprint
   - **Window → Show Extended Controls → Fingerprint**
4. Test database operations
5. Monitor logs: **View → Tool Windows → Logcat**

## Development Tips

### View Logcat Output
- **View → Tool Windows → Logcat** (or Alt+6)
- Filter by package: `com.stockkeeper.app`

### Debug the App
1. Set breakpoints (click line number)
2. Run with Debug: **Shift + F9**
3. Use Debug panel to inspect variables

### Monitor Resource Usage
- **View → Tool Windows → Profiler** (or Alt+6)
- Monitor CPU, Memory, Network usage

### Hot Reload (Faster Development)
1. After code changes, press **Ctrl+Shift+F10** (Run)
2. Use "Deploy" option for faster iteration (if available)

## Command Line Building (Alternative)

### Build APK
```bash
# Debug APK
./gradlew clean assembleDebug

# Release APK (requires signing configuration)
./gradlew clean assembleRelease
```

### Install APK to Device
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

### Run Tests
```bash
# Unit tests
./gradlew test

# Instrumented tests
./gradlew connectedAndroidTest
```

## Next Steps

1. ✅ Configure JDK in Android Studio (Java 21)
2. ✅ Sync Gradle (File → Sync Now)
3. ✅ Create Virtual Device or connect physical device
4. ✅ Run the app (Shift + F10)
5. ✅ Test biometric authentication
6. ✅ Explore the application features

## Additional Resources

- [Android Developers Official Docs](https://developer.android.com/)
- [Kotlin for Android Development](https://kotlinlang.org/docs/android-overview.html)
- [Room Database Guide](https://developer.android.com/training/data-storage/room)
- [Retrofit Documentation](https://square.github.io/retrofit/)
- [Biometric API Guide](https://developer.android.com/training/sign-in/biometric-auth)

## Support

For issues or questions:
1. Check the Logcat for error messages
2. Review build errors in the Build panel
3. Consult the troubleshooting section above
4. Check official Android documentation

---

**Last Updated**: March 2026
**Gradle Version**: 8.7+
**Java Version**: 21 (Recommended)
**Min Android SDK**: API 24
**Target Android SDK**: API 34

