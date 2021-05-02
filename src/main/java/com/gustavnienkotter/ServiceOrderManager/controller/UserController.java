package com.gustavnienkotter.ServiceOrderManager.controller;

import com.gustavnienkotter.ServiceOrderManager.dto.user.UserDTO;
import com.gustavnienkotter.ServiceOrderManager.model.User;
import com.gustavnienkotter.ServiceOrderManager.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("admin/user")
@Log4j2
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final UserService serviceUser;

    @GetMapping
    @Operation(summary = "List all users paginated", tags = {"User"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation")
    })
    public ResponseEntity<Page<User>> listAll(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(serviceUser.listAll(pageable));
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Get a user by their id", tags = {"User"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "When User not exists in database")
    })
    public ResponseEntity<User> findById(@PathVariable Long id) {
        return ResponseEntity.ok(serviceUser.findByIdOrThrowBadRequest(id));
    }

    @PostMapping(path = "create")
    @Operation(summary = "Will create a user", tags = {"User"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "If has some unexpected problem to include the user")
    })
    public ResponseEntity<User> create(@RequestBody @Valid UserDTO userDTO) {
        return new ResponseEntity<>(serviceUser.create(userDTO), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "delete/{id}")
    @Operation(summary = "Will Delete a user", tags = {"User"},
            description = "Note: Delete will return status 204 regardless of whether you deleted something or not")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful Operation")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        serviceUser.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "Update")
    @Operation(summary = "Will update user information by Id", tags = {"User"},
            description = "Note: If there are fields not informed, they will be filled in as null")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "When User not exists in database")
    })
    public ResponseEntity<User> update(@RequestBody @Valid UserDTO userDTO){
        return ResponseEntity.ok(serviceUser.update(userDTO));
    }

}
