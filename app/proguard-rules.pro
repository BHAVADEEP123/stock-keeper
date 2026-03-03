# This is a configuration file for ProGuard.
# http://proguard.sourceforge.net/index.html#manual/configuration.html

# For Android SDK 28+, you can specify to keep all public constructors:
-keepclasseswithmembernames class * {
    native <methods>;
}

# Keep Retrofit annotations
-keepattributes *Annotation*
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# Keep Room annotations
-keep class androidx.room.** { *; }
-keepclasseswithmembers class * {
    @androidx.room.* <methods>;
}

# Keep OkHttp
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
-dontwarn okhttp3.**
-dontwarn okio.**

# Keep Gson
-keep class com.google.gson.** { *; }
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Keep your app's models
-keep class com.stockkeeper.app.data.** { *; }
