package com.ncedu.user_manager.service.impl.auth;

import com.ncedu.user_manager.exception.BaseException;
import com.ncedu.user_manager.model.entity.UserEntity;
import com.ncedu.user_manager.model.security.UserPrincipal;
import com.ncedu.user_manager.repository.UserRepository;
import com.ncedu.user_manager.service.converter.UserConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ncedu.user_manager.service.error.Error.INVALID_LOGIN_OR_PASSWORD;

@Service
@RequiredArgsConstructor
public class LoginPasswordAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserConverter userConverter;

    @Override
    @Transactional(readOnly = true)
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserEntity user = userRepository.findByLogin(authentication.getName()).orElseThrow(() ->
                new BaseException(INVALID_LOGIN_OR_PASSWORD)
        );
        if(!passwordEncoder.matches((String) authentication.getCredentials(), user.getPassword())) {
            throw new BaseException(INVALID_LOGIN_OR_PASSWORD);
        }

        UserPrincipal principal = userConverter.toPrincipal(user);

        return new UsernamePasswordAuthenticationToken(
                principal,
                authentication.getCredentials(),
                principal.getAuthorities()
        );
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass == UsernamePasswordAuthenticationToken.class;
    }
}
