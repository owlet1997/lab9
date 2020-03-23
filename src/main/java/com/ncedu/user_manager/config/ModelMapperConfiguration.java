package com.ncedu.user_manager.config;

import com.ncedu.user_manager.service.converter.RoleToUserRoleConverter;
import com.ncedu.user_manager.service.converter.RoleToStringConverter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfiguration {

    @Bean
    public ModelMapper modelMapper(RoleToStringConverter roleToStringConverter,
                                   RoleToUserRoleConverter roleToUserRoleConverter) {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addConverter(roleToStringConverter);
        modelMapper.addConverter(roleToUserRoleConverter);

        return modelMapper;
    }

}
