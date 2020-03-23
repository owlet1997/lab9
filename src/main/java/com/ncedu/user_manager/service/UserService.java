package com.ncedu.user_manager.service;

import com.ncedu.user_manager.model.dto.LoginPasswordDTO;
import com.ncedu.user_manager.model.dto.NewUserDTO;
import com.ncedu.user_manager.model.dto.TokenDTO;
import com.ncedu.user_manager.model.dto.UserDTO;
import com.ncedu.user_manager.util.OffsetBasedPageRequest;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserDTO create(NewUserDTO newUserDTO);
    UserDTO update(UUID id, UserDTO userDTO);
    void delete(UUID id);

    List<UserDTO> find(OffsetBasedPageRequest pageRequest);

    TokenDTO login(LoginPasswordDTO loginPasswordDTO);
    TokenDTO login();

    TokenDTO getOrRefreshAccessToken();

    void logout();

    UserDTO addRoles(UUID id, List<String> roleCodes);
    UserDTO removeRoles(UUID id, List<String> roleCodes);

    TokenDTO getRefreshToken(String login);
}
