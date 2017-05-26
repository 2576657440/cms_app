# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\AppData\Local\Android\android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#表示proguard对代码进行迭代优化的次数，Android一般为5
#-optimizationpasses 5
#关闭混淆
#-dontobfuscate
# 混淆时是否记录日志
#-verbose
#-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,LocalVariable
#不混淆注解
#-keepattributes *Table,*Annotation*,Synthetic,EnclosingMethod

#关闭压缩
-dontshrink
# 泛型不混淆混淆
-keepattributes Signature
#极光推送
-dontoptimize
-dontpreverify
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }

-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.**{*;}

-keep class android.support.v4.**{*;}
-keep class android.support.v7.**{*;}

-keep class com.zhy.http.okhttp.**{*;}
-keep class okhttp3.**{*;}
-dontwarn okio.**
-keep class com.dou361.ijkplayer.** {*;}

-keep class com.nostra13.universalimageloader.**{*;}
-keep class org.com.gannan.farminginfoplatform.widget.*{*;}
-keep class org.com.gannan.farminginfoplatform.entity.*{*;}
-keep class org.com.gannan.farminginfoplatform.viewimage.*{*;}
-keep class org.com.gannan.farminginfoplatform.baseadapter.refresh.*{*;}

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
    public <fields>;

}