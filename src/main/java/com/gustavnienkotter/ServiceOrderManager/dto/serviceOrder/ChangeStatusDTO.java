package com.gustavnienkotter.ServiceOrderManager.dto.serviceOrder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gustavnienkotter.ServiceOrderManager.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class ChangeStatusDTO {

    @JsonIgnore
    private final DateUtil dateUtil;

    @NotNull
    private Long idServiceOrder;

    private Long idStatus;

    private Timestamp changeDate;

    @NotNull
    @NotEmpty
    private String statusInfo;

    public Timestamp getChangeDate() {
        if (changeDate == null) {
            changeDate = dateUtil.timestampNow();
        }
        return changeDate;
    }
}
