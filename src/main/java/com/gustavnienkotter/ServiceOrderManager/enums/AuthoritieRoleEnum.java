package com.gustavnienkotter.ServiceOrderManager.enums;

import lombok.Getter;

@Getter
public enum AuthoritieRoleEnum {

    ROLE_USER("USER"),
    ROLE_ADMIN("ADMIN");

    private final String value;

    AuthoritieRoleEnum(String value) {
        this.value = value;
    }

}
