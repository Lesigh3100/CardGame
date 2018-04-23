package com.kevin.android.cardgamev1.tools;


import android.content.Context;
import android.widget.Toast;

import com.kevin.android.cardgamev1.R;

public class Toasts {
Context mContext;

    public static void toastSuccess(Context context) {
        Toast.makeText(context, R.string.success, Toast.LENGTH_SHORT).show();
    }

    public static void toastFailure(Context context) {
        Toast.makeText(context, R.string.failure, Toast.LENGTH_SHORT).show();
    }
    public static void toastValidationFail(Context context) {
        Toast.makeText(context, R.string.validation_failed, Toast.LENGTH_SHORT).show();
    }

    public static void toastInput(Context context, String string){
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }
}
