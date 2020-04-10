package com.rvai.moapp;

public class AssesmentModel {
    String id;
    long timestamp;
    int bodytemp;
    int cough;
    int breathingdifficulty;
    int fever;

    public AssesmentModel() {
    }

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

    public int getBodytemp() {
        return bodytemp;
    }

    public void setBodytemp(int bodytemp) {
        this.bodytemp = bodytemp;
    }

    public int getCough() {
        return cough;
    }

    public void setCough(int cough) {
        this.cough = cough;
    }

    public int getBreathingdifficulty() {
        return breathingdifficulty;
    }

    public void setBreathingdifficulty(int breathingdifficulty) {
        this.breathingdifficulty = breathingdifficulty;
    }

    public int getFever() {
        return fever;
    }

    public void setFever(int fever) {
        this.fever = fever;
    }


}
