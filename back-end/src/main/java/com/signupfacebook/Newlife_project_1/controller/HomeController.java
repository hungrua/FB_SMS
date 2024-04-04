package com.signupfacebook.Newlife_project_1.controller;

import com.signupfacebook.Newlife_project_1.model.dto.Data;
import com.signupfacebook.Newlife_project_1.model.dto.ProcessData;
import com.signupfacebook.Newlife_project_1.model.entity1.ApiKeyEntity;
import com.signupfacebook.Newlife_project_1.model.entity1.ConfigEntity;
import com.signupfacebook.Newlife_project_1.model.entity1.ListSimEntity;
import com.signupfacebook.Newlife_project_1.model.entity1.PhoneNumberEntity;
import com.signupfacebook.Newlife_project_1.service.IConfigService;
import com.signupfacebook.Newlife_project_1.service.IPhoneNumberService;
import com.signupfacebook.Newlife_project_1.service.IProcessService;
import com.signupfacebook.Newlife_project_1.service.ISmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RequestMapping(value = "api")
@RestController
@CrossOrigin(origins = "*")
public class HomeController {

    private final String PYTHON_API_START = "http://127.0.0.1:8888/api/start";
    private final String PYTHON_API_ACTION = "http://127.0.0.1:8888/api/action/";
    private final String PYTHON_API_TEST = "http://127.0.0.1:8888/api/test";
    private ExecutorService executorService = Executors.newFixedThreadPool(3);
    private String actions = "continue";
    private ProcessData processData = new ProcessData();
    private Integer totalPhoneNumber = null;
    private String time_send = null;
    private static Integer offset = 0;
    private List<ProcessData> listResult = new ArrayList<>();

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private IConfigService configService;
    @Autowired
    private IProcessService processService;
    @Autowired
    private ISmsService smsService;


    @GetMapping("/play") // start program
    public String callPythonApi(){
        try{
            Long countProfile = configService.countProfile();
            if(countProfile < offset) {
                offset = 0;
                return "Hãy tạo thêm profile!";
            }
            List<PhoneNumberEntity> listPhoneNumber = configService.config();
            this.offset += 40;
            if(listPhoneNumber == null) {
                return "Không đủ profile để chạy vui lòng thêm mới profile";
            }
            List<Data> data = new ArrayList<>();
            this.totalPhoneNumber = listPhoneNumber.size();
            this.actions = "continue";
            ApiKeyEntity apiKeyEntity = configService.findApiById();
            if(apiKeyEntity == null) {
                return "Vui lòng thêm api key cho extension 2captcha!";
            }
//            String urlApi = PYTHON_API_START + "?profilePath=" + configEntity.getProfilePath() + "&listPhoneNumber=" + listPhoneNumber.toString();
            for(PhoneNumberEntity phoneNumberEntity : listPhoneNumber) {
                Data dataDto = new Data();
                dataDto.setProfile(phoneNumberEntity.getConfigEntity().getProfilePath());
                dataDto.setPhoneNumber(phoneNumberEntity.getPhoneNumber());
                dataDto.setApiKey(apiKeyEntity.getApiKey());
                data.add(dataDto);
            }
            String urlApi = PYTHON_API_START;
            ResponseEntity<Object> res = restTemplate.postForEntity(urlApi, data, Object.class);
            return res.getStatusCode().toString();
        }
        catch(Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @PostMapping("/process") // receive progress from api python
    public ResponseEntity<String> receiveProgress(@RequestBody ProcessData data) {
        CompletableFuture<Void> processingFuture = CompletableFuture.runAsync(() -> {
            if(!actions.equals("pause") && !actions.equals("finish")) {
                // Case success and wait SMS
                if(data.getStatus().equals("")) {
                    System.out.print("status " + data.getStatus());
                    pause();
                    setData(data);
                    processData.setStatus("Đang gửi tin nhắn");
//                    cập nhật số điện thoại
                    smsService.updatePhoneNumberInfor(data.getCurrent_phoneNumber());
                    try {
                        Thread.sleep(290000);
//                        System.out.println();
                        boolean checkSave = smsService.save(time_send, data.getCurrent_phoneNumber());
                        System.out.println("checkSave " + checkSave);
                        pause();
                        if(checkSave) {
                            if(this.totalPhoneNumber.equals(processData.getIndex()))
                                this.processData.setCheck(true);
                            this.processData.setStatus("Thành công");
                            this.processData.setMessage("Gửi SMS thành công");
                            this.listResult = processService.addProcess(this.listResult, this.processData);
                        }
                        else {
                            if(this.totalPhoneNumber.equals(processData.getIndex()))
                                this.processData.setCheck(true);
                            this.processData.setStatus("Thất bại");
                            this.processData.setMessage("Lỗi quá thời gian chờ");
                            this.listResult = processService.addProcess(this.listResult, this.processData);
                        }

                    }
                    catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                else {
                    setData(data);
                    this.listResult = processService.addProcess(this.listResult, this.processData);
                }
                // Thêm process vào listResult
            }
        }, executorService);
        return ResponseEntity.accepted().body("Success");
    }

    @PostMapping("/time_send") //receive time start send request to facebook from api python
    public void receiveTimeSend(@RequestParam("time") String time) {
        time_send = time;
    }

    @GetMapping("/process_current") // send progress current to client
    public CompletableFuture<List<ProcessData>> process_current() {
        CompletableFuture<List<ProcessData>> processDataFuture = CompletableFuture.supplyAsync(() -> {
            try{
                ProcessData response = processService.sendProcess(this.processData, this.totalPhoneNumber);
                if((response.getStatus().equals("Thất bại") || response.getStatus().equals("Thành công"))
                        && this.totalPhoneNumber.equals(response.getIndex())) {
                    response.setCheck(true);
                    System.out.println("Xin chao");
                }
                if(this.actions.equals("finish")) {
                    response.setCheck(true);
                }
                this.listResult = processService.addProcess(this.listResult, response);
                for(ProcessData process : listResult)
                    System.out.println(process.getCurrent_phoneNumber());
                return this.listResult;
            }
            catch (NullPointerException e) {
                return null;
            }
        }, executorService);
        return processDataFuture.thenApply((List<ProcessData> list) -> ResponseEntity.ok(list).getBody());
    }

    @GetMapping("/action") // pause, continue, finish
    public String action(@RequestParam("action") String action) {
        CompletableFuture<String> processDataFuture = CompletableFuture.supplyAsync(() -> {
            String url = PYTHON_API_ACTION + "?action=";
            this.actions = action;
            if(action.equals("pause")) {
                ResponseEntity<String> response = restTemplate.getForEntity(url + action, String.class);
                return response.toString();
            }
            else if(action.equals("continue")) {
                ResponseEntity<String> response = restTemplate.getForEntity(url + action, String.class);
                return response.toString();
            }
            else {
                ResponseEntity<String> response = restTemplate.getForEntity(url + action, String.class);
                try {
                    Thread.sleep(3000);
                    this.processData = new ProcessData();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }, executorService);
        return String.valueOf(processDataFuture.thenApply(message -> ResponseEntity.ok(message)));
    }

    private void setData(ProcessData data) {
        processData = new ProcessData();
        processData.setCurrent_phoneNumber(data.getCurrent_phoneNumber());
        processData.setIndex(data.getIndex());
        processData.setMessage(data.getMessage());
        processData.setStatus(data.getStatus());
        processData.setCheck(false);
        processData.setTotalPhoneNumber(totalPhoneNumber);
    }

    private void pause() {
        while (this.actions.equals("pause")) {
            try {
                Thread.sleep(3000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
