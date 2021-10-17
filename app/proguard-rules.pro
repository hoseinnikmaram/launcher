-dontnote retrofit2.Platform
-keepattributes Signature
-keepattributes Exceptions
-dontwarn okio.
-dontnote retrofit2.Platform
-dontwarn okhttp3.
-dontwarn retrofit2.
-dontwarn com.squareup.picasso.
-keep class retrofit2.**
-keepattributes Signature
-keepattributes Exceptions
-keepclasseswithmembers class * { @retrofit2.http.* <methods>; }
-keepclasseswithmembers interface * { @retrofit2.* <methods>; }
-dontwarn javax.annotation.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
-dontwarn org.codehaus.mojo.animal_sniffer.*
-dontwarn okhttp3.internal.platform.ConscryptPlatform
-dontwarn org.conscrypt.ConscryptHostnameVerifier
-keep class com.example.launcher.model.**
