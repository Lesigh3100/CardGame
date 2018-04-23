package com.kevin.android.cardgamev1.tools;


import android.net.Uri;



public class UriTools {


    // input resource id - aka R.drawable.cow - outputs resource' Uri
  public static Uri getResourceUri(int resId) {
        return Uri.parse("android.resource://"+"com.kevin.android.cardgamev1/res"+"/" + resId);
    }


}
