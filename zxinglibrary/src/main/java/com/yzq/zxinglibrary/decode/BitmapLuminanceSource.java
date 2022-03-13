package com.yzq.zxinglibrary.decode;

import android.graphics.Bitmap;

import com.google.zxing.LuminanceSource;

/**
 *
 * Bitmap LuminanceSource
 */
public class BitmapLuminanceSource extends LuminanceSource {

    private byte bitmapPixels[];

    public BitmapLuminanceSource(Bitmap bitmap) {
        super(bitmap.getWidth(), bitmap.getHeight());

        // get the contents of the pixel array for the image
        int[] data = new int[bitmap.getWidth() * bitmap.getHeight()];
        this.bitmapPixels = new byte[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getPixels(data, 0, getWidth(), 0, 0, getWidth(), getHeight());

        // Convert an int array to a byte array, take the blue part of the pixel value as the discrimination content
        for (int i = 0; i < data.length; i++) {
            this.bitmapPixels[i] = (byte) data[i];
        }
    }

    @Override
    public byte[] getMatrix() {
        // Returns the pixel data we generated
        return bitmapPixels;
    }

    @Override
    public byte[] getRow(int y, byte[] row) {
        // get the pixel data for the specified row
        System.arraycopy(bitmapPixels, y * getWidth(), row, 0, getWidth());
        return row;
    }
}
