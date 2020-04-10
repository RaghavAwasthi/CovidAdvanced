package com.rvai.moapp.models;

import java.io.Serializable;

public class LocationModel implements Serializable {
    long timestamp;
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    double lat,lon;

    public LocationModel() {
    }

    public LocationModel(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }


    public double getLon() {
        return lon;
    }

}
