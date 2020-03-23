package com.ncedu.user_manager.service.converter;

import com.ncedu.user_manager.model.entity.RoleEntity;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

@Service
public class RoleToStringConverter implements Converter<RoleEntity, String> {
    @Override
    public String convert(MappingContext<RoleEntity, String> mappingContext) {
        return mappingContext.getSource().getCode();
    }
}
