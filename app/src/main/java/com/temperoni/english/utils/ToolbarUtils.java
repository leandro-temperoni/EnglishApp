package com.temperoni.english.utils;

import android.content.Context;

/**
 * Created by leandro.temperoni on 3/22/2016.
 */
public class ToolbarUtils {

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}
