package com.gustavnienkotter.ServiceOrderManager.dto.user;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UserDTO {

    private Long id;

    @NotEmpty
    @NotNull
    private String name;

    @NotEmpty
    @NotNull
    private String username;

    @NotEmpty
    @NotNull
    private String password;
    private String authorities;

}
