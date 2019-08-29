package com.android.example.newsreportingapp.syncUtils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.android.example.newsreportingapp.MainActivity;
import com.android.example.newsreportingapp.R;

public class NotificationUtils {

    private static final String NOTIFICATION_CHANNEL_ID = "reminder_notification_channel";
    public static final String ACTION_LOADING = "load";
    public static String ACTION_DISMISS_NOTIFICATION = "dismiss";
    private static final int PENDING_INTENT_ID = 0;
    private static int NOTIFICATION_ID = 1;

    public static void remindUser(Context context) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    "notification",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(mChannel);
        }


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.mipmap.news)

                .setContentTitle(context.getString(R.string.noti_title))
                .setContentText(context.getString(R.string.noti_body))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(
                        context.getString(R.string.noti_body)))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                .setAutoCancel(true)
                .setOngoing(false)
                .addAction(ignoreReminder(context))
                .addAction(loadStories(context));


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());

    }

    private static PendingIntent contentIntent(Context context) {

        Intent startActivityIntent = new Intent(context, MainActivity.class);

        return PendingIntent.getActivity(
                context,
                PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static void clearAllNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    public static NotificationCompat.Action ignoreReminder(Context context) {


        Intent ignoreReminderIntent = new Intent(context, NewsIntentServices.class);

        ignoreReminderIntent.setAction(ACTION_DISMISS_NOTIFICATION);
        PendingIntent ignoreReminderPendingIntent = PendingIntent.getService(
                context,
                1,
                ignoreReminderIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action ignoreReminder = new NotificationCompat.Action(R.drawable.ic_cancel,
                "No, thanks.",
                ignoreReminderPendingIntent);


        return ignoreReminder;
    }

    public static NotificationCompat.Action loadStories(Context context) {

        Intent loadIntent = new Intent(context, NewsIntentServices.class);

        loadIntent.setAction(ACTION_LOADING);


        PendingIntent pendingIntent = PendingIntent.getService(
                context,
                2,
                loadIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Action action = new NotificationCompat.Action(R.drawable.ic_check,
                context.getString(R.string.noti_affirm),
                pendingIntent);

        return action;
    }
}
