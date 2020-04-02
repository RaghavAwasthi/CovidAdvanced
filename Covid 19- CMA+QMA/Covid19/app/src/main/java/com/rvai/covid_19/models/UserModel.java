package com.rvai.covid_19.models;

import java.io.Serializable;

public class UserModel implements Serializable {

    String id;
    String name, address;
    int dayStarted;
    int isQuarantined;
    String imageurl;
    String moId;

    public String getMoId() {
        return moId;
    }

    public void setMoId(String moId) {
        this.moId = moId;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public int getIsQuarantined() {
        return isQuarantined;
    }

    public void setIsQuarantined(int isQuarantined) {
        this.isQuarantined = isQuarantined;
    }

    LocationModel location;

    public UserModel() {
    }

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getDayStarted() {
        return dayStarted;
    }

    public void setDayStarted(int dayStarted) {
        this.dayStarted = dayStarted;
    }

    public LocationModel getLocation() {
        return location;
    }

    public void setLocation(LocationModel location) {
        this.location = location;
    }
}
