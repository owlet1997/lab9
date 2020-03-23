package com.ncedu.user_manager.model.dto;

import lombok.Data;

@Data
public class NewUserDTO {

    private String login;
    private String password;
    private String name;

}
