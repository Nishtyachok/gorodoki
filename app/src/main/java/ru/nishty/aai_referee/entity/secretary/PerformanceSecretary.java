package ru.nishty.aai_referee.entity.secretary;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class PerformanceSecretary implements Serializable {
    private UUID comp_id;
    private int id;
    private int internal_id;
    private String place;
    private String date;
    private String playground;
    private List<Player> players;
    private transient int judgeId;
    private String time;

    public PerformanceSecretary() {
    }
    public PerformanceSecretary(UUID comp_id, int id, int internal_id,  String place, String date, String playground,List<Player> players, int judgeId, String time) {
        this.comp_id = comp_id;
        this.id = id;
        this.internal_id = internal_id;
        this.place = place;
        this.date = date;
        this.playground = playground;
        this.players = players;
        this.judgeId = judgeId;
        this.time = time;
    }

    public int getId() {return id;}

    public void setId(int id) {
        this.id = id;
    }

    public UUID getComp_id() {
        return comp_id;
    }

    public void setComp_id(UUID comp_id) {
        this.comp_id = comp_id;
    }

    public int getInternal_id() {
        return internal_id;
    }
    public void setInternal_id(int internal_id) {
        this.internal_id = internal_id;
    }
    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getJudgeId() {
        return judgeId;
    }

    public void setJudgeId(int judgeId) {
        this.judgeId = judgeId;
    }

    public String getPlayground() {
        return playground;
    }

    public void setPlayground(String playground) {
        this.playground = playground;
    }

    public List<Player> getPlayers() {
        return  players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

}
