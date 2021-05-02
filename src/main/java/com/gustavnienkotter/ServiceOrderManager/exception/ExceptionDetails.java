package com.gustavnienkotter.ServiceOrderManager.exception;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;

@Data
@SuperBuilder
public class ExceptionDetails {
    protected String title;
    protected int status;
    protected String details;
    protected Timestamp timestamp;
}
