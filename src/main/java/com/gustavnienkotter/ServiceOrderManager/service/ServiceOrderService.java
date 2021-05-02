package com.gustavnienkotter.ServiceOrderManager.service;

import com.gustavnienkotter.ServiceOrderManager.dto.serviceOrder.ChangeStatusDTO;
import com.gustavnienkotter.ServiceOrderManager.dto.serviceOrder.ServiceOrderClientDTO;
import com.gustavnienkotter.ServiceOrderManager.dto.serviceOrder.ServiceOrderDTO;
import com.gustavnienkotter.ServiceOrderManager.dto.serviceOrder.ServiceOrderEquipmentDTO;
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

@Service
@RequiredArgsConstructor
public class ServiceOrderService {

    private final ServiceOrderRepository serviceOrderRepository;
    private final EquipmentService equipmentService;
    private final ClientService clientService;

    public Page<ServiceOrderProjection> listAll(Pageable pageable, User user) {
        return serviceOrderRepository.findAllByRegistrationUser(pageable, user);
    }

    public Page<ServiceOrderProjection> listAllOpenServiceOrder(Pageable pageable, User user) {
        return serviceOrderRepository.findAllByStatusAndRegistrationUser(pageable, StatusEnum.OPENED, user);
    }

    public Page<ServiceOrderProjection> listAllStoppedServiceOrder(Pageable pageable, User user) {
        return serviceOrderRepository.findAllByStatusAndRegistrationUser(pageable, StatusEnum.STOPPED, user);
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

    public ServiceOrderProjection changeStatusServiceOrder(ChangeStatusDTO changeStatusDTO, User user) {
        ServiceOrder serviceOrder = findByIdOrThrowBadRequest(changeStatusDTO.getIdServiceOrder(), user);
        serviceOrder.setStatusInfo(changeStatusDTO.getStatusInfo());
        serviceOrder.setStatus(StatusEnum.getById(changeStatusDTO.getIdStatus()));

        if (serviceOrder.getStatus().equals(StatusEnum.STOPPED)) {
            serviceOrder.setStoppedDate(changeStatusDTO.getChangeDate());
            serviceOrder.setFinishDate(null);
        } else if (serviceOrder.getStatus().equals(StatusEnum.FINISHED)) {
            serviceOrder.setStoppedDate(null);
            serviceOrder.setFinishDate(changeStatusDTO.getChangeDate());
        } else {
            serviceOrder.setStoppedDate(null);
            serviceOrder.setFinishDate(null);
        }

        return save(serviceOrder);
    }

    @Transactional
    public ServiceOrderProjection create(ServiceOrderDTO serviceOrderDTO, User user) {
        serviceOrderDTO.setId(null);
        serviceOrderDTO.setStatusInfo("Service Order created");
        return save(serviceOrderBuilder(serviceOrderDTO, user));
    }

    @Transactional
    public ServiceOrderProjection update(ServiceOrderDTO serviceOrderDTO, User user) {
        ServiceOrder serviceOrderInDatabase = findByIdOrThrowBadRequest(serviceOrderDTO.getId(), user);
        ServiceOrder serviceOrderToSave = serviceOrderBuilder(serviceOrderDTO, user);
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

    private ServiceOrder serviceOrderBuilder(ServiceOrderDTO serviceOrderDTO, User user) {
        return ServiceOrder.builder()
                .id(serviceOrderDTO.getId())
                .value(serviceOrderDTO.getValue())
                .startDate(serviceOrderDTO.getStartDate())
                .finishDate(serviceOrderDTO.getFinishDate())
                .equipment(serviceOrderDTO.getEquipments())
                .description(serviceOrderDTO.getDescription())
                .status(processStatus(serviceOrderDTO))
                .statusInfo(serviceOrderDTO.getStatusInfo())
                .responsibleCustomer(serviceOrderDTO.getResponsibleCustomer())
                .registrationUser(user)
                .build();
    }

    private StatusEnum processStatus(ServiceOrderDTO serviceOrderDTO) {
        StatusEnum status;
        if (serviceOrderDTO.getFinishDate() != null) {
            status = StatusEnum.FINISHED;
        } else if (serviceOrderDTO.getStartDate() != null) {
            status = StatusEnum.STOPPED;
        } else {
            status = StatusEnum.OPENED;
        }
        return status;
    }

}
