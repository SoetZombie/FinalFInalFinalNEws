package com.example.luke.finalfinalfinalnews;

public class News {
    public String getNewsTitle() {
        return newsTitle;
    }

    public String getNewsSection() {
        return newsSection;
    }

    public String getNewsAuthor() {
        return newsAuthor;
    }

    public String getNewsDate() {
        return newsDate;
    }

    public String getNewsUrl() {
        return newsUrl;
    }

    @Override
    public String toString() {
        return "News{" +
                "title='" + newsTitle + '\'' +
                ", topic='" + newsSection + '\'' +
                ", author='" + newsAuthor + '\'' +
                ", date='" + newsDate + '\'' +
                ", newsUrl='" + newsUrl + '\'' +
                '}';
    }

    private final String newsTitle;
    private final String newsSection;
    private final String newsAuthor;
    private final String newsDate;
    private final String newsUrl;

    public News(String title, String section, String author, String date, String url) {
        this.newsTitle = title;
        this.newsSection = section;
        this.newsAuthor = author;
        this.newsDate = date;
        this.newsUrl = url;
    }


}
