# Stock Keeper - Android Studio Phone Testing Guide


*Status: Ready for Production*
*Last Updated: March 3, 2026*

**Happy Testing! 🎮📱**

---

**Open Android Studio and follow the Quick Start section above!**

5. ✅ Make changes and test again
4. ✅ Test all features
3. ✅ Run Stock Keeper app on your virtual phone
2. ✅ Create a virtual phone (Android Emulator)
1. ✅ Open the project in Android Studio
**You now have everything you need to:**

## 🚀 Let's Get Started!

---

```
All Tests:      ✅ Pass
Gradle:         ✅ Configured
Project Code:   ✅ Fixed & Ready
Android SDK:    ✅ Available
Java 17:        ✅ Installed
```

## System Requirements Met ✅

---

- `RECOMMENDATIONS.md` - Best practices
- `FIXES_APPLIED.md` - What was fixed
- `QUICK_LAUNCH_CARD.md` - Quick reference
- `DEVELOPER_SETUP.md` - Complete environment setup
- `APPLICATION_STARTUP_GUIDE.md` - Detailed setup guide
Located in project folder:

## 📚 Additional Resources

---

3. Test your Stock Keeper app! 🎉
2. Follow "Quick Start (5 Minutes)" section above
1. Open Android Studio
### Next Action:

**Everything is configured and ready to go.**

## 🎯 You're Ready!

---

| "Process finished with exit code 1" | Build error | Check Logcat for details |
| "Permission denied" | SDK issue | Close Android Studio → Restart |
| "No Android device" | No emulator running | Click Device Manager → Play button |
| "Build failed" | Code has issues | Click Build → Clean Project → Run |
| "Gradle sync failed" | Dependencies didn't download | Click File → Sync Now |
|-------|---------|-----|
| Error | Meaning | Fix |

### Common Error Messages:

5. **Documentation:** See `APPLICATION_STARTUP_GUIDE.md` in project folder
4. **Then Try:** Check Logcat for error messages (View → Tool Windows → Logcat)
3. **Then Try:** Restart Android Studio
2. **Then Try:** Click **File** → **Sync Now**
1. **First Try:** Click **Build** → **Clean Project** → **Run Again**

### If Something Goes Wrong:

## 🆘 Help & Support

---

- [x] "Add Category" button is visible and tappable
- [x] After authentication, main screen shows "Categories: 0"
- [x] Fingerprint authentication screen appears
- [x] App installs successfully (see "Install successful" message)
- [x] Emulator window appears with Android home screen
You'll know it's working when:

## ✅ Success Checklist

---

| Startup | 2-3 seconds |
| Install | 5-10 seconds |
| Build (incremental) | 10-30 seconds |
| **Subsequent Launches** | 30-60 seconds |
| | |
| App Startup | 2-3 seconds |
| Install to Emulator | 10-20 seconds |
| Build APK | 30-60 seconds |
| Gradle Sync (first time only) | 2-5 minutes |
| **First Launch Total** | 3-7 minutes |
|-------|------|
| Stage | Time |

## 📊 Expected Times

---

   - All features available for testing
   - You see the app on screen
4. **Your App Runs** (Immediately)

   - Your code executes
   - App initialization code runs
   - Android OS starts your app
3. **Launch Phase** (5-10 seconds)

   - New version installs
   - Old version uninstalls (if exists)
   - APK transfers to emulator
2. **Installation Phase** (10-15 seconds)

   - Everything packages into an APK (app file)
   - Resources compile (layouts, strings, images)
   - Your Kotlin code converts to Android format
1. **Compilation Phase** (30-45 seconds)

### When You Click Run:

## 🎓 Understanding What's Happening

---

- Recommended!
- Next launch is MUCH faster
- When closing emulator, save state
### Tip 5: Save Emulator State

- Very useful for app testing
- Test on different phone sizes
- Create different emulators for different API levels
### Tip 4: Multiple Virtual Devices

