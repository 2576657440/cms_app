package org.com.gannan.farminginfoplatform.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Administrator on 2016/11/14.
 */

public class Util {

    public static float dpToPx(Context context, float dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return density * dp + 0.5f;
    }
    public static float spToPx(Context context, float sp) {
        float density = context.getResources().getDisplayMetrics().scaledDensity;
        return density * sp+ 0.5f;
    }
    public static float pxToDp(Context context, float px) {
        float density = context.getResources().getDisplayMetrics().scaledDensity;
        return density / px+ 0.5f;
    }
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo packageInfo = null;
        try {
            PackageManager pm = context.getPackageManager();
            packageInfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return packageInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return packageInfo;
    }

    // 获取当前日期
    public static String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(c.getTime());
    }

    public static boolean isEmoChar(char codePoint) {
        return !((codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }
    // 校验Tag Alias 只能是数字,英文字母和中文
    public static boolean isValidTagAndAlias(String s) {
        Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_!@#$&*+=.|]+$");
        Matcher m = p.matcher(s);
        return m.matches();
    }
}
