package ru.nishty.aai_referee.entity.secretary;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CompetitionSecretary {
    @SerializedName("i")
    private UUID uuid;
    @SerializedName("n")
    private String name;
    @SerializedName("y")
    private String year;
    @SerializedName("p1")
    private String place;
    @SerializedName("hj")
    private String headJudge;
    @SerializedName("hs")
    private String headSecretary;
    @SerializedName("p")
    private List<PerformanceSecretary> performanceSecretaries = new ArrayList<>();
    @SerializedName("j")
    private List<Judge> judges = new ArrayList<>();
    @SerializedName("c")
    private List<Category> categories = new ArrayList<>();
    @SerializedName("r")
    private List<Region> regions = new ArrayList<>();
    @SerializedName("pl")
    private List<Player> players = new ArrayList<>();

    public CompetitionSecretary(UUID uuid, String name, String year, String place, String headJudge, String headSecretary, List<PerformanceSecretary> performanceSecretaries, List<Judge> judges, List<Category> categories, List<Region> regions, List<Player> players) {

        this.uuid = uuid;
        this.name = name;
        this.year = year;
        this.place = place;
        this.headJudge = headJudge;
        this.headSecretary = headSecretary;
        this.performanceSecretaries = performanceSecretaries;
        this.judges = judges;
        this.categories = categories;
        this.regions = regions;
        this.players = players;
    }

    public CompetitionSecretary() {
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

    public String getHeadJudge() {
        return headJudge;
    }
    public void setHeadJudge(String headJudge) {
        this.headJudge = headJudge;
    }

    public String getHeadSecretary() {
        return headSecretary;
    }
    public void setHeadSecretary(String headSecretary) {
        this.headSecretary = headSecretary;
    }

    public List<PerformanceSecretary> getPerformances() {
        return performanceSecretaries;
    }
    public void setPerformances(List<PerformanceSecretary> performanceSecretaries) {
        this.performanceSecretaries = performanceSecretaries;
    }
    public void addPerformance(PerformanceSecretary performances){
        this.performanceSecretaries.add(performances);
    }

    public List<Judge> getJudges() {
        return judges;
    }
    public void setJudges(List<Judge> judges) {
        this.judges = judges;
    }
    public void addJudges(Judge judges){
        this.judges.add(judges);
    }

    public List<Category> getCategories() {
        return categories;
    }
    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
    public void addCategory(Category categories){
        this.categories.add(categories);
    }

    public List<Region> getRegions() {
        return regions;
    }
    public void setRegions(List<Region> regions) {
        this.regions = regions;
    }
    public void addRegions(Region regions){
        this.regions.add(regions);
    }

    public List<Player> getPlayers() {
        return players;
    }
    public void setPlayers(List<Player> players) {
        this.players = players;
    }
    public void addPlayer(Player players){
        this.players.add(players);
    }
}
