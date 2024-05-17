package ru.nishty.aai_referee.ui.secretary.result;

import java.util.List;

import ru.nishty.aai_referee.entity.secretary.Protocol;

public class ResultPlayer {
    private String comp_id;
    private String Name;
    private int Tours;
    private int Range;
    private String Region;
    private List<Protocol> protocols;

    public ResultPlayer(String comp_id, List<Protocol> protocols, String name,int tours,
                        int range, String region) {
        this.comp_id = comp_id;
        this.protocols = protocols;
        this.Name = name;
        this.Tours=tours;
        this.Range = range;
        this.Region = region;
    }

    public String getComp_id() {
        return comp_id;
    }

    public void setComp_id(String comp_id) {
        this.comp_id = comp_id;
    }

    public List<Protocol> getProtocols() {
        return protocols;
    }

    public int getTours() {
        return Tours;
    }

    public void setTours(int tours) {
        Tours = tours;
    }

    public void setProtocols(List<Protocol> protocols) {
        this.protocols = protocols;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getRange() {
        return Range;
    }

    public void setRange(int range) {
        Range = range;
    }

    public String getRegion() {
        return Region;
    }

    public void setRegion(String region) {
        Region = region;
    }
}

