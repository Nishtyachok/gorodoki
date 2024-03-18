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


    public void addCompetition(SQLiteDatabase db, CompetitionSecretary competitionSecretary) {
        ContentValues values = new ContentValues();
        values.put(DataBaseContractSecretary.Competition._ID, String.valueOf(competitionSecretary.getUuid()));
        values.put(DataBaseContractSecretary.Competition.COLUMN_NAME, competitionSecretary.getName());
        values.put(DataBaseContractSecretary.Competition.COLUMN_YEAR, competitionSecretary.getYear());
        values.put(DataBaseContractSecretary.Competition.COLUMN_PLACE, competitionSecretary.getPlace());
        values.put(DataBaseContractSecretary.Competition.COLUMN_HEADJUDGE, competitionSecretary.getHeadJudge());
        values.put(DataBaseContractSecretary.Competition.COLUMN_HEADSECRETARY, competitionSecretary.getHeadSecretary());
        db.insert(DataBaseContractSecretary.Competition.TABLE_NAME, null, values);

        for (Judge judge : competitionSecretary.getJudges()) {
            ContentValues judgeValues = new ContentValues();
            judgeValues.put(DataBaseContractSecretary.Judge.COLUMN_COMPETITION, String.valueOf(competitionSecretary.getUuid()));
            judgeValues.put(DataBaseContractSecretary.Judge.COLUMN_CATEGORY, judge.getCategory());
            judgeValues.put(DataBaseContractSecretary.Judge.COLUMN_REGION, judge.getRegion());
            judgeValues.put(DataBaseContractSecretary.Judge.COLUMN_NAME, judge.getName());
            db.insert(DataBaseContractSecretary.Judge.TABLE_NAME, null, judgeValues);
        }

        for (Category category : competitionSecretary.getCategories()) {
            ContentValues categoryValues = new ContentValues();
            categoryValues.put(DataBaseContractSecretary.Category.COLUMN_COMPETITION, String.valueOf(competitionSecretary.getUuid()));
            categoryValues.put(DataBaseContractSecretary.Category.COLUMN_LIMIT, category.getLimit());
            categoryValues.put(DataBaseContractSecretary.Category.COLUMN_FIGURES, category.getFigures());
            categoryValues.put(DataBaseContractSecretary.Category.COLUMN_TOURS, category.getTours());
            categoryValues.put(DataBaseContractSecretary.Category.COLUMN_NAME, category.getName());
            db.insert(DataBaseContractSecretary.Category.TABLE_NAME, null, categoryValues);
        }

        for (Region region : competitionSecretary.getRegions()) {
            ContentValues regionValues = new ContentValues();
            regionValues.put(DataBaseContractSecretary.Region.COLUMN_COMPETITION, String.valueOf(competitionSecretary.getUuid()));
            regionValues.put(DataBaseContractSecretary.Region.COLUMN_NAME, region.getName());
            db.insert(DataBaseContractSecretary.Region.TABLE_NAME, null, regionValues);
        }

        for (Player player : competitionSecretary.getPlayers()) {
            ContentValues playerValues = new ContentValues();
            playerValues.put(DataBaseContractSecretary.Player.COLUMN_COMPETITION, String.valueOf(competitionSecretary.getUuid()));
            playerValues.put(DataBaseContractSecretary.Player.COLUMN_REGION_ID, player.getRegionId());
            playerValues.put(DataBaseContractSecretary.Player.COLUMN_CATEGORY_ID, player.getCategoryId());
            playerValues.put(DataBaseContractSecretary.Player.COLUMN_GRADE, player.getGrade());
            playerValues.put(DataBaseContractSecretary.Player.COLUMN_NAME, player.getName());
            db.insert(DataBaseContractSecretary.Player.TABLE_NAME, null, playerValues);
        }
        long newRowId = db.insert(DataBaseContractSecretary.Competition.TABLE_NAME, null, values);

        if (newRowId == -1) {
            Log.e(DATABASE_NAME, "Ошибка при добавлении соревнования");
        } else {
            Log.d(DATABASE_NAME, "Соревнование успешно добавлено с ID: " + newRowId);
        }
}


    public long addCategory(SQLiteDatabase db, Category category, UUID competitionUuid) {
        Cursor cursor = db.query(
                DataBaseContractSecretary.Category.TABLE_NAME,
                new String[]{DataBaseContractSecretary.Category._ID},
                DataBaseContractSecretary.Category.COLUMN_NAME + " = ? AND " +
                        DataBaseContractSecretary.Category.COLUMN_COMPETITION + " = ?",
                new String[]{category.getName(), competitionUuid.toString()},
                null, null, null
        );
        long newRowId = -1;
        if (cursor.getCount() <= 0) {
            ContentValues values = new ContentValues();
            values.put(DataBaseContractSecretary.Category.COLUMN_COMPETITION, competitionUuid.toString());
            values.put(DataBaseContractSecretary.Category.COLUMN_NAME, category.getName());
            values.put(DataBaseContractSecretary.Category.COLUMN_LIMIT, category.getLimit());
            values.put(DataBaseContractSecretary.Category.COLUMN_FIGURES, category.getFigures());
            values.put(DataBaseContractSecretary.Category.COLUMN_TOURS, category.getTours());
            newRowId = db.insert(DataBaseContractSecretary.Category.TABLE_NAME, null, values);

            if (newRowId == -1) {
                Log.e(DATABASE_NAME, "Ошибка при добавлении категории");
            } else {
                Log.d(DATABASE_NAME, "Категория успешно добавлена с ID: " + newRowId);
            }
        } else {
            Log.d(DATABASE_NAME, "Категория уже существует и не была добавлена повторно");
        }
        cursor.close();
        return newRowId;
    }



    public List<Category> getCategories(SQLiteDatabase db, UUID competitionUuid) {
        List<Category> categories = new ArrayList<>();
        String[] projection = {
                DataBaseContractSecretary.Category._ID,
                DataBaseContractSecretary.Category.COLUMN_NAME,
                DataBaseContractSecretary.Category.COLUMN_LIMIT,
                DataBaseContractSecretary.Category.COLUMN_FIGURES,
                DataBaseContractSecretary.Category.COLUMN_TOURS
        };

        String selection = DataBaseContractSecretary.Category.COLUMN_COMPETITION + " = ?";
        String[] selectionArgs = { competitionUuid.toString() };

        Cursor cursor = db.query(
                DataBaseContractSecretary.Category.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            Category category = new Category();
            category.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseContractSecretary.Category._ID)));
            category.setName(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseContractSecretary.Category.COLUMN_NAME)));
            category.setLimit(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseContractSecretary.Category.COLUMN_LIMIT)));
            category.setFigures(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseContractSecretary.Category.COLUMN_FIGURES)));
            category.setTours(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseContractSecretary.Category.COLUMN_TOURS)));
            categories.add(category);
        }
        cursor.close();
        return categories;
    }

    public void setCategory(SQLiteDatabase db, Category category) {
        ContentValues values = new ContentValues();
        values.put(DataBaseContractSecretary.Category.COLUMN_COMPETITION, String.valueOf(category.getComp_id()));
        values.put(DataBaseContractSecretary.Category.COLUMN_NAME, category.getName());
        values.put(DataBaseContractSecretary.Category.COLUMN_LIMIT, category.getLimit());
        values.put(DataBaseContractSecretary.Category.COLUMN_FIGURES, category.getFigures());
        values.put(DataBaseContractSecretary.Category.COLUMN_TOURS, category.getTours());

        int count = db.update(
                DataBaseContractSecretary.Category.TABLE_NAME,
                values,
                DataBaseContractSecretary.Category._ID + " = ?",
                new String[]{String.valueOf(category.getId())}
        );

        if (count > 0) {
            Log.d(DATABASE_NAME, "Категория обновлена с ID: " + category.getId());
        } else {
            Log.e(DATABASE_NAME, "Ошибка при обновлении категории с ID: " + category.getId());
        }
    }

    /*
    *
    * категория +
    * судья
    * регион +
    * игрок
    * соревнование
    * игра
    * игра_игрок
    *
    * */
    public long addRegion(SQLiteDatabase db, Region region, UUID competitionUuid) {
        Cursor cursor = db.query(
                DataBaseContractSecretary.Region.TABLE_NAME,
                new String[]{DataBaseContractSecretary.Region._ID},
                DataBaseContractSecretary.Region.COLUMN_NAME + " = ? AND " +
                        DataBaseContractSecretary.Region.COLUMN_COMPETITION + " = ?",
                new String[]{region.getName(), competitionUuid.toString()},
                null, null, null
        );
       long newRowId = -1;
        if (cursor.getCount() <= 0) {
            ContentValues values = new ContentValues();
            values.put(DataBaseContractSecretary.Region.COLUMN_COMPETITION, competitionUuid.toString());
            values.put(DataBaseContractSecretary.Region.COLUMN_NAME, region.getName());
            db.insert(DataBaseContractSecretary.Region.TABLE_NAME, null, values);
            newRowId = db.insert(DataBaseContractSecretary.Region.TABLE_NAME, null, values);

            if (newRowId == -1) {
                Log.e(DATABASE_NAME, "Ошибка при добавлении региона");
            } else {
                Log.d(DATABASE_NAME, "Регион успешно добавлен с ID: " + newRowId);
            }
        } else {
            Log.d(DATABASE_NAME, "Регион уже существует и не был добавлен повторно");
        }
        cursor.close();
        return newRowId;
    }

    public List<Region> getRegions(SQLiteDatabase db, UUID competitionUuid) {
        List<Region> regions = new ArrayList<>();
        String[] projection = {
                DataBaseContractSecretary.Region._ID,
                DataBaseContractSecretary.Region.COLUMN_NAME
        };

        String selection = DataBaseContractSecretary.Region.COLUMN_COMPETITION + " = ?";
        String[] selectionArgs = { competitionUuid.toString() };

        Cursor cursor = db.query(
                DataBaseContractSecretary.Region.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            Region region = new Region();
            region.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseContractSecretary.Region._ID)));
            region.setName(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseContractSecretary.Region.COLUMN_NAME)));
            regions.add(region);
        }
        cursor.close();
        return regions;
    }
    public void setRegion(SQLiteDatabase db, Region region) {
        ContentValues values = new ContentValues();
        values.put(DataBaseContractSecretary.Region.COLUMN_COMPETITION, String.valueOf(region.getComp_id()));
        values.put(DataBaseContractSecretary.Region.COLUMN_NAME, region.getName());

        int count = db.update(
                DataBaseContractSecretary.Region.TABLE_NAME,
                values,
                DataBaseContractSecretary.Region._ID + " = ?",
                new String[]{String.valueOf(region.getId())}
        );

        if (count > 0) {
            Log.d(DATABASE_NAME, "Регион обновлен с ID: " + region.getId());
        } else {
            Log.e(DATABASE_NAME, "Ошибка при обновлении региона с ID: " + region.getId());
        }
    }
    public long addJudge(SQLiteDatabase db, Judge judge, UUID competitionUuid) {
        Cursor cursor = db.query(
                DataBaseContractSecretary.Judge.TABLE_NAME,
                new String[]{DataBaseContractSecretary.Judge._ID},
                DataBaseContractSecretary.Judge.COLUMN_NAME + " = ? AND " +
                        DataBaseContractSecretary.Judge.COLUMN_COMPETITION + " = ?",
                new String[]{judge.getName(), competitionUuid.toString()},
                null, null, null
        );
        long newRowId = -1;
        if (cursor.getCount() <= 0) {
            ContentValues values = new ContentValues();
            values.put(DataBaseContractSecretary.Judge.COLUMN_COMPETITION, competitionUuid.toString());
            values.put(DataBaseContractSecretary.Judge.COLUMN_CATEGORY, judge.getCategory());
            values.put(DataBaseContractSecretary.Judge.COLUMN_REGION, judge.getRegion());
            values.put(DataBaseContractSecretary.Judge.COLUMN_NAME, judge.getName());
            newRowId = db.insert(DataBaseContractSecretary.Judge.TABLE_NAME, null, values);

            if (newRowId == -1) {
                Log.e(DATABASE_NAME, "Ошибка при добавлении судьи");
            } else {
                Log.d(DATABASE_NAME, "Судья успешно добавлен с ID: " + newRowId);
            }
        } else {
            Log.d(DATABASE_NAME, "Судья уже существует и не был добавлен повторно");
        }
        cursor.close();
        return newRowId;
    }

    public List<Judge> getJudges(SQLiteDatabase db, UUID competitionUuid) {
        List<Judge> judges = new ArrayList<>();
        String[] projection = {
                DataBaseContractSecretary.Judge._ID,
                DataBaseContractSecretary.Judge.COLUMN_CATEGORY,
                DataBaseContractSecretary.Judge.COLUMN_REGION,
                DataBaseContractSecretary.Judge.COLUMN_NAME
        };

        String selection = DataBaseContractSecretary.Judge.COLUMN_COMPETITION + " = ?";
        String[] selectionArgs = { competitionUuid.toString() };

        Cursor cursor = db.query(
                DataBaseContractSecretary.Judge.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            Judge judge = new Judge();
            judge.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseContractSecretary.Judge._ID)));
            judge.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseContractSecretary.Judge.COLUMN_CATEGORY)));
            judge.setRegion(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseContractSecretary.Judge.COLUMN_REGION)));
            judge.setName(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseContractSecretary.Judge.COLUMN_NAME)));
            judges.add(judge);
        }
        cursor.close();
        return judges;
    }
    public void setJudge(SQLiteDatabase db, Judge judge) {
        ContentValues values = new ContentValues();
        values.put(DataBaseContractSecretary.Judge.COLUMN_COMPETITION, String.valueOf(judge.getComp_id()));
        values.put(DataBaseContractSecretary.Judge.COLUMN_CATEGORY, judge.getCategory());
        values.put(DataBaseContractSecretary.Judge.COLUMN_REGION, judge.getRegion());
        values.put(DataBaseContractSecretary.Judge.COLUMN_NAME, judge.getName());

        int count = db.update(
                DataBaseContractSecretary.Judge.TABLE_NAME,
                values,
                DataBaseContractSecretary.Judge._ID + " = ?",
                new String[]{String.valueOf(judge.getId())}
        );

        if (count > 0) {
            Log.d(DATABASE_NAME, "Судья обновлен с ID: " + judge.getId());
        } else {
            Log.e(DATABASE_NAME, "Ошибка при обновлении судьи с ID: " + judge.getId());
        }
    }
    public long addPlayer(SQLiteDatabase db, Player player) {
        Cursor cursor = db.query(
                DataBaseContractSecretary.Player.TABLE_NAME,
                new String[]{DataBaseContractSecretary.Player._ID},
                DataBaseContractSecretary.Player.COLUMN_NAME + " = ? AND " +
                        DataBaseContractSecretary.Player.COLUMN_COMPETITION + " = ?",
                new String[]{player.getName(), player.getComp_id().toString()},
                null, null, null
        );
        long newRowId=-1;
        if (cursor.getCount() <= 0) {
            ContentValues values = new ContentValues();
            values.put(DataBaseContractSecretary.Player.COLUMN_COMPETITION, player.getComp_id().toString());
            values.put(DataBaseContractSecretary.Player.COLUMN_NAME, player.getName());
            values.put(DataBaseContractSecretary.Player.COLUMN_REGION_ID, player.getRegionId());
            values.put(DataBaseContractSecretary.Player.COLUMN_CATEGORY_ID, player.getCategoryId());
            values.put(DataBaseContractSecretary.Player.COLUMN_GRADE, player.getGrade());
            newRowId = db.insert(DataBaseContractSecretary.Player.TABLE_NAME, null, values);

            if (newRowId == -1) {
                Log.e(DATABASE_NAME, "Ошибка при добавлении игрока");
            } else {
                Log.d(DATABASE_NAME, "Игрок успешно добавлен с ID: " + newRowId);
            }
        } else {
            Log.d(DATABASE_NAME, "Игрок уже существует и не был добавлен повторно");
        }
        cursor.close();
        return newRowId;
    }

    public List<Player> getPlayers(SQLiteDatabase db, UUID competitionUuid) {
        List<Player> players = new ArrayList<>();

        Cursor cursor = db.query(
                DataBaseContractSecretary.Player.TABLE_NAME,
                new String[]{
                        DataBaseContractSecretary.Player._ID,
                        DataBaseContractSecretary.Player.COLUMN_NAME,
                        DataBaseContractSecretary.Player.COLUMN_REGION_ID,
                        DataBaseContractSecretary.Player.COLUMN_CATEGORY_ID,
                        DataBaseContractSecretary.Player.COLUMN_GRADE
                },
                DataBaseContractSecretary.Player.COLUMN_COMPETITION  + " = ?",
                new String[]{ competitionUuid.toString() },
                null, null, null, null
        );

        while (cursor.moveToNext()) {
            Player player = new Player();
            player.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseContractSecretary.Player._ID)));
            player.setName(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseContractSecretary.Player.COLUMN_NAME)));
            player.setRegionId(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseContractSecretary.Player.COLUMN_REGION_ID)));
            player.setCategoryId(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseContractSecretary.Player.COLUMN_CATEGORY_ID)));
            player.setGrade(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseContractSecretary.Player.COLUMN_GRADE)));

            players.add(player);
        }
        cursor.close();
        return players;
    }

    public void setPlayer(SQLiteDatabase db, Player player) {
        ContentValues values = new ContentValues();
        values.put(DataBaseContractSecretary.Player.COLUMN_NAME, player.getName());
        values.put(DataBaseContractSecretary.Player.COLUMN_REGION_ID, player.getRegionId());
        values.put(DataBaseContractSecretary.Player.COLUMN_CATEGORY_ID, player.getCategoryId());
        values.put(DataBaseContractSecretary.Player.COLUMN_GRADE, player.getGrade());


        int count = db.update(
                DataBaseContractSecretary.Player.TABLE_NAME,
                values,
                DataBaseContractSecretary.Player._ID + " = ?",
                new String[]{String.valueOf(player.getId())}
        );

        if (count > 0) {
            Log.d(DATABASE_NAME, "Игрок обновлен с ID: " + player.getId());
        } else {
            Log.e(DATABASE_NAME, "Ошибка при обновлении игрока с ID: " + player.getId());
        }
    }
    public List<PerformanceSecretary> getPerformances(SQLiteDatabase db, UUID competitionUuid) {
        List<PerformanceSecretary> performanceSecretaries = new ArrayList<>();
        Cursor cursor = db.query(
                DataBaseContractSecretary.Performance.TABLE_NAME,
                new String[]{
                        DataBaseContractSecretary.Performance.COLUMN_INTERNAL_ID,
                        DataBaseContractSecretary.Performance.COLUMN_TIME,
                        DataBaseContractSecretary.Performance.COLUMN_PLACE,
                        DataBaseContractSecretary.Performance.COLUMN_PLAYGROUND,
                        DataBaseContractSecretary.Performance.COLUMN_DATE,
                        DataBaseContractSecretary.Performance.COLUMN_JUDGE_ID
                },
                DataBaseContractSecretary.Performance.COLUMN_COMPETITION + " = ?",
                new String[]{ competitionUuid.toString() },
                null, null, null
        );
        while (cursor.moveToNext()) {
            PerformanceSecretary performance = new PerformanceSecretary();
            performance.setInternal_id(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseContractSecretary.Performance.COLUMN_INTERNAL_ID)));
            performance.setTime(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseContractSecretary.Performance.COLUMN_TIME)));
            performance.setPlace(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseContractSecretary.Performance.COLUMN_PLACE)));
            performance.setPlayground(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseContractSecretary.Performance.COLUMN_PLAYGROUND)));
            performance.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseContractSecretary.Performance.COLUMN_DATE)));
            performance.setJudgeId(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseContractSecretary.Performance.COLUMN_JUDGE_ID)));
            performance.setPlayers(getPerformancePlayers(db,performance.getId()));
            performanceSecretaries.add(performance);
        }
        cursor.close();
        return performanceSecretaries;
    }

    public void addPerformance(SQLiteDatabase db, PerformanceSecretary performance, List<Player> players) {
        // Добавление информации о выступлении в базу данных
        ContentValues values = new ContentValues();
        values.put(DataBaseContractSecretary.Performance.COLUMN_COMPETITION, performance.getComp_id().toString());
        values.put(DataBaseContractSecretary.Performance.COLUMN_TIME, performance.getTime());
        values.put(DataBaseContractSecretary.Performance.COLUMN_PLACE, performance.getPlace());
        values.put(DataBaseContractSecretary.Performance.COLUMN_PLAYGROUND, performance.getPlayground());
        values.put(DataBaseContractSecretary.Performance.COLUMN_DATE, performance.getDate());
        values.put(DataBaseContractSecretary.Performance.COLUMN_JUDGE_ID, performance.getJudgeId());

        long performanceId = db.insert(DataBaseContractSecretary.Performance.TABLE_NAME, null, values);

        // Добавление игроков в выступление
        for (Player player : players) {
            ContentValues playerPerformanceValues = new ContentValues();
            playerPerformanceValues.put(DataBaseContractSecretary.PerformancePlayer.COLUMN_COMPETITION, performance.getComp_id().toString());
            playerPerformanceValues.put(DataBaseContractSecretary.PerformancePlayer.COLUMN_PERFORMANCE_ID, performanceId);
            playerPerformanceValues.put(DataBaseContractSecretary.PerformancePlayer.COLUMN_PLAYER_ID, player.getId());
            db.insert(DataBaseContractSecretary.PerformancePlayer.TABLE_NAME, null, playerPerformanceValues);
        }
        if (performanceId == -1) {
            Log.e(DATABASE_NAME, "Ошибка при добавлении игры");
        } else {
            Log.d(DATABASE_NAME, "Игра успешно добавлена с ID: " + performanceId);
        }
    }
    private String getCategoryNameById(SQLiteDatabase db, int categoryId) {
        Cursor cursor = db.query(
                DataBaseContractSecretary.Category.TABLE_NAME,
                new String[]{DataBaseContractSecretary.Category.COLUMN_NAME},
                DataBaseContractSecretary.Category._ID + " = ?",
                new String[]{String.valueOf(categoryId)},
                null,
                null,
                null
        );
        String categoryName = "";
        if (cursor.moveToFirst()) {
            categoryName = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseContractSecretary.Category.COLUMN_NAME));
        }
        cursor.close();
        return categoryName;
    }



