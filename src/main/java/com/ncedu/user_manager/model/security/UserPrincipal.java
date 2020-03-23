package com.ncedu.user_manager.model.security;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Data
@Accessors(chain = true)
public class UserPrincipal implements UserDetails {

    private static final long serialVersionUID = 2586932784379466242L;

    private List<UserRole> authorities;
    private String password;
    private String username;
    private String accessTokenId;
    private String refreshTokenId;

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
