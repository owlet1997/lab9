package com.ncedu.user_manager.service.converter;

import com.ncedu.user_manager.model.dto.UserDTO;
import com.ncedu.user_manager.model.entity.UserEntity;
import com.ncedu.user_manager.model.security.UserPrincipal;
import com.ncedu.user_manager.model.security.UserRole;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserConverter {

    private static final Type USER_ROLES_TYPE = new TypeToken<List<UserRole>>(){}.getType();

    private final ModelMapper modelMapper;

    public UserEntity toEntity(UserDTO dto) {
        return modelMapper.map(dto, UserEntity.class);
    }

    public UserDTO toDTO(UserEntity entity) {
        return modelMapper.map(entity, UserDTO.class);
    }

    public UserPrincipal toPrincipal(UserEntity entity) {
        return new UserPrincipal()
                .setAuthorities(modelMapper.map(new ArrayList<>(entity.getRoles()), USER_ROLES_TYPE))
                .setUsername(entity.getLogin())
                .setPassword(entity.getPassword())
                .setAccessTokenId(entity.getAccessTokenId())
                .setRefreshTokenId(entity.getRefreshTokenId());
    }

}
