package com.example.myapplication.models;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class TableConfig {

    // no constructor
    private TableConfig() {
    }

    public static class Note {
        // no constructor
        private Note() {
        }

        public static final String NOTE_TABLE_NAME = "AndroidNote";
        public static final String NOTE_ID = "id";
        public static final String NOTE_TITLE = "note_title";
        public static final String NOTE_CONTENT = "note_content";
        public static final String NOTE_START_TIME = "note_start_time";
        public static final String NOTE_MODIFY_TIME = "note_modify_time";
        public static final String NOTE_TAG = "note_tag";
        public static final String NOTE_GROUP = "note_group";
    }

    public static class Group {
        // no constructor
        private Group() {
        }

        public static final String GROUP_TABLE_NAME = "GroupTable";
        public static final String GROUP_NAME = "group_name";
    }

    public static class FileSave {
        // no constructor
        private FileSave() {
        }

        public static final String LIST_SEPARATOR = "Sep" + (char) 29;
        public static final String LINE_SEPARATOR = "Sep" + (char) 31;

        private static String savePath; // initialized in TableOperate.init()

        public static String getSavePath() {
            return FileSave.savePath;
        }

        public static void setSavePath(String savePath) {
            FileSave.savePath = savePath;
        }
    }

    public static class Sorter {
        // no constructor
        private Sorter() {
        }

        private static final String[] SORTER_FIELDS = {"sort_title", "sort_create_time", "sort_modify_time"};
        private static final Map<Integer, String> SORTER_OPTION_TO_FIELD = new HashMap<>();
        private static final Map<String, Comparator<com.se.npe.androidnote.models.Note>>
                SORTER_FIELD_TO_COMPARATOR = new HashMap<>();

        static {
            SORTER_OPTION_TO_FIELD.put(R.id.sort_title, SORTER_FIELDS[0]);
            SORTER_OPTION_TO_FIELD.put(R.id.sort_created_time, SORTER_FIELDS[1]);
            SORTER_OPTION_TO_FIELD.put(R.id.sort_modified_time, SORTER_FIELDS[2]);
            SORTER_FIELD_TO_COMPARATOR.put(SORTER_FIELDS[0], Comparator.comparing(com.se.npe.androidnote.models.Note::getTitle));
            SORTER_FIELD_TO_COMPARATOR.put(SORTER_FIELDS[1], Comparator.comparing(com.se.npe.androidnote.models.Note::getStartTime));
            SORTER_FIELD_TO_COMPARATOR.put(SORTER_FIELDS[2], Comparator.comparing(com.se.npe.androidnote.models.Note::getModifyTime));
        }

        public static String getDefaultSorterField() {
            return SORTER_FIELDS[0];
        }

        public static String[] getSorterFields() {
            return SORTER_FIELDS;
        }

        public static String getSorterOptionToField(Integer sorterOption) {
            return SORTER_OPTION_TO_FIELD.get(sorterOption);
        }

        public static Comparator<com.se.npe.androidnote.models.Note> getSorterFieldToComparator(String sorterField) {
            return SORTER_FIELD_TO_COMPARATOR.get(sorterField);
        }
    }
}