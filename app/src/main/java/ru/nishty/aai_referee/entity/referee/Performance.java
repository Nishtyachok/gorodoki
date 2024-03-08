package ru.nishty.aai_referee.entity.referee;

import java.util.UUID;

public class Performance {
    private UUID comp_id;
    private int id;
    private int internal_id;
    private String name;
    private String grade;
    private String region;
    private String place;
    private String date;
    private String playground;
    private String category;
    private String time;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getRegion() {return region;}

    public void setRegion(String region) {
        this.region = region;
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

    public String getPlayground() {
        return playground;
    }

    public void setPlayground(String playground) {
        this.playground = playground;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {this.category = category;}

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
