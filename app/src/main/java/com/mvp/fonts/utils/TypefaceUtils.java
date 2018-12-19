package com.mvp.fonts.utils;

import android.content.Context;
import android.graphics.Typeface;

import java.util.Hashtable;

public class TypefaceUtils {
    private static Hashtable sTypeFaces = new Hashtable(100);

    @SuppressWarnings("unchecked")
    public static Typeface getTypeFace(Context context, String path) {
        Typeface typeface = (Typeface) sTypeFaces.get(path);

        if (typeface == null) {
            if (Typeface.createFromFile(path) != null) {
                typeface = Typeface.createFromFile(path);
            } else {
                typeface = Typeface.DEFAULT;
            }

            sTypeFaces.put(path, typeface);
        }

        return typeface;
    }
}
