package edu.vit.vtop.navapp.Utils;

public class DataModel {
    String name;
    String abbreviations;
    double latitude;
    double longitude;
    String category;

    public DataModel(String name, String abbreviations, double latitude, double longitude, String category) {
        this.name = name;
        this.abbreviations = abbreviations;
        this.latitude = latitude;
        this.longitude = longitude;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbreviations() {
        return abbreviations;
    }

    public void setAbbreviations(String abbreviations) {
        this.abbreviations = abbreviations;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
