package com.mymonas.ngobrol.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;


/**
 * Created by Huteri on 11/2/2014.
 */
public class ImageUtils {
    public static Bitmap getResizedBitmap(Bitmap bm, int boundWidth, int boundHeight) {

        int width = bm.getWidth();
        int height = bm.getHeight();
        int newWidth = boundWidth;
        int newHeight = boundHeight;

        if(width > boundWidth) {
            newWidth = boundWidth;
            newHeight = (newWidth * height) / width;
        }

        if(newHeight > boundHeight) {
            newHeight = boundHeight;
            newWidth = (newHeight * width) / height;
        }

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // Create a matrix for the manipulation
        Matrix matrix = new Matrix();

        // Resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);

        // Recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;

    }


}
