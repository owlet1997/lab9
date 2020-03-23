package com.ncedu.user_manager.service.impl;

import com.ncedu.user_manager.exception.BaseException;
import com.ncedu.user_manager.model.dto.LoginPasswordDTO;
import com.ncedu.user_manager.model.dto.NewUserDTO;
import com.ncedu.user_manager.model.dto.TokenDTO;
import com.ncedu.user_manager.model.dto.UserDTO;
import com.ncedu.user_manager.model.entity.RoleEntity;
import com.ncedu.user_manager.model.entity.UserEntity;
import com.ncedu.user_manager.model.security.UserPrincipal;
import com.ncedu.user_manager.repository.RoleRepository;
import com.ncedu.user_manager.repository.UserRepository;
import com.ncedu.user_manager.service.error.Error;
import com.ncedu.user_manager.service.UserService;
import com.ncedu.user_manager.service.converter.UserConverter;
import com.ncedu.user_manager.util.OffsetBasedPageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserConverter userConverter;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTTokenService tokenService;

    @Override
    public UserDTO create(NewUserDTO newUserDTO) {
        if(userRepository.existsByLogin(newUserDTO.getLogin())) {
            throw new BaseException(Error.USER_WITH_PROVIDED_LOGIN_ALREADY_EXISTS);
        }

        UserEntity user = new UserEntity()
                .setLogin(newUserDTO.getLogin())
                .setPassword(passwordEncoder.encode(newUserDTO.getLogin()))
                .setName(newUserDTO.getName())
                .setRefreshTokenId(UUID.randomUUID().toString())
                .setAccessTokenCreatedWhen(new Date());

        return userConverter.toDTO(userRepository.save(user));
    }

    @Override
    public UserDTO update(UUID id, UserDTO userDTO) {
        UserEntity user = userRepository.findById(id).orElseThrow(
                () -> new BaseException(Error.USER_WITH_PROVIDED_LOGIN_ALREADY_EXISTS)
        );

        user.setName(userDTO.getName());

        return userConverter.toDTO(userRepository.save(user));
    }

    @Override
    public void delete(UUID id) {
        UserEntity user = userRepository.findById(id).orElseThrow(
                () -> new BaseException(Error.USER_WITH_PROVIDED_LOGIN_ALREADY_EXISTS)
        );
        userRepository.delete(user);
    }

    @Override
    public List<UserDTO> find(OffsetBasedPageRequest pageRequest) {
        return userRepository.findBy(pageRequest).stream()
                .map(userConverter::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TokenDTO login(LoginPasswordDTO loginPasswordDTO) {
        UserEntity userEntity = userRepository.findByLogin(loginPasswordDTO.getLogin()).orElseThrow(() ->
            new BaseException(Error.INVALID_LOGIN_OR_PASSWORD)
        );

        if(!passwordEncoder.matches(loginPasswordDTO.getPassword(), userEntity.getPassword())) {
            throw new BaseException(Error.INVALID_LOGIN_OR_PASSWORD);
        }

        userEntity.setAccessTokenId(UUID.randomUUID().toString());
        userEntity.setAccessTokenCreatedWhen(new Date());
        userRepository.save(userEntity);

        return new TokenDTO()
                .setAccessToken(
                        tokenService.generateAccessToken(
                                userConverter.toPrincipal(userEntity)
                        )
                );
    }

    @Override
    public TokenDTO login() {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserEntity userEntity = userRepository.findByLogin(principal.getUsername()).orElseThrow(() ->
                new BaseException(Error.INVALID_LOGIN_OR_PASSWORD)
        );

        userEntity.setAccessTokenId(UUID.randomUUID().toString());
        userEntity.setAccessTokenCreatedWhen(new Date());
        userRepository.save(userEntity);

        return new TokenDTO()
                .setAccessToken(
                        tokenService.generateAccessToken(
                                userConverter.toPrincipal(userEntity)
                        )
                );
    }

    @Override
    public TokenDTO getOrRefreshAccessToken() {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserEntity userEntity = userRepository.findByLogin(principal.getUsername()).orElseThrow(
                () -> new BaseException(Error.INVALID_LOGIN_OR_PASSWORD)
        );
        if(tokenService.isTokenExpired(userEntity.getAccessTokenCreatedWhen())) {
            userEntity.setAccessTokenId(UUID.randomUUID().toString());
            userEntity.setAccessTokenCreatedWhen(new Date());
            userEntity = userRepository.save(userEntity);
        }

        return new TokenDTO()
                .setAccessToken(
                        tokenService.generateAccessToken(
                                userConverter.toPrincipal(userEntity)
                        )
                );
    }

    @Override
    public TokenDTO getRefreshToken(String login) {
        UserEntity userEntity = userRepository.findByLogin(login).orElseThrow(
                () -> new BaseException(Error.INVALID_LOGIN_OR_PASSWORD)
        );

        return new TokenDTO()
                .setRefreshToken(
                        tokenService.generateRefreshToken(
                                userConverter.toPrincipal(userEntity)
                        )
                );
    }

    @Override
    public void logout() {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userRepository.findByLogin(principal.getUsername()).ifPresent(userEntity -> {
            userEntity.setAccessTokenId(UUID.randomUUID().toString());
            userEntity.setAccessTokenCreatedWhen(new Date());
            userRepository.save(userEntity);
        });
    }

    @Override
    @Transactional
    public UserDTO addRoles(UUID id, List<String> roleCodes) {
        UserEntity userEntity = userRepository.findLockedById(id).get();
        for (String role: roleCodes) {
            RoleEntity roleEntity = roleRepository.findByCode(role).get();
            userEntity.getRoles().add(roleEntity);
        }
        userRepository.save(userEntity);
        return userConverter.toDTO(userEntity);
    }

    @Override
    @Transactional
    public UserDTO removeRoles(UUID id, List<String> roleCodes) {
        UserEntity userEntity = userRepository.findLockedById(id).get();

        for (String role: roleCodes) {
            RoleEntity roleEntity = roleRepository.findByCode(role).get();
            userEntity.getRoles().remove(roleEntity);
        }

        userRepository.save(userEntity);
        return userConverter.toDTO(userEntity);
    }
}
