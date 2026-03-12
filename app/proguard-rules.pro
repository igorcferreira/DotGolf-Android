# Preserve line numbers and source-file metadata for crash and bug-reporting
# tools such as Firebase Crashlytics.
-keepattributes SourceFile,LineNumberTable

# The app module has its own @Serializable navigation route. Keep generated
# serializers so route encoding/decoding survives shrinking.
-keepclassmembers class com.futureworkshops.dotgolf.app.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keepclassmembers class com.futureworkshops.dotgolf.app.**$$serializer {
    *;
}
