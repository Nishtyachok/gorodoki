package ru.nishty.aai_referee.entity.secretary;


import java.util.UUID;

public class Protocol  {


    private UUID comp_id;
    private int perf_id;
    private int id;
    private int game1;
    private int game2;
    private int games_sum;
    private String shots1;
    private String shots2;
    private int player_id;
    private int limit;
    private int tours;

    public Protocol() {
    }

    public Protocol(UUID comp_id, int perf_id, int id, int game1, int game2, int games_sum, String shots1, String shots2, int player_id, int limit, int tours) {
        this.comp_id = comp_id;
        this.perf_id = perf_id;
        this.id = id;
        this.game1 = game1;
        this.game2 = game2;
        this.games_sum = games_sum;
        this.shots1 = shots1;
        this.shots2 = shots2;
        this.player_id = player_id;
        this.limit = limit;
        this.tours = tours;
    }

    public UUID getComp_id() {
        return comp_id;
    }

    public void setComp_id(UUID comp_id) {
        this.comp_id = comp_id;
    }

    public int getPerf_id() {
        return perf_id;
    }

    public void setPerf_id(int perf_id) {
        this.perf_id = perf_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGame1() {
        return game1;
    }

    public void setGame1(int game1) {
        this.game1 = game1;
    }

    public int getGame2() {
        return game2;
    }

    public void setGame2(int game2) {
        this.game2 = game2;
    }

    public int getPlayer_id() {
        return player_id;
    }

    public void setPlayer_id(int player_id) {
        this.player_id = player_id;
    }

    public int getGames_sum() {
        return games_sum;
    }

    public void setGames_sum(int games_sum) {
        this.games_sum = games_sum;
    }

    public String getShots1() {
        return shots1;
    }

    public void setShots1(String shots1) {
        this.shots1 = shots1;
    }

    public String getShots2() {
        return shots2;
    }

    public void setShots2(String shots2) {
        this.shots2 = shots2;
    }

    public int getTours() {
        return tours;
    }

    public void setTours(int tours) {
        this.tours = tours;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}

