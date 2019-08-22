package com.android.example.newsreportingapp;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link } subclass.
 */
public class Environment extends Fragment implements LoaderManager.LoaderCallbacks<List<News>> {
    private static final String MAIN = "https://content.guardianapis.com/search?order-by=newest&page-size=10&show-fields=thumbnail&q=nature,environment&order-date=newspaper-edition&api-key=test";
    NewsAdapter adapter;

    public Environment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.env_fragment, container, false);



        List<News> newsList = new ArrayList<News>();
        GridView grid = rootView.findViewById(R.id.grid);

        adapter = new NewsAdapter(getActivity(), newsList);
        grid.setAdapter(adapter);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News recentNew = adapter.getItem(position);
                Uri uri = Uri.parse(recentNew.url);
                Intent implicit = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(implicit);

            }
        });

        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(activeNetwork == null || !activeNetwork.isConnected()){

       View v= rootView.findViewById(R.id.empty_view);

           grid.setEmptyView(v);


        } if (activeNetwork != null) {

            LoaderManager loaderManager = LoaderManager.getInstance(this);

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(1, null, this);
        }

        final SwipeRefreshLayout swiper=(SwipeRefreshLayout)rootView.findViewById(R.id.refresh);
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                Toast.makeText(getContext(), "No More Stories", Toast.LENGTH_SHORT).show();
                swiper.setRefreshing(false);
            }
        });

        return rootView;
    }


    @NonNull
    @Override
    public Loader<List<News>> onCreateLoader(int i, @Nullable Bundle bundle) {
       // Uri baseUri = Uri.parse(GUARDIAN_ENV);
//        Uri.Builder uriBuilder = baseUri.buildUpon();

       /* uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);

        return new EarthquakeLoader(this, uriBuilder.toString());
*/
       //creates a custom loader
        //@param context, url from where we want to load the news items
       return new NewsLoader(getContext(), MAIN);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<News>> loader, List<News> news) {
    adapter.clear();
        if (news!= null && !news.isEmpty()) {
            adapter.addAll(news);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<News>> loader) {
    adapter.clear();
    }


}
