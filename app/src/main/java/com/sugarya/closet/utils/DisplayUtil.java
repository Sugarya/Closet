package com.sugarya.closet.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 */
public class DisplayUtil {

  public static int px2dip(float pxValue, Context context) {
    float scale = context.getResources().getDisplayMetrics().density;
    return (int) (pxValue / scale + 0.5f);
  }

  public static int dip2px(float dipValue, Context context) {
    float scale = context.getResources().getDisplayMetrics().density;
    return (int) (dipValue * scale + 0.5f);
  }

  public static float getScreenWidth(final Context context) {
    final DisplayMetrics metrics = getDisplayMetrics(context);
    return metrics.widthPixels;
  }

  public static float getScreenHeight(final Context context) {
    final DisplayMetrics metrics = getDisplayMetrics(context);
    return metrics.heightPixels;
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  public static int getMetricsSize(WindowManager windowManager) {
    if (windowManager == null) {
      return 0;
    }
    DisplayMetrics metrics = new DisplayMetrics();
    windowManager.getDefaultDisplay().getMetrics(metrics);
    return metrics.densityDpi;
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  public static String getDpi(WindowManager windowManager) {
    if (windowManager == null) {
      return "";
    }
    int densityDpi = getMetricsSize(windowManager);
    switch (densityDpi) {
      case DisplayMetrics.DENSITY_LOW:
        return "ldpi";
      case DisplayMetrics.DENSITY_MEDIUM:
        return "mdpi";
      case DisplayMetrics.DENSITY_HIGH:
        return "hdpi";
      case DisplayMetrics.DENSITY_XHIGH:
        return "xhpdi";
      case DisplayMetrics.DENSITY_XXHIGH:
      default:
        return "xxhdpi";
    }
  }

  public static boolean isPortrait(Context context) {
    return isPortrait(context.getResources().getConfiguration());
  }

  public static boolean isPortrait(Configuration newConfig) {
    if (newConfig != null && newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
      return false;
    }
    return true;
  }
  private static DisplayMetrics getDisplayMetrics(final Context context) {
    return context.getResources().getDisplayMetrics();
  }
}
