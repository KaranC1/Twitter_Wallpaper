package com.example.karan.wallpaper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

import java.io.File;
import java.io.IOException;

/**
 * Created by Karan on 11/24/15.
 */public class BitmapHelper {

    public static Bitmap overlayIntoCentre(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);//draw background bitmap

        //overlay the second in the centre of the first
        //(may experience issues if the first bitmap is smaller than the second, but then why would you want to overlay a bigger one over a smaller one?!)
        //EDIT: added Y offest fix - thanks @Jason Goff!
        canvas.drawBitmap(bmp2, (bmp1.getWidth()/2)-(bmp2.getWidth()/2), (bmp1.getHeight()/2)-(bmp2.getHeight()/2), null);

        return bmOverlay;
    }

    public static Bitmap createNewBitmap(int width, int height)
    {
        //create a blanks bitmap of the desired width/height
        return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    }
}