package com.ncedu.user_manager.service.error;

import org.springframework.http.HttpStatus;

import java.util.EnumMap;
import java.util.Map;

public class ErrorHttpStatus {

    private static final Map<Error, HttpStatus> HTTP_STATUS = new EnumMap<>(Error.class);
    private static void put(Error error, HttpStatus status) {
        HTTP_STATUS.put(error, status);
    }
    public static HttpStatus get(Error error) {
        return HTTP_STATUS.getOrDefault(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ErrorHttpStatus() {};

    static {
        put(Error.INVALID_LOGIN_OR_PASSWORD, HttpStatus.NOT_FOUND);
        put(Error.USER_WITH_TOKEN_NOT_FOUND, HttpStatus.NOT_FOUND);

        put(Error.NOT_IMPLEMENTED, HttpStatus.NOT_IMPLEMENTED);
        put(Error.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        put(Error.FORBIDDEN, HttpStatus.FORBIDDEN);
    }

}
