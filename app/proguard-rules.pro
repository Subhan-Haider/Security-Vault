# Project-specific ProGuard rules.

# Hilt / Dagger
-keep class dagger.** { *; }
-keep class hilt_aggregated_deps.** { *; }
-keep class dagger.hilt.** { *; }
-dontwarn dagger.hilt.**

# Room
-keep class androidx.room.** { *; }
-keepclassmembers class * extends androidx.room.RoomDatabase { *; }
-dontwarn androidx.room.**

# Firebase
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**

# CameraX
-keep class androidx.camera.** { *; }
-dontwarn androidx.camera.**

# Exp4j
-keep class net.objecthunter.exp4j.** { *; }
-dontwarn net.objecthunter.exp4j.**

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class com.bumptech.glide.** { *; }
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# AndroidX Core
-keep class androidx.core.app.CoreComponentFactory { *; }

# Prevent removal of View binding generated classes
-keepclassmembers class **.*Binding {
    public static ** inflate(...);
    public static ** bind(...);
}

# SQLCipher
-keep class net.sqlcipher.** { *; }
-keep class net.sqlcipher.database.** { *; }
-dontwarn net.sqlcipher.**

# AndroidX Security & Tink
-keep class androidx.security.crypto.** { *; }
-keep class com.google.crypto.tink.** { *; }
-dontwarn com.google.crypto.tink.**
