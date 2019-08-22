package com.android.example.newsreportingapp;

import android.content.Context;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>> {
   String urlId;


    public NewsLoader(@NonNull Context context, String url) {
        super(context);
        urlId=url;

    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable

    public List<News> loadInBackground() {
        List<News> newsList=new ArrayList<>();
        if(urlId==null){
            return null;
        }
        newsList=QueryUtils.fetchNewsData(urlId);
        return newsList;
    }
}
