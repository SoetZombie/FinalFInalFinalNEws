package com.example.luke.finalfinalfinalnews;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.text.TextUtils;
import java.util.List;

public class LoaderNews extends AsyncTaskLoader<List<News>> {

    private final String url;

    public LoaderNews(Context context, String url) {
        super(context);
        this.url = url;
    }
    @Override
    protected void onStartLoading() {

        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        if (TextUtils.isEmpty(url)) {
            return null;
        }

        return Query.fetchNewsData(url);
    }
}
