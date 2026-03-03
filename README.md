# Stock Keeper Android Application

A comprehensive Android application for tracking stock prices with fingerprint authentication, local storage, and background polling capabilities.

## Features

### 1. **Fingerprint Authentication**
- Uses Android Biometric API for secure fingerprint authentication
- Prevents unauthorized access to the app
- Graceful fallback if biometric is not available

### 2. **Local Data Storage**
- SQLite database via Room ORM
- Persistent storage of:
  - Categories (for organizing stocks)
  - Stocks (with buy prices and current prices)
  - Price Alerts (trigger-based notifications)
  - Polling History (tracking price updates)

### 3. **Category Management**
- Create categories to organize stocks
- Each category can contain multiple stocks
- Easy categorization for portfolio management

### 4. **Stock Management**
- Add stocks with:
  - Stock name and symbol
  - Buy price (purchase price)
  - Current price (updated via polling)
  - Quantity
  - Notification preferences
  
### 5. **Price Alerts & Notifications**
- Set price alerts (above/below target)
- Automatic notifications when triggers are met
- Alert history tracking

### 6. **Background Polling**
- Polls stock prices every 5 minutes
- Uses WorkManager for reliable background execution
- Continues polling even after app is closed
- Handles network failures gracefully

### 7. **Network Awareness**
- Detects network status changes
- Continues polling when network becomes available
- Records failed attempts for recovery
- Gracefully handles offline scenarios

---

## 🚀 QUICK START - Run on Android Phone/Emulator

### ⚡ The Fast Way (5 minutes)

