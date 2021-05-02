package com.gustavnienkotter.ServiceOrderManager.handler;

import com.gustavnienkotter.ServiceOrderManager.exception.BadRequestException;
import com.gustavnienkotter.ServiceOrderManager.exception.BadRequestExceptionDetails;
import com.gustavnienkotter.ServiceOrderManager.exception.ValidationExceptionDetails;
import com.gustavnienkotter.ServiceOrderManager.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Log4j2
@AllArgsConstructor
public class RestExceptionHandler {

    private final DateUtil dateUtil;

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<BadRequestExceptionDetails> handlerBadRequestException(BadRequestException exception) {
        return new ResponseEntity<>(
                BadRequestExceptionDetails.builder()
                        .timestamp(dateUtil.timestampNow())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .title("Bad Request Exception, Check the documentation")
                        .developerMessage(exception.getMessage())
                        .details(exception.getClass().getName())
                        .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationExceptionDetails> handlerMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        String fields = fieldErrors.stream().map(FieldError::getField).collect(Collectors.joining(","));
        String fieldsMessage = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(","));
        return new ResponseEntity<>(
                ValidationExceptionDetails.builder()
                        .timestamp(dateUtil.timestampNow())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .title("Bad Request Exception, Invalid Fields")
                        .details(exception.getClass().getName())
                        .fields(fields)
                        .fieldMessage(fieldsMessage)
                        .build(), HttpStatus.BAD_REQUEST);
    }
}
