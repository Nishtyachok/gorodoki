package ru.nishty.aai_referee.db.secretary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import ru.nishty.aai_referee.db.referee.DataBaseContractReferee;
import ru.nishty.aai_referee.entity.secretary.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DataBaseHelperSecretary extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "DataBaseSecretary.db";

    public DataBaseHelperSecretary(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public DataBaseHelperSecretary(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DataBaseHelperSecretary(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DataBaseContractSecretary.SQL_CREATE_ENTRIES_COMPETITION);
        db.execSQL(DataBaseContractSecretary.SQL_CREATE_ENTRIES_PERFORMANCE);
        db.execSQL(DataBaseContractSecretary.SQL_CREATE_ENTRIES_PROTOCOL);
        db.execSQL(DataBaseContractSecretary.SQL_CREATE_ENTRIES_CATEGORY);
        db.execSQL(DataBaseContractSecretary.SQL_CREATE_ENTRIES_JUDGE);
        db.execSQL(DataBaseContractSecretary.SQL_CREATE_ENTRIES_PERFORMANCEPLAYER);
        db.execSQL(DataBaseContractSecretary.SQL_CREATE_ENTRIES_PLAYER);
        db.execSQL(DataBaseContractSecretary.SQL_CREATE_ENTRIES_REGION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DataBaseContractSecretary.SQL_DELETE_ENTRIES_PROTOCOL);
        db.execSQL(DataBaseContractSecretary.SQL_DELETE_ENTRIES_PERFORMANCE);
        db.execSQL(DataBaseContractSecretary.SQL_DELETE_ENTRIES_COMPETITION);
        db.execSQL(DataBaseContractSecretary.SQL_DELETE_ENTRIES_CATEGORY);
        db.execSQL(DataBaseContractSecretary.SQL_DELETE_ENTRIES_JUDGE);
        db.execSQL(DataBaseContractSecretary.SQL_DELETE_ENTRIES_PERFORMANCEPLAYER);
        db.execSQL(DataBaseContractSecretary.SQL_DELETE_ENTRIES_PLAYER);
        db.execSQL(DataBaseContractSecretary.SQL_DELETE_ENTRIES_REGION);
        onCreate(db);
    }


    public void addCompetition(SQLiteDatabase db, CompetitionSecretary competitionSecretary){
        ContentValues values = new ContentValues();
        values.put(DataBaseContractSecretary.Competition._ID, String.valueOf(competitionSecretary.getUuid()));
        values.put(DataBaseContractSecretary.Competition.COLUMN_NAME, competitionSecretary.getName());
        values.put(DataBaseContractSecretary.Competition.COLUMN_YEAR, competitionSecretary.getYear());
        values.put(DataBaseContractSecretary.Competition.COLUMN_PLACE, competitionSecretary.getPlace());
        values.put(DataBaseContractSecretary.Competition.COLUMN_HEADJUDGE, competitionSecretary.getHeadJudge());
        values.put(DataBaseContractSecretary.Competition.COLUMN_HEADSECRETARY, competitionSecretary.getHeadSecretary());
        db.insert(DataBaseContractSecretary.Competition.TABLE_NAME,null,values);

        for (PerformanceSecretary performanceSecretary : competitionSecretary.getPerformances()) {
            values = new ContentValues();
            values.put(DataBaseContractSecretary.Performance.COLUMN_INTERNAL_ID,String.valueOf(performanceSecretary.getInternal_id()));
            values.put(DataBaseContractSecretary.Performance.COLUMN_COMPETITION,String.valueOf(competitionSecretary.getUuid()));
            values.put(DataBaseContractSecretary.Performance.COLUMN_REGION_ID, performanceSecretary.getRegionId());
            values.put(DataBaseContractSecretary.Performance.COLUMN_TIME, performanceSecretary.getTime());
            values.put(DataBaseContractSecretary.Performance.COLUMN_DATE, performanceSecretary.getDate());
            values.put(DataBaseContractSecretary.Performance.COLUMN_PLACE, performanceSecretary.getPlace());
            values.put(DataBaseContractSecretary.Performance.COLUMN_PLAYGROUND, performanceSecretary.getPlayground());
            values.put(DataBaseContractSecretary.Performance.COLUMN_CATEGORY_ID, performanceSecretary.getCategoryId());
            values.put(DataBaseContractSecretary.Performance.COLUMN_JUDGE_ID, performanceSecretary.getJudgeId());
            db.insert(DataBaseContractSecretary.Performance.TABLE_NAME,null,values);
        }
        for (Judge judge : competitionSecretary.getJudges()) {
            values = new ContentValues();
            values.put(DataBaseContractSecretary.Judge.COLUMN_COMPETITION,String.valueOf(competitionSecretary.getUuid()));
            values.put(DataBaseContractSecretary.Judge.COLUMN_CATEGORY,judge.getCategory());
            values.put(DataBaseContractSecretary.Judge.COLUMN_REGION,judge.getRegion());
            values.put(DataBaseContractSecretary.Judge.COLUMN_NAME,judge.getName());
            db.insert(DataBaseContractSecretary.Judge.TABLE_NAME,null,values);
        }
        for (Category category : competitionSecretary.getCategories()) {
            values = new ContentValues();
            values.put(DataBaseContractSecretary.Category.COLUMN_COMPETITION,String.valueOf(competitionSecretary.getUuid()));
            values.put(DataBaseContractSecretary.Category.COLUMN_LIMIT,category.getLimit());
            values.put(DataBaseContractSecretary.Category.COLUMN_FIGURES,category.getFigures());
            values.put(DataBaseContractSecretary.Category.COLUMN_TOURS,category.getTours());
            values.put(DataBaseContractSecretary.Category.COLUMN_NAME,category.getName());
            db.insert(DataBaseContractSecretary.Category.TABLE_NAME,null,values);
        }
        for (Region region : competitionSecretary.getRegions()) {
            values = new ContentValues();
            values.put(DataBaseContractSecretary.Region.COLUMN_COMPETITION,String.valueOf(competitionSecretary.getUuid()));
            values.put(DataBaseContractSecretary.Region.COLUMN_NAME,region.getName());
            db.insert(DataBaseContractSecretary.Region.TABLE_NAME,null,values);
        }
        for (Player player : competitionSecretary.getPlayers()) {
            values = new ContentValues();
            values.put(DataBaseContractSecretary.Player.COLUMN_COMPETITION,String.valueOf(competitionSecretary.getUuid()));
            values.put(DataBaseContractSecretary.Player.COLUMN_REGION_ID,player.getRegionId());
            values.put(DataBaseContractSecretary.Player.COLUMN_CATEGORY_ID,player.getCategoryId());
            values.put(DataBaseContractSecretary.Player.COLUMN_GRADE,player.getGrade());
            values.put(DataBaseContractSecretary.Player.COLUMN_NAME,player.getName());
            db.insert(DataBaseContractSecretary.Player.TABLE_NAME,null,values);
        }

    }


    public List<PerformanceSecretary> getPerformances(SQLiteDatabase db, UUID uuid){
        List<PerformanceSecretary> performanceSecretaries = new ArrayList<>();

        Cursor cursor = db.query(
                DataBaseContractSecretary.Performance.TABLE_NAME,
                new String[]{
                        DataBaseContractSecretary.Performance.COLUMN_INTERNAL_ID,
                        DataBaseContractSecretary.Performance.COLUMN_TIME,
                        DataBaseContractSecretary.Performance.COLUMN_PLACE,
                        DataBaseContractSecretary.Performance.COLUMN_PLAYGROUND,
                        DataBaseContractSecretary.Performance.COLUMN_DATE,
                        DataBaseContractSecretary.Performance.COLUMN_REGION_ID,
                        DataBaseContractSecretary.Performance.COLUMN_CATEGORY_ID,
                        DataBaseContractSecretary.Performance.COLUMN_JUDGE_ID



                },
                DataBaseContractSecretary.Performance.COLUMN_COMPETITION  + " = ?",
                new String[]{
                        uuid.toString()
                },
                null,
                null,
                null,
                null
        );
        PerformanceSecretary performanceSecretary;
        while (cursor.moveToNext()){
            performanceSecretary = new PerformanceSecretary();
            performanceSecretary.setInternal_id(cursor.getInt(
                    cursor.getColumnIndexOrThrow(
                            DataBaseContractSecretary.Performance.COLUMN_INTERNAL_ID
                    )
            ));

            performanceSecretary.setTime(cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            DataBaseContractSecretary.Performance.COLUMN_TIME
                    )
            ));
            performanceSecretary.setPlace(cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            DataBaseContractSecretary.Performance.COLUMN_PLACE
                    )
            ));
            performanceSecretary.setPlayground(cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            DataBaseContractSecretary.Performance.COLUMN_PLAYGROUND
                    )
            ));
            performanceSecretary.setDate(cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            DataBaseContractSecretary.Performance.COLUMN_DATE
                    )
            ));

            performanceSecretary.setRegionId(cursor.getInt(
                    cursor.getColumnIndexOrThrow(
                            DataBaseContractSecretary.Performance.COLUMN_REGION_ID
                    )
            ));
            performanceSecretary.setCategoryId(cursor.getInt(
                    cursor.getColumnIndexOrThrow(
                            DataBaseContractSecretary.Performance.COLUMN_CATEGORY_ID
                    )
            ));
            performanceSecretaries.add(0, performanceSecretary);
        }

        return performanceSecretaries;
    }

    public List<CompetitionSecretary> getCompetitions(SQLiteDatabase db){
        List<CompetitionSecretary> competitionSecretaries = new ArrayList<>();
        Cursor cursor = db.query(
                DataBaseContractSecretary.Competition.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
        CompetitionSecretary competitionSecretary;
        while (cursor.moveToNext()){
            competitionSecretary = new CompetitionSecretary();
            competitionSecretary.setUuid(UUID.fromString(cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            DataBaseContractSecretary.Competition._ID))));
            competitionSecretary.setName(cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            DataBaseContractSecretary.Competition.COLUMN_NAME
                    )
            ));
            competitionSecretary.setYear(cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            DataBaseContractSecretary.Competition.COLUMN_YEAR
                    )
            ));
            competitionSecretary.setPlace(cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            DataBaseContractSecretary.Competition.COLUMN_PLACE
                    )
            ));
            competitionSecretary.setHeadJudge(cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            DataBaseContractSecretary.Competition.COLUMN_HEADJUDGE
                    )
            ));
            competitionSecretary.setHeadSecretary(cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            DataBaseContractSecretary.Competition.COLUMN_HEADSECRETARY
                    )
            ));
            competitionSecretaries.add(0, competitionSecretary);
        }
        return competitionSecretaries;
    }

    public Protocol getProtocol(SQLiteDatabase db,UUID comp_id,int perf_id){
        Protocol protocol = new Protocol();
        Cursor cursor = db.query(
                DataBaseContractSecretary.Protocol.TABLE_NAME,
                null,
                DataBaseContractSecretary.Protocol.COLUMN_COMPETITION + " = ? AND "
                + DataBaseContractSecretary.Protocol.COLUMN_PERFORMANCE_ID + " = ?",
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
                cursor.getColumnIndexOrThrow(DataBaseContractSecretary.Protocol._ID)
        ));

        protocol.setComp_id(UUID.fromString(cursor.getString(
                cursor.getColumnIndexOrThrow(
                        DataBaseContractSecretary.Protocol.COLUMN_COMPETITION
                )
                        )
                )
        );
        protocol.setShots1(cursor.getString(
                cursor.getColumnIndexOrThrow(
                        DataBaseContractSecretary.Protocol.COLUMN_SHOTS1
                )
        ));
        protocol.setShots2(cursor.getString(
                cursor.getColumnIndexOrThrow(
                        DataBaseContractSecretary.Protocol.COLUMN_SHOTS2
                )
        ));
        protocol.setGame1(cursor.getInt(
                cursor.getColumnIndexOrThrow(
                        DataBaseContractSecretary.Protocol.COLUMN_GAME1
                )
        ));
        protocol.setGame2(cursor.getInt(
                cursor.getColumnIndexOrThrow(
                        DataBaseContractSecretary.Protocol.COLUMN_GAME2
                )
        ));
        protocol.setGames_sum(cursor.getInt(
                cursor.getColumnIndexOrThrow(
                        DataBaseContractSecretary.Protocol.COLUMN_GAMES_SUM
                )
        ));
        protocol.setLimit(cursor.getInt(
                cursor.getColumnIndexOrThrow(
                        DataBaseContractSecretary.Protocol.COLUMN_LIMIT
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
        values.put(DataBaseContractSecretary.Protocol.COLUMN_COMPETITION, String.valueOf(protocol.getComp_id()));
        values.put(DataBaseContractSecretary.Protocol.COLUMN_PERFORMANCE_ID, protocol.getPerf_id());
        values.put(DataBaseContractSecretary.Protocol.COLUMN_GAME1,protocol.getGame1());
        values.put(DataBaseContractSecretary.Protocol.COLUMN_GAME2,protocol.getGame2());
        values.put(DataBaseContractSecretary.Protocol.COLUMN_GAMES_SUM,protocol.getGames_sum());
        values.put(DataBaseContractSecretary.Protocol.COLUMN_LIMIT,protocol.getLimit());
        values.put(DataBaseContractSecretary.Protocol.COLUMN_SHOTS1,protocol.getShots1());
        values.put(DataBaseContractSecretary.Protocol.COLUMN_SHOTS2,protocol.getShots2());
        values.put(DataBaseContractSecretary.Protocol.COLUMN_PLAYER_ID,protocol.getPlayer_id());

        values.put(DataBaseContractSecretary.Protocol.COLUMN_SHOTS2,protocol.getTours());

        db.insert(DataBaseContractSecretary.Protocol.TABLE_NAME,null,values);
    }
}
