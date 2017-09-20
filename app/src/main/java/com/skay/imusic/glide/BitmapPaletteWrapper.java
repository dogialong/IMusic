package com.skay.imusic.glide;

import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;

/**
 * Created by longdg on 2017-08-29.
 */

public class BitmapPaletteWrapper {
    private final Bitmap mBitmap;
    private final Palette mPalette;

    public BitmapPaletteWrapper(Bitmap bitmap, Palette palette) {
        mBitmap = bitmap;
        mPalette = palette;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public Palette getPalette() {
        return mPalette;
    }
}
