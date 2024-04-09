package ru.nishty.aai_referee.entity.referee;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class PlayerRef {
    private UUID comp_id;
    private int perf_id;
    @SerializedName("c")
    private String category;
    @SerializedName("g")
    private int grade;
    @SerializedName("id")
    private int id;
    @SerializedName("n")
    private String name;
    @SerializedName("r")
    private String region;
    public PlayerRef() {
    }
    public PlayerRef(UUID comp_id,int perf_id, String name, int id, int grade, String region, String category) {
        this.comp_id = comp_id;
        this.perf_id = perf_id;
        this.name = name;
        this.id = id;
        this.grade = grade;
        this.region = region;
        this.category = category;
    }

    public UUID getComp_id() {
        return comp_id;
    }

    public void setComp_id(UUID comp_id) {
        this.comp_id = comp_id;
    }

    public int getPerf_id() {
        return perf_id;
    }

    public void setPerf_id(int perf_id) {
        this.perf_id = perf_id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
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

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
