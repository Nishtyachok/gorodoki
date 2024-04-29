package ru.nishty.aai_referee.entity.referee;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Protocol implements Serializable {

    @SerializedName("i")
    private String comp_id;
    @SerializedName("id")
    private int perf_id;
    @SerializedName("pid")
    private int id;
    @SerializedName("pl")
    private List<PlayerRef> players;
    private String limit;

    public Protocol() {
    }

    public Protocol(String comp_id) {
        this.comp_id = comp_id;
    }

    public String getComp_id() {
        return comp_id;
    }

    public void setComp_id(String comp_id) {
        this.comp_id = comp_id;
    }

    public List<PlayerRef> getPlayers() {
        return players;
    }
    public void setPlayers(List<PlayerRef> playerRefs) {
        players = playerRefs;
    }
    public void updatePlayer(PlayerRef player) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getId() == player.getId()) {
                players.set(i, player);
                break;
            }
        }
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

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }


}

