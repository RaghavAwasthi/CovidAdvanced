package com.rvai.covid_19.models;

import java.io.Serializable;

public class AlertsModel implements Serializable {
    String moid;
    long timetamp;
    String patientID;
    String patientName;
    String patientAddress;

    public String getPatientAddress() {
        return patientAddress;
    }

    public void setPatientAddress(String patientAddress) {
        this.patientAddress = patientAddress;
    }

    public String getMoid() {
        return moid;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public void setMoid(String moid) {
        this.moid = moid;
    }

    public long getTimetamp() {
        return timetamp;
    }

    public void setTimetamp(long timetamp) {
        this.timetamp = timetamp;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public AlertsModel() {
    }
}
