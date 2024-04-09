package ru.nishty.aai_referee.db.referee;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ru.nishty.aai_referee.entity.referee.Competition;
import ru.nishty.aai_referee.entity.referee.Performance;
import ru.nishty.aai_referee.entity.referee.PlayerRef;
import ru.nishty.aai_referee.entity.referee.Protocol;

public class DataBaseHelperReferee extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "DataBaseReferee.db";

    public DataBaseHelperReferee(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public DataBaseHelperReferee(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DataBaseHelperReferee(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DataBaseContractReferee.SQL_CREATE_ENTRIES_COMPETITION);
        db.execSQL(DataBaseContractReferee.SQL_CREATE_ENTRIES_PERFORMANCE);
        db.execSQL(DataBaseContractReferee.SQL_CREATE_ENTRIES_PROTOCOL);
        db.execSQL(DataBaseContractReferee.SQL_CREATE_ENTRIES_PLAYER_REF);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DataBaseContractReferee.SQL_DELETE_ENTRIES_PROTOCOL);
        db.execSQL(DataBaseContractReferee.SQL_DELETE_ENTRIES_PERFORMANCE);
        db.execSQL(DataBaseContractReferee.SQL_DELETE_ENTRIES_COMPETITION);
        db.execSQL(DataBaseContractReferee.SQL_DELETE_ENTRIES_PLAYER_REF);

        onCreate(db);
    }

    public void addCompetition(SQLiteDatabase db, Competition competition){
        ContentValues values = new ContentValues();
        values.put(DataBaseContractReferee.Competition._ID, String.valueOf(competition.getUuid()));
        values.put(DataBaseContractReferee.Competition.COLUMN_NAME, competition.getName());
        values.put(DataBaseContractReferee.Competition.COLUMN_YEAR,competition.getYear());
        values.put(DataBaseContractReferee.Competition.COLUMN_PLACE,competition.getPlace());
        db.insert(DataBaseContractReferee.Competition.TABLE_NAME,null,values);
        for (Performance performance :
                competition.getPerformances()) {
            values = new ContentValues();
            values.put(DataBaseContractReferee.Performance.COLUMN_INTERNAL_ID, performance.getInternal_id());
            values.put(DataBaseContractReferee.Performance.COLUMN_TIME, performance.getTime());
            values.put(DataBaseContractReferee.Performance.COLUMN_PLACE, performance.getPlace());
            values.put(DataBaseContractReferee.Performance.COLUMN_PLAYGROUND, performance.getPlayground());
            values.put(DataBaseContractReferee.Performance.COLUMN_DATE, performance.getDate());
            values.put(DataBaseContractReferee.Performance.COLUMN_COMPETITION, String.valueOf(competition.getUuid()));
            db.insert(DataBaseContractReferee.Performance.TABLE_NAME, null, values);
            for (PlayerRef playerRef:
                    performance.getPlayers()){
                values = new ContentValues();
                values.put(DataBaseContractReferee.PlayerRef.COLUMN_CATEGORY_ID,playerRef.getCategory());
                values.put(DataBaseContractReferee.PlayerRef.COLUMN_GRADE,playerRef.getGrade());
                values.put(DataBaseContractReferee.PlayerRef.COLUMN_NAME,playerRef.getName());
                values.put(DataBaseContractReferee.PlayerRef.COLUMN_REGION_ID,playerRef.getRegion());
                values.put(DataBaseContractReferee.PlayerRef.COLUMN_PERFORMANCE, playerRef.getPerf_id());
                values.put(DataBaseContractReferee.Performance.COLUMN_COMPETITION, String.valueOf(competition.getUuid()));
                db.insert(DataBaseContractReferee.PlayerRef.TABLE_NAME, null, values);
            }
        }
    }

    public List<Performance> getPerformances(SQLiteDatabase db, UUID uuid){
        List<Performance> performances = new ArrayList<>();

        Cursor cursor = db.query(
                DataBaseContractReferee.Performance.TABLE_NAME,
                new String[]{
                        DataBaseContractReferee.Performance.COLUMN_INTERNAL_ID,
                        DataBaseContractReferee.Performance.COLUMN_TIME,
                        DataBaseContractReferee.Performance.COLUMN_PLACE,
                        DataBaseContractReferee.Performance.COLUMN_PLAYGROUND,
                        DataBaseContractReferee.Performance.COLUMN_DATE

                },
                DataBaseContractReferee.Performance.COLUMN_COMPETITION  + " = ?",
                new String[]{
                        uuid.toString()
                },
                null,
                null,
                null,
                null
        );
        Performance performance;
        while (cursor.moveToNext()){
            performance = new Performance();
            performance.setInternal_id(cursor.getInt(
                    cursor.getColumnIndexOrThrow(
                            DataBaseContractReferee.Performance.COLUMN_INTERNAL_ID
                    )
            ));

            performance.setTime(cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            DataBaseContractReferee.Performance.COLUMN_TIME
                    )
            ));
            performance.setPlace(cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            DataBaseContractReferee.Performance.COLUMN_PLACE
                    )
            ));
            performance.setPlayground(cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            DataBaseContractReferee.Performance.COLUMN_PLAYGROUND
                    )
            ));
            performance.setDate(cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            DataBaseContractReferee.Performance.COLUMN_DATE
                    )
            ));

            performances.add(0,performance);
        }

        return performances;
    }
    public PlayerRef getPlayerById(SQLiteDatabase db, UUID comp_id, int perf_id, int player_id) {
        PlayerRef playerRef = new PlayerRef();
        Cursor cursor = db.query(
                DataBaseContractReferee.PlayerRef.TABLE_NAME,
                null,
                DataBaseContractReferee.PlayerRef.COLUMN_COMPETITION + " = ? AND "
                        + DataBaseContractReferee.PlayerRef.COLUMN_PERFORMANCE + " = ? AND "
                        + DataBaseContractReferee.PlayerRef._ID + " = ?",
                new String[]{
                        comp_id.toString(),
                        String.valueOf(perf_id),
                        String.valueOf(player_id)
                },
                null,
                null,
                null,
                null
        );

        cursor.moveToNext();

        playerRef.setId(cursor.getInt(
                cursor.getColumnIndexOrThrow(DataBaseContractReferee.PlayerRef._ID)
        ));
        playerRef.setCategory(cursor.getString(
                cursor.getColumnIndexOrThrow(DataBaseContractReferee.PlayerRef.COLUMN_CATEGORY_ID)
        ));
        playerRef.setGrade(cursor.getInt(
                cursor.getColumnIndexOrThrow(DataBaseContractReferee.PlayerRef.COLUMN_GRADE)
        ));
        playerRef.setName(cursor.getString(cursor.getColumnIndexOrThrow(
                DataBaseContractReferee.PlayerRef.COLUMN_NAME)
        ));
        playerRef.setRegion(cursor.getString(
                cursor.getColumnIndexOrThrow(DataBaseContractReferee.PlayerRef.COLUMN_REGION_ID)
        ));

        return playerRef;
    }

    public List<Competition> getCompetitions(SQLiteDatabase db){
        List<Competition> competitions = new ArrayList<>();
        Cursor cursor = db.query(
                DataBaseContractReferee.Competition.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
        Competition competition;
        while (cursor.moveToNext()){
            competition = new Competition();
            competition.setUuid(UUID.fromString(cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            DataBaseContractReferee.Competition._ID))));
            competition.setName(cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            DataBaseContractReferee.Competition.COLUMN_NAME
                    )
            ));
            competition.setYear(cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            DataBaseContractReferee.Competition.COLUMN_YEAR
                    )
            ));
            competition.setPlace(cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            DataBaseContractReferee.Competition.COLUMN_PLACE
                    )
            ));
            competitions.add(0,competition);
        }
        return competitions;
    }

    public Protocol getProtocol(SQLiteDatabase db,UUID comp_id,int perf_id){
        Protocol protocol = new Protocol();
        Cursor cursor = db.query(
                DataBaseContractReferee.Protocol.TABLE_NAME,
                null,
                DataBaseContractReferee.Protocol.COLUMN_COMPETITION + " = ? AND "
                + DataBaseContractReferee.Protocol.COLUMN_PERFORMANCE + " = ?",
                new String[]{
                        comp_id.toString(),
                        String.valueOf(perf_id)
                },
                null,
                null,
                null,
                null
        );

        cursor.moveToNext();

        protocol.setId(cursor.getInt(
                cursor.getColumnIndexOrThrow(DataBaseContractReferee.Protocol._ID)
        ));

        protocol.setComp_id(UUID.fromString(cursor.getString(
                cursor.getColumnIndexOrThrow(
                        DataBaseContractReferee.Protocol.COLUMN_COMPETITION
                )
                        )
                )
        );
        protocol.setShots1(cursor.getString(
                cursor.getColumnIndexOrThrow(
                        DataBaseContractReferee.Protocol.COLUMN_SHOTS1
                )
        ));
        protocol.setShots2(cursor.getString(
                cursor.getColumnIndexOrThrow(
                        DataBaseContractReferee.Protocol.COLUMN_SHOTS2
                )
        ));
        protocol.setGame1(cursor.getString(
                cursor.getColumnIndexOrThrow(
                        DataBaseContractReferee.Protocol.COLUMN_GAME1
                )
        ));
        protocol.setGame2(cursor.getString(
                cursor.getColumnIndexOrThrow(
                        DataBaseContractReferee.Protocol.COLUMN_GAME2
                )
        ));
        protocol.setGames_sum(cursor.getString(
                cursor.getColumnIndexOrThrow(
                        DataBaseContractReferee.Protocol.COLUMN_GAMES_SUM
                )
        ));
        protocol.setLimit(cursor.getString(
                cursor.getColumnIndexOrThrow(
                        DataBaseContractReferee.Protocol.COLUMN_LIMIT
                )
        ));
        protocol.setPerf_id(cursor.getInt(
                cursor.getColumnIndexOrThrow(
                        DataBaseContractReferee.Protocol.COLUMN_PERFORMANCE
                )
        ));

        return protocol;
    }

    public void setProtocol(SQLiteDatabase db, Protocol protocol) {
        ContentValues values = new ContentValues();
        values.put(DataBaseContractReferee.Protocol.COLUMN_COMPETITION, String.valueOf(protocol.getComp_id()));
        values.put(DataBaseContractReferee.Protocol.COLUMN_PERFORMANCE, protocol.getPerf_id());
        values.put(DataBaseContractReferee.Protocol.COLUMN_GAME1,protocol.getGame1());
        values.put(DataBaseContractReferee.Protocol.COLUMN_GAME2,protocol.getGame2());
        values.put(DataBaseContractReferee.Protocol.COLUMN_GAMES_SUM,protocol.getGames_sum());
        values.put(DataBaseContractReferee.Protocol.COLUMN_LIMIT,protocol.getLimit());
        values.put(DataBaseContractReferee.Protocol.COLUMN_SHOTS1,protocol.getShots1());
        values.put(DataBaseContractReferee.Protocol.COLUMN_SHOTS2,protocol.getShots2());
        db.insert(DataBaseContractReferee.Protocol.TABLE_NAME,null,values);
    }
}
