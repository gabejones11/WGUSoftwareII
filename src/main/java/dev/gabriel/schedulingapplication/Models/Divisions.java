package dev.gabriel.schedulingapplication.Models;

public class Divisions {
    private int divisionID;
    private String divisionName;
    private int countryID;

    public Divisions(int divisionID, String divisionName, int countryID){
        this.divisionID = divisionID;
        this.divisionName = divisionName;
        this.countryID = countryID;
    }

    //getters
    public int getDivisionID() {
        return divisionID;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public int getCountryID() {
        return countryID;
    }

}
