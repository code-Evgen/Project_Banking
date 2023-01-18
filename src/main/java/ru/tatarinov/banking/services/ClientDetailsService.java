package ru.tatarinov.banking.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.tatarinov.banking.Security.ClientDetails;
import ru.tatarinov.banking.model.Client;
import ru.tatarinov.banking.repositories.ClientRepository;

import java.util.Optional;

@Service
public class ClientDetailsService implements UserDetailsService {
    private final ClientRepository clientRepository;

    public ClientDetailsService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Client> client = clientRepository.getClientByLogin(username);
        if (client.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }
        return new ClientDetails(client.get());
    }
}
