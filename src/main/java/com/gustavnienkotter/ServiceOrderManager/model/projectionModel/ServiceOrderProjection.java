package com.gustavnienkotter.ServiceOrderManager.model.projectionModel;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public interface ServiceOrderProjection {

    Long getId();
    BigDecimal getValue();
    Timestamp getStartDate();
    Timestamp getFinishDate();
    Timestamp getRegisterDate();
    String getDescription();
    String getStatus();
    List<EquipmentProjection> getEquipment();
    ClientProjection getResponsibleCustomer();
}
