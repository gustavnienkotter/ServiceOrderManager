package com.gustavnienkotter.ServiceOrderManager.dto.serviceOrder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gustavnienkotter.ServiceOrderManager.enums.StatusEnum;
import com.gustavnienkotter.ServiceOrderManager.model.Client;
import com.gustavnienkotter.ServiceOrderManager.model.Equipment;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
public class ServiceOrderDTO {

    private Long id;

    private BigDecimal value;

    @JsonIgnore
    private StatusEnum status;

    @NotNull
    @NotEmpty
    private String statusInfo;
    private String description;

    @JsonIgnore
    private List<Equipment> equipments;

    @JsonIgnore
    private Client responsibleCustomer;

    private Timestamp startDate;
    private Timestamp finishDate;

    @JsonIgnore
    private Timestamp registerDate;

}
