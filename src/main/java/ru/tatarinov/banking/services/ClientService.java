package ru.tatarinov.banking.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tatarinov.banking.model.Card;
import ru.tatarinov.banking.model.Client;
import ru.tatarinov.banking.repositories.ClientRepository;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ClientService {
    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client getClientById(int id){
        return clientRepository.findById(id).orElse(null);
    }

    public Optional<Client> getClientByLogin(String login){
        return clientRepository.getClientByLogin(login);
    }

    @Transactional
    public void createUser(Client client){
        client.setRole("ROLE_USER");
        clientRepository.save(client);
    }

}
