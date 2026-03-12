# Retrofit relies on generic signatures and runtime annotations on service
# interfaces. Keep those metadata attributes so request/response adapters
# continue to resolve after shrinking.
-keepattributes Signature,InnerClasses,EnclosingMethod
-keepattributes RuntimeVisibleAnnotations,RuntimeVisibleParameterAnnotations
-keepattributes AnnotationDefault

# Keep Retrofit HTTP service interfaces. Obfuscation is fine, but removing the
# interfaces breaks dynamic proxy creation.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

# Keep Kotlinx Serialization generated serializers for DTOs used by Retrofit.
-keepclassmembers class com.futureworkshops.dotgolf.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keepclassmembers class com.futureworkshops.dotgolf.**$$serializer {
    *;
}
