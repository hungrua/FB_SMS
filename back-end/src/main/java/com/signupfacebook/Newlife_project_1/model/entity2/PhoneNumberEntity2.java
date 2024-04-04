package com.signupfacebook.Newlife_project_1.model.entity2;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sim_account_info")
public class PhoneNumberEntity2 {

    @Id
    private String id;
    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "action_type")
    private String action_type;
    @Column(name = "app_type")
    private String app_type;
    @Column(name = "sms_ack")
    private Integer sms_ack;
    @Column(name = "timestamp")
    private Long timestamp;
    @Column(name = "app_name")
    private String app_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAction_type() {
        return action_type;
    }

    public void setAction_type(String action_type) {
        this.action_type = action_type;
    }

    public String getApp_type() {
        return app_type;
    }

    public void setApp_type(String app_type) {
        this.app_type = app_type;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public Integer getSms_ack() {
        return sms_ack;
    }

    public void setSms_ack(Integer sms_ack) {
        this.sms_ack = sms_ack;
    }
}
