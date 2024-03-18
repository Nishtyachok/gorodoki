package ru.nishty.aai_referee.entity.secretary;

import java.util.UUID;

public class Region {
    private UUID comp_id;
    private int id;
    private String name;

    public Region() {
    }
    public Region(UUID comp_id, int id, String name) {
        this.comp_id = comp_id;
        this.id = id;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
