package com.android.example.newsreportingapp;

import android.graphics.drawable.Drawable;
import android.net.Uri;

public class News {

    String headLine;
    String date;
    String url;
    Drawable image;
    public News( String head, String dateObj,Drawable imageUri, String urlId){

        headLine=head;
        date=dateObj;
        image=imageUri;
        url=urlId;
    }
}
