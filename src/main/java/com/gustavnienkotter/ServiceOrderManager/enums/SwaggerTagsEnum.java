package com.gustavnienkotter.ServiceOrderManager.enums;

import lombok.Getter;

@Getter
public enum SwaggerTagsEnum {

    SERVICE_ORDER("Service Order"),
    EQUIPMENT("Equipment"),
    CLIENT("Client"),
    USER("User");

    private final String value;

    SwaggerTagsEnum(String value) {
        this.value = value;
    }
}
