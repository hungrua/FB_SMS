package com.signupfacebook.Newlife_project_1.model.dto;

public class SmsDto {

    private String id;
    private String content;
    private String sender;
    private String receiver;
    private String date_send;
    private String date_receive;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getDate_send() {
        return date_send;
    }

    public void setDate_send(String date_send) {
        this.date_send = date_send;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getDate_receive() {
        return date_receive;
    }

    public void setDate_receive(String date_receive) {
        this.date_receive = date_receive;
    }
}
