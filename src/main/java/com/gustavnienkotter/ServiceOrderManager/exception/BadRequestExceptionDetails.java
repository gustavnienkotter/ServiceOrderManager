package com.gustavnienkotter.ServiceOrderManager.exception;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class BadRequestExceptionDetails extends ExceptionDetails {
    protected String developerMessage;
}
