package com.gustavnienkotter.ServiceOrderManager.dto.equipmentDto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class EquipmentDTO {

    private Long id;

    @NotEmpty
    @NotNull
    private String name;

    @NotEmpty
    @NotNull
    private String type;

    @NotEmpty
    @NotNull
    private String brand;

    private String description;

}
