package ru.nishty.aai_referee.entity.secretary;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Player implements Serializable {
    @SerializedName("i")
    private String comp_id;
    @SerializedName("iid")
    private int id;
    @SerializedName("n")
    private String name;
    @SerializedName("r")
    private int regionId;
    @SerializedName("c")
    private int categoryId;
    @SerializedName("k")
    private int categoryConfig;

    @SerializedName("g")
    private int grade;

    public Player() {
    }

    public Player(String comp_id, int id, String name, int regionId, int categoryId,int categoryConfig, int grade) {
        this.comp_id = comp_id;
        this.id = id;
        this.name = name;
        this.regionId = regionId;
        this.categoryConfig = categoryConfig;
        this.categoryId = categoryId;
        this.grade = grade;
    }

    public String getComp_id() {
        return comp_id;
    }

    public void setComp_id(String comp_id) {
        this.comp_id = comp_id;
    }
    public int getCategoryConfig() {
        return categoryConfig;
    }

    public void setCategoryConfig(int categoryConfig) {
        this.categoryConfig = categoryConfig;
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

    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "Player{" +
                "comp_id=" + comp_id +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", regionId=" + regionId +
                ", categoryId=" + categoryId +
                ", grade=" + grade +
                '}';
    }
}
