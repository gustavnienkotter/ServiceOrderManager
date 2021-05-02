package com.gustavnienkotter.ServiceOrderManager.repository;

import com.gustavnienkotter.ServiceOrderManager.enums.StatusEnum;
import com.gustavnienkotter.ServiceOrderManager.model.ServiceOrder;
import com.gustavnienkotter.ServiceOrderManager.model.User;
import com.gustavnienkotter.ServiceOrderManager.model.projectionModel.ServiceOrderProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceOrderRepository extends JpaRepository<ServiceOrder, Long> {

    Page<ServiceOrderProjection> findAllByRegistrationUser(Pageable pageable, User user);
    Page<ServiceOrderProjection> findAllByStatusAndRegistrationUser(Pageable pageable, StatusEnum statusEnum, User user);
    ServiceOrder findByIdAndRegistrationUser(Long id, User user);
}
