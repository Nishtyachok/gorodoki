package ru.nishty.aai_referee.db.secretary;

import android.provider.BaseColumns;
import ru.nishty.aai_referee.R;

public final class DataBaseContractSecretary {

    public static class Competition implements BaseColumns {
        public static final String TABLE_NAME = "competition";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_YEAR = "year";
        public static final String COLUMN_PLACE = "place";
        public static final String COLUMN_HEADJUDGE = "headJudge";
        public static final String COLUMN_HEADSECRETARY = "headSecretary";
    }

    public static class Category implements BaseColumns {
        public static final String TABLE_NAME = "category";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_TOURS = "tours";
        public static final String COLUMN_FIGURES = "figures";
        public static final String COLUMN_LIMIT = "_limit";
        public static final String COLUMN_COMPETITION = "competition";

    }
    public static class Judge implements BaseColumns {
        public static final String TABLE_NAME = "judge";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_REGION = "region";
        public static final String COLUMN_COMPETITION = "competition";

    }
    public static class Region implements BaseColumns {
        public static final String TABLE_NAME = "region";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_COMPETITION = "competition";

    }
    public static class GradeHelper {
        public static int getGrade(int Grade){
            switch (Grade){
                case 1:
                    return R.string.gradeU1;
                case 2:
                    return R.string.gradeU2;
                case 3:
                    return R.string.gradeU3;
                case 4:
                    return R.string.grade1;
                case 5:
                    return R.string.grade2;
                case 6:
                    return R.string.grade3;
                case 7:
                    return R.string.gradeKMS;
                case 8:
                    return R.string.gradeMS;
                case 9:
                    return R.string.gradeZMS;
                default:
                    return -1;
            }
        }
    }
    public static class Player implements BaseColumns {
        public static final String TABLE_NAME = "player";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_REGION_ID = "region";
        public static final String COLUMN_CATEGORY_ID = "category";
        public static final String COLUMN_GRADE = "grade";
        public static final String COLUMN_COMPETITION = "competition";
    }

    public static class PerformancePlayer implements BaseColumns{
        public static final String TABLE_NAME = "performance_player";
        public static final String COLUMN_PLAYER_ID = "player";
        public static final String COLUMN_PERFORMANCE_ID = "performance";
        public static final String COLUMN_COMPETITION = "competition";
    }

    public static class Performance implements BaseColumns {
        public static final String TABLE_NAME = "performance";
        public static final String COLUMN_COMPETITION = "competition";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_PLACE = "place";
        public static final String COLUMN_JUDGE_ID = "judge_id";
        public static final String COLUMN_PLAYGROUND = "playground";
        public static final String COLUMN_INTERNAL_ID = "i_id";
    }

    public static class Protocol implements BaseColumns {
        public static final String TABLE_NAME = "protocol";
        public static final String COLUMN_PLAYER_ID = "player_id";
        public static final String COLUMN_COMPETITION = "competition";
        public static final String COLUMN_PERFORMANCE_ID = "performance";
        public static final String COLUMN_TOURS = "tours";
        public static final String COLUMN_SHOTS1 = "shots1";
        public static final String COLUMN_SHOTS2 = "shots2";
        public static final String COLUMN_LIMIT = "_limit";
        public static final String COLUMN_GAME1 = "game1";
        public static final String COLUMN_GAME2 = "game2";
        public static final String COLUMN_GAMES_SUM = "games_sum";
    }
    public static final String SQL_CREATE_ENTRIES_COMPETITION =
            "CREATE TABLE " + Competition.TABLE_NAME + " (" +
                    Competition._ID + " BLOB PRIMARY KEY, " +
                    Competition.COLUMN_NAME + " TEXT, " +
                    Competition.COLUMN_YEAR + " TEXT, " +
                    Competition.COLUMN_PLACE + " TEXT, " +
                    Competition.COLUMN_HEADJUDGE + " TEXT, " +
                    Competition.COLUMN_HEADSECRETARY + " TEXT)";

