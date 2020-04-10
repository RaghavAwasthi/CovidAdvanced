package com.rvai.moapp;

import java.io.Serializable;

public class LocationModel implements Serializable {
    long timestamp;

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
