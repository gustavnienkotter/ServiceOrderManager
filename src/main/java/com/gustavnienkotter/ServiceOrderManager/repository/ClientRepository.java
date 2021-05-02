package com.gustavnienkotter.ServiceOrderManager.repository;

import com.gustavnienkotter.ServiceOrderManager.model.Client;
import com.gustavnienkotter.ServiceOrderManager.model.User;
import com.gustavnienkotter.ServiceOrderManager.model.projectionModel.ClientProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Page<ClientProjection> findAllByRegistrationUser(Pageable pageable, User user);
    Client findByIdAndRegistrationUser(Long id, User user);
}
