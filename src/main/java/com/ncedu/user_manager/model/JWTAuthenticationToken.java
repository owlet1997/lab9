package com.ncedu.user_manager.model;

import com.ncedu.user_manager.model.security.UserPrincipal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Data
public class JWTAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = 3525249502992792478L;

    private Object credentials;
    private Object principal;

    public JWTAuthenticationToken(UserPrincipal principal,
                                  String jwtToken,
                                  Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = jwtToken;
    }
}
