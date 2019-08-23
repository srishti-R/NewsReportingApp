package com.android.example.newsreportingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {


    public NewsAdapter(Context context, List<News> objects) {
        super(context, 0, objects);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridViewItem = convertView;
        if (gridViewItem == null) {
            gridViewItem = LayoutInflater.from(getContext()).inflate(R.layout.grid_item, parent, false);

        }

        News recent = getItem(position);


        TextView date = gridViewItem.findViewById(R.id.date);
       date.setText(recent.date);

        TextView headline = gridViewItem.findViewById(R.id.headline);
        headline.setText(recent.headLine);

        ImageView image= gridViewItem.findViewById(R.id.image);
        image.setImageDrawable(recent.image);
        return gridViewItem;


    }


}
