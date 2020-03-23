package com.ncedu.user_manager.model.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRole implements GrantedAuthority {

    private static final long serialVersionUID = 412377020711181767L;

    public static final UserRole REFRESH_ACCESS_ROLE = new UserRole("REFRESH_ACCESS");

    private String authority;

}
