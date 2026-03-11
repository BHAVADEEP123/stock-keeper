## How to build and test the StockKeeper app in Android Studio

### 1. Open the project

- **Open Android Studio** (Hedgehog or newer is recommended).
- Choose **“Open”** and select the `stock-keeper` folder.
- Let Gradle sync finish. This can take a few minutes the first time.

### 2. Verify Gradle configuration

- In the **Gradle** tool window, make sure the app module is present.
- Check that the **Build Variant** is set to `app / debug`.
- If Gradle sync fails, click **“Try again”**; ensure you have:
  - Android SDK 34 installed.
  - Kotlin and Compose support enabled (Android Studio will prompt you if anything is missing).

### 3. Run the app on an emulator or device

1. Plug in a physical Android device with **USB debugging** enabled, or start an **Android Emulator** (API 24+).
2. In the toolbar, pick your device/emulator from the **device selector**.
3. Press **Run ▶** for the `app` configuration.
4. The app should install and launch, showing the **Sections** screen.

### 4. Basic functional test (sections and stocks)

1. **Create a section**
   - On the Sections screen, tap the **“+”** FAB.
   - Enter a section name (for example, `Long Term`, `Watchlist`, or `Options`) and tap **Add**.
   - You should see a card for the new section.

2. **Open the section**
   - Tap the section card.
   - You should see a **Stocks** screen for that section (initially empty).

3. **Add a stock (NSE/BSE only)**
   - Tap the **“+”** FAB on the Stocks screen.
   - Fill the form:
     - **Symbol**: e.g., `RELIANCE`, `TCS`, etc.
     - **Display name** (optional): a friendly name, e.g., `Reliance Industries`.
     - **Exchange**: tap `NSE` or `BSE` (only these two are supported).
     - **Alert price** (optional): a numeric value.
     - **Direction** (optional): tap `Above` or `Below` to define the alert direction.
   - Tap **Save**.
   - You return to the Stocks list and should see the new stock card.

4. **Edit / delete a stock**
   - Tap a stock card to edit it, change symbol/display/alert settings, and **Save**.
   - Tap the **trash** icon on a stock row to delete it.

### 5. Price polling and alerts (30‑minute worker)

The app uses **WorkManager** to refresh prices in the background every **30 minutes**:

- A periodic `PricePollingWorker` is scheduled from `StockKeeperApplication`.
- For each active stock, it:
  - Calls the configured stock price API.
  - Updates the stored `latestPrice`, `previousPrice`, and `lastUpdated`.
  - If `alertPrice` and `alertDirection` are set and the condition is met, it posts a **notification**.

#### Permissions

- On Android 13+ you will be prompted for **POST_NOTIFICATIONS** the first time a notification is shown.
- Make sure **notifications** are allowed for the app in system settings if you don’t see alerts.

#### For faster manual verification (developer shortcut)

Because waiting 30 minutes is inconvenient while testing:

1. Use **Run ▶** to launch the app.
2. After creating at least one stock with an alert:
   - Open **Logcat** to monitor background work.
   - From **Device Explorer** or **adb shell**, you can force-run the worker:
     - In Android Studio’s **Run/Debug** window, use **“Run selected worker”** (if available in your version) or:
     - From a terminal, run:

       ```bash
       adb shell cmd jobscheduler run -f com.stockkeeper.app 1
       ```

       (If job id differs, use the one shown in WorkManager logs.)

3. When the alert condition is met (latest price crosses the configured threshold), you should see a notification:
   - Title: `<DisplayName or Symbol> (NSE|BSE)`
   - Text: `Price is now <latestPrice>, which is above/below your target of <alertPrice>`.

> Note: The project uses a demo-friendly stock API configuration; for real prices you must plug in a valid key and ensure the symbol format for NSE/BSE matches your provider (e.g., `RELIANCE.NS`, `RELIANCE.BO`, etc.).

### 6. Common troubleshooting tips

- **Gradle sync errors**:
  - Update Android Gradle Plugin and Gradle wrapper to the versions recommended by Android Studio.
  - Ensure your internet connection works (for downloading dependencies).
- **App builds but crashes on launch**:
  - Check **Logcat** for stack traces.
  - Make sure your minimum API level is 24 or higher on the device/emulator.
- **No notifications appear**:
  - Confirm notification permission is granted.
  - Verify that at least one stock has `alertPrice` and a direction (`Above`/`Below`) set.
  - Ensure the device has network connectivity so the API call can succeed.

