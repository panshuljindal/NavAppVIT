package edu.vit.vtop.navapp.Utils;

import java.util.Date;

public class VersionModel {
        public String _id;
        public String name;

    public VersionModel() {
    }

    public String abbreviation;
        public double lat;

    public VersionModel(String _id, String name, String abbreviation, double lat, double lon, String category, boolean isIndependent, String address, Date createdAt, Date updatedAt, int __v) {
        this._id = _id;
        this.name = name;
        this.abbreviation = abbreviation;
        this.lat = lat;
        this.lon = lon;
        this.category = category;
        this.isIndependent = isIndependent;
        this.address = address;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.__v = __v;
    }

    public double lon;
        public String category;
        public boolean isIndependent;
        public String address;
        public Date createdAt;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isIndependent() {
        return isIndependent;
    }

    public void setIndependent(boolean independent) {
        isIndependent = independent;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int get__v() {
        return __v;
    }

    public void set__v(int __v) {
        this.__v = __v;
    }

    public Date updatedAt;
        public int __v;


}
