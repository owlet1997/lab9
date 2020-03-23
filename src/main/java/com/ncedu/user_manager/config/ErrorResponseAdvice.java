package com.ncedu.user_manager.config;

import com.ncedu.user_manager.exception.BaseException;
import com.ncedu.user_manager.model.dto.ErrorResponseDTO;
import com.ncedu.user_manager.service.error.Error;
import com.ncedu.user_manager.service.error.ErrorHttpStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "com.ncedu.user_manager")
public class ErrorResponseAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponseDTO> handleCommonException(Exception ex) {
        log.error("Internal server error", ex);
        return handleUserServiceException(new BaseException(Error.UNEXPECTED_ERROR));
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ResponseEntity<ErrorResponseDTO> handleAccessDeniedException(AccessDeniedException ex) {
        log.error("Unsupported operation error", ex);
        return handleUserServiceException(new BaseException(Error.FORBIDDEN));
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    @ResponseStatus(value = HttpStatus.NOT_IMPLEMENTED)
    public ResponseEntity<ErrorResponseDTO> handleUnsupportedOperationException(UnsupportedOperationException ex) {
        log.error("Unsupported operation error", ex);
        return handleUserServiceException(new BaseException(Error.NOT_IMPLEMENTED));
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserServiceException(BaseException ex) {
        log.error("User manager error", ex);
        HttpStatus httpStatus = ErrorHttpStatus.get(ex.getError());
        return ResponseEntity.status(httpStatus).body(
                new ErrorResponseDTO()
                        .setStatus(httpStatus.value())
                        .setCode(ex.getError().getCode())
                        .setMessage(ex.getError().getMessage())
                        .setExtra(ex.getExtra())
        );
    }

}
