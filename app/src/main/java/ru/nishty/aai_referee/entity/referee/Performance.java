package ru.nishty.aai_referee.entity.referee;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Performance {
    private UUID comp_id;
    @SerializedName("i")
    private int id;
    private int internal_id;
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

    public List<PlayerRef> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerRef> playerRefs) {
        this.players = playerRefs;
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
