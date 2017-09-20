package com.skay.imusic.model.helper;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.CheckResult;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * Created by longdg on 2017-08-30.
 */

public final class TintHelper {
    public TintHelper() {
    }


    @NonNull
    private static ColorStateList getDisabledColorStateList(@ColorInt int normal, @ColorInt int disabled) {
        return new ColorStateList(new int[][]{{-16842910}, {16842910}}, new int[]{disabled, normal});
    }

    @CheckResult
    @Nullable
    public static Drawable createTintedDrawable(@Nullable Drawable drawable, @ColorInt int color) {
        if(drawable == null) {
            return null;
        } else {
            drawable = DrawableCompat.wrap(drawable.mutate());
            DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN);
            DrawableCompat.setTint(drawable, color);
            return drawable;
        }
    }

    @CheckResult
    @Nullable
    public static Drawable createTintedDrawable(@Nullable Drawable drawable, @NonNull ColorStateList sl) {
        if(drawable == null) {
            return null;
        } else {
            drawable = DrawableCompat.wrap(drawable.mutate());
            DrawableCompat.setTintList(drawable, sl);
            return drawable;
        }
    }

    public static void setCursorTint(@NonNull EditText editText, @ColorInt int color) {
        try {
            Field e = TextView.class.getDeclaredField("mCursorDrawableRes");
            e.setAccessible(true);
            int mCursorDrawableRes = e.getInt(editText);
            Field fEditor = TextView.class.getDeclaredField("mEditor");
            fEditor.setAccessible(true);
            Object editor = fEditor.get(editText);
            Class clazz = editor.getClass();
            Field fCursorDrawable = clazz.getDeclaredField("mCursorDrawable");
            fCursorDrawable.setAccessible(true);
            Drawable[] drawables = new Drawable[]{ContextCompat.getDrawable(editText.getContext(), mCursorDrawableRes), null};
            drawables[0] = createTintedDrawable(drawables[0], color);
            drawables[1] = ContextCompat.getDrawable(editText.getContext(), mCursorDrawableRes);
            drawables[1] = createTintedDrawable(drawables[1], color);
            fCursorDrawable.set(editor, drawables);
        } catch (Exception var9) {
            var9.printStackTrace();
        }

    }
}

