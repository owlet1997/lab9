package com.ncedu.user_manager.service.impl;

import com.ncedu.user_manager.repository.UserRepository;
import com.ncedu.user_manager.service.converter.UserConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserConverter userConverter;
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return userRepository.findByLogin(login).map(userConverter::toPrincipal).orElseThrow(() ->
                new UsernameNotFoundException("User not found")
        );
    }
}
