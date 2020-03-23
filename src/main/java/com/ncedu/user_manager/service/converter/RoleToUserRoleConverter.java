package com.ncedu.user_manager.service.converter;

import com.ncedu.user_manager.model.entity.RoleEntity;
import com.ncedu.user_manager.model.security.UserRole;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

@Service
public class RoleToUserRoleConverter implements Converter<RoleEntity, UserRole> {

    @Override
    public UserRole convert(MappingContext<RoleEntity, UserRole> mappingContext) {
        return new UserRole(mappingContext.getSource().getCode());
    }
}
