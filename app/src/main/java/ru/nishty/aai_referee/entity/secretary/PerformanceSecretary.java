package ru.nishty.aai_referee.entity.secretary;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PerformanceSecretary implements Serializable {
    @SerializedName("i")
    private String comp_id;
    @SerializedName("id")
    private int id;
    @SerializedName("p")
    private String place;
    @SerializedName("d")
    private String date;
    @SerializedName("pg")
    private String playground;
    @SerializedName("pl")
    private List<Player> players;
    private transient int judgeId;
    @SerializedName("t")
    private String time;

    public PerformanceSecretary() {
    }
    public PerformanceSecretary(String comp_id, int id,  String place, String date, String playground,List<Player> players, int judgeId, String time) {
        this.comp_id = comp_id;
        this.id = id;
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

    public String getComp_id() {
        return comp_id;
    }

    public void setComp_id(String comp_id) {
        this.comp_id = comp_id;
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
