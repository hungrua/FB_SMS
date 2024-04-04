package com.signupfacebook.Newlife_project_1.service;

import com.signupfacebook.Newlife_project_1.model.dto.SmsDto;
import com.signupfacebook.Newlife_project_1.model.entity2.SmsEntity2;

import java.util.*;

public interface ISmsService {
    List<SmsDto> findAllByDateSendOrDateReceiveOrReceiver(String dateSend, String dateReceive, String receiver);
    List<SmsDto> findAllByDateSendAndDateReceiveAndReceiver(String dateSend, String dateReceive, String receiver);
    boolean save(String time_send, String sender);
    List<SmsEntity2> findAll();
    List<String> test();
    void updatePhoneNumberInfor(String phoneNumber);
    SmsEntity2 test2(String sender, String timeSend);
}
