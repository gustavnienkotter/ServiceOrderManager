package com.gustavnienkotter.ServiceOrderManager.service;

import com.gustavnienkotter.ServiceOrderManager.dto.clientDto.ClientDTO;
import com.gustavnienkotter.ServiceOrderManager.enums.ErrorResponseEnum;
import com.gustavnienkotter.ServiceOrderManager.exception.BadRequestException;
import com.gustavnienkotter.ServiceOrderManager.model.Client;
import com.gustavnienkotter.ServiceOrderManager.model.ServiceOrder;
import com.gustavnienkotter.ServiceOrderManager.model.User;
import com.gustavnienkotter.ServiceOrderManager.model.projectionModel.ClientProjection;
import com.gustavnienkotter.ServiceOrderManager.model.projectionModel.ServiceOrderProjection;
import com.gustavnienkotter.ServiceOrderManager.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public Page<ClientProjection> listAll(Pageable pageable, User user) {
        return clientRepository.findAllByRegistrationUser(pageable, user);
    }

    public ClientProjection findClientById(Long id, User user) {
        return clientProjectionBuilder(findByidOrThrowBadRequest(id, user));
    }

    @Transactional
    public ClientProjection create(ClientDTO clientDTO, User user) {
        clientDTO.setId(null);
        return save(clientBuild(clientDTO, user));
    }

    @Transactional
    public ClientProjection update(ClientDTO clientDTO, User user) {
        return save(clientBuild(clientDTO, user));
    }

    @Transactional
    public void delete(Long id, User user) {
        clientRepository.delete(findById(id, user));
    }

    private Client findById(Long id, User user) {
        return clientRepository.findByIdAndRegistrationUser(id, user);
    }

    public Client findByidOrThrowBadRequest(Long id, User user) {
        Client client = findById(id, user);
        if (client == null) {
            throw new BadRequestException(ErrorResponseEnum.CLIENT_NOT_FOUND.getValue());
        }
        return client;
    }

    private ClientProjection save(Client client) {
        return clientProjectionBuilder(clientRepository.save(client));
    }

    private ClientProjection clientProjectionBuilder(Client client) {
        ProjectionFactory pf = new SpelAwareProxyProjectionFactory();
        return pf.createProjection(ClientProjection.class, client);
    }

    private Client clientBuild(ClientDTO clientDTO, User user) {
        return Client.builder()
                .id(clientDTO.getId())
                .name(clientDTO.getName())
                .address(clientDTO.getAddress())
                .email(clientDTO.getEmail())
                .phone(clientDTO.getPhone())
                .registrationUser(user)
                .build();
    }
}
