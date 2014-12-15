package com.mymonas.ngobrol.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by Huteri on 11/6/2014.
 */
public class Utils {

    public static float dpToPx(Context context, float dp) {
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, r.getDisplayMetrics());
        return px;
    }
}
