package ru.nishty.aai_referee.entity.referee;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Competition {
    @SerializedName("c")
    private UUID uuid;
    @SerializedName("n")
    private String name;
    @SerializedName("y")
    private String year;
    @SerializedName("p1")
    private String place;
    @SerializedName("p")
    private List<Performance> performances = new ArrayList<>();

    public Competition(UUID uuid, String name, String year,String place, int discipline, List<Performance> performances) {
        this.uuid = uuid;
        this.name = name;
        this.year = year;
        this.place = place;
        this.performances = performances;
    }

    public Competition() {
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }


    public List<Performance> getPerformances() {
        return performances;
    }

    public void setPerformances(List<Performance> performances) {
        this.performances = performances;
    }

}
