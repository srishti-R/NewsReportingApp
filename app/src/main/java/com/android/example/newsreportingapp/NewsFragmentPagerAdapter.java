package com.android.example.newsreportingapp;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class NewsFragmentPagerAdapter extends FragmentPagerAdapter {
    public NewsFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i)
        {
            case 0:
                return new Environment(); //ChildFragment1 at position 0
            case 1:
                return new Politics(); //ChildFragment2 at position 1
//            case 2:
//                return new Space();
//
//            case 3:
//                return new Miscellaneous();*/
        }


        return null;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title="";
       switch(position){
           case 0: title="Environment";
                   break;
           case 1: title="Politics";
                   break;
           default:
               Log.e("this", "no position exists");
       }
        return title;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
