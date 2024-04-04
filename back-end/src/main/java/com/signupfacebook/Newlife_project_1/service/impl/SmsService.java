package com.signupfacebook.Newlife_project_1.service.impl;

import com.signupfacebook.Newlife_project_1.converter.SmsConverter;
import com.signupfacebook.Newlife_project_1.model.dto.SmsDto;
import com.signupfacebook.Newlife_project_1.model.entity1.SmsEntity1;
import com.signupfacebook.Newlife_project_1.model.entity2.PhoneNumberEntity2;
import com.signupfacebook.Newlife_project_1.model.entity2.SmsEntity2;
import com.signupfacebook.Newlife_project_1.repository.repository1.ISMSRepository;
import com.signupfacebook.Newlife_project_1.repository.repository2.IPhoneNumberRepository2;
import com.signupfacebook.Newlife_project_1.repository.repository2.ISmsRepository;
import com.signupfacebook.Newlife_project_1.service.ISmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.format.DateTimeFormatters;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class SmsService implements ISmsService {

    @Autowired
    private ISMSRepository smsRepository;
    @Autowired
    private ISmsRepository smsRepository2;
    @Autowired
    private IPhoneNumberRepository2 phoneNumberRepository;

    @Override
    public List<SmsDto> findAllByDateSendOrDateReceiveOrReceiver(String dateSend, String dateReceive, String receiver){
        List<SmsEntity1> resultSet = smsRepository.findAllByDateSendOrDateReceiveOrReceiver(dateSend, dateReceive, receiver);
        List<SmsDto> results = new ArrayList<>();
        for(SmsEntity1 smsEntity1 : resultSet) {
            results.add(SmsConverter.toDto(smsEntity1));
        }
//        List<SmsEntity2> resultSet = smsRepository2.findAll();
        return results;
    }

    @Override
    public List<SmsDto> findAllByDateSendAndDateReceiveAndReceiver(String dateSend, String dateReceive, String receiver) {
        List<SmsEntity1> resultSet = smsRepository.findAllByDateSendAndDateReceiveAndReceiver(dateSend, dateReceive, receiver);
        List<SmsDto> results = new ArrayList<>();
        for(SmsEntity1 smsEntity1 : resultSet) {
            results.add(SmsConverter.toDto(smsEntity1));
        }
        return results;
    }

    @Override
    public boolean save(String time_send, String sender) {
        try {
            time_send = time_send.trim();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime localDate = LocalDateTime.parse(time_send, dateTimeFormatter);
            String beforeSend = time_before_send(localDate);
            String afterSend = time_after_send(localDate);
            System.out.println("Before send = " + beforeSend);
            System.out.println("After send = " + afterSend);
            List<SmsEntity2> listSms = smsRepository2.findBySenderIdAndReceivedTime(sender, beforeSend, afterSend);
            List<SmsEntity1> listSmsResponse = new ArrayList<>();
            if(listSms.size() <= 0) {
                return false;
            }
            for(SmsEntity2 entity2 : listSms) {
                SmsEntity1 entity1 = new SmsEntity1();
                entity1.setDate_send(time_send);
                entity1 = SmsConverter.toEntity1(entity2, entity1);
                System.out.println("content = " + entity2.getContent());
                if(entity1 != null && entity2.getContent() != null) {
                    listSmsResponse.add(entity1);
                }
            }
            if(listSmsResponse.size() <= 0) {
                return false;
            }

            listSmsResponse = smsRepository.saveAll(listSmsResponse);
            return true;
        }
        catch (NullPointerException e ) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public List<String> test() {
        String time = "2023-08-17 11:19:29";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(time, dateTimeFormatter);
        List<String> res = new ArrayList<>();
        res.add("Thơi gian trước 2 phút = " + time_before_send((localDateTime)));
        res.add("Thời gian sau 2 phút = " + time_after_send(localDateTime));
        return res;
    }

    @Override
    public void updatePhoneNumberInfor(String phoneNumber) {
//        Lấy thông tin phoneNumber theo phoneNumber
        try{
            PhoneNumberEntity2 phoneNumberEntity = phoneNumberRepository.findByPhoneNumber(phoneNumber)
                    .orElse(null);
            phoneNumberEntity.setAction_type("Login");
            phoneNumberEntity.setSms_ack(0);
            phoneNumberEntity.setApp_name("Facebook");
            phoneNumberEntity.setTimestamp(System.currentTimeMillis());
            phoneNumberEntity.setApp_type("Facebook_HH");
            phoneNumberRepository.save(phoneNumberEntity);
            System.out.println("Cập nhật thông tin cho số: " + phoneNumber + " thành công");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public SmsEntity2 test2(String sender, String timeSend) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(timeSend, dateTimeFormatter);
        String beforeSend = time_before_send(localDateTime);
        String afterSend = time_after_send(localDateTime);
        System.out.println("Before Send = " + beforeSend);
        System.out.println("After Send = " + afterSend);
        List<SmsEntity2> list = smsRepository2.findBySenderIdAndReceivedTime(sender, beforeSend, afterSend);
        return list.get(0);
    }

    private String time_before_send(LocalDateTime localDate) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        LocalDateTime localDateTime = localDate.minus(5, ChronoUnit.MINUTES);
        String beforeSend = localDateTime.format(dateTimeFormatter);
        return beforeSend.trim().replace(" ", ". ");
    }

    private String time_after_send(LocalDateTime localDate) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        LocalDateTime localDateTime = localDate.plus(7, ChronoUnit.MINUTES);
        String afterSend = localDateTime.format(dateTimeFormatter);
        return afterSend.trim().replace(" ", ". ");
    }

    @Override
    public List<SmsEntity2> findAll() {
        return smsRepository2.findAllTest();
    }


}


// 11:59:20 ==> 12:01:20