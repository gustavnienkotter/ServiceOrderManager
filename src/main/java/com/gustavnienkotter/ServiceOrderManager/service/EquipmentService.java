package com.gustavnienkotter.ServiceOrderManager.service;

import com.gustavnienkotter.ServiceOrderManager.dto.equipmentDto.EquipmentDTO;
import com.gustavnienkotter.ServiceOrderManager.enums.ErrorResponseEnum;
import com.gustavnienkotter.ServiceOrderManager.exception.BadRequestException;
import com.gustavnienkotter.ServiceOrderManager.model.Equipment;
import com.gustavnienkotter.ServiceOrderManager.model.User;
import com.gustavnienkotter.ServiceOrderManager.model.projectionModel.EquipmentProjection;
import com.gustavnienkotter.ServiceOrderManager.repository.EquipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;

    public Page<EquipmentProjection> listAll(Pageable pageable, User user) {
        return equipmentRepository.findAllByRegistrationUser(pageable, user);
    }

    public EquipmentProjection findEquipmentById(Long id, User user) {
        return equipmentProjectionBuilder(findByIdOrThrowBadRequest(id, user));
    }

    @Transactional
    public EquipmentProjection create(EquipmentDTO equipmentDTO, User user) {
        equipmentDTO.setId(null);
        return save(equipmentBuild(equipmentDTO, user));
    }

    @Transactional
    public EquipmentProjection update(EquipmentDTO equipmentDTO, User user) {
        return save(equipmentBuild(equipmentDTO, user));
    }

    @Transactional
    public void delete(Long id, User user) {
        equipmentRepository.delete(findById(id, user));
    }

    public Equipment findByIdOrThrowBadRequest(Long id, User user) {
        Equipment equipment = findById(id, user);
        if (equipment == null) {
            throw new BadRequestException(ErrorResponseEnum.EQUIPMENT_NOT_FOUND.getValue());
        }
        return equipment;
    }

    private Equipment findById(Long id, User user) {
        return equipmentRepository.findByIdAndRegistrationUser(id, user);
    }

    private EquipmentProjection save(Equipment equipment) {
        return equipmentProjectionBuilder(equipmentRepository.save(equipment));
    }

    private EquipmentProjection equipmentProjectionBuilder(Equipment equipment) {
        ProjectionFactory pf = new SpelAwareProxyProjectionFactory();
        return pf.createProjection(EquipmentProjection.class, equipment);
    }

    private Equipment equipmentBuild(EquipmentDTO equipmentDTO, User user) {
        return Equipment.builder()
                .id(equipmentDTO.getId())
                .name(equipmentDTO.getName())
                .type(equipmentDTO.getType())
                .brand(equipmentDTO.getBrand())
                .description(equipmentDTO.getDescription())
                .registrationUser(user)
                .build();
    }



}
