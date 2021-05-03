package com.gustavnienkotter.ServiceOrderManager.enums;

import lombok.Getter;

@Getter
public enum ErrorResponseEnum {
    UNKNOWN("unknown"),
    INTERNAL("failed"),
    USERNAME_UNAVAILABLE("Username unavailable"),
    CLIENT_NOT_FOUND("Client not found"),
    EQUIPMENT_NOT_FOUND("Equipment not found"),
    SERVICE_ORDER_NOT_FOUND("Service Order not found"),
    USER_NOT_FOUND("User not found");

    private final String value;

    ErrorResponseEnum(String status) {
        this.value = status;
    }

    public String getValue() {
        return value + ", please check documentation at: https://github.com/gustavnienkotter/ServiceOrderManager";
    }
}