package ru.nishty.aai_referee.entity.secretary;

import java.util.UUID;

public class Category {
    private UUID comp_id;
    private int id;
    private String name;
    private int tours;
    private int figures;
    private int limit;

    public Category() {
    }
    public Category(UUID comp_id, int id, String name, int tours, int figures, int limit) {
        this.comp_id = comp_id;
        this.id = id;
        this.name = name;
        this.tours = tours;
        this.figures = figures;
        this.limit = limit;
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

    public int getTours() {
        return tours;
    }

    public void setTours(int tours) {
        this.tours = tours;
    }

    public int getFigures() {
        return figures;
    }

    public void setFigures(int figures) {
        this.figures = figures;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