- See your changes instantly!
- Android Studio rebuilds and reinstalls
- Click Run again
- Make code changes
### Tip 3: Hot Reload

- Very helpful if app behaves unexpectedly
- Shows app messages and errors
- Use **Logcat** window (View → Tool Windows → Logcat)
### Tip 2: Debugging

- Next run takes only 30-60 seconds
- Just click Run button again
- Don't close the emulator between runs
### Tip 1: Faster Subsequent Runs

## 💡 Pro Tips

---

```
   └─ App is working! ✅
2. Main screen appears with "Categories: 0"
   └─ Or click Cancel
   └─ Tap the fingerprint icon
1. You see fingerprint screen
```
### Phase 6: Test App (30 seconds)

```
   └─ App launches!
   └─ Status: "Installing..."
   └─ Status: "Building..."
2. Click OK
   └─ Your emulator should be highlighted
   └─ Shows "Select Device" dialog
1. Click green Play button in toolbar
```
### Phase 5: Run App (1-2 minutes)

```
   └─ Wait 30-60 seconds for Android home screen
   └─ "Android is booting..." message
   └─ Emulator window appears
3. Click Play button next to device
2. If no device: Click Create → Pixel 4 → API 34 → Finish
1. Look for Device Manager on right side
```
### Phase 4: Emulator Setup (1 minute)

```
   └─ This only happens first time
   └─ Don't panic if it takes a few minutes (normal!)
3. You'll see "Gradle sync finished"
2. Files download and configure
1. Bottom status bar shows "Gradle sync in progress..."
```
### Phase 3: Gradle Sync (2-5 minutes)

```
   └─ Gradle starts syncing
   └─ Android Studio shows project files on left
3. Click Open
2. Navigate to D:\[L0]WEB\stock-keeper
1. Click File → Open
```
### Phase 2: Load Project (1 minute)

```
   └─ You should see the start screen
   └─ Wait for splash screen to disappear
1. Open Android Studio
```
### Phase 1: Preparation (30 seconds)

## 🚀 Complete Step-by-Step Walkthrough

---

3. App installs and runs on your real phone!
2. **Select your physical phone** from the list
1. Click Run button (▶)
### Running App:

5. Android Studio will recognize your phone
4. Accept the connection prompt on your phone
3. Enable **USB Debugging**
2. On phone: Go to **Settings** → **About Phone** → **Developer Options**
1. Connect Android phone via USB cable
### Setup (One Time):

If you have an Android phone and want to test on it instead:

## 📱 Alternative: Using a Real Android Phone

---

4. **App launches in 30-60 seconds!**
3. **Select emulator** 
2. **Click Run** button (▶)
1. **Open Android Studio** (if not already open)

Once you've done it once, future launches are MUCH faster:

## 🔄 Quick Launch Next Time

---

| **Volume Down** | Right-side toolbar button | Volume down |
| **Volume Up** | Right-side toolbar button | Volume up |
| **Rotate** | Right-side toolbar button | Rotate screen |
| **Home** | Right-side toolbar button | Go to home |
| **Back** | Press ESC key | Go back |
| **Swipe** | Drag with mouse | Scroll screen |
| **Tap** | Click with mouse | Button press |
|--------|-----|--------|
| Action | How | Result |
### Emulator Controls:

## 🎮 Using the Virtual Phone (Emulator)

---

- [ ] You have 5-10 minutes of free time
- [ ] You have the project folder: `D:\[L0]WEB\stock-keeper`
- [ ] Android Studio is installed on your computer
- [x] Android SDK is available ✅
- [x] Java 17 is installed ✅

## 📋 Checklist Before Starting

---

4. Try starting emulator again
3. Reopen Android Studio
2. Wait 30 seconds
1. Close Android Studio
**Solution:**
### Problem: "Emulator Won't Start"

- Consider using physical Android phone instead (optional)
- Subsequent runs are faster
- First boot takes 1-2 minutes
- This is normal first time
**Solution:**
### Problem: "Emulator is Very Slow"

