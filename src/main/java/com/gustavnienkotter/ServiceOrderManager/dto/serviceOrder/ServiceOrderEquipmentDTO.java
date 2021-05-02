package com.gustavnienkotter.ServiceOrderManager.dto.serviceOrder;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ServiceOrderEquipmentDTO {

    @NotNull
    private Long idServiceOrder;

    @NotNull
    private Long idEquipment;
}
