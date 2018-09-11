# Information to debug stack traces.
-renamesourcefileattribute SourceFile
-keepattributes SourceFile, LineNumberTable
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes Exceptions

###################################################################################################
#Firebase Crashlytics
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**

###################################################################################################
# Retrofit Proguard Configuration
# Retrofit does reflection on generic parameters and InnerClass is required to use Signature.
-keepattributes Signature, InnerClasses

# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**

# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit

-dontwarn com.squareup.okhttp.*
-dontwarn okio.**

-dontwarn okhttp3.**
-dontwarn org.conscrypt.**

# This rule may be not needed but use it if so. It´s adviced at
# https://github.com/square/okhttp/blob/5fe3cc2d089810032671d6135ad137af6f491d28/README.md#proguard
# A resource is loaded with a relative path so the package of this class must be preserved.
#-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

###################################################################################################