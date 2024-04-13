package ru.nishty.aai_referee.entity.referee;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Performance implements Serializable {
    @SerializedName("i")
    private String comp_id;
    @SerializedName("id")
    private int id;
    @SerializedName("pl")
    private List<PlayerRef> players = new ArrayList<>();
    @SerializedName("p")
    private String place;
    @SerializedName("d")
    private String date;
    @SerializedName("t")
    private String time;
    @SerializedName("pg")
    private String playground;

    public int getId() {
        return id;
    }

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

    public List<PlayerRef> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerRef> playerRefs) {
        players = playerRefs;
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

    public String getPlayground() {
        return playground;
    }

    public void setPlayground(String playground) {
        this.playground = playground;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
