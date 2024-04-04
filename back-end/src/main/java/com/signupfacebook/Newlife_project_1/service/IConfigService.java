package com.signupfacebook.Newlife_project_1.service;

import com.signupfacebook.Newlife_project_1.model.dto.ApiKeyDto;
import com.signupfacebook.Newlife_project_1.model.dto.ConfigDto;
import com.signupfacebook.Newlife_project_1.model.entity1.ApiKeyEntity;
import com.signupfacebook.Newlife_project_1.model.entity1.ConfigEntity;
import com.signupfacebook.Newlife_project_1.model.entity1.PhoneNumberEntity;

import java.util.*;

public interface IConfigService {
    String save();
    ConfigEntity findById(Long id);
    List<ConfigDto> findAll();
    List<ApiKeyDto> findAllApiKey();
    String saveApiKey(String apiKey);
    ApiKeyEntity findApiById();
    Long countProfile();
    List<PhoneNumberEntity> config();
}
