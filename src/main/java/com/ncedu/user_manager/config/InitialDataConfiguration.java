package com.ncedu.user_manager.config;

import com.ncedu.user_manager.model.entity.RoleEntity;
import com.ncedu.user_manager.model.entity.UserEntity;
import com.ncedu.user_manager.repository.RoleRepository;
import com.ncedu.user_manager.repository.UserRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

@Configuration
public class InitialDataConfiguration {

    private static final String USER_ADMIN_LOGIN = "user-admin";
    private static final String USER_ADMIN_PASSWORD = "password";
    private static final String USER_ADMIN_ROLE = "USER_ADMIN";

    @EventListener(ApplicationReadyEvent.class)
    public void createInitialRolesAndUsers(ApplicationReadyEvent event) {
        ConfigurableApplicationContext context = event.getApplicationContext();
        RoleRepository roleRepository = context.getBean(RoleRepository.class);
        UserRepository userRepository = context.getBean(UserRepository.class);
        PasswordEncoder passwordEncoder = context.getBean(PasswordEncoder.class);

        RoleEntity userAdminRole = roleRepository.findByCode(USER_ADMIN_ROLE).orElseGet(() ->
                roleRepository.save(new RoleEntity()
                        .setCode(USER_ADMIN_ROLE)
                        .setName("User Administrator"))
        );

        userRepository.findByLogin(USER_ADMIN_LOGIN).orElseGet(() ->
                userRepository.save(new UserEntity()
                        .setLogin(USER_ADMIN_LOGIN)
                        .setPassword(passwordEncoder.encode(USER_ADMIN_PASSWORD))
                        .setName("User Administrator")
                        .setRoles(Collections.singleton(userAdminRole)))
        );

    }

}
