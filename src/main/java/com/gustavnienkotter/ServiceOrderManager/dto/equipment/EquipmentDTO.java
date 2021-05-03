package com.gustavnienkotter.ServiceOrderManager.dto.equipment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

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

    @JsonIgnore
    private Timestamp registerDate;

}
