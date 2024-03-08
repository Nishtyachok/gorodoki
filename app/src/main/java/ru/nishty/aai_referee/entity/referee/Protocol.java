package ru.nishty.aai_referee.entity.referee;


import java.io.Serializable;
import java.util.UUID;

public class Protocol implements Serializable {

    private UUID comp_id;
    private int perf_id;
    private int id;
    private String name;
    private String shots1;
    private String shots2;
    private String limit;
    private String game1;
    private String game2;
    private String games_sum;

    public Protocol() {
    }

    public Protocol(UUID comp_id) {
        this.comp_id = comp_id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getGame1() {
        return game1;
    }

    public void setGame1(String game1) {
        this.game1 = game1;
    }

    public String getGame2() {
        return game2;
    }

    public void setGame2(String game2) {
        this.game2 = game2;
    }

    public String getGames_sum() {
        return games_sum;
    }

    public void setGames_sum(String games_sum) {
        this.games_sum = games_sum;
    }


}

