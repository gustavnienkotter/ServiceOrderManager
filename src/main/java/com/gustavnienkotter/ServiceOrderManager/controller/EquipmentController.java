package com.gustavnienkotter.ServiceOrderManager.controller;

import com.gustavnienkotter.ServiceOrderManager.dto.equipment.EquipmentDTO;
import com.gustavnienkotter.ServiceOrderManager.model.User;
import com.gustavnienkotter.ServiceOrderManager.model.projectionModel.EquipmentProjection;
import com.gustavnienkotter.ServiceOrderManager.service.EquipmentService;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("equipment")
public class EquipmentController {

    private final EquipmentService equipmentService;

    @GetMapping
    @Operation(summary = "List all Equipments paginated by logged user", tags = {"Equipment"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation")
    })
    public ResponseEntity<Page<EquipmentProjection>> listAll(@ParameterObject Pageable pageable, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(equipmentService.listAll(pageable, user));
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Get a Equipment by their id", tags = {"Equipment"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "When Equipment not exists in database")
    })
    public ResponseEntity<EquipmentProjection> findById(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(equipmentService.findEquipmentById(id, user));
    }

    @PostMapping(path = "create")
    @Operation(summary = "Will create a Equipment", tags = {"Equipment"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "If has some unexpected problem to include the Equipment")
    })
    public ResponseEntity<EquipmentProjection> create(@RequestBody @Valid EquipmentDTO equipmentDTO, @AuthenticationPrincipal User user) {
        return new ResponseEntity<>(equipmentService.create(equipmentDTO, user), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "delete/{id}")
    @Operation(summary = "Will delete a Equipment", description = "Note: Delete will return status 204 regardless of whether you deleted something or not",
            tags = {"Equipment"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful Operation")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal User user) {
        equipmentService.delete(id, user);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "update")
    @Operation(summary = "Will update Equipment information by Id", description = "Note: If there are fields not informed, they will be filled in as null",
            tags = {"Equipment"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "When Equipment not exists in database")
    })
    public ResponseEntity<EquipmentProjection> update(@RequestBody @Valid EquipmentDTO equipmentDTO,
                                                            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(equipmentService.update(equipmentDTO, user));
    }

}
