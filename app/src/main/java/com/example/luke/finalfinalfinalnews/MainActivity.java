package com.example.luke.finalfinalfinalnews;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.app.LoaderManager;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    private static final String NEWS_API_URL = "http://content.guardianapis.com/search";
    private static final String NEWS_API_KEY = "test";
    private static final int NEWS_LOADER_ID = 1;
    private Button updateButton;
    private RecyclerView.Adapter adapter;
    private TextView console;
    private ArrayList<News> newsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.news_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        console = (TextView) findViewById(R.id.console);
        updateButton = (Button) findViewById(R.id.update_button);
        newsArrayList = new ArrayList<News>();
        adapter = new Adapter(newsArrayList);
        recyclerView.setAdapter(adapter);


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected()) {
                    getLoaderManager().restartLoader(0, null, MainActivity.this);
                } else {
                    if (adapter instanceof Adapter) {
                        ((Adapter) adapter).swapData(new ArrayList<News>());
                    }
                    console.setText(R.string.not_connected);
                    console.setVisibility(View.VISIBLE);
                }

            }
        });

        if (isConnected()) {

            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            console.setText(getString(R.string.not_connected));
        }
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        Date today = new Date();
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(today);
        Uri baseUri = Uri.parse(NEWS_API_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("show-fields", "byline");
        uriBuilder.appendQueryParameter("order-by", "newest");
        uriBuilder.appendQueryParameter("use-date", "published");
        uriBuilder.appendQueryParameter("from-date", date);
        uriBuilder.appendQueryParameter("api-key", NEWS_API_KEY);

        return new LoaderNews(this, uriBuilder.toString());
    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    @Override
    public void onLoaderReset(android.content.Loader loader) {
        newsArrayList.clear();
        if (adapter instanceof Adapter) {
            ((Adapter) adapter).swapData(newsArrayList);
        }
    }
    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> newsLoader) {

        console.setText(getString(R.string.nothing));
        if (newsLoader != null && !newsLoader.isEmpty()) {
            console.setVisibility(View.GONE);
        } else {
            console.setVisibility(View.VISIBLE);
        }

        if (adapter instanceof Adapter) {
            ((Adapter) adapter).swapData(newsLoader);
        }
    }



}