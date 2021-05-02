package com.gustavnienkotter.ServiceOrderManager.model;

import com.gustavnienkotter.ServiceOrderManager.enums.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "M_SERVICE_ORDER")
public class ServiceOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private StatusEnum status;
    private String statusInfo;

    @NotNull
    private BigDecimal value;

    @NotNull
    private Timestamp startDate;
    private Timestamp stoppedDate;
    private Timestamp finishDate;

    private String description;

    @ManyToOne
    private User registrationUser;

    @ManyToOne
    private Client responsibleCustomer;

    @ManyToMany
    private List<Equipment> equipment;

    @CreatedDate
    private Timestamp registerDate;

}
