package com.ncedu.user_manager.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TokenDTO {

    private String accessToken;
    private String refreshToken;

}