    public static final String SQL_CREATE_ENTRIES_JUDGE =
            "CREATE TABLE " + Judge.TABLE_NAME + " (" +
                    Judge._ID + " INTEGER PRIMARY KEY, " +
                    Performance.COLUMN_COMPETITION + " BLOB, " +
                    Judge.COLUMN_NAME + " TEXT, " +
                    Judge.COLUMN_CATEGORY + " TEXT, " +
                    Judge.COLUMN_REGION + " TEXT, " +
                    "FOREIGN KEY (" + Judge.COLUMN_COMPETITION + ") REFERENCES " +
                    Competition.TABLE_NAME + "(" + Competition._ID + ") ON DELETE CASCADE ON UPDATE CASCADE); ";
    public static final String SQL_CREATE_ENTRIES_REGION =
            "CREATE TABLE " + Region.TABLE_NAME + " (" +
                    Region._ID + " INTEGER PRIMARY KEY, " +
                    Region.COLUMN_NAME + " TEXT, " +
                    Region.COLUMN_COMPETITION + " BLOB, " +
                    "FOREIGN KEY (" + Region.COLUMN_COMPETITION + ") REFERENCES " +
                    Competition.TABLE_NAME + "(" + Competition._ID + ") ON DELETE CASCADE ON UPDATE CASCADE); ";
    public static final String SQL_CREATE_ENTRIES_PLAYER =
            "CREATE TABLE " + Player.TABLE_NAME + " (" +
                    Player._ID + " INTEGER PRIMARY KEY, " +
                    Player.COLUMN_NAME + " TEXT, " +
                    Player.COLUMN_REGION_ID + " INTEGER, " +
                    Player.COLUMN_CATEGORY_ID + " INTEGER, " +
                    Player.COLUMN_GRADE + " INTEGER, " +
                    Player.COLUMN_COMPETITION + " BLOB, " +
                    "FOREIGN KEY (" + Player.COLUMN_COMPETITION + ") REFERENCES " +
                    Competition.TABLE_NAME + "(" + Competition._ID + ") ON DELETE CASCADE ON UPDATE CASCADE "+
                    "FOREIGN KEY (" + Player.COLUMN_REGION_ID + ") REFERENCES " +
                    Region.TABLE_NAME + "(" + Region._ID + ") ON DELETE CASCADE ON UPDATE CASCADE " +
                    "FOREIGN KEY (" + Player.COLUMN_CATEGORY_ID + ") REFERENCES " +
                    Category.TABLE_NAME + "(" + Category._ID + ") ON DELETE CASCADE ON UPDATE CASCADE);";
    public static final String SQL_CREATE_ENTRIES_PERFORMANCE =
            "CREATE TABLE " + Performance.TABLE_NAME + " (" +
                    Performance._ID + " INTEGER PRIMARY KEY, " +
                    Performance.COLUMN_COMPETITION + " BLOB, " +
                    Performance.COLUMN_TIME + " TEXT, " +
                    Performance.COLUMN_DATE + " TEXT, " +
                    Performance.COLUMN_PLACE + " TEXT, " +
                    Performance.COLUMN_PLAYGROUND + " TEXT, " +

                    Performance.COLUMN_INTERNAL_ID + " INTEGER, " +
                    Performance.COLUMN_JUDGE_ID + " INTEGER, " +
                    "FOREIGN KEY (" + Performance.COLUMN_COMPETITION + ") REFERENCES " +
                    Competition.TABLE_NAME + "(" + Competition._ID + ") ON DELETE CASCADE ON UPDATE CASCADE " +
                    "FOREIGN KEY (" + Performance.COLUMN_JUDGE_ID + ") REFERENCES " +
                    Judge.TABLE_NAME + "(" + Judge._ID + ") ON DELETE CASCADE ON UPDATE CASCADE);";
    public static final String SQL_CREATE_ENTRIES_CATEGORY =
            "CREATE TABLE " + Category.TABLE_NAME + " (" +
                    Category._ID + " INTEGER PRIMARY KEY, " +
                    Category.COLUMN_NAME + " TEXT, " +
                    Category.COLUMN_TOURS + " INTEGER, " +
                    Category.COLUMN_FIGURES + " INTEGER, " +
                    Category.COLUMN_LIMIT + " INTEGER, " +
                    Category.COLUMN_COMPETITION + " BLOB)";
    public static final String SQL_CREATE_ENTRIES_PERFORMANCEPLAYER =
            "CREATE TABLE " + PerformancePlayer.TABLE_NAME + " (" +
                    PerformancePlayer._ID + " INTEGER PRIMARY KEY, " +
                    PerformancePlayer.COLUMN_PLAYER_ID + " INTEGER, " +
                    PerformancePlayer.COLUMN_PERFORMANCE_ID + " INTEGER, "+
                    PerformancePlayer.COLUMN_COMPETITION + " INTEGER, " +
                    "FOREIGN KEY (" + PerformancePlayer.COLUMN_PERFORMANCE_ID + ") REFERENCES " +
                    Performance.TABLE_NAME + "(" + Performance._ID + ")ON DELETE CASCADE ON UPDATE CASCADE "+
                    "FOREIGN KEY (" + PerformancePlayer.COLUMN_PERFORMANCE_ID + ") REFERENCES " +
                    Player.TABLE_NAME + "(" + Player._ID + ")ON DELETE CASCADE ON UPDATE CASCADE "+
                    "FOREIGN KEY (" + PerformancePlayer.COLUMN_COMPETITION + ") REFERENCES " +
                    Competition.TABLE_NAME + "(" + Competition._ID + ")ON DELETE CASCADE ON UPDATE CASCADE);";

