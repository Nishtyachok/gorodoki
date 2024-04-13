package ru.nishty.aai_referee.db.referee;

import android.provider.BaseColumns;

public final class DataBaseContractReferee {

    public static class Competition implements BaseColumns{
        public static final String TABLE_NAME = "competition";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_YEAR = "year";
        public static final String COLUMN_PLACE = "place";

    }

    public static class Performance implements BaseColumns{
        public static final String TABLE_NAME = "performance";
        public static final String COLUMN_COMPETITION = "competition";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_PLACE = "place";
        public static final String COLUMN_PLAYGROUND = "playground";
    }

    public static class PlayerRef implements BaseColumns {
        public static final String TABLE_NAME = "player_ref";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_REGION = "region";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_GRADE = "grade";
        public static final String COLUMN_COMPETITION = "competition";
        public static final String COLUMN_PERFORMANCE = "performance";

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

    public static final String SQL_CREATE_ENTRIES_COMPETITION =
            "CREATE TABLE " + Competition.TABLE_NAME + " (" +
                    Competition._ID + " TEXT PRIMARY KEY, " +
                    Competition.COLUMN_NAME + " TEXT, " +
                    Competition.COLUMN_YEAR + " TEXT, " +
                    Competition.COLUMN_PLACE + " TEXT);";
    public static final String SQL_CREATE_ENTRIES_PERFORMANCE =
            "CREATE TABLE " + Performance.TABLE_NAME + " (" +
                    Performance._ID + " INTEGER PRIMARY KEY, " +
                    Performance.COLUMN_COMPETITION + " TEXT, " +
                    Performance.COLUMN_PLACE + " TEXT, " +
                    Performance.COLUMN_DATE + " TEXT, " +
                    Performance.COLUMN_PLAYGROUND + " TEXT, " +
                    Performance.COLUMN_TIME + " TEXT, " +


                    "FOREIGN KEY (" + Performance.COLUMN_COMPETITION +
                    ") REFERENCES " + Competition.TABLE_NAME + "(" +
                    Competition._ID + ") ON DELETE CASCADE ON UPDATE CASCADE);";

    public static final String SQL_CREATE_ENTRIES_PLAYER_REF =
            "CREATE TABLE " + PlayerRef.TABLE_NAME + " (" +
                    PlayerRef._ID + " INTEGER PRIMARY KEY, " +
                    PlayerRef.COLUMN_NAME + " TEXT, " +
                    PlayerRef.COLUMN_COMPETITION + " TEXT, " +
                    PlayerRef.COLUMN_PERFORMANCE + " TEXT, " +
                    PlayerRef.COLUMN_REGION + " TEXT, " +
                    PlayerRef.COLUMN_CATEGORY + " TEXT, " +
                    PlayerRef.COLUMN_GRADE + " INTEGER, " +

                    "FOREIGN KEY (" + PlayerRef.COLUMN_COMPETITION +
                    ") REFERENCES " + Competition.TABLE_NAME + "(" +
                    Competition._ID + ") ON DELETE CASCADE ON UPDATE CASCADE " +
                    "FOREIGN KEY (" + PlayerRef.COLUMN_PERFORMANCE +
                    ") REFERENCES " + Performance.TABLE_NAME + "(" +
                    Performance._ID + ") ON DELETE CASCADE ON UPDATE CASCADE);";
    public static final String SQL_CREATE_ENTRIES_PROTOCOL =
            "CREATE TABLE " + Protocol.TABLE_NAME + " (" +
                    Protocol._ID + " INTEGER PRIMARY KEY, " +
                    Protocol.COLUMN_COMPETITION + " TEXT, " +
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
    public static final String SQL_DELETE_ENTRIES_PLAYER_REF =
            "DROP TABLE IF EXISTS " +  PlayerRef.TABLE_NAME +";";


}
