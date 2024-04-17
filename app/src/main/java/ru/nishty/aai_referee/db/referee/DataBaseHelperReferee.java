package ru.nishty.aai_referee.db.referee;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

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

    public void addCompetition(SQLiteDatabase db, Competition competition) {
        ContentValues values = new ContentValues();
        values.put(DataBaseContractReferee.Competition._ID, competition.getUuid());
        values.put(DataBaseContractReferee.Competition.COLUMN_NAME, competition.getName());
        values.put(DataBaseContractReferee.Competition.COLUMN_YEAR, competition.getYear());
        values.put(DataBaseContractReferee.Competition.COLUMN_PLACE, competition.getPlace());
        db.insert(DataBaseContractReferee.Competition.TABLE_NAME, null, values);
        /*for (Performance performance :
                competition.getPerformances()) {
            values = new ContentValues();
            values.put(DataBaseContractReferee.Performance.COLUMN_INTERNAL_ID, performance.getInternal_id());
            values.put(DataBaseContractReferee.Performance.COLUMN_TIME, performance.getTime());
            values.put(DataBaseContractReferee.Performance.COLUMN_PLACE, performance.getPlace());
            values.put(DataBaseContractReferee.Performance.COLUMN_PLAYGROUND, performance.getPlayground());
            values.put(DataBaseContractReferee.Performance.COLUMN_DATE, performance.getDate());
            values.put(DataBaseContractReferee.Performance.COLUMN_COMPETITION, competition.getUuid());
            db.insert(DataBaseContractReferee.Performance.TABLE_NAME, null, values);
            for (PlayerRef playerRef :
                    performance.getPlayers()) {
                values = new ContentValues();
                values.put(DataBaseContractReferee.PlayerRef.COLUMN_CATEGORY, playerRef.getCategory());
                values.put(DataBaseContractReferee.PlayerRef.COLUMN_GRADE, playerRef.getGrade());
                values.put(DataBaseContractReferee.PlayerRef.COLUMN_NAME, playerRef.getName());
                values.put(DataBaseContractReferee.PlayerRef.COLUMN_REGION, playerRef.getRegion());
                values.put(DataBaseContractReferee.PlayerRef.COLUMN_PERFORMANCE, playerRef.getPerf_id());
                values.put(DataBaseContractReferee.Performance.COLUMN_COMPETITION, competition.getUuid());
                db.insert(DataBaseContractReferee.PlayerRef.TABLE_NAME, null, values);
            }
        }*/
    }

    public Competition getCompetitionByUuid(SQLiteDatabase db, String uuid) {
        Competition competition = new Competition();
        Cursor cursor = db.query(
                DataBaseContractReferee.Competition.TABLE_NAME,
                new String[]{DataBaseContractReferee.Competition.COLUMN_NAME, DataBaseContractReferee.Competition.COLUMN_YEAR, DataBaseContractReferee.Competition.COLUMN_PLACE}, // Только требуемые столбцы
                BaseColumns._ID + "=?", // Поиск по столбцу ID
                new String[]{uuid}, // Значение ID
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseContractReferee.Competition.COLUMN_NAME));
            String year = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseContractReferee.Competition.COLUMN_YEAR));
            String place = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseContractReferee.Competition.COLUMN_PLACE));
            competition = new Competition();
            competition.setUuid(uuid);
            competition.setName(name);
            competition.setYear(year);
            competition.setPlace(place);

            competition.setPerformances(loadPerformances(db, uuid));
        }
        if (cursor != null) {
            cursor.close();
        }
        return competition;
    }

    private List<Performance> loadPerformances(SQLiteDatabase db, String competitionUuid) {
        List<Performance> performances = new ArrayList<>();
        Cursor cursor = db.query(
                DataBaseContractReferee.Performance.TABLE_NAME,
                new String[]{DataBaseContractReferee.Performance._ID, DataBaseContractReferee.Performance.COLUMN_TIME, DataBaseContractReferee.Performance.COLUMN_PLACE}, // Пример столбцов для выступления
                DataBaseContractReferee.Performance.COLUMN_COMPETITION + " = ?",
                new String[]{competitionUuid},
                null, null, null);

        while (cursor != null && cursor.moveToNext()) {
            Performance performance = new Performance();
            performance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseContractReferee.Performance._ID)));
            performance.setTime(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseContractReferee.Performance.COLUMN_TIME)));
            performance.setPlace(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseContractReferee.Performance.COLUMN_PLACE)));
            performances.add(performance);
        }
        if (cursor != null) {
            cursor.close();
        }
        return performances;
    }

    public void addPerformance(SQLiteDatabase db, String uuid, Performance performance) {
        ContentValues values = new ContentValues();
        values.put(DataBaseContractReferee.Performance.COLUMN_COMPETITION, uuid);
        values.put(DataBaseContractReferee.Performance.COLUMN_TIME, performance.getTime());
        values.put(DataBaseContractReferee.Performance.COLUMN_PLACE, performance.getPlace());
        values.put(DataBaseContractReferee.Performance.COLUMN_PLAYGROUND, performance.getPlayground());
        values.put(DataBaseContractReferee.Performance.COLUMN_DATE, performance.getDate());

        long performanceId = db.insert(DataBaseContractReferee.Performance.TABLE_NAME, null, values);
        if (performanceId == -1) {
            Log.e(DATABASE_NAME, "Ошибка при добавлении выступления");
        } else {
            Log.d(DATABASE_NAME, "Выступление успешно добавлено с ID: " + performanceId);
            for (PlayerRef player : performance.getPlayers()) {
                ContentValues playerValues = new ContentValues();
                playerValues.put(DataBaseContractReferee.PlayerRef.COLUMN_COMPETITION, uuid);
                playerValues.put(DataBaseContractReferee.PlayerRef.COLUMN_PERFORMANCE, performanceId);
                playerValues.put(DataBaseContractReferee.PlayerRef.COLUMN_NAME, player.getName());
                playerValues.put(DataBaseContractReferee.PlayerRef.COLUMN_GRADE, player.getGrade());
                playerValues.put(DataBaseContractReferee.PlayerRef.COLUMN_CATEGORY, player.getCategory());
                playerValues.put(DataBaseContractReferee.PlayerRef.COLUMN_REGION, player.getRegion());
                long playerId = db.insert(DataBaseContractReferee.PlayerRef.TABLE_NAME, null, playerValues);
                if (playerId == -1) {
                    Log.e(DATABASE_NAME, "Ошибка при добавлении игрока");
                } else {
                    Log.d(DATABASE_NAME, "Игрок успешно добавлено с ID: " + playerId);
                }
            }
        }
    }

    public List<Performance> getPerformances(SQLiteDatabase db, String uuid) {
        List<Performance> performances = new ArrayList<>();

        Cursor cursor = db.query(
                DataBaseContractReferee.Performance.TABLE_NAME,
                new String[]{
                        DataBaseContractReferee.Performance.COLUMN_TIME,
                        DataBaseContractReferee.Performance.COLUMN_PLACE,
                        DataBaseContractReferee.Performance.COLUMN_PLAYGROUND,
                        DataBaseContractReferee.Performance.COLUMN_DATE,
                        DataBaseContractReferee.Performance._ID

                },
                DataBaseContractReferee.Performance.COLUMN_COMPETITION + " = ?",
                new String[]{
                        uuid
                },
                null,
                null,
                null,
                null
        );
        Performance performance;
        while (cursor.moveToNext()) {
            performance = new Performance();
            performance.setTime(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseContractReferee.Performance.COLUMN_TIME)));
            performance.setPlace(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseContractReferee.Performance.COLUMN_PLACE)));
            performance.setPlayground(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseContractReferee.Performance.COLUMN_PLAYGROUND)));
            performance.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseContractReferee.Performance.COLUMN_DATE)));
            performance.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseContractReferee.Performance._ID)));

            List<PlayerRef> players = getPlayerRefsForPerformance(db, uuid, performance.getId());
            performance.setPlayers(players);

            performances.add(performance);
        }

        cursor.close();

        return performances;
    }

    // Метод для получения списка игроков для определенного выступления
    public List<PlayerRef> getPlayerRefsForPerformance(SQLiteDatabase db, String compId, int performanceId) {
        List<PlayerRef> players = new ArrayList<>();

        Cursor cursor = db.query(
                DataBaseContractReferee.PlayerRef.TABLE_NAME,
                new String[]{
                        DataBaseContractReferee.PlayerRef._ID,
                        DataBaseContractReferee.PlayerRef.COLUMN_NAME,
                        DataBaseContractReferee.PlayerRef.COLUMN_CATEGORY,
                        DataBaseContractReferee.PlayerRef.COLUMN_GRADE,
                        DataBaseContractReferee.PlayerRef.COLUMN_REGION,
                        DataBaseContractReferee.PlayerRef.COLUMN_PERFORMANCE,
                        DataBaseContractReferee.PlayerRef.COLUMN_COMPETITION
                },
                DataBaseContractReferee.PlayerRef.COLUMN_COMPETITION + " = ? AND " +
                        DataBaseContractReferee.PlayerRef.COLUMN_PERFORMANCE + " = ?",
                new String[]{
                        compId,
                        String.valueOf(performanceId)
                },
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            PlayerRef player = new PlayerRef();
            player.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseContractReferee.PlayerRef._ID)));
            player.setName(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseContractReferee.PlayerRef.COLUMN_NAME)));
            player.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseContractReferee.PlayerRef.COLUMN_CATEGORY)));
            player.setGrade(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseContractReferee.PlayerRef.COLUMN_GRADE)));
            player.setRegion(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseContractReferee.PlayerRef.COLUMN_REGION)));
            player.setComp_id(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseContractReferee.PlayerRef.COLUMN_COMPETITION)));
            player.setPerf_id(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseContractReferee.PlayerRef.COLUMN_PERFORMANCE)));

            players.add(player);
        }

        cursor.close();

        return players;
    }


    /*public PlayerRef getPlayerById(SQLiteDatabase db, String comp_id, int perf_id, int player_id) {
        PlayerRef playerRef = new PlayerRef();
        Cursor cursor = db.query(
                DataBaseContractReferee.PlayerRef.TABLE_NAME,
                null,
                DataBaseContractReferee.PlayerRef.COLUMN_COMPETITION + " = ? AND "
                        + DataBaseContractReferee.PlayerRef.COLUMN_PERFORMANCE + " = ? AND "
                        + DataBaseContractReferee.PlayerRef._ID + " = ?",
                new String[]{
                        comp_id,
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
                cursor.getColumnIndexOrThrow(DataBaseContractReferee.PlayerRef.COLUMN_CATEGORY)
        ));
        playerRef.setGrade(cursor.getInt(
                cursor.getColumnIndexOrThrow(DataBaseContractReferee.PlayerRef.COLUMN_GRADE)
        ));
        playerRef.setName(cursor.getString(cursor.getColumnIndexOrThrow(
                DataBaseContractReferee.PlayerRef.COLUMN_NAME)
        ));
        playerRef.setRegion(cursor.getString(
                cursor.getColumnIndexOrThrow(DataBaseContractReferee.PlayerRef.COLUMN_REGION)
        ));

        return playerRef;
    }*/

    public List<Competition> getCompetitions(SQLiteDatabase db) {
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
        while (cursor.moveToNext()) {
            competition = new Competition();
            competition.setUuid(cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            DataBaseContractReferee.Competition._ID)));
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
            competitions.add(0, competition);
        }
        return competitions;
    }

    public Protocol getProtocol(SQLiteDatabase db, String comp_id, int perf_id) {
        Protocol protocol = new Protocol();
        Cursor cursor = db.query(
                DataBaseContractReferee.Protocol.TABLE_NAME,
                null,
                DataBaseContractReferee.Protocol.COLUMN_COMPETITION + " = ? AND "
                        + DataBaseContractReferee.Protocol.COLUMN_PERFORMANCE + " = ?",
                new String[]{
                        comp_id,
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

        protocol.setComp_id(cursor.getString(
                cursor.getColumnIndexOrThrow(
                        DataBaseContractReferee.Protocol.COLUMN_COMPETITION
                )
        ));
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
        values.put(DataBaseContractReferee.Protocol.COLUMN_GAME1, protocol.getGame1());
        values.put(DataBaseContractReferee.Protocol.COLUMN_GAME2, protocol.getGame2());
        values.put(DataBaseContractReferee.Protocol.COLUMN_GAMES_SUM, protocol.getGames_sum());
        values.put(DataBaseContractReferee.Protocol.COLUMN_LIMIT, protocol.getLimit());
        values.put(DataBaseContractReferee.Protocol.COLUMN_SHOTS1, protocol.getShots1());
        values.put(DataBaseContractReferee.Protocol.COLUMN_SHOTS2, protocol.getShots2());
        db.insert(DataBaseContractReferee.Protocol.TABLE_NAME, null, values);
    }
}
