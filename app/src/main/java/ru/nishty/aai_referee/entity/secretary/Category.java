package ru.nishty.aai_referee.entity.secretary;

public class Category {
    private String comp_id;
    private int id;
    private String name;
    private String agelimit;
    private int tours;
    private int config;
    private int limit;

    public Category() {
    }
    public Category(String comp_id, int id, String name,String agelimit, int tours, int config, int limit) {
        this.comp_id = comp_id;
        this.id = id;
        this.name = name;
        this.agelimit = agelimit;
        this.tours = tours;
        this.config = config;
        this.limit = limit;
    }

    public String getComp_id() {
        return comp_id;
    }

    public void setComp_id(String comp_id) {
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

    public String getAgelimit() {
        return agelimit;
    }

    public void setAgelimit(String agelimit) {
        this.agelimit = agelimit;
    }

    public int getTours() {
        return tours;
    }

    public void setTours(int tours) {
        this.tours = tours;
    }

    public int getConfig() {
        return config;
    }

    public void setConfig(int config) {
        this.config = config;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
