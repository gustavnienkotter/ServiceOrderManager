package com.gustavnienkotter.ServiceOrderManager.controller;

import com.gustavnienkotter.ServiceOrderManager.dto.serviceOrder.*;
import com.gustavnienkotter.ServiceOrderManager.model.User;
import com.gustavnienkotter.ServiceOrderManager.model.projectionModel.ServiceOrderProjection;
import com.gustavnienkotter.ServiceOrderManager.service.ServiceOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("service-order")
@Log4j2
@RequiredArgsConstructor
public class ServiceOrderController {

    private final ServiceOrderService serviceOrderService;

    @GetMapping
    @Operation(summary = "List all Services Order paginated by logged user", tags = {"Service Order"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation")
    })
    public ResponseEntity<Page<ServiceOrderProjection>> listAll(@ParameterObject Pageable pageable, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(serviceOrderService.listAll(pageable, user));
    }

    @GetMapping(path = "open")
    @Operation(summary = "List all open Services Order paginated by logged user", tags = {"Service Order"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation")
    })
    public ResponseEntity<Page<ServiceOrderProjection>> listAllOpenServiceOrder(@ParameterObject Pageable pageable,
                                                                                @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(serviceOrderService.listAllOpenServiceOrder(pageable, user));
    }

    @GetMapping(path = "stopped")
    @Operation(summary = "List all stopped Services Order paginated by logged user", tags = {"Service Order"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation")
    })
    public ResponseEntity<Page<ServiceOrderProjection>> listAllStoppedServiceOrder(@ParameterObject Pageable pageable,
                                                                                @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(serviceOrderService.listAllStoppedServiceOrder(pageable, user));
    }

    @GetMapping(path = "finished")
    @Operation(summary = "List all finished Services Order paginated by logged user", tags = {"Service Order"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation")
    })
    public ResponseEntity<Page<ServiceOrderProjection>> listAllFinishedServiceOrder(@ParameterObject Pageable pageable,
                                                                                @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(serviceOrderService.listAllFinishedServiceOrder(pageable, user));
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Get a Service Order by their id and logged user", tags = {"Service Order"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "When Service Order don't exists in database")
    })
    public ResponseEntity<ServiceOrderProjection> findServiceOrderById(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(serviceOrderService.findServiceOrderById(id, user));
    }

    @PostMapping(path = "add-client")
    @Operation(summary = "Will relate a Client to a Service Order", tags = {"Service Order"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "When Service Order or Client don't exists in database")
    })
    public ResponseEntity<ServiceOrderProjection> addClient(@RequestBody @Valid ServiceOrderClientDTO serviceOrderClientDTO,
                                                            @AuthenticationPrincipal User user){
        return ResponseEntity.ok(serviceOrderService.addClient(serviceOrderClientDTO, user));
    }

    @PostMapping(path = "add-equipment")
    @Operation(summary = "Will add a Equipment to the list of Equipments a Service Order", tags = {"Service Order"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "When Service Order or Equipment don't exists in database")
    })
    public ResponseEntity<ServiceOrderProjection> addEquipment(@RequestBody @Valid ServiceOrderEquipmentDTO serviceOrderEquipmentDTO,
                                                               @AuthenticationPrincipal User user){
        return ResponseEntity.ok(serviceOrderService.addEquipment(serviceOrderEquipmentDTO, user));
    }

    @PostMapping(path = "remove-equipment")
    @Operation(summary = "Will remove a Equipment to the list of Equipments a Service Order", tags = {"Service Order"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "When Service Order or Equipment don't exists in database")
    })
    public ResponseEntity<ServiceOrderProjection> removeEquipment(@RequestBody @Valid ServiceOrderEquipmentDTO serviceOrderEquipmentDTO,
                                                               @AuthenticationPrincipal User user){
        return ResponseEntity.ok(serviceOrderService.removeEquipment(serviceOrderEquipmentDTO, user));
    }

    @PostMapping(path = "change-status")
    @Operation(summary = "Will change status of a Service Order to status informed", tags = {"Service Order"},
            description = "Notes: If you don't enter the date, it will be the current date. " +
                    "<br>If the status is null, it will be OPENNED by default" +
                    "<br>StatusEnum: " +
                    "<br>1 - OPENNED<br>2 - STOPPED<br>3 - FINISHED")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "When Service Order don't exists in database")
    })
    public ResponseEntity<ServiceOrderProjection> changeStatusServiceOrder(@RequestBody @Valid ChangeStatusDTO changeStatusDTO,
                                                                   @AuthenticationPrincipal User user){
        return ResponseEntity.ok(serviceOrderService.changeStatusServiceOrder(changeStatusDTO, user));
    }

    @PostMapping(path = "create")
    @Operation(summary = "Will create a Service Order", tags = {"Service Order"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "If has some unexpected problem to include the Service Order")
    })
    public ResponseEntity<ServiceOrderProjection> create(@RequestBody @Valid ServiceOrderDTO serviceOrderDTO, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(serviceOrderService.create(serviceOrderDTO, user));
    }

    @DeleteMapping(path = "delete/{id}")
    @Operation(summary = "Will Delete a Service Order", tags = {"Service Order"},
            description = "Note: Delete will return status 204 regardless of whether you deleted something or not")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful Operation")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal User user) {
        serviceOrderService.delete(id, user);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "update")
    @Operation(summary = "Will update user information by Id", tags = {"Service Order"},
            description = "Note: If there are fields not informed, they will be filled in as null")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "When User not exists in database")
    })
    public ResponseEntity<ServiceOrderProjection> update(@RequestBody @Valid ServiceOrderDTO serviceOrderDTO,
                                                    @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(serviceOrderService.update(serviceOrderDTO, user));
    }


}
