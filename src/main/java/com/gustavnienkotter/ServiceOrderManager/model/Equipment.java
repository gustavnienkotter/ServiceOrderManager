package com.gustavnienkotter.ServiceOrderManager.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "M_EQUIPMENT")
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;
    private String brand;
    private String description;

    @CreatedDate
    private Timestamp registerDate;

    @ManyToOne
    private User registrationUser;
}
