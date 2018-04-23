package com.kevin.android.cardgamev1.tools;

import android.app.Activity;
import android.content.Context;
import android.view.WindowManager;


public class ScreenTools {


    public static void keepScreenOn(Activity activity) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
   public static void stopKeepingScreenOn(Activity activity) {
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

}
