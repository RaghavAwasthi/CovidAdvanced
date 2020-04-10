package com.rvai.covid_19.models;

public class ContactModel {
    String city;
    String mob;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public ContactModel(String city, String mob) {
        this.city = city;
        this.mob = mob;
    }

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }

    public ContactModel() {
    }
}
