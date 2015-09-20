package ru.kadei.diaryworkouts.managers;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.graphics.Typeface.createFromAsset;

/**
 * Created by kadei on 20.09.15.
 */
public class ResourceManager {

    private final Context context;
    private final Resources resources;

    private final Map<String, Typeface> fonts;
    private int counterRequestFont = 0; // for test

    public ResourceManager(Context context) {
        this.context = context;
        resources = context.getResources();

        fonts = new HashMap<>(16);
        loadFonts();
    }

    private void loadFonts() {
        AssetManager am = context.getAssets();
        try {
            String[] fileNames = am.list("");
            for (String s : fileNames)
                if (s.endsWith(".ttf")) fonts.put(s, createFromAsset(am, s));

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Resources getResources() {
        return resources;
    }

    public Typeface getFont(String name) {
        ++counterRequestFont;
        return fonts.get(name);
    }

    public Drawable getDrawable(@DrawableRes int id) {
        if (Build.VERSION.SDK_INT == 21)
            return resources.getDrawable(id, context.getTheme());
        else
            return resources.getDrawable(id);
    }

    public String[] getStringArray(@ArrayRes int id) {
        return resources.getStringArray(id);
    }

    public int[] getIntArray(@ArrayRes int id) {
        return resources.getIntArray(id);
    }

    public int getColor(@ColorRes int id) {
        return Build.VERSION.SDK_INT == 21
                ? resources.getColor(id, context.getTheme())
                : resources.getColor(id);
    }

    public ColorStateList getColorStateList(@ColorRes int id) {
        return  Build.VERSION.SDK_INT == 21
            ? resources.getColorStateList(id, context.getTheme())
            : resources.getColorStateList(id);
    }

    public int getDimensionPixelSize(@DimenRes int id) {
        return resources.getDimensionPixelSize(id);
    }

    public float getDimension(@DimenRes int id) {
        return resources.getDimension(id);
    }

    public int getCounterRequestFont() {
        return counterRequestFont;
    }
}
