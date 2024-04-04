package com.signupfacebook.Newlife_project_1.controller;

import com.signupfacebook.Newlife_project_1.model.dto.ApiKeyDto;
import com.signupfacebook.Newlife_project_1.model.dto.ConfigDto;
import com.signupfacebook.Newlife_project_1.service.IConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class ConfigController {

    @Autowired
    private IConfigService configService;

    @PostMapping("/config/auto")
    public String saveConfig(){
        String message = configService.save(); // lưu toàn bộ profile
        return message;
    }
    @PostMapping("/config/apiKey")
    public String saveApiKey(@RequestParam("apiKey") String apiLKey) {
        String mess = configService.saveApiKey(apiLKey);
        return mess;
    }

    @GetMapping("/apiKey")
    public List<ApiKeyDto> findAll() {
        List<ApiKeyDto> results = configService.findAllApiKey();
        return results;
    }

}
