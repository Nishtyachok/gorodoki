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
    private String player_id;
    private String shots2;
    private int limit;

    private int tours;

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

    public String getPlayer_id() {
        return player_id;
    }

    public void setPlayer_id(String player_id) {
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

