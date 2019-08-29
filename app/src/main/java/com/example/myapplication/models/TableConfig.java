package com.example.myapplication.models;

public class TableConfig {

    // no constructor
    private TableConfig() {
    }

    public static class News {
        // no constructor
        private News() {
        }

        public static final String NEWS_TABLE_NAME = "AndroidNews";
        public static final String NEWS_ID = "id";
        public static final String NEWS_TITLE = "news_title";
        public static final String NEWS_CONTENT = "news_content";
        public static final String NEWS_TIME = "news_time";
    }
}