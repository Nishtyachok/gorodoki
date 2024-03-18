package ru.nishty.aai_referee.entity.secretary;

import java.util.UUID;

public class PerformancePlayer {
    private UUID comp_id;
    private int id;
    private int playerId;
    private int performanceId;
    public PerformancePlayer() {
    }
    public PerformancePlayer(UUID comp_id, int id,int playerId,int performanceId) {
        this.comp_id = comp_id;
        this.id = id;
        this.playerId = playerId;
        this.performanceId = performanceId;
    }

    public UUID getComp_id() {
        return comp_id;
    }

    public void setComp_id(UUID comp_id) {
        this.comp_id = comp_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getPerformanceId() {
        return performanceId;
    }

    public void setPerformanceId(int performanceId) {
        this.performanceId = performanceId;
    }
}
