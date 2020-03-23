package com.ncedu.user_manager.model.dto;

import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class UserDTO {

    private UUID id;
    private String name;
    private Set<String> roles;

}
