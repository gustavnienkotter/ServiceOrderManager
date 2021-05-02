package com.gustavnienkotter.ServiceOrderManager.dto.serviceOrderDto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gustavnienkotter.ServiceOrderManager.model.Client;
import com.gustavnienkotter.ServiceOrderManager.model.Equipment;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
public class ServiceOrderDTO {

    private Long id;

    private BigDecimal value;

    private String status;
    private String description;

    @JsonIgnore
    private List<Equipment> equipaments;

    @JsonIgnore
    private Client responsibleCustomer;

    private Timestamp startDate;
    private Timestamp finishDate;

}
