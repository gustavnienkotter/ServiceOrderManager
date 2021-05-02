package com.gustavnienkotter.ServiceOrderManager.service;

import com.gustavnienkotter.ServiceOrderManager.dto.serviceOrderDto.EndServiceOrderDTO;
import com.gustavnienkotter.ServiceOrderManager.dto.serviceOrderDto.ServiceOrderClientDTO;
import com.gustavnienkotter.ServiceOrderManager.dto.serviceOrderDto.ServiceOrderDTO;
import com.gustavnienkotter.ServiceOrderManager.dto.serviceOrderDto.ServiceOrderEquipmentDTO;
import com.gustavnienkotter.ServiceOrderManager.enums.ErrorResponseEnum;
import com.gustavnienkotter.ServiceOrderManager.enums.StatusEnum;
import com.gustavnienkotter.ServiceOrderManager.exception.BadRequestException;
import com.gustavnienkotter.ServiceOrderManager.model.Client;
import com.gustavnienkotter.ServiceOrderManager.model.Equipment;
import com.gustavnienkotter.ServiceOrderManager.model.ServiceOrder;
import com.gustavnienkotter.ServiceOrderManager.model.User;
import com.gustavnienkotter.ServiceOrderManager.model.projectionModel.ServiceOrderProjection;
import com.gustavnienkotter.ServiceOrderManager.repository.ServiceOrderRepository;
import com.gustavnienkotter.ServiceOrderManager.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class ServiceOrderService {

    private final ServiceOrderRepository serviceOrderRepository;
    private final EquipmentService equipmentService;
    private final ClientService clientService;
    private final DateUtil dateUtil;

    public Page<ServiceOrderProjection> listAll(Pageable pageable, User user) {
        return serviceOrderRepository.findAllByRegistrationUser(pageable, user);
    }

    public Page<ServiceOrderProjection> listAllOpenServiceOrder(Pageable pageable, User user) {
        return serviceOrderRepository.findAllByStatusAndRegistrationUser(pageable, StatusEnum.OPENED, user);
    }

    public Page<ServiceOrderProjection> listAllFinishedServiceOrder(Pageable pageable, User user) {
        return serviceOrderRepository.findAllByStatusAndRegistrationUser(pageable, StatusEnum.FINISHED, user);
    }

    public ServiceOrderProjection findServiceOrderById(Long id, User user) {
        return serviceOrderProjectionBuilder(findByIdOrThrowBadRequest(id, user));
    }

    public ServiceOrderProjection addClient(ServiceOrderClientDTO serviceOrderClientDTO, User user) {
        ServiceOrder serviceOrder = findByIdOrThrowBadRequest(serviceOrderClientDTO.getIdServiceOrder(), user);
        Client responsibleCustomer = clientService.findByidOrThrowBadRequest(serviceOrderClientDTO.getIdClient(), user);
        serviceOrder.setResponsibleCustomer(responsibleCustomer);
        return save(serviceOrder);
    }

    public ServiceOrderProjection addEquipment(ServiceOrderEquipmentDTO serviceOrderEquipmentDTO, User user) {
        ServiceOrder serviceOrder = findByIdOrThrowBadRequest(serviceOrderEquipmentDTO.getIdServiceOrder(), user);
        Equipment equipment = equipmentService.findByIdOrThrowBadRequest(serviceOrderEquipmentDTO.getIdEquipment(), user);
        serviceOrder.getEquipment().add(equipment);
        return save(serviceOrder);
    }

    public ServiceOrderProjection removeEquipment(ServiceOrderEquipmentDTO serviceOrderEquipmentDTO, User user) {
        ServiceOrder serviceOrder = findByIdOrThrowBadRequest(serviceOrderEquipmentDTO.getIdServiceOrder(), user);
        Equipment equipment = equipmentService.findByIdOrThrowBadRequest(serviceOrderEquipmentDTO.getIdEquipment(), user);
        serviceOrder.getEquipment().remove(equipment);
        return save(serviceOrder);
    }

    public ServiceOrderProjection endServiceOrder(EndServiceOrderDTO endServiceOrderDTO, User user) {
        ServiceOrder serviceOrder = findByIdOrThrowBadRequest(endServiceOrderDTO.getIdServiceOrder(), user);
        if (serviceOrder.getFinishDate() == null) {
            serviceOrder.setFinishDate(dateUtil.timestampNow());
        }
        return save(serviceOrder);
    }

    @Transactional
    public ServiceOrderProjection create(ServiceOrderDTO serviceOrderDTO, User user) {
        serviceOrderDTO.setId(null);
        return save(serviceOrderBuild(serviceOrderDTO, user));
    }

    @Transactional
    public ServiceOrderProjection update(ServiceOrderDTO serviceOrderDTO, User user) {
        ServiceOrder serviceOrderInDatabase = findByIdOrThrowBadRequest(serviceOrderDTO.getId(), user);
        ServiceOrder serviceOrderToSave = serviceOrderBuild(serviceOrderDTO, user);
        serviceOrderToSave.setStartDate(serviceOrderInDatabase.getStartDate());
        serviceOrderToSave.setResponsibleCustomer(serviceOrderInDatabase.getResponsibleCustomer());
        serviceOrderToSave.setEquipment(serviceOrderInDatabase.getEquipment());
        return save(serviceOrderToSave);
    }

    @Transactional
    public void delete(Long id, User user) {
        serviceOrderRepository.delete(findById(id, user));
    }

    private ServiceOrder findByIdOrThrowBadRequest(Long id, User user) {
        ServiceOrder serviceOrder = findById(id, user);
        if (serviceOrder == null) {
            throw new BadRequestException(ErrorResponseEnum.SERVICE_ORDER_NOT_FOUND.getValue());
        }
        return serviceOrder;
    }

    private ServiceOrder findById(Long id, User user) {
        return serviceOrderRepository.findByIdAndRegistrationUser(id, user);
    }

    private ServiceOrderProjection save(ServiceOrder serviceOrder) {
        return serviceOrderProjectionBuilder(serviceOrderRepository.save(serviceOrder));
    }

    private ServiceOrderProjection serviceOrderProjectionBuilder(ServiceOrder serviceOrder) {
        ProjectionFactory pf = new SpelAwareProxyProjectionFactory();
        return pf.createProjection(ServiceOrderProjection.class, serviceOrder);
    }

    private ServiceOrder serviceOrderBuild(ServiceOrderDTO serviceOrderDTO, User user) {
        return ServiceOrder.builder()
                .id(serviceOrderDTO.getId())
                .value(serviceOrderDTO.getValue())
                .startDate(serviceOrderDTO.getStartDate())
                .finishDate(serviceOrderDTO.getFinishDate())
                .equipment(serviceOrderDTO.getEquipaments())
                .description(serviceOrderDTO.getDescription())
                .status(processStatus(serviceOrderDTO.getFinishDate()))
                .responsibleCustomer(serviceOrderDTO.getResponsibleCustomer())
                .registrationUser(user)
                .build();
    }

    private StatusEnum processStatus(Timestamp finishDate) {
        StatusEnum status;
        if (finishDate == null) {
            status = StatusEnum.OPENED;
        } else {
            status = StatusEnum.FINISHED;
        }
        return status;
    }

}
