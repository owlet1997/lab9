package com.ncedu.user_manager.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@Accessors(chain = true)
public class ErrorResponseDTO {

    private int status;

    @NotNull
    private String code;

    @NotNull
    private String message;

    private Map<String, Object> extra;

}