    public static final String SQL_CREATE_ENTRIES_PROTOCOL =
            "CREATE TABLE " + Protocol.TABLE_NAME + " (" +
                    Protocol._ID + " INTEGER PRIMARY KEY, " +
                    Protocol.COLUMN_COMPETITION + " BLOB, " +
                    Protocol.COLUMN_PERFORMANCE_ID + " INTEGER, " +
                    Protocol.COLUMN_PLAYER_ID + " INTEGER, " +
                    Protocol.COLUMN_SHOTS1 + " TEXT, " +
                    Protocol.COLUMN_SHOTS2 + " TEXT, " +
                    Protocol.COLUMN_LIMIT + " INTEGER, " +
                    Protocol.COLUMN_TOURS + " INTEGER, " +
                    Protocol.COLUMN_GAME1 + " TEXT, " +
                    Protocol.COLUMN_GAME2 + " TEXT, " +
                    Protocol.COLUMN_GAMES_SUM + " TEXT, " +
                    "FOREIGN KEY (" + Protocol.COLUMN_COMPETITION + ") REFERENCES " +
                    Competition.TABLE_NAME + "(" + Competition._ID + ") ON DELETE CASCADE ON UPDATE CASCADE " +
                    "FOREIGN KEY (" + Protocol.COLUMN_PERFORMANCE_ID + ") REFERENCES " +
                    Performance.TABLE_NAME + "(" + Performance._ID + ") ON DELETE CASCADE ON UPDATE CASCADE " +
                    "FOREIGN KEY (" + Protocol.COLUMN_PLAYER_ID + ") REFERENCES " +
                    Player.TABLE_NAME + "(" + Player._ID + ") ON DELETE CASCADE ON UPDATE CASCADE);";
    public static final String SQL_DELETE_ENTRIES_COMPETITION =
            "DROP TABLE IF EXISTS " +  Competition.TABLE_NAME +";";
    public static final String SQL_DELETE_ENTRIES_PERFORMANCE =
            "DROP TABLE IF EXISTS " +  Performance.TABLE_NAME +";";
    public static final String SQL_DELETE_ENTRIES_PROTOCOL =
            "DROP TABLE IF EXISTS " +  Protocol.TABLE_NAME +";";
    public static final String SQL_DELETE_ENTRIES_JUDGE =
            "DROP TABLE IF EXISTS " +  Judge.TABLE_NAME +";";
    public static final String SQL_DELETE_ENTRIES_REGION =
            "DROP TABLE IF EXISTS " +  Region.TABLE_NAME +";";
    public static final String SQL_DELETE_ENTRIES_PLAYER =
            "DROP TABLE IF EXISTS " +  Player.TABLE_NAME +";";
    public static final String SQL_DELETE_ENTRIES_CATEGORY =
            "DROP TABLE IF EXISTS " +  Category.TABLE_NAME +";";
    public static final String SQL_DELETE_ENTRIES_PERFORMANCEPLAYER =
            "DROP TABLE IF EXISTS " +  PerformancePlayer.TABLE_NAME +";";
}
