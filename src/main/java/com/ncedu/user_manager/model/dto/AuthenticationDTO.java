package com.ncedu.user_manager.model.dto;

import lombok.Data;

@Data
public class AuthenticationDTO {

    private String login;
    private String password;

}
