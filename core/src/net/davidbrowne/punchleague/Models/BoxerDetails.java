package net.davidbrowne.punchleague.Models;


public class BoxerDetails {
    private String name;
    private int id;
    private boolean champion;


    public BoxerDetails(String name,int id) {
        this.name = name;
        this.id = id;

    }

    public BoxerDetails() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChampion() {
        return champion;
    }

    public void setChampion(boolean champion) {
        this.champion = champion;
    }


}
