package com.gustavnienkotter.ServiceOrderManager.dto.client;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class ClientDTO {

    private Long id;

    @NotEmpty
    @NotNull
    private String name;

    @NotEmpty
    @NotNull
    private String address;

    @NotEmpty
    @NotNull
    private String phone;

    @NotEmpty
    @NotNull
    private String email;

}
