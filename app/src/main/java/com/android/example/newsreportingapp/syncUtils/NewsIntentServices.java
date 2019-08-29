package com.android.example.newsreportingapp.syncUtils;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.android.example.newsreportingapp.MainActivity;

public class NewsIntentServices extends IntentService {
    public NewsIntentServices() {

        super("NewsIntentServices");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        Context context = NewsIntentServices.this;
        if (action.equals(NotificationUtils.ACTION_DISMISS_NOTIFICATION)) {
            NotificationUtils.clearAllNotifications(context);
        }
        if (action.equals(NotificationUtils.ACTION_LOADING)) {
            Intent intent1 = new Intent(context, MainActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent1);
        }
    }
}