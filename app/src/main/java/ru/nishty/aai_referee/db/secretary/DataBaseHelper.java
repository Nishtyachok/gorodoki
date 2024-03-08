package ru.nishty.aai_referee.db.secretary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import ru.nishty.aai_referee.db.referee.DataBaseContract;
import ru.nishty.aai_referee.entity.referee.Competition;
import ru.nishty.aai_referee.entity.referee.Performance;
import ru.nishty.aai_referee.entity.referee.Protocol;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "DataBase.db";

    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public DataBaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DataBaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ru.nishty.aai_referee.db.referee.DataBaseContract.SQL_CREATE_ENTRIES_COMPETITION);
        db.execSQL(ru.nishty.aai_referee.db.referee.DataBaseContract.SQL_CREATE_ENTRIES_PERFORMANCE);
        db.execSQL(ru.nishty.aai_referee.db.referee.DataBaseContract.SQL_CREATE_ENTRIES_PROTOCOL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(ru.nishty.aai_referee.db.referee.DataBaseContract.SQL_DELETE_ENTRIES_PROTOCOL);
        db.execSQL(ru.nishty.aai_referee.db.referee.DataBaseContract.SQL_DELETE_ENTRIES_PERFORMANCE);
        db.execSQL(ru.nishty.aai_referee.db.referee.DataBaseContract.SQL_DELETE_ENTRIES_COMPETITION);
        onCreate(db);
    }

    public void addCompetition(SQLiteDatabase db, Competition competition){
        ContentValues values = new ContentValues();
        values.put(ru.nishty.aai_referee.db.referee.DataBaseContract.Competition._ID, String.valueOf(competition.getUuid()));
        values.put(ru.nishty.aai_referee.db.referee.DataBaseContract.Competition.COLUMN_NAME, competition.getName());
        values.put(ru.nishty.aai_referee.db.referee.DataBaseContract.Competition.COLUMN_YEAR,competition.getYear());
        values.put(ru.nishty.aai_referee.db.referee.DataBaseContract.Competition.COLUMN_PLACE,competition.getPlace());
        values.put(ru.nishty.aai_referee.db.referee.DataBaseContract.Competition.COLUMN_DISCIPLINE,competition.getDiscipline());
        db.insert(ru.nishty.aai_referee.db.referee.DataBaseContract.Competition.TABLE_NAME,null,values);
        for (Performance performance :
                competition.getPerformances()) {
            values = new ContentValues();
            values.put(ru.nishty.aai_referee.db.referee.DataBaseContract.Performance.COLUMN_INTERNAL_ID,String.valueOf(performance.getInternal_id()));
            values.put(ru.nishty.aai_referee.db.referee.DataBaseContract.Performance.COLUMN_NAME,performance.getName());
            values.put(ru.nishty.aai_referee.db.referee.DataBaseContract.Performance.COLUMN_TIME,performance.getTime());
            values.put(ru.nishty.aai_referee.db.referee.DataBaseContract.Performance.COLUMN_GRADE,performance.getGrade());
            values.put(ru.nishty.aai_referee.db.referee.DataBaseContract.Performance.COLUMN_CATEGORY,performance.getCategory());
            values.put(ru.nishty.aai_referee.db.referee.DataBaseContract.Performance.COLUMN_REGION,performance.getRegion());
            values.put(ru.nishty.aai_referee.db.referee.DataBaseContract.Performance.COLUMN_PLACE,performance.getPlace());
            values.put(ru.nishty.aai_referee.db.referee.DataBaseContract.Performance.COLUMN_PLAYGROUND,performance.getPlayground());
            values.put(ru.nishty.aai_referee.db.referee.DataBaseContract.Performance.COLUMN_DATE,performance.getDate());

            values.put(ru.nishty.aai_referee.db.referee.DataBaseContract.Performance.COLUMN_COMPETITION,String.valueOf(competition.getUuid()));
            db.insert(ru.nishty.aai_referee.db.referee.DataBaseContract.Performance.TABLE_NAME,null,values);
        }
    }

    public List<Performance> getPerformances(SQLiteDatabase db, UUID uuid){
        List<Performance> performances = new ArrayList<>();

        Cursor cursor = db.query(
                ru.nishty.aai_referee.db.referee.DataBaseContract.Performance.TABLE_NAME,
                new String[]{
                        ru.nishty.aai_referee.db.referee.DataBaseContract.Performance.COLUMN_INTERNAL_ID,
                        ru.nishty.aai_referee.db.referee.DataBaseContract.Performance.COLUMN_NAME,
                        ru.nishty.aai_referee.db.referee.DataBaseContract.Performance.COLUMN_TIME,
                        ru.nishty.aai_referee.db.referee.DataBaseContract.Performance.COLUMN_PLACE,
                        ru.nishty.aai_referee.db.referee.DataBaseContract.Performance.COLUMN_PLAYGROUND,
                        ru.nishty.aai_referee.db.referee.DataBaseContract.Performance.COLUMN_DATE,
                        ru.nishty.aai_referee.db.referee.DataBaseContract.Performance.COLUMN_GRADE,
                        ru.nishty.aai_referee.db.referee.DataBaseContract.Performance.COLUMN_REGION,
                        ru.nishty.aai_referee.db.referee.DataBaseContract.Performance.COLUMN_CATEGORY

                },
                ru.nishty.aai_referee.db.referee.DataBaseContract.Performance.COLUMN_COMPETITION  + " = ?",
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
                            ru.nishty.aai_referee.db.referee.DataBaseContract.Performance.COLUMN_INTERNAL_ID
                    )
            ));
            performance.setName(cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            ru.nishty.aai_referee.db.referee.DataBaseContract.Performance.COLUMN_NAME
                    )
            ));
            performance.setTime(cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            ru.nishty.aai_referee.db.referee.DataBaseContract.Performance.COLUMN_TIME
                    )
            ));
            performance.setPlace(cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            ru.nishty.aai_referee.db.referee.DataBaseContract.Performance.COLUMN_PLACE
                    )
            ));
            performance.setPlayground(cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            ru.nishty.aai_referee.db.referee.DataBaseContract.Performance.COLUMN_PLAYGROUND
                    )
            ));
            performance.setDate(cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            ru.nishty.aai_referee.db.referee.DataBaseContract.Performance.COLUMN_DATE
                    )
            ));
            performance.setGrade(cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            ru.nishty.aai_referee.db.referee.DataBaseContract.Performance.COLUMN_GRADE
                    )
            ));
            performance.setRegion(cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            ru.nishty.aai_referee.db.referee.DataBaseContract.Performance.COLUMN_REGION
                    )
            ));
            performance.setCategory(cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            ru.nishty.aai_referee.db.referee.DataBaseContract.Performance.COLUMN_CATEGORY
                    )
            ));
            performances.add(0,performance);
        }

        return performances;
    }

    public List<Competition> getCompetitions(SQLiteDatabase db){
        List<Competition> competitions = new ArrayList<>();
        Cursor cursor = db.query(
                ru.nishty.aai_referee.db.referee.DataBaseContract.Competition.TABLE_NAME,
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
                            ru.nishty.aai_referee.db.referee.DataBaseContract.Competition._ID))));
            competition.setName(cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            ru.nishty.aai_referee.db.referee.DataBaseContract.Competition.COLUMN_NAME
                    )
            ));
            competition.setYear(cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            ru.nishty.aai_referee.db.referee.DataBaseContract.Competition.COLUMN_YEAR
                    )
            ));
            competition.setPlace(cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            ru.nishty.aai_referee.db.referee.DataBaseContract.Competition.COLUMN_PLACE
                    )
            ));
            competition.setDiscipline(cursor.getInt(
                    cursor.getColumnIndexOrThrow(
                            ru.nishty.aai_referee.db.referee.DataBaseContract.Competition.COLUMN_DISCIPLINE
                    )
            ));
            competitions.add(0,competition);
        }
        return competitions;
    }

    public Protocol getProtocol(SQLiteDatabase db,UUID comp_id,int perf_id){
        Protocol protocol = new Protocol();
        Cursor cursor = db.query(
                ru.nishty.aai_referee.db.referee.DataBaseContract.Protocol.TABLE_NAME,
                null,
                ru.nishty.aai_referee.db.referee.DataBaseContract.Protocol.COLUMN_COMPETITION + " = ? AND "
                + ru.nishty.aai_referee.db.referee.DataBaseContract.Protocol.COLUMN_PERFORMANCE + " = ?",
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
                cursor.getColumnIndexOrThrow(ru.nishty.aai_referee.db.referee.DataBaseContract.Protocol._ID)
        ));

        protocol.setComp_id(UUID.fromString(cursor.getString(
                cursor.getColumnIndexOrThrow(
                        ru.nishty.aai_referee.db.referee.DataBaseContract.Protocol.COLUMN_COMPETITION
                )
                        )
                )
        );
        protocol.setShots1(cursor.getString(
                cursor.getColumnIndexOrThrow(
                        ru.nishty.aai_referee.db.referee.DataBaseContract.Protocol.COLUMN_SHOTS1
                )
        ));
        protocol.setShots2(cursor.getString(
                cursor.getColumnIndexOrThrow(
                        ru.nishty.aai_referee.db.referee.DataBaseContract.Protocol.COLUMN_SHOTS2
                )
        ));
        protocol.setGame1(cursor.getString(
                cursor.getColumnIndexOrThrow(
                        ru.nishty.aai_referee.db.referee.DataBaseContract.Protocol.COLUMN_GAME1
                )
        ));
        protocol.setGame2(cursor.getString(
                cursor.getColumnIndexOrThrow(
                        ru.nishty.aai_referee.db.referee.DataBaseContract.Protocol.COLUMN_GAME2
                )
        ));
        protocol.setGames_sum(cursor.getString(
                cursor.getColumnIndexOrThrow(
                        ru.nishty.aai_referee.db.referee.DataBaseContract.Protocol.COLUMN_GAMES_SUM
                )
        ));
        protocol.setLimit(cursor.getString(
                cursor.getColumnIndexOrThrow(
                        ru.nishty.aai_referee.db.referee.DataBaseContract.Protocol.COLUMN_LIMIT
                )
        ));
        protocol.setPerf_id(cursor.getInt(
                cursor.getColumnIndexOrThrow(
                        ru.nishty.aai_referee.db.referee.DataBaseContract.Protocol.COLUMN_PERFORMANCE
                )
        ));

        return protocol;
    }

    public void setProtocol(SQLiteDatabase db, Protocol protocol) {
        ContentValues values = new ContentValues();
        values.put(ru.nishty.aai_referee.db.referee.DataBaseContract.Protocol.COLUMN_COMPETITION, String.valueOf(protocol.getComp_id()));
        values.put(ru.nishty.aai_referee.db.referee.DataBaseContract.Protocol.COLUMN_PERFORMANCE, protocol.getPerf_id());
        values.put(ru.nishty.aai_referee.db.referee.DataBaseContract.Protocol.COLUMN_GAME1,protocol.getGame1());
        values.put(ru.nishty.aai_referee.db.referee.DataBaseContract.Protocol.COLUMN_GAME2,protocol.getGame2());
        values.put(ru.nishty.aai_referee.db.referee.DataBaseContract.Protocol.COLUMN_GAMES_SUM,protocol.getGames_sum());
        values.put(ru.nishty.aai_referee.db.referee.DataBaseContract.Protocol.COLUMN_LIMIT,protocol.getLimit());
        values.put(ru.nishty.aai_referee.db.referee.DataBaseContract.Protocol.COLUMN_SHOTS1,protocol.getShots1());
        values.put(ru.nishty.aai_referee.db.referee.DataBaseContract.Protocol.COLUMN_SHOTS2,protocol.getShots2());
        db.insert(DataBaseContract.Protocol.TABLE_NAME,null,values);
    }
}
