package com.android.example.newsreportingapp.syncUtils;


import android.content.Context;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobService;

public class NewsDispatcher extends JobService {
    private AsyncTask mBackgroundTask;

    @Override
    public boolean onStartJob(final com.firebase.jobdispatcher.JobParameters job) {

        mBackgroundTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                Context context = NewsDispatcher.this;
                NotificationUtils.remindUser(context);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                jobFinished(job, false);
            }
        };

        mBackgroundTask.execute();
        return true;


    }

    @Override
    public boolean onStopJob(com.firebase.jobdispatcher.JobParameters job) {
        if (mBackgroundTask != null) {
            mBackgroundTask.cancel(true);

        }
        return true;
    }
}

