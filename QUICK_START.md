# Quick Start Guide - Stock Keeper

## 🚀 Quick Fix Applied

The Gradle incompatibility issue has been **automatically fixed**:
- ✅ Updated Gradle from **8.4** → **8.7**
- ✅ Now compatible with **Java 21**
- ✅ No manual version changes needed

## ⚡ 3-Minute Setup

### Step 1: Configure JDK in Android Studio
1. Open Android Studio
2. **File → Settings → Build, Execution, Deployment → Gradle**
3. Set **Gradle JDK** to: `jbr-21` or your Java 21 installation
4. Click **OK**

### Step 2: Sync Gradle
1. **File → Sync Now** 
2. Wait for completion (~2-5 minutes)

### Step 3: Run the App
1. Press **Shift + F10** (or click green Run button)
2. Select emulator or connected device
3. App will launch in ~30-60 seconds

## 📱 Emulator Setup (First Time Only)

1. **Tools → Device Manager → Create Device**
2. Pick: **Pixel 5** (or any device)
3. System Image: **API 34** (or API 24+)
4. Click play icon to start

## ✅ Verification

After sync completes, you should see:
- ✅ No "Gradle JVM version incompatible" error
- ✅ All dependencies downloaded
- ✅ Build folder created
- ✅ Green checkmark in Gradle sync

## 🛠️ Common Issues

| Issue | Fix |
|-------|-----|
| "Gradle sync failed" | File → Invalidate Caches → Restart, then Sync |
| "JVM incompatible" | Set JDK to 20+ in Gradle settings |
| "APK install failed" | Restart emulator or reconnect device |
| "Compilation error" | Rebuild Project (Ctrl+Shift+Shift+B) |

## 📖 Full Documentation

For complete setup details, see: **ANDROID_SETUP.md**

## 🎯 What's Next?

1. Test biometric authentication
2. Navigate through the app
3. Check Logcat (View → Logcat) for any warnings
4. Review app code in `app/src/main/java/com/stockkeeper/app/`

---

**Status**: ✅ Ready to Run  
**Gradle Version**: 8.7  
**Java Compatibility**: 1.8 - 21  
**Min SDK**: 24 (Android 7.0)  
**Target SDK**: 34 (Android 14)

