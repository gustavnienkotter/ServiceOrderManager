package com.gustavnienkotter.ServiceOrderManager.model.projectionModel;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public interface ServiceOrderProjection {

    Long getId();
    String getStatus();
    String getStatusInfo();
    BigDecimal getValue();
    Timestamp getStartDate();
    Timestamp getStoppedDate();
    Timestamp getFinishDate();
    Timestamp getRegisterDate();
    String getDescription();
    List<EquipmentProjection> getEquipment();
    ClientProjection getResponsibleCustomer();
}
