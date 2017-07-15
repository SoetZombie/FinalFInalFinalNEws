package com.example.luke.finalfinalfinalnews;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;



public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private ArrayList<News> news;

    public Adapter(ArrayList<News> newsInfo) {
        this.news = newsInfo;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.news_style, parent, false);

        return new Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final News currentNews = news.get(position);
        holder.title.setText(currentNews.getNewsTitle());
        holder.topic.setText(currentNews.getNewsSection());
        holder.author.setText(currentNews.getNewsAuthor());
        holder.date.setText(formatDate(currentNews.getNewsDate()));

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri newsUri = Uri.parse(currentNews.getNewsUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                v.getContext().startActivity(websiteIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (news == null) {
            return 0;
        }
        return news.size();
    }

    public void swapData(List<News> newNewsList) {
        if (news != null && !news.isEmpty()) {
            news.clear();
            if (newNewsList == null) {
                news = new ArrayList<News>();
            } else {
                news.addAll(newNewsList);
            }
        } else {
            news = (ArrayList<News>) newNewsList;
        }
        notifyDataSetChanged();
    }

    private String formatDate(String jsonDateString) {
        if (jsonDateString != null && !jsonDateString.isEmpty()) {
            String jsonDatePattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
            SimpleDateFormat jsonFormatter = new SimpleDateFormat(jsonDatePattern, Locale.getDefault());
            try {
                Date parsedJsonDate = jsonFormatter.parse(jsonDateString);
                String finalDatePattern = "yyyy-MM-dd HH:mm";
                SimpleDateFormat finalDateFormatter = new SimpleDateFormat(finalDatePattern, Locale.getDefault());
                return finalDateFormatter.format(parsedJsonDate);
            } catch (ParseException e) {
               e.printStackTrace();
            }
        }
        return "";
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView title;
        final TextView date;
        final View layout;
        final TextView topic;
        final TextView author;

        ViewHolder(View itemView) {
            super(itemView);
            layout = itemView;
            topic = (TextView) itemView.findViewById(R.id.topic);
            date = (TextView) itemView.findViewById(R.id.date);
            author = (TextView) itemView.findViewById(R.id.author);
            title = (TextView) itemView.findViewById(R.id.title);

        }
    }
}