1. **Open Android Studio**
2. **File** → **Open** → Select `D:\[L0]WEB\stock-keeper`
3. **Wait** for Gradle sync (you'll see "Gradle sync finished")
4. **Device Manager** (right side) → Create device or click Play ▶
5. **Click green Run button** (▶) at top
6. **Select emulator** → **OK**
7. **Watch your app run on a virtual phone!** 🎉

**That's it! Takes 5 minutes total (including wait times).**

### 📱 What You'll See

**Screen 1 - Fingerprint Authentication:**
```
Stock Keeper
   🔒
Authenticate with your fingerprint
[Progress bar...]
```
→ Tap the fingerprint icon or click Cancel

**Screen 2 - Main Application:**
```
Categories: 0

[Add Category]
```
→ App is working! You can tap "Add Category" to test

### 📚 Full Guides Available

Choose based on your preference:

| Guide | Best For | Time |
|-------|----------|------|
| **QUICK_PHONE_TEST.md** | Fast learners | 2 min read |
| **README_PHONE_TESTING.md** | Complete reference | 10 min read |
| **VISUAL_PHONE_TESTING_GUIDE.md** | Visual learners | 10 min read |

### ⏱️ Time Breakdown

**First Time:**
- Android Studio opening: 2 min
- Project loading: 1 min  
- Gradle sync: 2-5 min (downloads)
- Device creation: 2-5 min (downloads)
- Emulator boot: 1-2 min
- Build & run: 1 min
- **Total: 10-20 minutes** ⏲️

**Next Times:**
- Click Run button: 30 seconds
- Build: 10-30 seconds
- Install: 5-10 seconds
- Launch: 2 seconds
- **Total: 30-60 seconds** ⚡

### 🎮 Control Your Virtual Phone

| Action | How |
|--------|-----|
| Tap button | Click with mouse |
| Swipe | Drag with mouse |
| Go back | Press ESC key |
| Home button | Right toolbar |
| Rotate phone | Right toolbar |

### ⚠️ If Something Goes Wrong

| Problem | Fix |
|---------|-----|
| Gradle won't sync | Click File → Sync Now |
| Build fails | Click Build → Clean → Run again |
| No device found | Device Manager → Create Device |
| Emulator slow | Normal first time - takes 1-2 min |
| App crashes | Check Logcat (View → Tool Windows → Logcat) |

### ✅ Success Indicators

You'll know it's working when:
- ✅ Emulator window opens showing Android
- ✅ App installs without errors
- ✅ Fingerprint screen appears on virtual phone
- ✅ After auth, you see "Categories: 0"
- ✅ You can tap "Add Category" button

### 🎯 System Requirements Met

| Requirement | Status |
|-------------|--------|
| Java 17 | ✅ Installed |
| Android SDK | ✅ Available |
| Project Code | ✅ All issues fixed |
| Build System | ✅ Ready |

---

## Project Structure

```
stock-keeper/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/stockkeeper/app/
│   │   │   │   ├── data/
│   │   │   │   │   ├── api/             # Retrofit API services
│   │   │   │   │   ├── db/              # Room database setup
│   │   │   │   │   │   ├── dao/         # Database access objects
│   │   │   │   │   │   └── entity/      # Data models
│   │   │   │   │   └── repository/      # Data repositories
│   │   │   │   ├── ui/                  # UI Activities & Fragments
│   │   │   │   │   ├── auth/            # Fingerprint auth
│   │   │   │   │   └── category/        # Category UI
│   │   │   │   ├── worker/              # Background workers
│   │   │   │   ├── receiver/            # Broadcast receivers
│   │   │   │   ├── notification/        # Notification management
│   │   │   │   └── utils/               # Utility functions
│   │   │   └── res/
│   │   │       ├── layout/              # XML layouts
│   │   │       ├── values/              # Strings, colors, styles
│   │   │       └── xml/                 # Backup & extraction rules
│   │   ├── AndroidManifest.xml
│   │   └── ...
│   ├── build.gradle
│   └── proguard-rules.pro
├── build.gradle
└── settings.gradle
```

## Dependencies

### Core Android Libraries
- `androidx.appcompat:appcompat` - AppCompat support
- `androidx.core:core` - Core Android functionality
- `com.google.android.material:material` - Material Design components

### Biometric Authentication
- `androidx.biometric:biometric` - Fingerprint/biometric auth

### Database
- `androidx.room:room-runtime` - Database ORM
- `androidx.room:room-ktx` - Room Kotlin extensions

### Background Tasks
- `androidx.work:work-runtime-ktx` - WorkManager for background jobs

### Networking
- `com.squareup.retrofit2:retrofit` - HTTP client
- `com.squareup.okhttp3:okhttp` - OkHttp networking
- `com.google.code.gson:gson` - JSON serialization

### Asynchronous Programming
- `org.jetbrains.kotlinx:kotlinx-coroutines` - Kotlin coroutines

### Lifecycle Management
- `androidx.lifecycle:lifecycle` - Lifecycle management

## Setup Instructions

### Prerequisites
- Android Studio (latest version)
- Android SDK 24 (API level 24) or higher
- Java 11+

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd stock-keeper
   ```

2. **Open in Android Studio**
   - File → Open → Select the stock-keeper folder

3. **Configure API Key**
   - Update `API_KEY` in `RetrofitClient.kt` with your stock API key
   - Recommended: Use Alpha Vantage API (https://www.alphavantage.co/)

4. **Build & Run**
   ```bash
   ./gradlew build
   ```
   - Or use Android Studio's Run button (Shift + F10)

## Configuration

### Stock API Configuration
Edit `app/src/main/java/com/stockkeeper/app/data/api/RetrofitClient.kt`:
```kotlin
private const val BASE_URL = "https://www.alphavantage.co/"
// Update API key in getStockApiService()
```

### Polling Interval
Edit `app/src/main/java/com/stockkeeper/app/worker/PricePollingWorker.kt`:
```kotlin
PeriodicWorkRequestBuilder<PricePollingWorker>(
    5, TimeUnit.MINUTES // Change interval as needed
)
```

### App Class Configuration
Update `AndroidManifest.xml` to use the Application class:
```xml
<application android:name=".StockKeeperApplication" ... >
```

## Usage

### First Launch
1. App will request fingerprint authentication
2. Authenticate with your registered fingerprint
3. You'll be taken to the main screen

### Adding Categories
1. Click "Add Category" button
2. Enter category name and description
3. Save

### Adding Stocks
1. Navigate to a category
2. Click "Add Stock"
3. Enter:
   - Stock name (e.g., "Apple Inc.")
   - Stock symbol (e.g., "AAPL")
   - Buy price
4. Save

### Setting Price Alerts
1. Select a stock
2. Create alert for "Above" or "Below" target price
3. You'll receive notifications when triggered

### Monitoring
- App automatically polls prices every 5 minutes
- Notifications appear when alerts trigger
- Polling continues even when app is closed (via WorkManager)

## Database Schema

### Categories Table
```
id (PK)
name
description
createdAt
updatedAt
```

### Stocks Table
```
id (PK)
categoryId (FK)
name
symbol
buyPrice
currentPrice
quantity
notificationEnabled
createdAt
updatedAt
```

### Price Alerts Table
```
id (PK)
stockId (FK)
triggerPrice
alertType (ABOVE/BELOW)
isTriggered
triggeredAt
createdAt
```

### Polling History Table
```
id (PK)
stockId (FK)
price
status (SUCCESS/FAILED/NO_NETWORK)
timestamp
```

## Network Handling

### Online Scenarios
- Polls fetch latest stock prices
- Updates database with new prices
- Triggers alerts if conditions are met

### Offline Scenarios
- Polling requests are retried when network becomes available
- Requests are logged in polling history with "NO_NETWORK" status
- App continues to function with cached data
- BroadcastReceiver monitoring for network state changes

## Troubleshooting

### Biometric Not Working
- Ensure device has fingerprint enrolled
- Check permissions in device settings
- App will gracefully continue without biometric

### Polling Not Working
- Verify internet connectivity
- Check API key configuration
- Review app logs for errors
- Ensure WorkManager has battery optimization disabled for the app

### Database Issues
- Clear app cache: Settings → Apps → Stock Keeper → Storage → Clear Cache
- Or reinstall the app (this will clear the database)

## Permissions

The app requires the following permissions:
- `INTERNET` - For API calls
- `ACCESS_NETWORK_STATE` - To check network status
- `USE_BIOMETRIC` - For fingerprint authentication
- `POST_NOTIFICATIONS` - For alert notifications
- `READ_EXTERNAL_STORAGE` - For potential file storage
- `WRITE_EXTERNAL_STORAGE` - For data backup

## Future Enhancements

- [ ] Portfolio performance analytics
- [ ] Export data to CSV
- [ ] Multiple alert types (percentage change, etc.)
- [ ] User profiles and cloud sync
- [ ] Advanced charting
- [ ] Push notifications via FCM
- [ ] Integration with multiple stock APIs

## Contributing

Feel free to fork and submit pull requests for any improvements.

## License

MIT License - Feel free to use for your projects.

## Support

For issues or questions, please create an issue in the repository.
