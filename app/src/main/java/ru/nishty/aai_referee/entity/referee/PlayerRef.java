package ru.nishty.aai_referee.entity.referee;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PlayerRef implements Serializable {

    @SerializedName("i")
    private String comp_id;
    @SerializedName("id")
    private Integer perf_id;
    private int g1;
    private int g2;
    private String s1;
    private String s2;
    @SerializedName("c")
    private String category;
    @SerializedName("g")
    private Integer grade;
    @SerializedName("nid")
    private Integer id;
    @SerializedName("iid")
    private int iid;

    @SerializedName("n")
    private String name;
    @SerializedName("r")
    private String region;
    public PlayerRef() {
    }
    public PlayerRef(String comp_id,Integer perf_id, String name, Integer id,Integer iid, int grade, String region, String category) {
        this.comp_id = comp_id;
        this.perf_id = perf_id;
        this.name = name;
        this.id = id;
        this.iid = iid;
        this.grade = grade;
        this.region = region;
        this.category = category;
    }

    public String getComp_id() {
        return comp_id;
    }

    public void setComp_id(String comp_id) {
        this.comp_id = comp_id;
    }

    public int getIid() {
        return iid;
    }

    public void setIid(int iid) {
        this.iid = iid;
    }

    public Integer getPerf_id() {
        return perf_id;
    }

    public void setPerf_id(Integer perf_id) {
        this.perf_id = perf_id;
    }

    public int getG1() {
        return g1;
    }

    public void setG1(int g1) {
        this.g1 = g1;
    }

    public int getG2() {
        return g2;
    }

    public void setG2(int g2) {
        this.g2 = g2;
    }

    public String getS1() {
        return s1;
    }

    public void setS1(String s1) {
        this.s1 = s1;
    }

    public String getS2() {
        return s2;
    }

    public void setS2(String s2) {
        this.s2 = s2;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
