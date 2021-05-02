package com.gustavnienkotter.ServiceOrderManager.dto.serviceOrderDto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
public class EndServiceOrderDTO {

    @NotNull
    private Long idServiceOrder;
    private Timestamp finishDate;

}