//////////////////////////////////////////////////


    public void setPerformance(SQLiteDatabase db, PerformanceSecretary performance, List<Player> players) {
        ContentValues values = new ContentValues();
        values.put(DataBaseContractSecretary.Performance.COLUMN_TIME, performance.getTime());
        values.put(DataBaseContractSecretary.Performance.COLUMN_PLACE, performance.getPlace());
        values.put(DataBaseContractSecretary.Performance.COLUMN_PLAYGROUND, performance.getPlayground());
        values.put(DataBaseContractSecretary.Performance.COLUMN_DATE, performance.getDate());
        values.put(DataBaseContractSecretary.Performance.COLUMN_JUDGE_ID, performance.getJudgeId());

        db.update(DataBaseContractSecretary.Performance.TABLE_NAME, values,
                DataBaseContractSecretary.Performance.COLUMN_INTERNAL_ID + " = ?",
                new String[]{String.valueOf(performance.getInternal_id())}
        );

        // Обновляем участников выступления
        db.delete(DataBaseContractSecretary.PerformancePlayer.TABLE_NAME,
                DataBaseContractSecretary.PerformancePlayer.COLUMN_PERFORMANCE_ID + " = ?",
                new String[]{String.valueOf(performance.getInternal_id())});

        for (Player player : players) {
            ContentValues playerPerformanceValues = new ContentValues();
            playerPerformanceValues.put(DataBaseContractSecretary.PerformancePlayer.COLUMN_PERFORMANCE_ID, performance.getInternal_id());
            playerPerformanceValues.put(DataBaseContractSecretary.PerformancePlayer.COLUMN_PLAYER_ID, player.getId());
            db.insert(DataBaseContractSecretary.PerformancePlayer.TABLE_NAME, null, playerPerformanceValues);
        }
    }

    public List<Player> getPerformancePlayers(SQLiteDatabase db, int performanceId) {
        List<Player> players = new ArrayList<>();
        String query = "SELECT p.* FROM Player p INNER JOIN PerformancePlayer pp ON p.id = pp.player_id WHERE pp.performance_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(performanceId)});

        while (cursor.moveToNext()) {
            Player player = new Player();
            players.add(player);
        }
        cursor.close();
        return players;
    }

    public void setPerformancePlayer(SQLiteDatabase db, int performanceId, int playerId, int newPerformanceId) {
        ContentValues values = new ContentValues();
        values.put(DataBaseContractSecretary.PerformancePlayer.COLUMN_PERFORMANCE_ID, newPerformanceId);
        db.update(DataBaseContractSecretary.PerformancePlayer.TABLE_NAME, values,
                DataBaseContractSecretary.PerformancePlayer.COLUMN_PERFORMANCE_ID + " = ? AND " +
                        DataBaseContractSecretary.PerformancePlayer.COLUMN_PLAYER_ID + " = ?",
                new String[]{String.valueOf(performanceId), String.valueOf(playerId)});
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
        values.put(DataBaseContractSecretary.Protocol.COLUMN_TOURS,protocol.getTours());

        db.insert(DataBaseContractSecretary.Protocol.TABLE_NAME,null,values);
    }
}
