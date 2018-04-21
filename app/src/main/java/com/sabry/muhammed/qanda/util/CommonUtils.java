package com.sabry.muhammed.qanda.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.Toast;

import com.sabry.muhammed.qanda.R;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class CommonUtils {
    private static boolean quitApp = false;
    private static Timer timer;

    static {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                quitApp = false;
            }
        }, new Date(), 200);
    }

    public static void shutDownActivity(Activity activity) {
        if (quitApp) {
            activity.finishAffinity();
        } else {
            quitApp = true;
            Toast.makeText(activity
                    , activity.getResources().getString(R.string.quit_app)
                    , Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public static RoundedBitmapDrawable roundImage(Bitmap bitmap, Context context) {
        RoundedBitmapDrawable bitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), bitmap);
        bitmapDrawable.setCircular(true);
        bitmapDrawable.setCornerRadius(Math.max(bitmap.getWidth(), bitmap.getHeight()) / 2.0f);
        return bitmapDrawable;
    }
}
