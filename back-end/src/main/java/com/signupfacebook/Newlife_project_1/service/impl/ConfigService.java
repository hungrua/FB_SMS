package com.signupfacebook.Newlife_project_1.service.impl;

import com.signupfacebook.Newlife_project_1.converter.ApiKeyConverter;
import com.signupfacebook.Newlife_project_1.model.dto.ApiKeyDto;
import com.signupfacebook.Newlife_project_1.model.dto.ConfigDto;
import com.signupfacebook.Newlife_project_1.model.entity1.ApiKeyEntity;
import com.signupfacebook.Newlife_project_1.model.entity1.ConfigEntity;
import com.signupfacebook.Newlife_project_1.model.entity1.ListSimEntity;
import com.signupfacebook.Newlife_project_1.model.entity1.PhoneNumberEntity;
import com.signupfacebook.Newlife_project_1.model.entity2.PhoneNumberEntity2;
import com.signupfacebook.Newlife_project_1.repository.repository1.IApiKeyRepository;
import com.signupfacebook.Newlife_project_1.repository.repository1.IConfigRepository;
import com.signupfacebook.Newlife_project_1.repository.repository1.IListSimRepository;
import com.signupfacebook.Newlife_project_1.repository.repository1.IPhoneNumberRepository;
import com.signupfacebook.Newlife_project_1.repository.repository2.IPhoneNumberRepository2;
import com.signupfacebook.Newlife_project_1.service.IConfigService;
import com.signupfacebook.Newlife_project_1.util.GenericUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ConfigService implements IConfigService {

    @Autowired
    private IConfigRepository configRepository;

    @Autowired
    private IApiKeyRepository apiKeyRepository;

    @Autowired
    private IPhoneNumberRepository2 phoneNumberRepository;

    @Autowired
    private IPhoneNumberRepository phoneNumberRepository1;

    @Autowired
    private IListSimRepository listSimRepository;


    @Override
    public String save() {
        try {
            List<ConfigEntity> listConfigInDB = configRepository.findAll();
            String urlPath = "C:\\Users\\newlife_pc20\\.hidemyacc\\profiles";
            File file = new File(urlPath);
            List<ConfigEntity> listProfile = new ArrayList<>();
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                if (children == null) {
                    return "Chưa có profile nào";
                }
                for (File fileChildren : children) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
                            .withZone(ZoneId.of("UTC"));
//                    FileTime creationTime = (FileTime) Files.getAttribute(fileChildren.toPath(), "creationTime");
//                    Instant instant1 = Instant.parse(creationTime.toString());
                    ConfigEntity configEntity = new ConfigEntity();
                    String fileName = fileChildren.getAbsolutePath();
                    configEntity.setProfilePath(fileName.replace('\\', '/'));
//                    String createDate = formatter.format(instant1);
                    configEntity.setCreate_date(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
                    configEntity.setStatus(0);
                    if(!checkExisit(listConfigInDB, configEntity.getProfilePath())) {
                        listProfile.add(configEntity);
                    }
                }
                // save all profile have in computer
                configRepository.saveAll(listProfile);
                return "Lưu thành công";
            }
            return null;
        }
        catch (Exception e) {
            System.out.println("Lưu profile lỗi rồi!");
            return "Lưu profile lỗi rồi!";
        }
    }

    @Override
    public ConfigEntity findById(Long id) {
        ConfigEntity configEntity = configRepository.findById(id)
                .orElse(null);
        return configEntity;
    }

    @Override
    public List<ConfigDto> findAll() {
        try {
            List<ConfigEntity> configs = configRepository.findAll();
            List<ConfigDto> results = new ArrayList<>();
            for(ConfigEntity configEntity : configs) {
                ConfigDto configDto = new ConfigDto();
                configDto.setId(configEntity.getId());
                configDto.setProfilePath(configEntity.getProfilePath());
                results.add(configDto);
            }
            return results;
        }
        catch(Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public List<ApiKeyDto> findAllApiKey() {
        List<ApiKeyEntity> listEntity = apiKeyRepository.findAll();
        List<ApiKeyDto> apiKeyDtos = new ArrayList<>();
        for(ApiKeyEntity apiKeyEntity : listEntity) {
            apiKeyDtos.add(ApiKeyConverter.toDto(apiKeyEntity));
        }
        return apiKeyDtos;
    }

    @Override
    public String saveApiKey(String apiKey) {
        try {
            ApiKeyEntity apiKeyEntity = new ApiKeyEntity();
            apiKeyEntity.setApiKey(apiKey);
            apiKeyRepository.save(apiKeyEntity);
            return "Save " + apiKey + "Success";
        }
        catch (Exception e) {
            System.out.println("Không thể thêm api key");
            e.printStackTrace();
            return "Save" + apiKey + " error!";
        }
    }

    @Override
    public ApiKeyEntity findApiById() {
        ApiKeyEntity apiKeyEntity = apiKeyRepository.findOneRanDom()
                .orElse(null);
        if(apiKeyEntity == null) {
            return null;
        }
        return apiKeyEntity;
    }

    @Override
    public Long countProfile() {
        return configRepository.count();
    }

    @Override
    public List<PhoneNumberEntity> config() {
        List<ConfigEntity> listProfile = configRepository.findAllConfig(); // lấy ra 50 profile trong database
        updateProfile(listProfile);
        if(listProfile.size() <= 0) {
            return null;
        }
        List<PhoneNumberEntity> phoneNumbers = new ArrayList<>();
        List<PhoneNumberEntity2> listPhoneNumber = findAlPhoneNumber(); // lấy ra 50 sim ngẫu nhiên trong database
        for(int i = 0; i < listPhoneNumber.size(); i++) {
            Integer indexProfile = i;
            if (i >= listProfile.size()) {
                indexProfile = i % listProfile.size();
            }
            ConfigEntity entity = listProfile.get(indexProfile);
            entity.setStatus(1);
            entity.setCreate_date(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
            configRepository.save(entity); // cập nhật trạng thái profile
            System.out.println("Profile số: " + entity.getId());
            PhoneNumberEntity phoneNumberEntity = new PhoneNumberEntity();
            phoneNumberEntity.setId(listPhoneNumber.get(i).getId());
            phoneNumberEntity.setConfigEntity(entity);
            phoneNumberEntity.setStatus(0);
            phoneNumberEntity.setCreateDate(new Date());
            phoneNumberEntity.setPhoneNumber(listPhoneNumber.get(i).getPhoneNumber());
            phoneNumbers.add(phoneNumberEntity);
            phoneNumberRepository1.save(phoneNumberEntity);
        }
        return phoneNumbers;
    }

    private List<PhoneNumberEntity2> findAlPhoneNumber() {
        // lấy ra các số điện thoại đã từng chạy
        List<PhoneNumberEntity> listPhoneNumberLocal = phoneNumberRepository1.findAllByStatus(0);
        List<String> existPhoneNumber = new ArrayList<>();
        for(PhoneNumberEntity phoneNumber : listPhoneNumberLocal) {
            existPhoneNumber.add(phoneNumber.getPhoneNumber());
        }
        String existPhoneNummberStr = String.join(", ", existPhoneNumber);
        System.out.println("existPhoneNummberStr = " + existPhoneNummberStr);
//        Lấy ra danh sách các số có thể sử dụng để chạy
        List<PhoneNumberEntity2> listPhoneNumber = phoneNumberRepository.findAllByActive(existPhoneNummberStr);
        updatePhoneNumberToDBLocal(listPhoneNumberLocal);
        return listPhoneNumber;
    }

    private void updatePhoneNumberToDBLocal(List<PhoneNumberEntity> listPhoneNumberLocal) {
        LocalDateTime timeNow = LocalDateTime.now();
//      Cập nhật status các số sau 5 ngày
        for(PhoneNumberEntity phoneNumber : listPhoneNumberLocal) {
            LocalDateTime createDate = phoneNumber.getCreateDate()
                                        .toInstant()
                                        .atZone(ZoneId.of("UTC"))
                                        .toLocalDateTime();
            Long distanceDay = Math.abs(ChronoUnit.DAYS.between(timeNow, createDate));
//            có thể sử dung lại số này sau 6 ngày
            if(distanceDay >= 5) {
                phoneNumber.setStatus(1);
                phoneNumber.setCreateDate(new Date());
                phoneNumberRepository1.save(phoneNumber);
            }
        }
    }

    private void updateProfile(List<ConfigEntity> listConfig) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime dateNow = LocalDateTime.now();
        for(ConfigEntity config : listConfig) {
            LocalDateTime createDate = LocalDateTime.parse(config.getCreate_date(), formatter);
            Long distanceDay = Math.abs(ChronoUnit.DAYS.between(dateNow, createDate));
//            có thể sử dung lại số này sau 5 ngày
            if(distanceDay >= 5) {
                config.setStatus(1);
                config.setCreate_date(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
            }
        }
    }

    private Boolean checkExisit(List<ConfigEntity> listConfig, String profileName) {
        return listConfig.stream().anyMatch(config -> config.getProfilePath().equals(profileName));
    }
}
