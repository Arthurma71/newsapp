package com.example.panjunchen.models;

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
        public static final String NEWS_PUBLISH_TIME = "news_publish_time";
        public static final String NEWS_HASHCODE = "news_hashcode";
        public static final String NEWS_PUBLISHER = "news_publisher";
        public static final String NEWS_FAVORITE = "news_isfavorite";
        public static final String NEWS_READTIME = "news_readtime";
        public static final String NEWS_CATEGORY = "news_category";
        public static final String NEWS_IMAGE = "news_image";
        public static final String NEWS_VIDEO = "news_video";
    }

    public static class Tags {
        private Tags() {
        }

        public static final String TAGS_TABLE_NAME = "TagList";
        public static final String TAGS_TAG = "tag";
        public static final String TAGS_INDEX = "news_index";
        public static final String TAGS_VAL = "tag_val";
    }
}