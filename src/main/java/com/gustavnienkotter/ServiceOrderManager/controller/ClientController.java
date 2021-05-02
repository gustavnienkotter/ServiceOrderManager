package com.gustavnienkotter.ServiceOrderManager.controller;

import com.gustavnienkotter.ServiceOrderManager.dto.clientDto.ClientDTO;
import com.gustavnienkotter.ServiceOrderManager.model.Client;
import com.gustavnienkotter.ServiceOrderManager.model.User;
import com.gustavnienkotter.ServiceOrderManager.model.projectionModel.ClientProjection;
import com.gustavnienkotter.ServiceOrderManager.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("client")
@Log4j2
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping
    @Operation(summary = "List all Clients paginated by logged user", tags = {"Client"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation")
    })
    public ResponseEntity<Page<ClientProjection>> listAll(@ParameterObject Pageable pageable, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(clientService.listAll(pageable, user));
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Get a Client by their id", tags = {"Client"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "When Client not exists in database")
    })
    public ResponseEntity<ClientProjection> findById(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(clientService.findClientById(id, user));
    }

    @PostMapping(path = "create")
    @Operation(summary = "Will create a Client", tags = {"Client"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "If has some unexpected problem to include the Client")
    })
    public ResponseEntity<ClientProjection> create(@RequestBody @Valid ClientDTO clientDTO, @AuthenticationPrincipal User user) {
        return new ResponseEntity<>(clientService.create(clientDTO, user), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "delete/{id}")
    @Operation(summary = "Will delete a Client", description = "Note: Delete will return status 204 regardless of whether you deleted something or not",
            tags = {"Client"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful Operation")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal User user) {
        clientService.delete(id, user);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "update")
    @Operation(summary = "Will update Client information by Id", description = "Note: If there are fields not informed, they will be filled in as null",
            tags = {"Client"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "When Client not exists in database")
    })
    public ResponseEntity<ClientProjection> update(@RequestBody ClientDTO clientDTO, @AuthenticationPrincipal User user) {
        return new ResponseEntity<>(clientService.update(clientDTO, user), HttpStatus.CREATED);
    }
}
