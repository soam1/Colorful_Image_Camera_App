package com.akashsoam.colorfulimageapp;

import android.graphics.Bitmap;
import android.graphics.Color;

public class Colorful {
    private Bitmap bitmap;
    private float redColorValue;
    private float greenColorValue;
    private float blueColorValue;

    public Colorful(Bitmap bitmap, float redColorValue, float greenColorValue, float blueColorValue) {
        this.bitmap = bitmap;
        this.redColorValue = redColorValue;
        this.greenColorValue = greenColorValue;
        this.blueColorValue = blueColorValue;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setRedColorValue(float redColorValue) {
        if (redColorValue >= 0 && redColorValue <= 1)
            this.redColorValue = redColorValue;
    }

    public void setGreenColorValue(float greenColorValue) {
        if (greenColorValue >= 0 && greenColorValue <= 1)
            this.greenColorValue = greenColorValue;
    }

    public void setBlueColorValue(float blueColorValue) {
        if (blueColorValue >= 0 && blueColorValue <= 1)
            this.blueColorValue = blueColorValue;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public float getRedColorValue() {
        return redColorValue;
    }

    public float getGreenColorValue() {
        return greenColorValue;
    }

    public float getBlueColorValue() {
        return blueColorValue;
    }

    public Bitmap returnTheColorizedBitmap() {
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        Bitmap.Config bitmapConfig = bitmap.getConfig();
        Bitmap localBitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, bitmapConfig);
        for (int row = 0; row < bitmapWidth; row++) {
            for (int col = 0; col < bitmapHeight; col++) {
                int pixelColor = bitmap.getPixel(row, col);
                pixelColor = Color.argb(Color.alpha(pixelColor), (int) redColorValue * Color.red(pixelColor), (int) greenColorValue * Color.green(pixelColor), (int) blueColorValue * Color.blue(pixelColor));
                localBitmap.setPixel(row, col, pixelColor);
            }
        }
        return localBitmap;
    }
}

