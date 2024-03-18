package ru.nishty.aai_referee.entity.secretary;

import java.util.UUID;

public class Player {
    private UUID comp_id;
    private int id;
    private String name;
    private int regionId;
    private int categoryId;
    private int grade;

    public Player() {
    }
    public Player(UUID comp_id, int id, String name, int regionId, int categoryId, int grade) {
        this.comp_id = comp_id;
        this.id = id;
        this.name = name;
        this.regionId = regionId;
        this.categoryId = categoryId;
        this.grade = grade;
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
