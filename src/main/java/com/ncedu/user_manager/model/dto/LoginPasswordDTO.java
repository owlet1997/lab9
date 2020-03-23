package com.ncedu.user_manager.model.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginPasswordDTO {

    @NotEmpty
    private String login;

    @NotEmpty
    private String password;

}
