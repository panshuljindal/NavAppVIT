package edu.vit.vtop.navapp.Utils;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DataModel implements Serializable {

    @SerializedName("_id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("abbreviation")
    private String abbreviation;

    @SerializedName("lat")
    private Double lat;

    @SerializedName("lon")
    private Double lon;

    @SerializedName("category")
    private String category;

    @SerializedName("isIndependent")
    private Boolean isIndependent;

    @SerializedName("address")
    private String address;

    private Boolean isInfoShown = false;

    public boolean getInfoShown(){return isInfoShown;}

    public void setInfoShown(boolean isInfoShown){this.isInfoShown = isInfoShown;}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getIsIndependent() {
        return isIndependent;
    }

    public void setIsIndependent(Boolean isIndependent) {
        this.isIndependent = isIndependent;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}