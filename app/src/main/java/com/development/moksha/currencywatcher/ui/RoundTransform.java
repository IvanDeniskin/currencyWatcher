package com.development.moksha.currencywatcher.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

import com.squareup.picasso.Transformation;

public class RoundTransform implements Transformation {

    int mRealWidth = 58; //Bad image ratio from icon service
    int mRealHeight = 46;
    int mTargetSize = 100;
    int mRadius = 40;

    @Override
    public Bitmap transform(Bitmap source) {
        int maxSize = Math.max(source.getWidth(), source.getHeight());
        Bitmap transformed = Bitmap.createBitmap(source, (source.getWidth()- mRealWidth)/2, (source.getHeight()- mRealHeight)/2, mRealWidth, mRealHeight);
        Bitmap scaled = Bitmap.createScaledBitmap(transformed, mTargetSize, mTargetSize,false);
        Bitmap output = Bitmap.createBitmap(scaled.getWidth(), scaled.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, scaled.getWidth(), scaled.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);

        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));

        canvas.drawCircle(mTargetSize /2-1, mTargetSize /2-1, mRadius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));


        canvas.drawBitmap(scaled, rect, rect, paint);

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(mTargetSize /2-1, mTargetSize /2-1, mRadius-1, paint);

        if (source != output) {
            source.recycle();
        }
        return output;
    }



    @Override
    public String key() {
        return "round";
    }
}
