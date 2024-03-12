package ru.nishty.aai_referee.db.referee;

import android.provider.BaseColumns;

import ru.nishty.aai_referee.R;

public final class DataBaseContractReferee {

    public static class Competition implements BaseColumns{
        public static final String TABLE_NAME = "competition";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_YEAR = "year";

        public static final String COLUMN_PLACE = "place";
        public static final String COLUMN_DISCIPLINE = "discipline";
    }

    public static class Performance implements BaseColumns{
        public static final String TABLE_NAME = "performance";
        public static final String COLUMN_COMPETITION = "competition";
        public static final String COLUMN_NAME = "name";

        public static final String COLUMN_REGION = "region";

        public static final String COLUMN_GRADE = "grade";

        public static final String COLUMN_TIME = "time";

        public static final String COLUMN_DATE = "date";

        public static final String COLUMN_PLACE = "place";

        public static final String COLUMN_PLAYGROUND = "playground";

        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_INTERNAL_ID = "i_id";
    }

    public static class Protocol implements BaseColumns {
        public static final String TABLE_NAME = "protocol";
        public static final String COLUMN_COMPETITION = "competition";
        public static final String COLUMN_PERFORMANCE = "performance";
        public static final String COLUMN_TECHNIQUE = "technique";
        public static final String COLUMN_ARTISTRY = "artistry";
        public static final String COLUMN_GAME1 = "game1";
        public static final String COLUMN_GAME2 = "game2";
        public static final String COLUMN_GAMES_SUM = "games_sum";
        public static final String COLUMN_LIMIT = "_limit";
        public static final String COLUMN_SHOTS1 = "shots1";
        public static final String COLUMN_SHOTS2 = "shots2";

        public static final String COLUMN_COMPLEXITY = "complexity";
    }

    public static class DisciplineHelper {
        public static int getDiscipline(int Discipline){
            switch (Discipline){
                case 1:
                    return R.string.solo;
                case 2:
                    return R.string.duel;
                case 3:
                    return R.string.group;
                case 4:
                    return R.string.ensemble;
                default:
                    return -1;
            }
        }
    }

    public static final String SQL_CREATE_ENTRIES_COMPETITION =
            "CREATE TABLE " + Competition.TABLE_NAME + " (" +
                    Competition._ID + " BLOB PRIMARY KEY, " +
                    Competition.COLUMN_NAME + " TEXT, " +
                    Competition.COLUMN_YEAR + " TEXT, " +
                    Competition.COLUMN_PLACE + " TEXT, " +
                    Competition.COLUMN_DISCIPLINE + " INTEGER);";
    public static final String SQL_CREATE_ENTRIES_PERFORMANCE =
            "CREATE TABLE " + Performance.TABLE_NAME + " (" +
                    Performance._ID + " INTEGER PRIMARY KEY, " +
                    Performance.COLUMN_COMPETITION + " BLOB, " +
                    Performance.COLUMN_NAME + " TEXT, " +
                    Performance.COLUMN_GRADE + " TEXT, " +
                    Performance.COLUMN_REGION + " TEXT, " +
                    Performance.COLUMN_PLACE + " TEXT, " +
                    Performance.COLUMN_DATE + " TEXT, " +
                    Performance.COLUMN_PLAYGROUND + " TEXT, " +
                    Performance.COLUMN_CATEGORY + " TEXT, " +
                    Performance.COLUMN_TIME + " TEXT, " +

                    Performance.COLUMN_INTERNAL_ID + " INTEGER, " +
                    "FOREIGN KEY (" + Performance.COLUMN_COMPETITION +
                    ") REFERENCES " + Competition.TABLE_NAME + "(" +
                    Competition._ID + ") ON DELETE CASCADE ON UPDATE CASCADE);";
    public static final String SQL_CREATE_ENTRIES_PROTOCOL =
            "CREATE TABLE " + Protocol.TABLE_NAME + " (" +
                    Protocol._ID + " INTEGER PRIMARY KEY, " +
                    Protocol.COLUMN_COMPETITION + " BLOB, " +
                    Protocol.COLUMN_PERFORMANCE + " INTEGER, " +
                    Protocol.COLUMN_TECHNIQUE + " TEXT, " +
                    Protocol.COLUMN_COMPLEXITY + " TEXT, " +
                    Protocol.COLUMN_ARTISTRY + " TEXT, " +
                    Protocol.COLUMN_SHOTS1 + " TEXT, " +
                    Protocol.COLUMN_SHOTS2 + " TEXT, " +
                    Protocol.COLUMN_LIMIT + " INTEGER, " +
                    Protocol.COLUMN_GAME1 + " INTEGER, " +
                    Protocol.COLUMN_GAME2 + " INTEGER, " +
                    Protocol.COLUMN_GAMES_SUM + " INTEGER, " +

            "FOREIGN KEY (" + Protocol.COLUMN_COMPETITION +
                    ") REFERENCES " + Competition.TABLE_NAME + "(" +
                    Competition._ID + ") ON DELETE CASCADE ON UPDATE CASCADE " +
                    "FOREIGN KEY (" + Protocol.COLUMN_PERFORMANCE +
                    ") REFERENCES " + Performance.TABLE_NAME + "(" +
                    Performance._ID + ") ON DELETE CASCADE ON UPDATE CASCADE);";
    public static final String SQL_DELETE_ENTRIES_COMPETITION =
            "DROP TABLE IF EXISTS " +  Competition.TABLE_NAME +";";
    public static final String SQL_DELETE_ENTRIES_PERFORMANCE =
            "DROP TABLE IF EXISTS " +  Performance.TABLE_NAME +";";
    public static final String SQL_DELETE_ENTRIES_PROTOCOL =
            "DROP TABLE IF EXISTS " +  Protocol.TABLE_NAME +";";



}