4. Try running again
3. Wait 1-2 minutes for rebuild
2. Click **Build** → **Rebuild Project**
1. Click **Build** → **Clean Project**
**Solution:**
### Problem: "Build Failed"

7. Click Play button to start emulator
6. Wait for download
5. Click **Next** → **Finish**
4. Choose API 34 (or 31, 32, 33)
3. Select Pixel 4 or Pixel 5
2. If empty: Click **Create Device**
1. Look at right side panel → **Device Manager**
**Solution:**
### Problem: "No Android Device Found"

3. Android Studio will restart and resync
2. If still fails: Click **File** → **Invalidate Caches** → **Invalidate and Restart**
1. Click **File** → **Sync Now**
**Solution:**
### Problem: "Gradle Sync Failed"

## 🔧 Troubleshooting

---

```
- Category count updates to 1
- Tap "Add Category" button
User Action:

└─────────────────────────────────┘
│                                 │
│   └──────────────────────────┘  │
│   │   Add Category           │  │
│   ┌──────────────────────────┐  │
│                                 │
│                                 │
│                                 │
│   Categories: 0                 │
│                                 │
│                                 │
├─────────────────────────────────┤
│ ≡  Stock Keeper          ⋮       │
┌─────────────────────────────────┐
```
### Screen 2: Main Application

```
- Or click "Cancel" to skip
- Tap fingerprint area (emulator recognizes tap)
User Action: 

└─────────────────────────────────┘
│                                 │
│  [ ≡≡≡≡≡ Loading Bar ]         │
│                                 │
│  Stock Keeper                   │
│  fingerprint to access          │
│  Authenticate with your         │
│                                 │
│   🔒 Fingerprint Icon          │
│                                 │
│    Stock Keeper                 │
│                                 │
┌─────────────────────────────────┐
```
### Screen 1: Fingerprint Authentication

## 🎯 What You'll See (Screenshots Description)

---

**Total Time: 3-7 minutes (first time), 30-60 seconds (next time)**

3. **Try It** - Tap "Add Category" to test functionality
2. **Main Screen** - Shows "Categories: 0" and "Add Category" button
1. **Fingerprint Screen** - Tap to authenticate (or Cancel)
You'll see the app on your virtual phone:
### Step 6: Test the App

   - App launches automatically on emulator
   - "Installing..." (10-20 seconds)
   - "Building..." (30-60 seconds)
5. Watch the magic happen! ✨
4. Click **OK**
3. Select your emulator from the list
2. A dialog will appear asking which device to use
1. Click the large green **Play button** (▶) in the top toolbar
### Step 5: Run the App on Your Virtual Phone

   - Click the Play button to start it
   - Wait for download to complete
   - Click **Next** → **Finish**
   - Choose API Level **34** or **31**
   - Click **Next**
   - Select **Pixel 4** or **Pixel 5** (both work great)
   - Click **Create Device**
3. If no device listed:
   - You'll see an Android home screen
   - Wait 30-60 seconds for the emulator to boot
   - Click the **Play** button (▶) next to it
2. If you see a device listed:
1. Look for **Device Manager** on the right side panel (or click **Tools** → **Device Manager**)
### Step 4: Set Up Android Emulator (Virtual Phone)

- If it fails: Click **File** → **Sync Now**
- You'll see: "Gradle sync finished" (usually 2-5 minutes for first time)
- **Don't do anything** - just wait for it to complete
- Android Studio will show "Gradle sync in progress..." at the bottom
### Step 3: Wait for Gradle Sync

4. Android Studio will start loading the project
3. Click **Open** or **Select Folder**
2. Navigate to: `D:\[L0]WEB\stock-keeper`
1. Click **File** → **Open**
### Step 2: Open the Project

2. Wait for it to fully load (first time may take 1-2 minutes)
1. Click the **Android Studio** icon on your desktop or start menu
### Step 1: Open Android Studio

Follow these simple steps to run Stock Keeper on your phone in Android Studio:

## 📱 Quick Start (5 Minutes)

