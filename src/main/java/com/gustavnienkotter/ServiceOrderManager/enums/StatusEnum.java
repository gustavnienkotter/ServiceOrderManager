package com.gustavnienkotter.ServiceOrderManager.enums;

import lombok.Getter;

@Getter
public enum StatusEnum {

    OPENED(0, "Em aberto"),
    FINISHED(1, "Finalizada");

    private final int id;
    private final String status;

    StatusEnum(int id, String status) {
        this.id = id;
        this.status = status;
    }
}
