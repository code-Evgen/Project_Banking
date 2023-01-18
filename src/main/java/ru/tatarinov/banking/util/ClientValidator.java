package ru.tatarinov.banking.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.tatarinov.banking.model.Client;
import ru.tatarinov.banking.services.ClientService;

@Component
public class ClientValidator implements Validator {
    private final ClientService clientService;

    public ClientValidator(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Client.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Client client = (Client) target;
        if (clientService.getClientByLogin(client.getLogin()).isPresent()){
            errors.rejectValue("login", "", "Login уже занят");
        }
    }
}
