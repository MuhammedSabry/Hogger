package com.sabry.muhammed.hogger.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.Button;
import android.widget.Toast;

import com.sabry.muhammed.hogger.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommonUtil {
    private static boolean quitApp = false;
    private static Timer timer;

    static {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                quitApp = false;
            }
        }, new Date(), 2000);
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

    public static void loadImage(CircleImageView view, String url) {
        if (url.isEmpty())
            view.setImageResource(R.drawable.ic_no_photo);
        Picasso.get().load(url).into(view);
    }

    public static void loadImage(Target target, String url) {
        Picasso.get().load(url).into(target);
    }

    public static void buttonLiked(Button button) {
        button.setTextColor(button.getContext().getResources().getColor(R.color.colorPrimary));
        button.setCompoundDrawablesWithIntrinsicBounds(button.getContext().getResources().getDrawable(R.drawable.ic_liked), null, null, null);
    }

    public static void buttonNotLiked(Button button) {
        button.setTextColor(button.getContext().getResources().getColor(R.color.black));
        button.setCompoundDrawablesWithIntrinsicBounds(button.getContext().getResources().getDrawable(R.drawable.ic_not_liked), null, null, null);
    }
}
