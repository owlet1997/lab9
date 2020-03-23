package com.ncedu.user_manager.service.error;

import lombok.Getter;

@Getter
public enum Error {
    //Internal errors 001-099
    UNEXPECTED_ERROR("001", "Unexpected exception"),
    INVALID_OFFSET("002", "Offset must be not negative"),
    INVALID_LIMIT("003", "Limit must be positive"),
    INVALID_RANGE("004", "Could not extract range"),
    INVALID_SORT("005", "Could not extract sort"),
    NOT_IMPLEMENTED("006", "Implementation required"),
    UNAUTHORIZED("007", "Unauthorized"),
    FORBIDDEN("008", "Forbidden"),

    //User errors 100-199
    INVALID_LOGIN_OR_PASSWORD("100", "Invalid login or password"),
    USER_WITH_TOKEN_NOT_FOUND("101", "User with provided token not found"),
    USER_WITH_PROVIDED_LOGIN_ALREADY_EXISTS("102", "User with provided login already exists")

    //Role errors 200-299

    ;
    private final String code;
    private final String message;

    Error(String code, String message) {
        this.code = "UM-" + code;
        this.message = message;
    }
}
