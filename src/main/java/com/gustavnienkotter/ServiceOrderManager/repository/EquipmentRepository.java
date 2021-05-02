package com.gustavnienkotter.ServiceOrderManager.repository;

import com.gustavnienkotter.ServiceOrderManager.model.Equipment;
import com.gustavnienkotter.ServiceOrderManager.model.User;
import com.gustavnienkotter.ServiceOrderManager.model.projectionModel.EquipmentProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    Page<EquipmentProjection> findAllByRegistrationUser(Pageable pageable, User user);
    Equipment findByIdAndRegistrationUser(Long id, User user);

}
