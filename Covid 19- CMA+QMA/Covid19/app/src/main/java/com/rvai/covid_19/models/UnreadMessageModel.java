package com.rvai.covid_19.models;

public class UnreadMessageModel {
    String SenderId;
    String ReceiverId;

    public UnreadMessageModel() {
    }

    public UnreadMessageModel(String senderId, String receiverId, int count) {
        SenderId = senderId;
        ReceiverId = receiverId;
        this.count = count;
    }

    public String getSenderId() {
        return SenderId;
    }

    public void setSenderId(String senderId) {
        SenderId = senderId;
    }

    public String getReceiverId() {
        return ReceiverId;
    }

    public void setReceiverId(String receiverId) {
        ReceiverId = receiverId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    int count;

}
