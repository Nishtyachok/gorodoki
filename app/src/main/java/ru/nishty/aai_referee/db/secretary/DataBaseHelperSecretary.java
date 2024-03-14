package ru.nishty.aai_referee.db.secretary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import androidx.annotation.Nullable;
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
    public void addCategory(SQLiteDatabase db, Category category, UUID competitionUuid) {
        ContentValues values = new ContentValues();
        values.put(DataBaseContractSecretary.Category.COLUMN_COMPETITION, competitionUuid.toString());
        values.put(DataBaseContractSecretary.Category.COLUMN_NAME, category.getName());
        values.put(DataBaseContractSecretary.Category.COLUMN_LIMIT, category.getLimit());
        values.put(DataBaseContractSecretary.Category.COLUMN_FIGURES, category.getFigures());
        values.put(DataBaseContractSecretary.Category.COLUMN_TOURS, category.getTours());
        db.insert(DataBaseContractSecretary.Category.TABLE_NAME, null, values);
        long newRowId = db.insert(DataBaseContractSecretary.Category.TABLE_NAME, null, values);

        if (newRowId == -1) {
            // Вставка не удалась, выполните соответствующие действия
            Log.e(DATABASE_NAME, "Ошибка при добавлении категории");
        } else {
            // Категория успешно добавлена, выполните соответствующие действия
            Log.d(DATABASE_NAME, "Категория успешно добавлена с ID: " + newRowId);
        }
    }

    /*

    соревнование
    категория+
    судья
    регион
    игрок
    игрок_игра
    игра
    протокол

    */

    public List<Category> getCategories(SQLiteDatabase db, UUID competitionUuid) {
        List<Category> categories = new ArrayList<>();

        Cursor cursor = db.query(
                DataBaseContractSecretary.Category.TABLE_NAME,
                new String[]{
                        DataBaseContractSecretary.Category._ID,
                        DataBaseContractSecretary.Category.COLUMN_NAME,
                        DataBaseContractSecretary.Category.COLUMN_LIMIT,
                        DataBaseContractSecretary.Category.COLUMN_FIGURES,
                        DataBaseContractSecretary.Category.COLUMN_TOURS
                },
                DataBaseContractSecretary.Category.COLUMN_COMPETITION  + " = ?",
                new String[]{
                        competitionUuid.toString()
                },
                null,
                null,
                null,
                null
        );
        Category category;
        while (cursor.moveToNext()) {
            category = new Category();
            category.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseContractSecretary.Category._ID)));
            category.setName(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseContractSecretary.Category.COLUMN_NAME)));
            category.setLimit(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseContractSecretary.Category.COLUMN_LIMIT)));
            category.setFigures(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseContractSecretary.Category.COLUMN_FIGURES)));
            category.setTours(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseContractSecretary.Category.COLUMN_TOURS)));
            categories.add(0, category);
        }
        return categories;
    }
    public void setCategory(SQLiteDatabase db, Category category) {
        ContentValues values = new ContentValues();
        values.put(DataBaseContractSecretary.Category._ID, category.getId());
        values.put(DataBaseContractSecretary.Category.COLUMN_COMPETITION, String.valueOf(category.getComp_id()));
        values.put(DataBaseContractSecretary.Category.COLUMN_NAME,category.getName());
        values.put(DataBaseContractSecretary.Category.COLUMN_LIMIT,category.getLimit());
        values.put(DataBaseContractSecretary.Category.COLUMN_FIGURES,category.getFigures());
        values.put(DataBaseContractSecretary.Category.COLUMN_TOURS,category.getLimit());

        db.insert(DataBaseContractSecretary.Category.TABLE_NAME,null,values);
    }
    public void addRegion(SQLiteDatabase db, Region region, UUID competitionUuid) {
        ContentValues values = new ContentValues();
        values.put(DataBaseContractSecretary.Region.COLUMN_COMPETITION, competitionUuid.toString());
        values.put(DataBaseContractSecretary.Region.COLUMN_NAME, region.getName());
        db.insert(DataBaseContractSecretary.Region.TABLE_NAME, null, values);
        long newRowId = db.insert(DataBaseContractSecretary.Region.TABLE_NAME, null, values);

        if (newRowId == -1) {
            // Вставка не удалась, выполните соответствующие действия
            Log.e(DATABASE_NAME, "Ошибка при добавлении региона");
        } else {
            // Категория успешно добавлена, выполните соответствующие действия
            Log.d(DATABASE_NAME, "Регион успешно добавлен с ID: " + newRowId);
        }
    }

    public List<Region> getRegions(SQLiteDatabase db, UUID competitionUuid) {
        List<Region> regions = new ArrayList<>();

        Cursor cursor = db.query(
                DataBaseContractSecretary.Region.TABLE_NAME,
                new String[]{
                        DataBaseContractSecretary.Region._ID,
                        DataBaseContractSecretary.Region.COLUMN_NAME
                },
                DataBaseContractSecretary.Region.COLUMN_COMPETITION  + " = ?",
                new String[]{
                        competitionUuid.toString()
                },
                null,
                null,
                null,
                null
        );
        Region region;
        while (cursor.moveToNext()) {
            region = new Region();
            region.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseContractSecretary.Region._ID)));
            region.setName(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseContractSecretary.Region.COLUMN_NAME)));
            regions.add(0, region);
        }
        return regions;
    }
    public void setRegion(SQLiteDatabase db, Region region) {
        ContentValues values = new ContentValues();
        values.put(DataBaseContractSecretary.Region._ID, region.getId());
        values.put(DataBaseContractSecretary.Region.COLUMN_COMPETITION, String.valueOf(region.getComp_id()));
        values.put(DataBaseContractSecretary.Region.COLUMN_NAME,region.getName());

        db.insert(DataBaseContractSecretary.Region.TABLE_NAME,null,values);
    }
    public void addJudge(SQLiteDatabase db, Judge judge, UUID competitionUuid) {
        ContentValues values = new ContentValues();
        values.put(DataBaseContractSecretary.Judge.COLUMN_COMPETITION, competitionUuid.toString());
        values.put(DataBaseContractSecretary.Judge.COLUMN_NAME, judge.getName());
        values.put(DataBaseContractSecretary.Judge.COLUMN_CATEGORY, judge.getCategory());
        values.put(DataBaseContractSecretary.Judge.COLUMN_REGION, judge.getRegion());
        db.insert(DataBaseContractSecretary.Judge.TABLE_NAME, null, values);
        long newRowId = db.insert(DataBaseContractSecretary.Judge.TABLE_NAME, null, values);

        if (newRowId == -1) {
            // Вставка не удалась, выполните соответствующие действия
            Log.e(DATABASE_NAME, "Ошибка при добавлении судьи");
        } else {
            // Категория успешно добавлена, выполните соответствующие действия
            Log.d(DATABASE_NAME, "Судья успешно добавлен с ID: " + newRowId);
        }
    }

    public List<Judge> getJudges(SQLiteDatabase db, UUID competitionUuid) {
        List<Judge> judges = new ArrayList<>();

        Cursor cursor = db.query(
                DataBaseContractSecretary.Judge.TABLE_NAME,
                new String[]{
                        DataBaseContractSecretary.Judge._ID,
                        DataBaseContractSecretary.Judge.COLUMN_NAME,
                        DataBaseContractSecretary.Judge.COLUMN_CATEGORY,
                        DataBaseContractSecretary.Judge.COLUMN_REGION
                },
                DataBaseContractSecretary.Judge.COLUMN_COMPETITION  + " = ?",
                new String[]{
                        competitionUuid.toString()
                },
                null,
                null,
                null,
                null
        );
        Judge judge;
        while (cursor.moveToNext()) {
            judge = new Judge();
            judge.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseContractSecretary.Judge._ID)));
            judge.setName(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseContractSecretary.Judge.COLUMN_NAME)));
            judge.setName(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseContractSecretary.Judge.COLUMN_CATEGORY)));
            judge.setName(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseContractSecretary.Judge.COLUMN_REGION)));

            judges.add(0, judge);
        }
        return judges;
    }
    public void setJudge(SQLiteDatabase db, Judge judge) {
        ContentValues values = new ContentValues();
        values.put(DataBaseContractSecretary.Judge._ID, judge.getId());
        values.put(DataBaseContractSecretary.Judge.COLUMN_COMPETITION, String.valueOf(judge.getComp_id()));
        values.put(DataBaseContractSecretary.Judge.COLUMN_NAME,judge.getName());
        values.put(DataBaseContractSecretary.Judge.COLUMN_CATEGORY,judge.getCategory());
        values.put(DataBaseContractSecretary.Judge.COLUMN_REGION,judge.getRegion());

        db.insert(DataBaseContractSecretary.Judge.TABLE_NAME,null,values);
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
            performanceSecretary.setJudgeId(cursor.getInt(
                    cursor.getColumnIndexOrThrow(
                            DataBaseContractSecretary.Performance.COLUMN_JUDGE_ID
                    )
            ));

            //performanceSecretaries.add(0, performanceSecretary);
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
                        DataBaseContractSecretary.Protocol.COLUMN_PERFORMANCE_ID
                )
        ));

        return protocol;
    }
    public void addPlayer(SQLiteDatabase db, Player player, UUID competitionUuid, int regionId, int categoryId) {
        ContentValues values = new ContentValues();
        values.put(DataBaseContractSecretary.Player.COLUMN_COMPETITION, competitionUuid.toString());
        values.put(DataBaseContractSecretary.Player.COLUMN_NAME, player.getName());
        values.put(DataBaseContractSecretary.Player.COLUMN_REGION_ID, regionId);
        values.put(DataBaseContractSecretary.Player.COLUMN_CATEGORY_ID, categoryId);
        db.insert(DataBaseContractSecretary.Player.TABLE_NAME, null, values);
    }

    public List<Judge> getPlayer(SQLiteDatabase db, UUID competitionUuid) {
        List<Judge> judges = new ArrayList<>();

        Cursor cursor = db.query(
                DataBaseContractSecretary.Judge.TABLE_NAME,
                new String[]{
                        DataBaseContractSecretary.Judge.COLUMN_NAME,
                        DataBaseContractSecretary.Judge.COLUMN_CATEGORY,
                        DataBaseContractSecretary.Judge.COLUMN_REGION
                },
                DataBaseContractSecretary.Judge.COLUMN_COMPETITION  + " = ?",
                new String[]{
                        competitionUuid.toString()
                },
                null,
                null,
                null,
                null
        );
        Judge judge;
        while (cursor.moveToNext()) {
            judge = new Judge();
            judge.setName(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseContractSecretary.Judge.COLUMN_NAME)));
            judge.setName(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseContractSecretary.Judge.COLUMN_CATEGORY)));
            judge.setName(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseContractSecretary.Judge.COLUMN_REGION)));

            judges.add(0, judge);
        }
        return judges;
    }
    public void setPlayer(SQLiteDatabase db, Judge judge) {
        ContentValues values = new ContentValues();
        values.put(DataBaseContractSecretary.Judge.COLUMN_COMPETITION, String.valueOf(judge.getComp_id()));
        values.put(DataBaseContractSecretary.Judge.COLUMN_NAME,judge.getName());
        values.put(DataBaseContractSecretary.Judge.COLUMN_CATEGORY,judge.getCategory());
        values.put(DataBaseContractSecretary.Judge.COLUMN_REGION,judge.getRegion());

        db.insert(DataBaseContractSecretary.Judge.TABLE_NAME,null,values);
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
