package com.example.ehospital;

public class MessageModel {
    public String messagetext,timestamp,sentby,type;

    public MessageModel(String messagetext, String timestamp, String sentby,String type) {
        this.messagetext = messagetext;
        this.timestamp = timestamp;
        this.sentby = sentby;
        this.type=type;
    }
    public MessageModel() {
    }
}