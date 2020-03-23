package com.ncedu.user_manager.exception;

import com.ncedu.user_manager.service.error.Error;
import lombok.Getter;

import java.util.Map;

@Getter
public class BaseException extends RuntimeException {

    private final Error error;
    private final Map<String, Object> extra;

    public BaseException(Error error) {
        this(error, null);
    }

    public BaseException(Error error, Map<String, Object> extra) {
        super(error.getMessage());
        this.error = error;
        this.extra = extra;
    }
}
