package com.gustavnienkotter.ServiceOrderManager.model.projectionModel;

import java.sql.Timestamp;

public interface EquipmentProjection {

    Long getId();
    String getName();
    String getType();
    String getBrand();
    String getDescription();
    Timestamp getRegisterDate();
}
