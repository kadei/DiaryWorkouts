package ru.kadei.diaryworkouts.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import ru.kadei.diaryworkouts.activities.MainActivity;
import ru.kadei.diaryworkouts.managers.ResourceManager;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

/**
 * Created by kadei on 29.06.2015.
 */
public class DrawerSymbol {

    private ResourceManager resManager;

    private Paint paint;
    private float textSize = 1f;
    private float textWidth = 1f;
    private int height = 1;
    private int width = 1;

    private Canvas canvas = new Canvas();

    private final char[] buffSymbol = new char[]{'\0'};
    private final float[] buffWidth = new float[]{0f};

    public DrawerSymbol(MainActivity mainActivity) {
        resManager = mainActivity.getResourceManager();

        paint = new Paint(ANTI_ALIAS_FLAG);
        paint.setTypeface(resManager.getFont("roboto_regular.ttf"));
        paint.setColor(resManager.getColor(android.R.color.black));
        paint.setTextSize(textSize);

        buffSymbol[0] = '0';
        paint.getTextWidths(buffSymbol, 0, 1, buffWidth);
        textWidth = buffWidth[0];
    }

    public void config(int textSizeResID, int textColorResID, int dimenWidth, int dimenHeight) {
        width = resManager.getDimensionPixelSize(dimenWidth);
        height = resManager.getDimensionPixelSize(dimenHeight);

        textSize = resManager.getDimension(textSizeResID);
        paint.setTextSize(textSize);
        paint.setColor(resManager.getColor(textColorResID));
    }

    public Drawable draw(char ch) {
        buffSymbol[0] = ch;
        paint.getTextWidths(buffSymbol, 0, 1, buffWidth);
        textWidth = buffWidth[0];

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);

        float centerX = width / 2f;
        float centerY = height / 2f;
        canvas.drawText(buffSymbol, 0, 1,
                centerX - (textWidth / 2f),
                centerY + (textSize / 3),
                paint);

        return new BitmapDrawable(resManager.getResources(), bitmap);
    }
}
