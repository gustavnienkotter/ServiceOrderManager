package com.gustavnienkotter.ServiceOrderManager.model.projectionModel;

import java.sql.Timestamp;

public interface ClientProjection {

    Long getId();
    String getName();
    String getAddress();
    String getPhone();
    String getEmail();
    Timestamp getRegisterDate();
}
