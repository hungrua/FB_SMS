package com.signupfacebook.Newlife_project_1.converter;

import com.signupfacebook.Newlife_project_1.model.dto.ApiKeyDto;
import com.signupfacebook.Newlife_project_1.model.entity1.ApiKeyEntity;
import org.modelmapper.ModelMapper;

public class ApiKeyConverter {
    public static ApiKeyDto toDto(ApiKeyEntity apiKeyEntity) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(apiKeyEntity, ApiKeyDto.class);
    }
}
