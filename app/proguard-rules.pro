# Information to debug stack traces.
-renamesourcefileattribute SourceFile
-keepattributes SourceFile, LineNumberTable
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes Exceptions

##################################
#Firebase Crashlytics
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**