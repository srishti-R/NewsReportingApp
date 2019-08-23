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
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Politics extends Fragment implements LoaderManager.LoaderCallbacks<List<News>>, DownloadCallbacks {
private static final String POLITICS="https://content.guardianapis.com/search?order-by=newest&show-fields=thumbnail&page-size=10&q=politics&order-date=newspaper-edition&api-key=test";
    NewsAdapter adapter;
    ProgressBar bar;
    View rootView;
    GridView grid;



    public Politics() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.env_fragment, container, false);



        List<News> newsList = new ArrayList<News>();
        grid = rootView.findViewById(R.id.grid);
        bar=rootView.findViewById(R.id.progress_bar);
        adapter = new NewsAdapter(getActivity(), newsList);
        grid.setAdapter(adapter);
        registerForContextMenu(grid);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News recentNew = adapter.getItem(position);
                Uri uri = Uri.parse(recentNew.url);
                Intent implicit = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(implicit);

            }
        });

       NetworkInfo info=getActiveNetworkInfo();
       updateFromDownload(info);


        final SwipeRefreshLayout swiper= rootView.findViewById(R.id.refresh);
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
            NetworkInfo info=getActiveNetworkInfo();
            updateFromDownload(info);
                Toast.makeText(getContext(), "Refreshing..", Toast.LENGTH_SHORT).show();
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
        bar.setVisibility(View.VISIBLE);
        return new NewsLoader(getContext(), POLITICS);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<News>> loader, List<News> news) {
       bar.setVisibility(View.GONE);
        adapter.clear();
        if (news!= null && !news.isEmpty()) {
            adapter.addAll(news);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<News>> loader) {
        adapter.clear();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);


        if(v.getId()==R.id.grid){
            MenuInflater inflater=getActivity().getMenuInflater();
            inflater.inflate(R.menu.menu, menu);
        }
        menu.setHeaderIcon(R.drawable.ic_action_share);
        menu.setHeaderTitle("Share this news with your friends");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        News news = (News) grid.getItemAtPosition(info.position);

        if(item.getItemId()==R.id.share_news){

            Intent sendIntent=new Intent();

            sendIntent.setAction(Intent.ACTION_SEND);

            sendIntent.putExtra(Intent.EXTRA_TEXT, news.url);
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Share using.."));
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork;

    }
    @Override
    public void updateFromDownload(NetworkInfo activeNetwork) {
        View v= rootView.findViewById(R.id.empty_view);
        if(activeNetwork == null || !activeNetwork.isConnected()){

            bar.setVisibility(View.GONE);

            grid.setEmptyView(v);


        } if (activeNetwork != null) {
            bar.setVisibility(View.GONE);
            LoaderManager loaderManager = LoaderManager.getInstance(this);

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(1, null, this);
        }
    }


    @Override
    public void finishDownloading() {
    return;
    }
}




