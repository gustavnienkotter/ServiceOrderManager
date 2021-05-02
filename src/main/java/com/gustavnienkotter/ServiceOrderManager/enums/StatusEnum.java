package com.gustavnienkotter.ServiceOrderManager.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum StatusEnum {

    OPENED(1L, "Em aberto"),
    STOPPED(2L, "Parada"),
    FINISHED(3L, "Finalizada");

    private final Long id;
    private final String status;

    StatusEnum(Long id, String status) {
        this.id = id;
        this.status = status;
    }

    private static final Map<Long, StatusEnum> byId = new HashMap<Long, StatusEnum>();

    static {
        for (StatusEnum e : StatusEnum.values()) {
            if (byId.put(e.getId(), e) != null) {
                throw new IllegalArgumentException("duplicate id: " + e.getId());
            }
        }
    }

    public static StatusEnum getById(Long id) {
        StatusEnum statusEnum = byId.get(id);
        if (statusEnum == null) {
            statusEnum = StatusEnum.OPENED;
        }
        return statusEnum;
    }
}
